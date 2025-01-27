package org.example.sbb.app.domain.question.question.repository;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.comment.QComment;
import org.example.sbb.app.domain.question.answer.QAnswer;
import org.example.sbb.app.domain.question.answer.SortOption;
import org.example.sbb.app.domain.question.question.QQuestion;
import org.example.sbb.app.domain.question.question.Question;
import org.example.sbb.app.domain.question.question.dto.QuestionListDto;
import org.example.sbb.app.domain.question.recommend.QQuestionVoter;
import org.example.sbb.app.domain.user.QSiteUser;
import org.example.sbb.app.domain.user.dto.SiteUserDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class QueryDslQuestionH2Repository {

    private final JPAQueryFactory queryFactory;

    public Question findById(Long id, Pageable pageable, SortOption sortOption){
        QQuestion target = QQuestion.question;
        QQuestionVoter voters= QQuestionVoter.questionVoter;

        JPAQuery<Question> query = queryFactory.select(target)
                .from(target)
                .where(target.id.eq(id))
                .leftJoin(target.answers)
                .leftJoin(target.voters, voters)
                .leftJoin(voters.voter, QSiteUser.siteUser)
                .leftJoin(target.author)
                .orderBy(voters.count().desc());

        return query.fetchOne();
    }

    public Page<QuestionListDto> findAll(String keyword, Pageable pageable, SortOption sortOption) {
        return switch (sortOption) {
            case RECOMMEND -> findAllByRecommend(keyword, pageable);
            case RECENT_ANSWER -> findAllByRecentAnswer(keyword, pageable);
            case RECENT_COMMENT -> findAllByRecentComment(keyword, pageable);
            default -> findAllByTime(keyword, pageable);
        };
    }

    private Page<QuestionListDto> findAllByRecentComment(String keyword, Pageable pageable) {
        QQuestion target = QQuestion.question;
        QComment comments = new QComment("comments");
        QQuestionVoter voters = new QQuestionVoter("voters");
        JPAQuery<Tuple> query = queryFactory
                .select(target.id, target.subject, target.author, target.answers.size(), target.createdAt,
                        target.voters.size(), comments.createdAt.max())
                .from(target)
                .leftJoin(target.author)
                .leftJoin(target.voters, voters)
                .leftJoin(voters.voter, QSiteUser.siteUser)
                .leftJoin(target.comments, comments)
                .groupBy(target.id)
                .orderBy(comments.createdAt.max().desc());

        if(!keyword.isEmpty())
            query.where(target.subject.contains(keyword));

        long cnt = query.fetchCount();

        List<QuestionListDto> fetched = query.offset(pageable.getOffset()).limit(pageable.getPageSize())
                .stream().map(q ->
                        new QuestionListDto(q.get(target.id),
                                q.get(target.subject),
                                SiteUserDto.of(q.get(target.author)),
                                q.get(target.answers.size()),
                                q.get(target.voters.size()),
                                q.get(target.createdAt)))
                .toList();
        return new PageImpl<>(fetched, pageable, cnt);
    }

    private Page<QuestionListDto> findAllByTime(String keyword, Pageable pageable) {
        QQuestion target = QQuestion.question;
        QQuestionVoter voters= QQuestionVoter.questionVoter;

        JPAQuery<Tuple> query = queryFactory.select(target.id, target.subject, target.author, target.answers.size(), target.createdAt,
                        target.voters.size())
                .from(target)
                .leftJoin(target.author)
                .leftJoin(target.voters, voters)
                .leftJoin(voters.voter, QSiteUser.siteUser)
                .groupBy(target.id)
                .orderBy(target.createdAt.desc());

        if (!keyword.isEmpty())
            query.where(target.subject.contains(keyword));

        long cnt = query.fetchCount();
        List<QuestionListDto> fetched = query.offset(pageable.getOffset()).limit(pageable.getPageSize())
                .stream().map(q ->
                        new QuestionListDto(q.get(target.id),
                                q.get(target.subject),
                                SiteUserDto.of(q.get(target.author)),
                                q.get(target.answers.size()),
                                q.get(target.voters.size()),
                                q.get(target.createdAt)))
                .toList();

        return new PageImpl<>(fetched, pageable, cnt);
    }

    private Page<QuestionListDto> findAllByRecommend(String keyword, Pageable pageable){
        QQuestion target = QQuestion.question;
        QQuestionVoter voters= QQuestionVoter.questionVoter;

        JPAQuery<Tuple> query = queryFactory.select(target.id, target.subject, target.author, target.answers.size(), target.createdAt,
                        target.voters.size())
                .from(target)
                .leftJoin(target.author)
                .leftJoin(target.voters, voters)
                .leftJoin(voters.voter, QSiteUser.siteUser)
                .groupBy(target.id)
                .orderBy(voters.count().desc());

        if (!keyword.isEmpty()) {
            query.where(target.subject.contains(keyword));
        }

        long cnt = query.fetchCount();
        List<QuestionListDto> fetched = query.offset(pageable.getOffset()).limit(pageable.getPageSize())
                .stream().map(q ->
                        new QuestionListDto(q.get(target.id),
                                q.get(target.subject),
                                SiteUserDto.of(q.get(target.author)),
                                q.get(target.answers.size()),
                                q.get(target.voters.size()),
                                q.get(target.createdAt)))
                .toList();

        return new PageImpl<>(fetched, pageable, cnt);
    }

    private Page<QuestionListDto> findAllByRecentAnswer(String keyword, Pageable newPage) {
        QQuestion question = QQuestion.question;
        QAnswer answer = QAnswer.answer;
        JPAQuery<Tuple> query = queryFactory.select(question.id, question.subject, question.author, question.answers.size(),
                        question.createdAt, question.voters.size(), answer.createdAt.max())
                .from(question)
                .distinct()
                .leftJoin(question.answers, answer)
                .leftJoin(question.voters)
                .leftJoin(question.author, new QSiteUser("author"))
                .where(question.subject.contains(keyword))
                .orderBy(answer.createdAt.max().desc());

        long cnt = query.fetchCount();
        List<QuestionListDto> content = query
                .offset(newPage.getOffset())
                .limit(newPage.getPageSize())
                .fetch().stream().map(item ->
                new QuestionListDto(
                        item.get(question.id),
                        item.get(question.subject),
                        SiteUserDto.of(item.get(question.author)),
                        item.get(question.answers.size()),
                        item.get(question.voters.size()),
                        item.get(question.createdAt)
                )).toList();
        return new PageImpl<>(content, newPage,cnt);
    }
}

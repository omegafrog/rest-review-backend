package org.example.sbb.app.domain.answer;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.example.sbb.app.domain.relation.QAnswerVoter;
import org.example.sbb.app.domain.user.QSiteUser;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
@RequiredArgsConstructor
public class QueryDslAnswerRepository {

    private final JPAQueryFactory factory;

    public Page<Answer> findAllByQuestionId(Long questionId, Pageable pageable, SortOption sortOption) {
        QAnswer target = QAnswer.answer;
        QAnswerVoter voters = QAnswerVoter.answerVoter;
        QSiteUser voter = QSiteUser.siteUser;

        OrderSpecifier specifier = switch (sortOption) {
            case TIME -> target.createdAt.desc();
            case RECOMMEND -> voters.answer.count().desc();
        };

        JPAQuery<Answer> query = factory.selectFrom(target)
                .leftJoin(target.author)
                .leftJoin(target.voters, voters)
                .leftJoin(voters.voter, voter)
                .where(target.question.id.eq(questionId))
                .orderBy(specifier)
                .groupBy(target.id);


        long cnt = query.fetchCount();

        List<Answer> fetched = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(fetched, pageable, cnt);
    }
}

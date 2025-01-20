package org.example.sbb.app.domain.comment;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class QueryDslCommentRepository {

    private final JPAQueryFactory queryFactory;

    public Page<Comment> findAllByQuestion(Long questionId, Pageable pageable) {

        JPAQuery<Comment> query = queryFactory
                .selectFrom(QComment.comment)
                .where(QComment.comment.targetQuestion.id.eq(questionId))
                .orderBy(QComment.comment.createdAt.desc());

        long cnt = query.fetchCount();

        List<Comment> fetch = query.offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        return new PageImpl<>(fetch, pageable, cnt);
    }
}

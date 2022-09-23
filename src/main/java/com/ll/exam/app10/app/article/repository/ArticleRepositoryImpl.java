package com.ll.exam.app10.app.article.repository;

import com.ll.exam.app10.app.article.entity.Article;
import com.ll.exam.app10.util.Util;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.ll.exam.app10.app.article.entity.QArticle.article;
import static com.ll.exam.app10.app.hashTag.entity.QHashTag.hashTag;
import static com.ll.exam.app10.app.keyword.entity.QKeyword.keyword;

@RequiredArgsConstructor
public class ArticleRepositoryImpl implements ArticleRepositoryCustom {
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public List<Article> getQslArticlesOrderByIdDesc() {
        return jpaQueryFactory
                .select(article)
                .from(article)
                .orderBy(article.id.desc())
                .fetch();
    }

    @Override
    public List<Article> searchQsl(String kwType, String kw) {
        // 키워드가 없거나, 검색타입이 hashTag가 아닌경우, 전체 게시물
        JPAQuery<Article> jpqQuery = jpaQueryFactory
                .select(article)
                .distinct()
                .from(article);

        // 키워드가 존재하고
        if (Util.str.empty(kw) == false) {
            // 키워드 타입이 해시태그이다.
            if (Util.str.eq(kwType, "hashTag")) {
                jpqQuery
                        .innerJoin(hashTag)
                        .on(article.eq(hashTag.article))
                        .innerJoin(keyword)
                        .on(keyword.eq(hashTag.keyword))
                        .where(keyword.content.eq(kw));

            }
        }

        jpqQuery.orderBy(article.id.desc());

        return jpqQuery.fetch();
    }
}

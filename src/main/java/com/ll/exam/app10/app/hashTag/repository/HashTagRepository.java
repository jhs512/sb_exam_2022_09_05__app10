package com.ll.exam.app10.app.hashTag.repository;

import com.ll.exam.app10.app.hashTag.entity.HashTag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface HashTagRepository extends JpaRepository<HashTag, Long> {
    Optional<HashTag> findByArticleIdAndKeywordId(Long articleId, Long keywordId);

    List<HashTag> findAllByArticleId(Long articleId);

    List<HashTag> findAllByArticleIdIn(long[] ids);
}

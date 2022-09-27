package com.ll.exam.app10.app.article.service;

import com.ll.exam.app10.app.article.entity.Article;
import com.ll.exam.app10.app.article.repository.ArticleRepository;
import com.ll.exam.app10.app.gen.entity.GenFile;
import com.ll.exam.app10.app.gen.service.GenFileService;
import com.ll.exam.app10.app.hashTag.entity.HashTag;
import com.ll.exam.app10.app.hashTag.service.HashTagService;
import com.ll.exam.app10.app.member.entity.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

@Service
@RequiredArgsConstructor
@Slf4j
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final GenFileService genFileService;

    private final HashTagService hashTagService;

    public Article write(Long authorId, String subject, String content) {
        return write(new Member(authorId), subject, content);
    }

    public Article write(Long authorId, String subject, String content, String hashTagContents) {
        return write(new Member(authorId), subject, content, hashTagContents);
    }

    public Article write(Member author, String subject, String content) {
        return write(author, subject, content, "");
    }

    public Article write(Member author, String subject, String content, String hashTagContents) {
        Article article = Article
                .builder()
                .author(author)
                .subject(subject)
                .content(content)
                .build();

        articleRepository.save(article);

        hashTagService.applyHashTags(article, hashTagContents);

        return article;
    }

    public Article getArticleById(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public void addGenFileByUrl(Article article, String typeCode, String type2Code, int fileNo, String url) {
        genFileService.addGenFileByUrl("article", article.getId(), typeCode, type2Code, fileNo, url);
    }

    public Article getForPrintArticleById(Long id) {
        Article article = getArticleById(id);

        loadForPrintData(article);

        return article;
    }

    public void modify(Article article, String subject, String content, String hashTagContents) {
        article.setSubject(subject);
        article.setContent(content);
        articleRepository.save(article);

        hashTagService.applyHashTags(article, hashTagContents);
    }

    public List<Article> getArticles() {
        return articleRepository.getQslArticlesOrderByIdDesc();
    }

    public List<Article> search(String kwType, String kw) {
        return articleRepository.searchQsl(kwType, kw);
    }

    public void loadForPrintData(Article article) {
        Map<String, GenFile> genFileMap = genFileService.getRelGenFileMap(article);
        List<HashTag> hashTags = hashTagService.getHashTags(article);

        article.getExtra().put("hashTags", hashTags);
        article.getExtra().put("genFileMap", genFileMap);
    }

    public void loadForPrintData(List<Article> articles) {
        long[] ids = articles
                .stream()
                .mapToLong(Article::getId)
                .toArray();

        List<HashTag> hashTagsByArticleIds = hashTagService.getHashTagsByArticleIdIn(ids);

        Map<Long, List<HashTag>> hashTagsByArticleIdsMap = hashTagsByArticleIds.stream()
                .collect(groupingBy(
                        hashTag -> hashTag.getArticle().getId(), toList()
                ));

        articles.stream().forEach(article -> {
            List<HashTag> hashTags = hashTagsByArticleIdsMap.get(article.getId());

            if (hashTags == null || hashTags.size() == 0) return;

            article.getExtra().put("hashTags", hashTags);
        });

        List<GenFile> genFilesByRelIdIn = genFileService.getRelGenFilesByRelIdIn("article", ids);

        Map<Long, List<GenFile>> genFilesMap = genFilesByRelIdIn
                .stream()
                .collect(groupingBy(
                        GenFile::getRelId, toList()
                ));

        articles.stream().forEach(article -> {
            List<GenFile> genFiles = genFilesMap.get(article.getId());

            if (genFiles == null || genFiles.size() == 0) return;

            article.getExtra().put("genFileMap", genFileService.getRelGenFileMap(genFiles));
        });

        log.debug("articles : " + articles);
    }
}

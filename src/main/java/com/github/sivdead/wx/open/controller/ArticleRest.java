package com.github.sivdead.wx.open.controller;

import com.github.sivdead.wx.open.dao.eo.Article;
import com.github.sivdead.wx.open.dao.repository.ArticleRepository;
import com.github.sivdead.wx.open.dto.ArticleDto;
import com.github.sivdead.wx.open.dto.mapper.ArticleMapper;
import com.github.sivdead.wx.open.web.R;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/v1/article")
public class ArticleRest {

    private final ArticleRepository articleRepository;
    private final ArticleMapper articleMapper = ArticleMapper.INSTANCE;

    public ArticleRest(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
    }

    @PostMapping
    public R<ArticleDto> addArticle(@RequestBody@Validated ArticleDto articleDto){
        Article article = articleMapper.toEo(articleDto);
        article = articleRepository.save(article);
        articleDto.setId(article.getId());
        return R.ok(articleDto);
    }

}

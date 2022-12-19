package org.swyg.greensumer.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.swyg.greensumer.domain.Article;
import org.swyg.greensumer.domain.UserAccount;
import org.swyg.greensumer.domain.constant.RoleType;

import java.util.List;

@Slf4j
@RequestMapping("/articles")
@Controller
public class ArticleController {

    @GetMapping
    public List<Article> articles(){

        return List.of();
    }

    @GetMapping("/{article_id}")
    public Article articles(@PathVariable Long article_id){

        log.info("게시글 id: {}", article_id);
        UserAccount user = UserAccount.of("test", "test", "test@mail.com", "test", RoleType.USER);
        return Article.of(user,"test", "test", "#test", "test");
    }
}

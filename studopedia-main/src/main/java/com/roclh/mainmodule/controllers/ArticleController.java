package com.roclh.mainmodule.controllers;

import com.roclh.mainmodule.exceptions.ArticleNotFoundException;
import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.services.ArticleService;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/article/{article_id}")
@RequiredArgsConstructor
public class ArticleController {

    private final Logger log = LogManager.getLogger(ArticleController.class);
    private final ArticleService service;
    private Account principal;

    @PutMapping("/up")
    public ResponseEntity<?> upAnArticle(@PathVariable(name = "article_id") Long articleId) throws ArticleNotFoundException {
        principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long accountId =  principal.getId();
        service.upAnArticle(articleId, accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/down")
    public ResponseEntity<?> downAnArticle(@PathVariable(name = "article_id") Long articleId) throws ArticleNotFoundException {
        principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long accountId =  principal.getId();
        service.downAnArticle(articleId, accountId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/comment")
    public ResponseEntity<?> commentArticle(@PathVariable(name = "article_id") Long articleId, @RequestParam(name = "comment") String commentString) throws ArticleNotFoundException {
        principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long accountId =  principal.getId();

        service.commentArticle(articleId, accountId, commentString);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/comment")
    public ResponseEntity<?> deleteComment(@PathVariable(name = "article_id") Long articleId, @RequestParam(name = "comment_id") Long commentId) throws ArticleNotFoundException {
        principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long accountId =  principal.getId();

        service.deleteComment(articleId, accountId, commentId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/")
    public ResponseEntity<?> deleteArticle(@PathVariable(name = "article_id") Long articleId) throws ArticleNotFoundException {
        principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        Long accountId = principal.getId();
        service.deleteArticle(articleId, accountId);
        return ResponseEntity.ok().build();
    }

}

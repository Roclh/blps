package com.roclh.mainmodule.controllers;

import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.entities.StudopediaArticle;
import org.roclh.common.exceptions.ArticleExistsException;
import org.roclh.common.exceptions.ArticleNotFoundException;
import org.roclh.common.exceptions.DataValidationException;
import com.roclh.mainmodule.services.StudopediaService;
import com.roclh.mainmodule.utils.ValidationUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

import static org.roclh.common.constants.Constants.EMPTY_MESSAGE;
import static org.roclh.common.constants.Constants.SPECIAL_CHARACTERS_MESSAGE;
import static org.roclh.common.constants.Constants.WRONG_PAGE_NUMBER_MESSAGE;
import static org.roclh.common.constants.Constants.WRONG_PAGE_SIZE_MESSAGE;


@RestController
@Slf4j
@RequestMapping("/api")
public class StudopediaController  {
    private final StudopediaService service;

    @Autowired
    public StudopediaController(StudopediaService service) {
        log.info("Initializing Studopedia Controller");
        this.service = service;
    }

    @GetMapping("/articles/{name}")
    public ResponseEntity<StudopediaArticle> getArticle(@PathVariable(name = "name") String articleName) throws ArticleNotFoundException, DataValidationException {
        log.info("Received request to get article with name {}", articleName);
        ValidationUtils.validate(articleName, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(articleName, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        return ResponseEntity.ok(service.getArticleByName(articleName));
    }

    @GetMapping("/articles")
    public ResponseEntity<List<StudopediaArticle>> getAllArticle()  {
        log.info("Received request to get all the articles");
        return ResponseEntity.ok(service.getArticlesAsList());
    }

    @GetMapping("/articles-page")
    public ResponseEntity<List<StudopediaArticle>> getAllArticleWithPageSize(@RequestParam(name = "page") int page, @RequestParam(name = "page_size") int pageSize) throws DataValidationException {

        log.info("Received request to get all the articles, page {}, page size {}", page, pageSize);
        ValidationUtils.validate(page, (val) -> val < 0, WRONG_PAGE_NUMBER_MESSAGE);
        ValidationUtils.validate(pageSize, (val) -> val <= 0, WRONG_PAGE_SIZE_MESSAGE);
        return ResponseEntity.ok(service.getArticlesAsListWithPageSize(page, pageSize));
    }

    @GetMapping("/articles/search")
    public ResponseEntity<List<StudopediaArticle>> searchArticle(@RequestParam(name = "search") String search, @RequestParam(name = "page") int page) throws DataValidationException {
        log.info("Received a search for " + search + " article request");
        ValidationUtils.validate(search, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(search, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(page, (val) -> val < 0, WRONG_PAGE_NUMBER_MESSAGE);
        return ResponseEntity.ok(service.getArticlesAsPage(search, page));
    }

    @GetMapping("/article/search-page")
    public ResponseEntity<List<StudopediaArticle>> searchArticleWithPage(@RequestParam(name = "search") String search, @RequestParam(name = "page") int page, @RequestParam(name = "page_size") int pageSize) throws DataValidationException {
        log.info("Received a search for " + search + " article request, page: {}, page size: {}", page, pageSize);
        ValidationUtils.validate(search, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(search, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(page, (val) -> val < 0, WRONG_PAGE_NUMBER_MESSAGE);
        ValidationUtils.validate(pageSize, (val) -> val <= 0, WRONG_PAGE_SIZE_MESSAGE);
        return ResponseEntity.ok(service.getArticlesAsPageWithSize(search, page, pageSize));
    }

    @GetMapping("/article/suggest")
    public ResponseEntity<List<StudopediaArticle>> suggestArticle(@RequestParam(name = "search") String search) throws DataValidationException {
        log.info("Suggestions!!!");
        ValidationUtils.validate(search, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(search, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        return ResponseEntity.ok(service.getArticleSuggestionBySubStr(search));
    }

    @GetMapping("/article/random")
    public ResponseEntity<StudopediaArticle> randomArticle() throws ArticleNotFoundException {
        log.info("Received a request for random article");
        return ResponseEntity.ok(service.getRandomArticle());
    }

    @GetMapping("/article/{category}")
    public ResponseEntity<List<StudopediaArticle>> getArticlesByCategory(
            @PathVariable(name = "category") String categoryName, @RequestParam(name = "page") int page) throws ArticleNotFoundException, DataValidationException {
        log.info("Received a request for articles in category: " + categoryName);
        ValidationUtils.validate(categoryName, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(categoryName, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(page, (val) -> val < 0, WRONG_PAGE_NUMBER_MESSAGE);
        return ResponseEntity.ok(service.getArticleByCategory(categoryName, page));
    }

    @GetMapping("/article/category-page")
    public ResponseEntity<List<StudopediaArticle>> getArticlesByCategoryWithPage(
            @RequestParam(name = "category") String categoryName, @RequestParam(name = "page") int page, @RequestParam(name = "page_size") int pageSize) throws ArticleNotFoundException, DataValidationException {
        log.info("Received a request for articles in category: {}, page: {}, page size: {}", categoryName, page, pageSize);
        ValidationUtils.validate(categoryName, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(categoryName, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(page, (val) -> val < 0, WRONG_PAGE_NUMBER_MESSAGE);
        ValidationUtils.validate(pageSize, (val) -> val <= 0, WRONG_PAGE_SIZE_MESSAGE);
        return ResponseEntity.ok(service.getArticleByCategoryWithSize(categoryName, page, pageSize));
    }


    @PostMapping("/article")
    public ResponseEntity<?> addArticle(
            @RequestParam(name = "category") String categoryName,
            @RequestParam(name = "content") String content,
            @RequestParam(name = "title") String title) throws DataValidationException {
        log.info("Received a request to add an article with title {}, content {} and category name {}", title, content, categoryName);
        ValidationUtils.validate(title, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(title, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(categoryName, ValidationUtils::containsSpecialCharacters, SPECIAL_CHARACTERS_MESSAGE);
        ValidationUtils.validate(categoryName, String::isEmpty, EMPTY_MESSAGE);
        ValidationUtils.validate(content, String::isEmpty, EMPTY_MESSAGE);
        Account principal = (Account) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.addArticle(title, content, categoryName, principal.getId());
        log.info("Successfully added an article with title {}, content {} and category name {}", title, content, categoryName);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/article/new-request")
    public ResponseEntity<List<StudopediaArticle>> getNewPostRequests(){
        log.info("Admin requested articles to approve");
        return ResponseEntity.ok(service.getAllNotApprovedArticles());
    }

    @PutMapping("/article/new-request/{article_id}")
    public ResponseEntity<?> approveNewPostRequest(
            @PathVariable(name = "article_id") Long articleId
    ) throws ArticleExistsException, ArticleNotFoundException {
        service.approveAnArticle(articleId);
        return ResponseEntity.ok().build();
    }

}





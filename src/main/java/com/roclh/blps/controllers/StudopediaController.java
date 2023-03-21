package com.roclh.blps.controllers;

import com.roclh.blps.Exceptions.ArticleNotFoundException;
import com.roclh.blps.entities.StudopediaArticle;
import com.roclh.blps.service.StudopediaService;
import io.swagger.annotations.ApiOperation;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api")
public class StudopediaController {

    private static final Logger log = LogManager.getLogger(StudopediaController.class);
    private final StudopediaService service;

    @Autowired
    public StudopediaController(StudopediaService service){
        log.info("Initializing Studopedia Controller");
        this.service = service;
    }

    @GetMapping("/article")
    @ApiOperation(value = "Get article by name", notes = "Throws exception if Article doesn't exist")
    public StudopediaArticle getArticle(@RequestParam(name="name") String articleName) throws ArticleNotFoundException {
        log.info("Received request to get article with name {}", articleName);
            return service.getArticleByName(articleName);
    }

    @GetMapping("/all-articles")
    public List<StudopediaArticle> getAllArticle(@RequestParam(name="page") int page, @RequestParam(name="paging_size", required = false) Integer paging_size){
        log.info("Received request to get all the articles");
        return service.getArticlesAsList(page, paging_size);
    }

    @PostMapping("/article/search")
    public List<StudopediaArticle> searchArticle(@RequestParam(name="search") String search, @RequestParam(name="page") int page, @RequestParam(name="paging_size", required = false) Integer paging_size) {
        log.info("Received a search for " + search + " article request");
        return service.getArticlesAsPage(search,page, paging_size);
    }

    @PostMapping("/article/suggest")
    public List<StudopediaArticle> suggestArticle(@RequestParam(name="search") String search){
        log.info("Suggestions!!!");
        return service.getArticleSuggestionBySubStr(search);
    }

    @PostMapping("/article/random")
    public StudopediaArticle randomArticle() throws ArticleNotFoundException {
        log.info("Received a request for random article");
        return service.getRandomArticle();
    }

    @PostMapping("/article/category")
    public List<StudopediaArticle> getArticlesByCategory(
            @RequestParam(name = "category") String categoryName, @RequestParam(name = "page") int page, @RequestParam(name = "paging_size", required = false) Integer paging_size) throws ArticleNotFoundException {
        log.info("Received a request for articles in category: " + categoryName);
        return service.getArticleByCategory(categoryName, page, paging_size);
    }
}





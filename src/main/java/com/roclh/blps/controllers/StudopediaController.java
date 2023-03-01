package com.roclh.blps.controllers;

import com.roclh.blps.Exceptions.ArticleNotFoundException;
import com.roclh.blps.entities.StudopediaArticle;
import com.roclh.blps.service.StudopediaService;
import com.roclh.blps.utils.HttpResponseErrorMessages;
import io.swagger.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;


@RestController
@RequestMapping("/api")
@Api(tags = "Studopedia Controller")
@ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_NOT_FOUND, message = HttpResponseErrorMessages.NOT_FOUND)
})
public class StudopediaController {

    private static final Logger log = LoggerFactory.getLogger(StudopediaController.class);
    private final StudopediaService service;

    @Autowired
    public StudopediaController(StudopediaService service){
        log.info("Initializing Studopedia Controller");
        this.service = service;
    }

    @GetMapping("/article")
    @ApiOperation(value = "Get article by name", notes = "Throws exception if Article doesn't exist")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "success!", response = StudopediaArticle.class)
    public StudopediaArticle getArticle(@RequestParam(name="name") String articleName) throws ArticleNotFoundException {
        log.info("Received request to get article with name {}", articleName);
            return service.getArticleByName(articleName);
    }

    @GetMapping("/all-articles")
    @ApiOperation(value = "Get all the articles page by page")
    @ApiResponse(code = HttpServletResponse.SC_OK, message = "success!")
    public List<StudopediaArticle> getAllArticle(@RequestParam(name="page") int page){
        log.info("Received request to get all the articles");
        return service.getArticlesAsList(page);
    }

    @PostMapping("/article/search")
    @ApiOperation(value="Search for an article by it's name", notes = "Can be partially correct")
    @ApiResponse(code=HttpServletResponse.SC_OK, message = "success!")
    public List<StudopediaArticle> searchArticle(@RequestParam(name="search") String search, @RequestParam(name="page") int page) {
        log.info("Received a search for " + search + " article request");
        return service.getArticlesAsPage(search,page);
    }

    @PostMapping("/article/suggest")
    @ApiOperation(value = "Suggests articles while user is typing")
    @ApiResponse(code=HttpServletResponse.SC_OK, message = "success!")
    public List<StudopediaArticle> suggestArticle(@RequestParam(name="search") String search){
        log.info("Suggestions!!!");
        return service.getArticleSuggestionBySubStr(search);
    }

    @PostMapping("/article/random")
    @ApiOperation(value = "Gets random article.")
    @ApiResponse(code=HttpServletResponse.SC_OK, message = "success!", response = StudopediaArticle.class)
    public StudopediaArticle randomArticle() throws ArticleNotFoundException {
        log.info("Received a request for random article");
        return service.getRandomArticle();
    }

    @PostMapping("/article/category")
    @ApiOperation(value = "Filter articles by Category")
    @ApiResponse(code=HttpServletResponse.SC_OK, message = "success!", response = StudopediaArticle.class)
    public List<StudopediaArticle> getArticlesByCategory(
            @RequestParam(name = "category") String categoryName, @RequestParam(name = "page") int page) throws ArticleNotFoundException {
        log.info("Received a request for articles in category: " + categoryName);
        return service.getArticleByCategory(categoryName, page);
    }
}





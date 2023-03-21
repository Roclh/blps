package com.roclh.blps.service;

import com.roclh.blps.Exceptions.ArticleNotFoundException;
import com.roclh.blps.database.CategoryDatabase;
import com.roclh.blps.database.StudopediaDatabase;
import com.roclh.blps.entities.Category;
import com.roclh.blps.entities.StudopediaArticle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StudopediaService {

    private final int FIRST_ARTICLE = 0;
    private final int MAX_ARTICLE = 5;

    private final StudopediaDatabase articleRepository;
    private final CategoryDatabase categoryRepository;

    @Autowired
    public StudopediaService(StudopediaDatabase articleRepository, CategoryDatabase categoryRepository){
        this.articleRepository = articleRepository;
        this.categoryRepository = categoryRepository;
    }

    public List<StudopediaArticle> getArticlesAsList(int page, Integer paging_size){
        if (paging_size == null) paging_size = MAX_ARTICLE;
        Pageable pageWithFiveElements = PageRequest.of(page, paging_size);
        return articleRepository.findByNameContainsIgnoreCase("", pageWithFiveElements);
    }

    public List<StudopediaArticle> getArticlesAsPage(String search, int page, Integer paging_size){
        if (paging_size == null) paging_size = MAX_ARTICLE;
        Pageable pageWithFiveElements = PageRequest.of(page, paging_size);
        return articleRepository.findByNameContainsIgnoreCase(search, pageWithFiveElements);
    }

    public StudopediaArticle getArticleByID(Long id) throws ArticleNotFoundException {
        Optional<StudopediaArticle> optional = articleRepository.findByIdEquals(id);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        return optional.get();
    }

    public StudopediaArticle getArticleByName(String name) throws ArticleNotFoundException{
        Optional<StudopediaArticle> optional = articleRepository.findByNameEqualsIgnoreCase(name);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        return optional.get();
    }

    public List<StudopediaArticle> getArticleSuggestionBySubStr(String subString){
        Pageable pageOneWithFiveElements = PageRequest.of(FIRST_ARTICLE, MAX_ARTICLE);
        return articleRepository.findByNameContainsIgnoreCase(subString, pageOneWithFiveElements);
    }

    public List<StudopediaArticle> getArticleByCategory(String category, int page, Integer paging_size) throws ArticleNotFoundException {
        if (paging_size == null) paging_size = MAX_ARTICLE;
        Pageable pageWithFiveElements = PageRequest.of(page, paging_size);
        Optional<Category> categoryOptional = categoryRepository.findByNameLikeIgnoreCase(category);
        if (categoryOptional.isEmpty()) throw new ArticleNotFoundException();
        return articleRepository.findByCategoryEquals(categoryOptional.get(), pageWithFiveElements);
    }

    public List<StudopediaArticle> getAllArticles(){
        return articleRepository.getAllArticles();
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public StudopediaArticle getRandomArticle() throws ArticleNotFoundException {
        List<StudopediaArticle> allArticles = getAllArticles();
        Random random = new Random();
        if(allArticles.size() == 0){
            throw new ArticleNotFoundException();
        }
        int randomIndex = random.nextInt(allArticles.size());
        return allArticles.get(randomIndex);
    }




}

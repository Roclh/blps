package com.roclh.mainmodule.services;

import org.roclh.common.exceptions.ArticleExistsException;
import org.roclh.common.exceptions.ArticleNotFoundException;
import org.roclh.common.exceptions.DataValidationException;
import com.roclh.mainmodule.database.CategoryDatabase;
import com.roclh.mainmodule.database.StudopediaDatabase;
import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.entities.Category;
import com.roclh.mainmodule.entities.StudopediaArticle;
import com.roclh.mainmodule.utils.ValidationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class StudopediaService {
    private final static String ARTICLE_EXISTS = "Article already exists";

    private final int DEFAULT_SIZE = 5;

    private final StudopediaDatabase articleRepository;
    private final CategoryDatabase categoryRepository;
    private Account principal;

    @Autowired
    public StudopediaService(StudopediaDatabase database, CategoryDatabase categoryRepository){
        this.articleRepository = database;
        this.categoryRepository = categoryRepository;
    }


    public List<StudopediaArticle> getArticlesAsList() {
        return articleRepository.getByApprovedTrue();
    }

    public List<StudopediaArticle> getArticlesAsListWithPageSize(int page, int page_size) {
        Pageable pages = PageRequest.of(page, page_size);
        return articleRepository.findByApprovedTrue(pages);
    }

    public List<StudopediaArticle> getArticlesAsPage(String search, int page) {
        Pageable pageWithFiveElements = PageRequest.of(page, DEFAULT_SIZE);
        return articleRepository.findByNameLikeIgnoreCaseAndApprovedTrue(search, pageWithFiveElements);
    }

    public List<StudopediaArticle> getArticlesAsPageWithSize(String search, int page, int pageSize) {
        Pageable pages = PageRequest.of(page, pageSize);
        return articleRepository.findByNameLikeIgnoreCaseAndApprovedTrue(search, pages);
    }

    public StudopediaArticle getArticleByID(Long id) throws ArticleNotFoundException {
        Optional<StudopediaArticle> optional = articleRepository.findByIdEquals(id);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        return optional.get();
    }

    public StudopediaArticle getArticleByName(String name) throws ArticleNotFoundException {
        Optional<StudopediaArticle> optional = articleRepository.findByNameLikeAndApprovedTrue(name);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        return optional.get();
    }

    public List<StudopediaArticle> getArticleSuggestionBySubStr(String subString) {
        int FIRST_ARTICLE = 0;
        Pageable pageOneWithFiveElements = PageRequest.of(FIRST_ARTICLE, DEFAULT_SIZE);
        return articleRepository.findByNameLikeIgnoreCaseAndApprovedTrue(subString, pageOneWithFiveElements);
    }

    public List<StudopediaArticle> getArticleByCategory(String category, int page) throws ArticleNotFoundException {
        Pageable pageWithFiveElements = PageRequest.of(page, DEFAULT_SIZE);
        Optional<Category> categoryOptional = categoryRepository.findByNameLikeIgnoreCase(category);
        if (categoryOptional.isEmpty()) throw new ArticleNotFoundException();
        return articleRepository.findByCategoryEqualsAndApprovedTrue(categoryOptional.get(), pageWithFiveElements);
    }

    public List<StudopediaArticle> getArticleByCategoryWithSize(String category, int page, int pageSize) throws ArticleNotFoundException {
        Pageable pages = PageRequest.of(page, pageSize);
        Optional<Category> categoryOptional = categoryRepository.findByNameLikeIgnoreCase(category);
        if (categoryOptional.isEmpty()) throw new ArticleNotFoundException();
        return articleRepository.findByCategoryEqualsAndApprovedTrue(categoryOptional.get(), pages);
    }

    public List<StudopediaArticle> getAllApprovedArticles(){
        return articleRepository.findByApprovedTrue(Pageable.unpaged());
    }

    public List<StudopediaArticle> getAllNotApprovedArticles(){
        return articleRepository.findByApprovedFalse();
    }

    public List<StudopediaArticle> getAllArticles() {
        return articleRepository.findAll();
    }

    public long getArticleCount() {
        return articleRepository.count();
    }

    public StudopediaArticle getRandomArticle() throws ArticleNotFoundException {
        List<StudopediaArticle> allArticles = getAllArticles();
        Random random = new Random();
        if (allArticles.size() == 0) {
            throw new ArticleNotFoundException();
        }
        int randomIndex = random.nextInt(allArticles.size());
        return allArticles.get(randomIndex);
    }

    @Transactional
    public void addArticle(String title, String content, String category, Long userId) throws DataValidationException {
        ValidationUtils.validate(title, (val) -> articleRepository.findByNameLikeAndApprovedTrue(val).isPresent(), ARTICLE_EXISTS);
        Category categoryObj = getOrCreateCategory(category);
        System.out.println(principal.getUsername());
        articleRepository.save(new StudopediaArticle(
                title,
                content,
                categoryObj,
                userId
        ));
    }

    @Transactional
    public Category getOrCreateCategory(String categoryName) {
        Optional<Category> optional = categoryRepository.findByNameLikeIgnoreCase(categoryName);
        Category category;
        if (optional.isEmpty()) {
            category = new Category(categoryName);
            categoryRepository.save(category);
        }
        else {
            category = optional.get();
        }
        return category;
    }


    public void approveAnArticle(Long articleId) throws ArticleExistsException, ArticleNotFoundException {
        Optional<StudopediaArticle> optional = articleRepository.findByIdEqualsAndApprovedTrue(articleId);
        if (optional.isPresent()) throw new ArticleExistsException();
        optional = articleRepository.findByIdEquals(articleId);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        if (optional.get().getApproved()) throw new ArticleExistsException();
        optional.get().approve();
        articleRepository.save(optional.get());
    }
}

package com.roclh.commentmodule.services;

import com.roclh.commentmodule.repositories.ArticleRepository;
import com.roclh.commentmodule.repositories.CommentRepository;
import org.roclh.common.exceptions.CommentNotFoundException;
import com.roclh.mainmodule.entities.Comment;
import com.roclh.mainmodule.entities.CommentKey;
import com.roclh.mainmodule.entities.StudopediaArticle;
import org.roclh.common.exceptions.ArticleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class CommentService {

    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    @Autowired
    public CommentService(ArticleRepository articleRepository, CommentRepository commentRepository){
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public void addComment(Long articleId, Long accountId, String comment) throws ArticleNotFoundException {
        log.info("CommentService: adding a new comment to article with id {}", articleId);
        Optional<StudopediaArticle> article = articleRepository.findById(articleId);
        if (article.isEmpty()) throw new ArticleNotFoundException();
        StudopediaArticle article1 = article.get();
        Comment comment1 = new Comment(article1, accountId , comment);
        commentRepository.save(comment1);
    }

    public void deleteComment(Long articleId, Long accountId, Long commentId) throws ArticleNotFoundException, CommentNotFoundException {
        log.info("CommentService: deleting comment to article with id {}", articleId);
        Optional<StudopediaArticle> article = articleRepository.findById(articleId);
        if (article.isEmpty()) throw new ArticleNotFoundException();
        CommentKey key = new CommentKey(commentId, article.get().getId(), accountId);
        Optional<Comment> comment = commentRepository.findById(key);
        if (comment.isEmpty()) throw new CommentNotFoundException();
        long deletedNumber = commentRepository.deleteByIdAndAccountIdAndArticleId(commentId, articleId, accountId);
        log.info("CommentService: deleted {} comments", deletedNumber);
    }
}

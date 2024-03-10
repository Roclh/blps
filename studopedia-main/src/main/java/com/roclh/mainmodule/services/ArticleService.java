package com.roclh.mainmodule.services;

import com.roclh.mainmodule.database.AccountDatabase;
import com.roclh.mainmodule.database.StudopediaDatabase;
import com.roclh.mainmodule.entities.Account;
import com.roclh.mainmodule.entities.StudopediaArticle;
import com.roclh.mainmodule.messaging.JMSMessage.AddCommentMessage;
import com.roclh.mainmodule.messaging.JMSMessage.DeleteCommentMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.roclh.common.exceptions.ArticleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Objects;
import java.util.Optional;

@Service
@Slf4j
@AllArgsConstructor
public class ArticleService {
    private final StudopediaDatabase studopediaDatabase;
    private final AccountDatabase accountDatabase;
    private final JmsTemplate jmsTemplate;


    @Transactional
    public void upAnArticle(Long articleId, Long accountId) throws ArticleNotFoundException {
        log.info("Account Service: got a job to up an article");
        Optional<StudopediaArticle> optional = studopediaDatabase.findByIdEquals(articleId);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        Optional<Account> accountOptional = accountDatabase.findByIdEquals(accountId);
        if (accountOptional.isEmpty()) throw new UsernameNotFoundException("No user found");
        StudopediaArticle article = optional.get();
        Account account = accountOptional.get();

        if (article.getUppedUsers().contains(account)){
            article.getUppedUsers().remove(account);
            article.removeUp();
        }
        else{
            article.getUppedUsers().add(account);
            article.addUp();
        }
        studopediaDatabase.save(article);
    }

    @Transactional
    public void downAnArticle(Long articleId, Long accountId) throws ArticleNotFoundException {
        log.info("Account Service: got a job to down an article");
        Optional<StudopediaArticle> optional = studopediaDatabase.findByIdEquals(articleId);
        if (optional.isEmpty()) throw new ArticleNotFoundException();
        Optional<Account> accountOptional = accountDatabase.findByIdEquals(accountId);
        if (accountOptional.isEmpty()) throw new UsernameNotFoundException("No user found");
        StudopediaArticle article = optional.get();
        Account account = accountOptional.get();

        if (article.getDownedUsers().contains(account)){
            article.getDownedUsers().remove(account);
            article.removeDown();
        }
        else{
            article.getDownedUsers().add(account);
            article.addDown();
        }
        studopediaDatabase.save(article);
    }

    @Transactional
    public void deleteArticle(Long articleId, Long accountId) throws ArticleNotFoundException {
        log.info("Account Service: got a job to delete an article");
        Optional<StudopediaArticle> article = studopediaDatabase.findByIdEquals(articleId);
        if (article.isEmpty()) throw new ArticleNotFoundException();
        if (!Objects.equals(article.get().getAccountId(), accountId)) throw new ArticleNotFoundException();
        studopediaDatabase.delete(article.get());
    }

    public void commentArticle(Long articleId, Long accountId, String commentString) throws UsernameNotFoundException {
        log.info("Account Service: sending Comment to CommentService to ADD");
        Optional<Account> account = accountDatabase.findByIdEquals(accountId);
        if (account.isEmpty()) throw new UsernameNotFoundException("User not found");
        AddCommentMessage addCommentMessage = new AddCommentMessage(articleId, accountId, commentString);
        jmsTemplate.convertAndSend("addQueue", addCommentMessage.toString());
    }

    public void deleteComment(Long articleId, Long accountId, Long commentId) throws UsernameNotFoundException {
        log.info("Account Service: sending Comment to CommentService to DELETE");
        Optional<Account> account = accountDatabase.findByIdEquals(accountId);
        if (account.isEmpty()) throw new UsernameNotFoundException("User not found");
        DeleteCommentMessage deleteCommentMessage = new DeleteCommentMessage(articleId, accountId, commentId);
        jmsTemplate.convertAndSend("deleteQueue", deleteCommentMessage.toString());
    }


}

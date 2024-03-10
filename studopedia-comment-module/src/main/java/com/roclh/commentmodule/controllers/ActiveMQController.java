package com.roclh.commentmodule.controllers;

import org.roclh.common.exceptions.CommentNotFoundException;
import com.roclh.commentmodule.services.CommentService;
import org.roclh.common.exceptions.ArticleNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
@Slf4j
public class ActiveMQController {

    private final CommentService commentService;

    @Autowired
    public ActiveMQController(CommentService commentService) {
        this.commentService = commentService;
    }

    @JmsListener(destination = "addQueue")
    public void addComment(String message) throws ArticleNotFoundException {
        log.info("Received " + message);
        String[] result = message.split(";");
        log.info("Array: " + Arrays.toString(result));
        Long articleId = Long.parseLong(result[0]);
        Long accountId = Long.parseLong(result[1]);
        String comment = result[2];
        commentService.addComment(articleId, accountId, comment);

    }

    @JmsListener(destination = "deleteQueue")
    public void deleteComment(String  message) throws ArticleNotFoundException, CommentNotFoundException {
        log.info("Received " + message);
        String[] result = message.split(";");
        log.info("Array: " + Arrays.toString(result));
        Long articleId = Long.parseLong(result[0]);
        Long accountId = Long.parseLong(result[1]);
        Long commentId = Long.parseLong(result[2]);
        commentService.deleteComment(articleId, accountId, commentId);
    }
}

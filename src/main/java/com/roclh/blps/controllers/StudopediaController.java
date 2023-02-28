package com.roclh.blps.controllers;

import com.roclh.blps.entities.StudopediaArticle;
import com.roclh.blps.service.StudopediaService;
import com.roclh.blps.utils.HttpResponseErrorMessages;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.inject.Inject;


@Controller
@ApiResponses({
        @ApiResponse(code = HttpServletResponse.SC_BAD_REQUEST, message = HttpResponseErrorMessages.BAD_REQUEST)
})
public class StudopediaController {

    private static final Logger log = LoggerFactory.getLogger(StudopediaController.class);
    private final StudopediaService service;

    @Inject
    public StudopediaController(StudopediaService service){
        log.info("Initializing Studopedia Controller");
        this.service = service;
    }

    @RequestMapping(path = "api/article/get-article", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "Get article by name")
    public StudopediaArticle getArticle(@RequestBody @ApiParam("Article name") String articleName,
                                        HttpServletResponse response){
        log.info("Received request to get article with name {}", articleName);
        return service.getArticle(articleName);
    }

}
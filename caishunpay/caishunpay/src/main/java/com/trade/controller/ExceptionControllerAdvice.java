package com.trade.controller;

import com.trade.bean.response.Response;
import com.trade.enums.ResponseEnum;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class ExceptionControllerAdvice {
    private static Logger log = LoggerFactory.getLogger(ExceptionControllerAdvice.class);

    @ExceptionHandler(Exception.class)
    @ResponseBody
    public Object ExceptionHandler(Exception e) {
        log.error("exception occured", e);
        e.printStackTrace();
        if (e instanceof IllegalArgumentException || e instanceof BindException) {
            return Response.with(ResponseEnum.FAIL_PARAM);
        }
        return Response.with(ResponseEnum.FAIL_SYSTEM);
    }
}

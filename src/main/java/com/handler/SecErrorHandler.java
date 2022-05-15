package com.handler;

import com.alibaba.fastjson.JSON;
import com.entity.SecRespondBody;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletResponse;

@ControllerAdvice
public class SecErrorHandler {
    @ExceptionHandler(value = Exception.class)
    @ResponseBody
    public void defaultErrorHandler(HttpServletResponse response,Exception e) throws Exception {
        SecRespondBody responseBody = new SecRespondBody();
        if (e instanceof org.springframework.web.servlet.NoHandlerFoundException) {
            responseBody.setStatus("404");
            responseBody.setMsg("Page not found");
        }else {
            responseBody.setStatus("500");
            responseBody.setMsg("No handler!");
        }
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

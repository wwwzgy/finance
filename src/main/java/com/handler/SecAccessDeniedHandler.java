package com.handler;

import com.alibaba.fastjson.JSON;
import com.entity.SecRespondBody;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {
        SecRespondBody responseBody = new SecRespondBody();
        responseBody.setStatus("003");
        responseBody.setMsg("Need authorities!");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

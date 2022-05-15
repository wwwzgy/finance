package com.handler;

import com.alibaba.fastjson.JSON;
import com.entity.SecRespondBody;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class SecLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        SecRespondBody responseBody = new SecRespondBody();
        responseBody.setStatus("00");
        responseBody.setMsg("Logout Successfully!");
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

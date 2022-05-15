package com.handler;

import com.alibaba.fastjson.JSON;
import com.entity.*;
import com.repository.MenuInterface;
import com.repository.UserInterface;
import com.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class SecAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
    @Autowired
    UserInterface userInterface;
    @Autowired
    MenuInterface menuInterface;
    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        response.setContentType("text/html;charset=UTF-8");
        User user = (User) authentication.getPrincipal();
        String userid=user.getUsername();
        SecRespondBody responseBody = new SecRespondBody();
        responseBody.setStatus("000");
        responseBody.setMsg("Login Success!");
        responseBody.setName(user.getName()); //用户姓名
        List<Role> roles= userInterface.getRolesByUserId(userid); //返回用户所有角色
        List <String> menuName=new ArrayList<>();
        List <Menu> menus= null;
        if(roles.size()!=0) {
            for(Role role: roles) {
                menus=menuInterface.getMenuByRoleId(role.getId());  //根据角色获取其有权限的菜单
                if(menus.size()!=0){
                    for(Menu element: menus){
                        menuName.add(element.getName());
                    }
                }
            }
            responseBody.setMenu(menuName);  //返回有权限的菜单
        }
        String jwtToken = JwtTokenUtils.generateToken(userid, 3000);  //token有效时间3000秒
        responseBody.setJwtToken(jwtToken);
        response.getWriter().write(JSON.toJSONString(responseBody));
    }
}

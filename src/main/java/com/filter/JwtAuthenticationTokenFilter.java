package com.filter;

import com.service.SecUserDetails;
import com.utils.JwtTokenUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class JwtAuthenticationTokenFilter extends OncePerRequestFilter {
    @Autowired
    SecUserDetails userDetailsService;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //请求头为 Authorization
        //请求体为 Bearer token
        String authHeader = request.getHeader("Authorization");  //获取请求头
        if (authHeader != null && authHeader.startsWith("Bearer ")) {  //如果请求头不为空，且以Bearer 开头

            final String authToken = authHeader.substring("Bearer ".length());  //获取令牌

            String username = JwtTokenUtils.parseToken(authToken);  //从令牌中获取用户名
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {  //如果用户认证信息不为空
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);
                if (userDetails != null) {
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request, response);  //请求头为空，或重新认证不通过，则直接进入下一个过滤链
    }
}

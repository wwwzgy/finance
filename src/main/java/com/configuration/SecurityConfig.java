package com.configuration;

import com.component.MetaDataSource;
import com.filter.JwtAuthenticationTokenFilter;
import com.filter.LoginFilter;
import com.handler.SecAccessDeniedHandler;
import com.handler.SecAuthenticationEntryPoint;
import com.handler.SecAuthenticationSuccessHandler;
import com.handler.SecLogoutSuccessHandler;
import com.service.SecUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.UrlAuthorizationConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {
    @Autowired
    private SecUserDetails secUserDetails;
    @Autowired
    private SecAuthenticationEntryPoint authenticationEntryPoint;
    @Autowired
    private JwtAuthenticationTokenFilter jwtAuthenticationTokenFilter;
    @Autowired
    private SecLogoutSuccessHandler logoutSuccessHandler;
    @Autowired
    private SecAccessDeniedHandler accessDeniedHandler;
    @Autowired
    private SecAuthenticationSuccessHandler authenticationSuccessHandler;
    @Autowired
    private MetaDataSource metadataSource;

    //身份验证的Filter
    @Bean
    LoginFilter loginFilter() throws Exception{
        LoginFilter loginFilter=new LoginFilter();
        loginFilter.setAuthenticationManager(authenticationManagerBean());
        loginFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        return loginFilter;
    }

    //选择加密方法Bcrypt方法
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * 安全项设置
     * @param http metadataSource 是uri与角色的对应信息
     * @throws Exception
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        ApplicationContext applicationContext=http.getSharedObject(ApplicationContext.class);
        //动态权限资源配置，metadataSource作为配置来源
        http.apply(new UrlAuthorizationConfigurer<>(applicationContext) )
                .withObjectPostProcessor(new ObjectPostProcessor<FilterSecurityInterceptor>() {
                    @Override
                    public <O extends FilterSecurityInterceptor> O postProcess(O object) {
                        object.setSecurityMetadataSource(metadataSource);
                        //object.setRejectPublicInvocations(true); //所要请求路径都要在资源里配置才可访问（包括匿名访问）
                        return object;
                    }
                });

        //无状态登录，取消session
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .httpBasic().authenticationEntryPoint(authenticationEntryPoint) //未登录处理
                .and()
                .authorizeRequests()
                .antMatchers("/auth","/error","/getkey","/getKaptchaCode").permitAll() //自定义登录入口、错误返回
                .and()
                .logout().logoutSuccessHandler(logoutSuccessHandler).permitAll()
                .and()
                .csrf().disable();

        //勾选“记住我”时
        http.rememberMe().rememberMeParameter("remember-me")
                .userDetailsService(secUserDetails).tokenValiditySeconds(300);

        //身份验证异常处理
        http.exceptionHandling().accessDeniedHandler(accessDeniedHandler);

        //请求资源时，如果带了token令牌，则使用jwt的Authentication进行认证
        http.addFilterBefore(jwtAuthenticationTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // 禁用headers缓存
        http.headers().cacheControl();
    }
}

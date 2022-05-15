package com.component;

import com.entity.Resource;
import com.repository.ResourceInterface;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.access.SecurityConfig;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterInvocationSecurityMetadataSource;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;

import java.util.Collection;
import java.util.List;

@Component
public class MetaDataSource implements FilterInvocationSecurityMetadataSource {
    @Autowired
    ResourceInterface resourceInterface;
    AntPathMatcher antPathMatcher = new AntPathMatcher();
    @Override
    public Collection<ConfigAttribute> getAttributes(Object object) throws IllegalArgumentException {
        String requestURI = ((FilterInvocation) object).getRequest().getRequestURI();
        List<Resource> URLs = resourceInterface.getAllUrl();

        for(Resource resource: URLs){
            if (antPathMatcher.match(resource.getUrl(), requestURI)){
                String[] roles = resource.getRoles().stream().map(r -> r.getName()).toArray(String[]::new);
                return SecurityConfig.createList(roles);
            }
        }
        return null;
    }
    /**
     * spring security 在启动时校验相关配置是否正确，如果不需要校验，则返回null
     * @return 返回所有定义好的权限资源
     */
    @Override
    public Collection<ConfigAttribute> getAllConfigAttributes() {
        return null;
    }
    /**
     * 是否支持校验
     * @param clazz
     * @return
     */
    @Override
    public boolean supports(Class<?> clazz) {
        return FilterInvocation.class.isAssignableFrom(clazz);
    }
}

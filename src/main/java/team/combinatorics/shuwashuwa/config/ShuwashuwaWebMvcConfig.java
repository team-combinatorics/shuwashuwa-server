package team.combinatorics.shuwashuwa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.combinatorics.shuwashuwa.interceptor.AuthorizationInterceptor;
import team.combinatorics.shuwashuwa.utils.PropertiesConstants;

@Configuration
@DependsOn("constants")
public class ShuwashuwaWebMvcConfig implements WebMvcConfigurer {

    private AuthorizationInterceptor authorizationInterceptor;

    private final String IMAGE_STORAGE = PropertiesConstants.PIC_STORAGE_DIR;

    @Autowired
    public void setAuthorizationInterceptor(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/test/auth/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/img/**").addResourceLocations("file:"+IMAGE_STORAGE);
    }
}

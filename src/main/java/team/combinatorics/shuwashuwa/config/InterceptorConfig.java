package team.combinatorics.shuwashuwa.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.combinatorics.shuwashuwa.interceptor.AuthorizationInterceptor;

@Configuration
@PropertySource(value = {"classpath:shuwashuwa.properties"})
public class InterceptorConfig implements WebMvcConfigurer {

    private AuthorizationInterceptor authorizationInterceptor;

    private String IMAGE_STORAGE;

    @Autowired
    public void setAuthorizationInterceptor(AuthorizationInterceptor authorizationInterceptor) {
        this.authorizationInterceptor = authorizationInterceptor;
    }

    @Value("${dir.pictures}")
    public void setIMAGE_STORAGE(String IMAGE_STORAGE) {
        this.IMAGE_STORAGE = IMAGE_STORAGE;
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

package team.combinatorics.shuwashuwa.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import team.combinatorics.shuwashuwa.interceptor.AuthorizationInterceptor;

@Configuration
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private final AuthorizationInterceptor authorizationInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry){
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/api/**");
        registry.addInterceptor(authorizationInterceptor).addPathPatterns("/test/auth/**");
    }
}

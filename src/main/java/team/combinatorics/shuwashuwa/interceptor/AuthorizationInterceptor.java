package team.combinatorics.shuwashuwa.interceptor;

import com.auth0.jwt.JWT;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import team.combinatorics.shuwashuwa.annotation.PassToken;
import team.combinatorics.shuwashuwa.annotation.UserLoginToken;
import team.combinatorics.shuwashuwa.service.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Member;
import java.lang.reflect.Method;

@Component
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {
    public static final String LOGIN_USER_KEY = "LOGIN_USER_KEY";

    UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws Exception {
        String token = httpServletRequest.getHeader("token");

        if(!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否有ignore注释
        if(method.isAnnotationPresent(PassToken.class)){
            PassToken passToken = method.getAnnotation(PassToken.class);
            return passToken.required();
        }

        // 检查是否需要权限
        if (method.isAnnotationPresent(UserLoginToken.class)){
            UserLoginToken userLoginToken = method.getAnnotation(UserLoginToken.class);
            //TODO: 应该为这里定义一个token
            if(token == null)
                throw new RuntimeException("无token，请重新登录");
//            // 获取token中的userName
//            String username = JWT.decode(token).getAudience().get(0);
//            UserInfoDTO user = userService.findUserByName(username);
//
//            JWTVerifier jwtVerifier = JWT.require(Algorithm.HMAC256(username)).build();
//            try{
//                jwtVerifier.verify(token);
//            }catch (JWTVerificationException e){
//                throw new RuntimeException("401");
//            }
            return true;
        }



        return false;
    }
}

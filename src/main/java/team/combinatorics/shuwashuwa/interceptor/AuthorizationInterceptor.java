package team.combinatorics.shuwashuwa.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import team.combinatorics.shuwashuwa.annotation.AdminOnly;
import team.combinatorics.shuwashuwa.annotation.ClientOnly;
import team.combinatorics.shuwashuwa.annotation.NoLogin;
import team.combinatorics.shuwashuwa.annotation.VolunteerOnly;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.GlobalException;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final int client = 1;
    private static final int volunteer = 2;
    private static final int admin = 4;
    private static final int su = 8;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws GlobalException {
        String token = httpServletRequest.getHeader("token");

        if(!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否需要用户登录
        if (method.isAnnotationPresent(NoLogin.class))
            return true;
        if(token==null) {
            throw new GlobalException(ErrorInfoEnum.TOKEN_LOST);
        }

        // 验证签名，同时取出权限码
        // 可能因为签名篡改或过期而抛出异常
        int tokenAuthority = TokenUtil.verifyToken(token).get("authority").asInt();
        if(method.isAnnotationPresent(AdminOnly.class) && (tokenAuthority & admin) == 0) {
            throw new GlobalException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
        }
        if(method.isAnnotationPresent(ClientOnly.class) && (tokenAuthority & client) == 0) {
            throw new GlobalException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
        }
        if(method.isAnnotationPresent(VolunteerOnly.class) && (tokenAuthority & volunteer) == 0) {
            throw new GlobalException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
        }
        return true;
    }
}

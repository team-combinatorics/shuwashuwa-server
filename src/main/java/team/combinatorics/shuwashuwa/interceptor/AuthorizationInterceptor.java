package team.combinatorics.shuwashuwa.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import team.combinatorics.shuwashuwa.annotation.AdminAccess;
import team.combinatorics.shuwashuwa.annotation.ClientAccess;
import team.combinatorics.shuwashuwa.annotation.NoToken;
import team.combinatorics.shuwashuwa.annotation.VolunteerAccess;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    //按位或之后得到一个用户的身份
    private static final int CLIENT = 1;
    private static final int VOLUNTEER = 2;
    private static final int ADMIN = 4;
    private static final int SU = 8;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object handler) throws KnownException {
        String token = httpServletRequest.getHeader("token");

        if(!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否需要用户登录
        if (method.isAnnotationPresent(NoToken.class))
            return true;
        if(token==null) {
            throw new KnownException(ErrorInfoEnum.TOKEN_LOST);
        }

        // 验证签名，同时取出权限码
        // 可能因为签名篡改或过期而抛出异常
        int tokenAuthority = TokenUtil.verifyToken(token).get("authority").asInt();

        if(method.isAnnotationPresent(AdminAccess.class) && (tokenAuthority & ADMIN) != 0 ||
                method.isAnnotationPresent(ClientAccess.class) && (tokenAuthority & CLIENT) != 0 ||
                method.isAnnotationPresent(VolunteerAccess.class) && (tokenAuthority & VOLUNTEER) != 0)
            return true;
        else throw new KnownException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
    }
}

package team.combinatorics.shuwashuwa.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import team.combinatorics.shuwashuwa.annotation.*;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.pojo.User;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

@Component
@AllArgsConstructor
public class AuthorizationInterceptor implements HandlerInterceptor {

    //用于读取权限
    private UserDao userDao;

    @Autowired
    public void setUserDao(UserDao userDao) {
        this.userDao = userDao;
    }

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
        Integer userid = TokenUtil.verifyToken(token).get("userid").asInt();

        //检查权限
        User currentUser = userDao.findUserByUserid(userid);
        if(method.isAnnotationPresent(AllAccess.class) ||
            method.isAnnotationPresent(AdminAccess.class) && currentUser.is_admin() ||
            method.isAnnotationPresent(VolunteerAccess.class) && currentUser.is_volunteer() ||
            method.isAnnotationPresent(SUAccess.class) && currentUser.is_su())
            return true;
        else throw new KnownException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
    }
}

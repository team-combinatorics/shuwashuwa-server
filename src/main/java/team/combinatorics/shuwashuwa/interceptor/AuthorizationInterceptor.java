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
import team.combinatorics.shuwashuwa.model.po.UserPO;
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

        if (!(handler instanceof HandlerMethod))
            return true;

        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        // 检查是否需要用户登录
        if (method.isAnnotationPresent(NoToken.class))
            return true;
        if (token == null) {
            throw new KnownException(ErrorInfoEnum.TOKEN_LOST);
        }

        // 验证签名，同时取出权限码
        // 可能因为签名篡改或过期而抛出异常
        Integer userid = TokenUtil.verifyToken(token).get("userid").asInt();

        //检查权限
        UserPO currentUserPO = userDao.getUserByUserid(userid);

        if (currentUserPO == null)
            throw new KnownException(ErrorInfoEnum.USER_NOT_EXISTING);

        if (!method.isAnnotationPresent(AllAccess.class) &&
                !(method.isAnnotationPresent(AdminAccess.class) && currentUserPO.getAdmin()) &&
                !(method.isAnnotationPresent(VolunteerAccess.class) && currentUserPO.getVolunteer()) &&
                !(method.isAnnotationPresent(SUAccess.class) && currentUserPO.getSu()) )
        throw new KnownException(ErrorInfoEnum.AUTHORITY_UNMATCHED);

        //检查参数
        if(method.isAnnotationPresent(UserParam.class) && !currentUserPO.getAdmin() && !currentUserPO.getVolunteer()) {
            String paraName = method.getAnnotation(UserParam.class).value();
            String paraValue = httpServletRequest.getParameter(paraName);
            if(paraValue == null || !Integer.valueOf(paraName).equals(userid))
                throw new KnownException(ErrorInfoEnum.AUTHORITY_UNMATCHED);
        }

        return true;
    }
}

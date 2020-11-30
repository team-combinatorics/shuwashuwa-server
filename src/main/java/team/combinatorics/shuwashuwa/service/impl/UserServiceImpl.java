package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;
import team.combinatorics.shuwashuwa.model.pojo.User;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

@PropertySource("classpath:wx.properties")
@Component
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    //private final RestTemplate restTemplate;

    private final WechatUtil wechatUtil;

    private final UserDao userDao;

//    public UserServiceImpl(WechatUtil wechatUtil, UserDao userDao) {
//        //this.restTemplate = restTemplate;
//        this.wechatUtil = wechatUtil;
//        this.userDao = userDao;
//    }

    @Override
    public int deleteOneUser(int userid) {
        System.out.println("要删除用户的userid为：" + userid);
        return userDao.deleteUserByUserid(userid);
    }

    @Override
    public void deleteAllUsers() {
        userDao.deleteAllUsers();
    }

    @Override
    public LogInSuccessDto wechatLogin(LogInInfoDto logInInfoDto) throws Exception {
        JsonNode root = wechatUtil.getWechatInfo(logInInfoDto.getCode());
        String openid = root.path("openid").asText();
        String sessionKey = root.path("session_key").asText();
        LogInSuccessDto logInSuccessDto = new LogInSuccessDto();
        User user = userDao.findUserByOpenid(openid);
        if (user == null) {
            logInSuccessDto.setFirstLogin(true);
            userDao.addUserOpenid(openid);
            user = userDao.findUserByOpenid(openid);
        } else
            logInSuccessDto.setFirstLogin(false);
        String token = TokenUtil.createToken(user.getUserid());
        logInSuccessDto.setToken(token);
        return logInSuccessDto;
    }

    @Override
    public void updateUserInfo(int userid, UpdateUserInfoDto updateUserInfoDto) {
        System.out.println("即将更新用户信息");
        System.out.println("待更新的用户userid为：" + userid);
        System.out.println(updateUserInfoDto.toString());
        userDao.updateUserInfo(userid, updateUserInfoDto);
    }

    @Override
    public User getUserInfo(int userid) {
        System.out.println("想要获取" + userid + "的信息");
        User user = userDao.findUserByUserid(userid);
        user.setOpenid("你无权获取openid");
        return user;
    }


    @Override
    public String test(LogInInfoDto logInInfoDto) throws Exception {
        JsonNode root = wechatUtil.getWechatInfo(logInInfoDto.getCode());
        // 如果返回中有错误码且不等于零说明出错
        if (root.has("errcode") && root.path("errcode").asInt() != 0)
            return "Yes " + root.toString();
        else {
            String openid = root.path("openid").asText();
            String sessionKey = root.path("session_key").asText();
            userDao.addUserOpenid(openid);
            return userDao.findUserByOpenid(openid).toString();
        }
    }

}

package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDTO;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDTO;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;
import team.combinatorics.shuwashuwa.model.pojo.UserDO;
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
    public LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception {
        JsonNode root = wechatUtil.getWechatInfo(logInInfoDto.getCode());
        String openid = root.path("openid").asText();
        String sessionKey = root.path("session_key").asText();
        LogInSuccessDTO logInSuccessDto = new LogInSuccessDTO();
        UserDO userDO = userDao.findUserByOpenid(openid);
        if (userDO == null) {
            logInSuccessDto.setFirstLogin(true);
            userDao.addUserOpenid(openid);
            userDO = userDao.findUserByOpenid(openid);
        } else
            logInSuccessDto.setFirstLogin(false);
        String token = TokenUtil.createToken(userDO.getId());
        logInSuccessDto.setToken(token);
        return logInSuccessDto;
    }

    @Override
    public void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) {
        System.out.println("即将更新用户信息");
        System.out.println("待更新的用户userid为：" + userid);
        System.out.println(updateUserInfoDto.toString());
        userDao.updateUserInfo(userid, updateUserInfoDto);
    }

    @Override
    public UserDO getUserInfo(int userid) {
        System.out.println("想要获取" + userid + "的信息");
        UserDO userDO = userDao.findUserByUserid(userid);
        userDO.setOpenid("你无权获取openid");
        return userDO;
    }


    @Override
    public String test(LogInInfoDTO logInInfoDto) throws Exception {
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

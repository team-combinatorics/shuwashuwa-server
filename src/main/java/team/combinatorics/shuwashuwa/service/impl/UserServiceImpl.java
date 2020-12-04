package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.dao.VolunteerApplicationDao;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final WechatUtil wechatUtil;

    private final UserDao userDao;
    private final VolunteerApplicationDao applicationDao;

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
        UserPO userPO = userDao.selectUserByOpenid(openid);
        if (userPO == null) {
            logInSuccessDto.setFirstLogin(true);
            userDao.insertUserByOpenid(openid);
            userPO = userDao.selectUserByOpenid(openid);
        } else
            logInSuccessDto.setFirstLogin(false);
        String token = TokenUtil.createToken(userPO.getId());
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
    public UserPO getUserInfo(int userid) {
        System.out.println("想要获取" + userid + "的信息");
        UserPO userPO = userDao.selectUserByUserid(userid);
        userPO.setOpenid("你无权获取openid");
        return userPO;
    }

    @Override
    public void addVolunteerApplication(int userid, VolunteerApplicationDTO volunteerApplicationDTO) {
        applicationDao.insert(userid,volunteerApplicationDTO);
    }

    @Override
    public List<VolunteerApplicationPO> getUnauditedVolunteerApplicationList() {
        return applicationDao.selectByCondition(SelectApplicationCO.builder().status(0).build());
    }

    @Override
    public void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO) {
        applicationDao.updateApplicationByAdmin(userid,updateDTO);
        if(updateDTO.getStatus() == 1)
            userDao.updateUserVolunteerAuthority(applicationDao.selectByFormId(updateDTO.getFormID()).getUserId(),true);
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
            userDao.insertUserByOpenid(openid);
            return userDao.selectUserByOpenid(openid).toString();
        }
    }

}

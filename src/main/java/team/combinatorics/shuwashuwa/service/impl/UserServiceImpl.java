package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.AllArgsConstructor;
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
        JsonNode root = WechatUtil.getWechatInfo(logInInfoDto.getCode());
        String openid = root.path("openid").asText();
        String sessionKey = root.path("session_key").asText();
        System.out.println("用户登录 @Service");
        System.out.println("openid: " + openid);
        LogInSuccessDTO logInSuccessDto = new LogInSuccessDTO();
        UserPO userPO = userDao.getUserByOpenid(openid);
        if (userPO == null) {
            logInSuccessDto.setFirstLogin(true);
            userDao.insertUserByOpenid(openid);
            userPO = userDao.getUserByOpenid(openid);
        } else
            logInSuccessDto.setFirstLogin(false);
        String token = TokenUtil.createToken(userPO.getId());
        logInSuccessDto.setToken(token);
        return logInSuccessDto;
    }

    @Override
    public void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) {
        userDao.updateUserInfo(userid, updateUserInfoDto);
    }

    @Override
    public UserPO getUserInfo(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        userPO.setOpenid("你无权获取openid");
        return userPO;
    }

    @Override
    public void addVolunteerApplication(int userid, VolunteerApplicationDTO volunteerApplicationDTO) {
        VolunteerApplicationPO.VolunteerApplicationPOBuilder volunteerApplicationPOBuilder
                = VolunteerApplicationPO.builder();
        volunteerApplicationPOBuilder.userId(userid);
        // TODO 这里做合法性判断，例如是否为null
        volunteerApplicationPOBuilder.cardPicLocation(volunteerApplicationDTO.getCardPicLocation());
        volunteerApplicationPOBuilder.reasonForApplication(volunteerApplicationDTO.getReasonForApplication());
        VolunteerApplicationPO volunteerApplicationPO = volunteerApplicationPOBuilder.build();
        applicationDao.insertApplication(volunteerApplicationPO);
    }

    @Override
    public List<VolunteerApplicationPO> getUnauditedVolunteerApplicationList() {
        return applicationDao.listApplicationsByCondition(SelectApplicationCO.builder().status(0).build());
    }

    @Override
    public void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO) {
        applicationDao.updateApplicationByAdmin(userid, updateDTO);
        if (updateDTO.getStatus() == 1)
            userDao.updateUserVolunteerAuthority(applicationDao.getApplicationByFormId(updateDTO.getFormID()).getUserId(), true);
    }

    @Override
    public String test(LogInInfoDTO logInInfoDto) throws Exception {
        JsonNode root = WechatUtil.getWechatInfo(logInInfoDto.getCode());
        // 如果返回中有错误码且不等于零说明出错
        if (root.has("errcode") && root.path("errcode").asInt() != 0)
            return "Yes " + root.toString();
        else {
            String openid = root.path("openid").asText();
            String sessionKey = root.path("session_key").asText();
            userDao.insertUserByOpenid(openid);
            return userDao.getUserByOpenid(openid).toString();
        }
    }

}
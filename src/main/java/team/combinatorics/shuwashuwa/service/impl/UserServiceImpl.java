package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDTO;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDTO;
import team.combinatorics.shuwashuwa.model.dto.UserInfoResponseDTO;
import team.combinatorics.shuwashuwa.model.dto.UserInfoUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserDao userDao;
    ImageStorageService imageStorageService;

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
        String openid = WechatUtil.getOpenID(logInInfoDto.getCode());
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
    public void updateUserInfo(int userid, UserInfoUpdateDTO userInfoUpdateDto) {
        if (DTOUtil.fieldAllNull(userInfoUpdateDto))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        userDao.updateUserInfo(userid, userInfoUpdateDto);
    }

    @Override
    public UserInfoResponseDTO getUserInfo(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        return (UserInfoResponseDTO) DTOUtil.convert(userPO, UserInfoResponseDTO.class);
    }

    @Override
    public boolean isPlainUser(int userid) {
        UserPO userPO = userDao.getUserByUserid(userid);
        return !userPO.getVolunteer() && !userPO.getAdmin() && !userPO.getSu();
    }
}
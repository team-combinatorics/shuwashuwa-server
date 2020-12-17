package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.*;

import java.util.List;

public interface UserService {
    LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UserInfoUpdateDTO userInfoUpdateDto) throws Exception;

    UserInfoResponseDTO getUserInfo(int userid) throws Exception;

}

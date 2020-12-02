package team.combinatorics.shuwashuwa.service;

import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDTO;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDTO;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;
import team.combinatorics.shuwashuwa.model.po.UserPO;

@Service
public interface UserService {

    LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) throws Exception;

    UserPO getUserInfo(int userid) throws Exception;

    // boolean suicide();

    String test(LogInInfoDTO logInInfoDto) throws Exception;
}

package team.combinatorics.shuwashuwa.service;

import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;
import team.combinatorics.shuwashuwa.model.pojo.User;

@Service
public interface UserService {

    LogInSuccessDto wechatLogin(LogInInfoDto logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UpdateUserInfoDto updateUserInfoDto) throws Exception;

    User getUserInfo(int userid) throws Exception;

    // boolean suicide();

    String test(LogInInfoDto logInInfoDto) throws Exception;
}

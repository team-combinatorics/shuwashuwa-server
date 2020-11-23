package team.combinatorics.shuwashuwa.service;

import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;

@Service
public interface UserService {

    LogInSuccessDto wechatLogin(LogInInfoDto logInInfoDto) throws Exception;

    // boolean suicide();

    String test(LogInInfoDto logInInfoDto) throws Exception;

    void updateUserInfo(int openid, UpdateUserInfoDto updateUserInfoDto);
}

package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

import java.util.List;

public interface UserService {
    LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) throws Exception;

    UpdateUserInfoDTO getUserInfo(int userid) throws Exception;

    void addVolunteerApplication(int userid, VolunteerApplicationDTO volunteerApplicationDTO);

    List<VolunteerApplicationPO> listUnauditedVolunteerApplication();

    void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO);

}

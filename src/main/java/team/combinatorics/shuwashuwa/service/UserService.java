package team.combinatorics.shuwashuwa.service;

import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.model.dto.*;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;

import java.util.List;

@Service
public interface UserService {

    LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UpdateUserInfoDTO updateUserInfoDto) throws Exception;

    UserPO getUserInfo(int userid) throws Exception;

    void addVolunteerApplication(int userid, VolunteerApplicationDTO volunteerApplicationDTO);

    List<VolunteerApplicationPO> getUnauditedVolunteerApplicationList();

    void completeApplicationAudition(int formid, int userid, VolunteerApplicationUpdateDTO updateDTO);

    // boolean suicide();

    String test(LogInInfoDTO logInInfoDto) throws Exception;
}

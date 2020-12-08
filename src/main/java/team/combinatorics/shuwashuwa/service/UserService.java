package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.*;

import java.util.List;

public interface UserService {
    LogInSuccessDTO wechatLogin(LogInInfoDTO logInInfoDto) throws Exception;

    int deleteOneUser(int userid) throws Exception;

    void deleteAllUsers();

    void updateUserInfo(int userid, UserInfoDTO userInfoDto) throws Exception;

    UserInfoDTO getUserInfo(int userid) throws Exception;

    void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO volunteerApplicationAdditionDTO);

    List<VolunteerApplicationResponseForAdminDTO> listUnauditedVolunteerApplication();

    List<VolunteerApplicationResultDTO> listVolunteerApplicationOf(int userid);

    void completeApplicationAudition(int userid, VolunteerApplicationUpdateDTO updateDTO);

}

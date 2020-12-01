package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;
import team.combinatorics.shuwashuwa.model.pojo.UserDO;

@Component
public interface UserDao {

    void insertUserByOpenid(@Param("openid") String openid);

    UserDO selectUserByOpenid(@Param("openid") String openid);

    UserDO selectUserByUserid(@Param("id") Integer id);

    // 一个通用的更新方法，使用特定的DTO传输
    void updateUserInfo(@Param("id") Integer id, @Param("user") UpdateUserInfoDTO user);

    Integer deleteUserByUserid(@Param("id") Integer id);

    Integer deleteUserByOpenid(@Param("openid") String openid);

    void deleteAllUsers();
}

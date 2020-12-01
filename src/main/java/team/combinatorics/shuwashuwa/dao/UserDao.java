package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;
import team.combinatorics.shuwashuwa.model.pojo.UserDO;

@Mapper
@Component(value = "userDao")
public interface UserDao {
    @Insert("insert into user(openid) values (#{openid})")
    void addUserOpenid(String openid);

    @Select("SELECT * FROM user where openid=#{openid}")
    UserDO findUserByOpenid(String openid);

    UserDO findUserByUserid(@Param("id") Integer id);

//    // 一个通用的更新方法，使用xml实现
//    void updateUserInfo(@Param("user") UserDO user);

    // 一个通用的更新方法，使用特定的DTO传输
    void updateUserInfo(@Param("id") int id, @Param("user") UpdateUserInfoDTO user);

    @Delete("DELETE FROM user where userid=#{userid}")
    Integer deleteUserByUserid(int userid);

    @Delete("DELETE FROM user where openid=#{openid}")
    Integer deleteUserByOpenid(String openid);

    @Delete("DELETE FROM user")
    void deleteAllUsers();
}

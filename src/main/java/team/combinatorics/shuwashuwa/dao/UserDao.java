package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.pojo.User;

@Mapper
@Component(value = "userDao")
public interface UserDao {
    @Insert("insert into user(openid) values (#{openid})")
    void addUserOpenid(String openid);

    @Select("SELECT * FROM user where openid=#{openid}")
    User findUserByOpenid(String openid);

    @Select("SELECT * FROM user where userid=#{userid}")
    User findUserByUserid(int userid);

    // 一个通用的更新方法，使用xml实现
    void updateUserInfo(@Param("user") User user);

    @Delete("DELETE FROM user where userid=#{userid}")
    Integer deleteUserByUserid(int userid);

    @Delete("DELETE FROM user where openid=#{openid}")
    Integer deleteUserByOpenid(String openid);

    @Delete("DELETE FROM user")
    void deleteAllUsers();
}

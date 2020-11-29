package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.pojo.User;

@Mapper
@Component(value = "userDao")
public interface UserDao {
    @Insert("insert into user_info(openid, authority) values (#{openid}, 1)")
    void addUserOpenid(String openid);

    @Select("SELECT * FROM user_info where openid=#{openid}")
    User findUserByOpenid(String openid);

    @Select("SELECT * FROM user_info where userid=#{userid}")
    User findUserByUserid(int userid);

    @Select("SELECT authority FROM user_info where userid=#{userid}")
    Integer getAuthorityByUserid(int userid);

    @Delete("DELETE FROM user_info where userid=#{userid}")
    Integer deleteUserByUserid(int userid);

    @Delete("DELETE FROM user_info where openid=#{openid}")
    Integer deleteUserByOpenid(String openid);

    @Delete("DELETE FROM user_info")
    void deleteAllUsers();
}

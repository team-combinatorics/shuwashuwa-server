package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.pojo.User;

@Mapper
@Component(value = "userDao")
public interface UserDao {
    @Insert("insert into user_info(openid) values (#{openid})")
    void addUserOpenid(String openid);

    @Select("SELECT * FROM user_info where openid=#{openid}")
    User findUserByOpenid(String openid);

    @Delete("DELETE FROM user_info where openid=#{openid}")
    int deleteUserByOpenid(String openid);

    @Delete("DELETE FROM user_info")
    void deleteAllUsers();
}

package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDTO;
import team.combinatorics.shuwashuwa.model.po.UserPO;

@Repository
public interface UserDao {

    /**
     * 通过用户id添加用户，由于openid具有唯一性，需要保证没有重复的openid才能添加
     * @param openid 微信提供的openid
     */

    void insertUserByOpenid(@Param("openid") String openid);

    /**
     * 通过openid查找用户
     * @param openid 微信提供的openid
     * @return 一个UserDO类
     */
    UserPO getUserByOpenid(@Param("openid") String openid);

    /**
     * 通过userid选择用户
     * @param id userid
     * @return UserDO对象
     */
    UserPO getUserByUserid(@Param("id") int id);

    /**
     * 更新用户基本信息，updateUserInfoDTO中不为null的属性会被更新，但是要保证updateUserInfoDTO不能全空，否则会出错
     * @param id userid
     * @param updateUserInfoDTO 需要更新的信息
     */
    void updateUserInfo(@Param("id") int id, @Param("user") UpdateUserInfoDTO updateUserInfoDTO);

    /**
     * 更新用户的志愿者权限，只能由超级管理员或管理员使用
     * @param id 用户id
     * @param volunteer 是否为志愿者
     */
    void updateUserVolunteerAuthority(@Param("id") int id, @Param("volunteer") boolean volunteer);

    /**
     * 更新用户的管理员权限，只能由超级管理员使用
     * @param id 用户id
     * @param admin 是否为管理员
     */
    void updateUserAdminAuthority(@Param("id") int id, @Param("admin") boolean admin);

    /**
     * 根据userid删除用户
     * @param id userid
     * @return 删除用户的数量，在该系统设计中，只会为0或者1
     */
    int deleteUserByUserid(@Param("id") int id);

    /**
     * 根据openid删除用户
     * @param openid openid
     * @return 删除用户的数量，在该系统设计中，只会为0或者1
     */
    int deleteUserByOpenid(@Param("openid") String openid);

    /**
     * 删除所有用户
     * @return 删除用户的数量，在该系统设计中，只会为0或者1
     */
    int deleteAllUsers();
}

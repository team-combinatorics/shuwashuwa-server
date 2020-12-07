package team.combinatorics.shuwashuwa.service;

import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;

import java.util.List;

public interface SuperAdministratorService {

    /**
     * 超级管理员登录
     * @param userName 输入的超级管理员用户名
     * @param password 输入的超级管理员密码
     * @return 超管的token
     */
    String checkInfo(String userName, String password);

    /**
     * 超级管理员修改密码
     * @param oldPassword 原始密码
     * @param newPassword 新密码
     * @return true 修改成功
     *         false 修改失败--应为原密码输入错误导致的
     */
    boolean changePassword(String oldPassword, String newPassword);

    /**
     * 超级管理员添加新管理员
     * @param adminDTO 待添加的管理员信息
     * @return 成功添加应该返回1
     */
    int addAdministrator(AdminDTO adminDTO);

    /**
     * 超级管理员获取管理员列表
     * @return 管理员列表
     */
    List<AdminPO> getAdministratorList();

    /**
     * 超级管理员删除管理员
     * @param userID 待删除管理员的用户id
     * @return 正常删除应该返回1，出现异常应该返回其他值
     */
    int deleteAdministrator(int userID);

    /**
     * 超级管理员根据用户id获取用户详细信息
     * @param userID 欲获取信息的管理员的用户id
     * @return 管理员的信息体
     */
    AdminDTO getAdministratorInfo(int userID);

    /**
     * 超级管理员根据用户id和传过来的管理员待修改信息更新数据库
     * @param userID 待更新信息的管理员的用户id
     * @return 如果更新成功应该返回修改成功的记录数量（大于0，因为不允许传入的数据全为空）
     */
    int updateAdministratorInfo(int userID, AdminDTO adminDTO);
}

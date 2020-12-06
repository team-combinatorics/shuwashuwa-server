package team.combinatorics.shuwashuwa.service;

public interface SuperAdministratorService {

    /**
     * 超级管理员登录
     * @param userName 输入的超级管理员用户名
     * @param password 输入的超级管理员密码
     * @return true，输入正确，登录成功
     *         false，用户名或密码输入错误，登录失败
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
}

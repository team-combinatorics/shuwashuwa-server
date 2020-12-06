package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.AdminDao;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.utils.MD5Util;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

import java.util.List;

@Service
@AllArgsConstructor
public class SuperAdministratorServiceImpl implements SuperAdministratorService {

    private final UserDao userDao;
    private final AdminDao adminDao;

    @Override
    public String checkInfo(String userName, String password) {
        String md5Password = MD5Util.getMD5(password);
        String realName = userDao.getSuUsername();
        String realPassword = userDao.getSuEncryptedPSW();

        if(realName.equals(userName) && realPassword.equals(md5Password))
            return TokenUtil.createToken(1);
        return null;
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword) {
        String md5OldPassword = MD5Util.getMD5(oldPassword);
        String trueOldPassword = userDao.getSuEncryptedPSW();
        if(md5OldPassword.equals(trueOldPassword)) {
            String newMD5Password = MD5Util.getMD5(newPassword);
            System.out.println("超级管理员请求修改密码");
            System.out.println("old MD5 password is "+trueOldPassword);
            System.out.println("new MD5 password is "+newMD5Password);
            int cnt = userDao.updateSuPSW(newMD5Password);
            return cnt == 1;
        }
        return false;
    }

    @Override
    public int addAdministrator(AdminDTO adminDTO) {
        AdminPO adminPO = AdminPO.builder()
                .userid(adminDTO.getUserid())
                .userName(adminDTO.getUserName())
                .phoneNumber(adminDTO.getPhoneNumber())
                .email(adminDTO.getEmail())
                .identity(adminDTO.getIdentity())
                .department(adminDTO.getDepartment())
                .studentId(adminDTO.getStudentId())
                .build();
        System.out.println(adminPO.getUserid());
        int cnt = adminDao.insert(adminPO);
        if(cnt == 1) {
            userDao.updateUserAdminAuthority(Integer.parseInt(adminDTO.getUserid()), true);
        }
        return cnt;
    }

    @Override
    public List<AdminPO> getAdministratorList() {
        return adminDao.listAdmins();
    }
}

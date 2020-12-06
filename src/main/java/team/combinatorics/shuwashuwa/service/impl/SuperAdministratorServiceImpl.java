package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.utils.MD5Util;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@Service
@AllArgsConstructor
public class SuperAdministratorServiceImpl implements SuperAdministratorService {

    private final UserDao userDao;

    @Override
    public String checkInfo(String userName, String password)
    {
        String md5Password = MD5Util.getMD5(password);

        String realName = userDao.getSuUsername();
        String realPassword = userDao.getSuEncryptedPSW();

        if(realName.equals(userName) && realPassword.equals(md5Password))
        {
            return TokenUtil.createToken(1);
        }
        else
        {
            return null;
        }
    }

    @Override
    public boolean changePassword(String oldPassword, String newPassword)
    {
        String md5OldPassword = MD5Util.getMD5(oldPassword);
        String trueOldPassword = userDao.getSuEncryptedPSW();
        if(!md5OldPassword.equals(trueOldPassword))
        {
            return false;
        }
        else
        {
            String newMD5Password = MD5Util.getMD5(newPassword);
            System.out.println("old MD5 password is "+trueOldPassword);
            System.out.println("new MD5 password is "+newMD5Password);
            userDao.updateSuPSW(newMD5Password);
            return true;
        }
    }
}

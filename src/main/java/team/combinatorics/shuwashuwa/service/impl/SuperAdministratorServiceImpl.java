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
}

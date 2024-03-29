package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.AdminDao;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.AdminDTO;
import team.combinatorics.shuwashuwa.model.dto.UserInfoResponseDTO;
import team.combinatorics.shuwashuwa.model.po.AdminPO;
import team.combinatorics.shuwashuwa.model.po.UserPO;
import team.combinatorics.shuwashuwa.service.SuperAdministratorService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;
import team.combinatorics.shuwashuwa.utils.MD5Util;
import team.combinatorics.shuwashuwa.utils.TokenUtil;
import team.combinatorics.shuwashuwa.utils.WechatUtil;

import java.util.List;
import java.util.Vector;

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
        AdminPO adminPO = DTOUtil.convert(adminDTO,AdminPO.class);
        System.out.println("用户"+adminPO.getUserid()+"将成为管理员");
        Integer cnt = adminDao.insert(adminPO);
        if(cnt==null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        if(cnt == 1) {
            userDao.updateUserAdminAuthority(adminDTO.getUserid(), true);
        }
        return cnt;
    }

    @Override
    public List<AdminDTO> getAdministratorList() {
        List<AdminPO> list = adminDao.listAdmins();
        List<AdminDTO> returnList = new Vector<>();
        for(AdminPO adminPO:list) {
            AdminDTO adminDTO = DTOUtil.convert(adminPO,AdminDTO.class);
            returnList.add(adminDTO);
        }
        return returnList;
    }

    @Override
    public int deleteAdministrator(int userID) {
        int adminID = adminDao.getAdminIDByUserID(userID);
        userDao.updateUserAdminAuthority(userID, false);
        Integer cnt =  adminDao.deleteByID(adminID);
        if(cnt == null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        return cnt;
    }

    @Override
    public AdminDTO getAdministratorInfo(int userID) {
        int adminID = adminDao.getAdminIDByUserID(userID);
        AdminPO adminPO = adminDao.getByID(adminID);
        if(adminPO == null)
            return null;
        return DTOUtil.convert(adminPO,AdminDTO.class);
    }

    @Override
    public int updateAdministratorInfo(AdminDTO adminDTO) {
        int adminID = adminDao.getAdminIDByUserID(adminDTO.getUserid());
        AdminPO adminPO = DTOUtil.convert(adminDTO,AdminPO.class);
        adminPO.setId(adminID);
        Integer cnt =  adminDao.update(adminPO);
        if(cnt==null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        return cnt;
    }

    @Override
    public byte[] getWechatQRCode(int activityID) throws Exception {
        return WechatUtil.generateAppCode(activityID);
    }

    /* from UserServiceImpl.java #L60 */
    @Override
    public UserInfoResponseDTO getUserInfo(int userID){
        UserPO userPO = userDao.getUserByUserid(userID);
        return DTOUtil.convert(userPO, UserInfoResponseDTO.class);
    }
}

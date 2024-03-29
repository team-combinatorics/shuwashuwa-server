package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import team.combinatorics.shuwashuwa.dao.AdminDao;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.dao.VolunteerApplicationDao;
import team.combinatorics.shuwashuwa.dao.VolunteerDao;
import team.combinatorics.shuwashuwa.dao.co.SelectApplicationCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.bo.VolunteerApplicationDetailBO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAuditDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerDTO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.model.po.VolunteerPO;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.VolunteerService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

import java.util.List;
import java.util.Vector;

@Service
@AllArgsConstructor
public class VolunteerServiceImpl implements VolunteerService {
    private final VolunteerApplicationDao volunteerApplicationDao;
    private final VolunteerDao volunteerDao;
    private final ImageStorageService imageStorageService;
    private final UserDao userDao;
    private final AdminDao adminDao;

    /**
     * 申请成为志愿者
     *
     * @param userid                          要申请成为志愿者的用户id
     * @param volunteerApplicationAdditionDTO 申请时的补充信息，包括申请理由和学生证照片
     */
    @Override
    public synchronized void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO volunteerApplicationAdditionDTO) {
        //参数检查
        if (DTOUtil.fieldExistNull(volunteerApplicationAdditionDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //筛选已有为审核申请
        SelectApplicationCO duplicateCO = SelectApplicationCO.builder()
                .userId(userid)
                .status(0)
                .build();
        int countOldApplication = volunteerApplicationDao.listApplicationDetailByCondition(duplicateCO).size();
        //如果已经是志愿者，或提交过申请但没被审核，不允许申请
        if (userDao.getUserByUserid(userid).getVolunteer() || countOldApplication > 0)
            throw new KnownException(ErrorInfoEnum.DUPLICATED_PROMOTION);

        //插入新的申请
        VolunteerApplicationPO volunteerApplicationPO =
                DTOUtil.convert(volunteerApplicationAdditionDTO, VolunteerApplicationPO.class);

        volunteerApplicationPO.setUserId(userid);

        volunteerApplicationDao.insert(volunteerApplicationPO);

        //存档图片。所用图片阅后即焚，所以设置为一次性使用
        imageStorageService.setDisposableUse(userid, volunteerApplicationAdditionDTO.getCardPicLocation());
    }

    /**
     * 根据条件获取摘要信息列表
     *
     * @param selectApplicationCO 由controller构造的条件结构
     * @return 申请表摘要信息列表
     */
    @Override
    public List<VolunteerApplicationDetailBO>
    listVolunteerApplicationByCondition(SelectApplicationCO selectApplicationCO) {
        return volunteerApplicationDao.listApplicationDetailByCondition(selectApplicationCO);
    }

    /**
     * 根据申请表id获取申请表的详细信息
     *
     * @param formId 申请表的id
     */
    @Override
    public VolunteerApplicationDetailBO getApplicationDetailByFormId(int formId) {
        // 这里要检查一些什么呢？
        return volunteerApplicationDao.getApplicationDetailByFormId(formId);
    }

    /**
     * 管理员完成维修单的填写
     *
     * @param adminUserId 管理员的用户id
     * @param auditDTO    管理员回复的结构
     * @return 新增志愿者的志愿者id
     */
    @Override
    public int completeApplicationByAdmin(int adminUserId, VolunteerApplicationAuditDTO auditDTO) {
        // 获取管理员id
        int adminID = adminDao.getAdminIDByUserID(adminUserId);
        // 判断申请表更新数据是否完整
        if (auditDTO.getFormId() == null || auditDTO.getStatus() == null || auditDTO.getReplyByAdmin() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // 如果申请状态为成功，则用户的信息都不能缺少
        if (auditDTO.getStatus() == 1 && DTOUtil.fieldExistNull(auditDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        // 随便设置个尝试上限
        for (int i = 0; i < 3; i++) {
            // 首先取出当前的申请表结构
            VolunteerApplicationPO volunteerApplicationPO = volunteerApplicationDao.getApplicationByFormId(auditDTO.getFormId());

            // 状态不是待审核，更新失败
            if (volunteerApplicationPO.getStatus() != 0)
                throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);

            // 更新申请表状态，附带上次更新时间信息用于判断版本
            int returnValue = volunteerApplicationDao.updateApplicationByAdmin(adminID,
                    auditDTO,
                    volunteerApplicationPO.getUpdatedTime());

            // 返回值为1说明更新成功，然后进行之后的处理
            if (returnValue == 1) {
                imageStorageService.delete(volunteerApplicationPO.getCardPicLocation());
                if (auditDTO.getStatus() == 1) {
                    // 将用户插入志愿者表中
                    VolunteerPO volunteerPO = DTOUtil.convert(auditDTO, VolunteerPO.class);
                    volunteerPO.setUserid(volunteerApplicationPO.getUserId());
                    volunteerDao.insert(volunteerPO);
                    // 更新用户的志愿者权限
                    userDao.updateUserVolunteerAuthority(volunteerApplicationPO.getUserId(), true);
                    return volunteerPO.getId();
                }
                return 0;
            }
        }
        // 理论上不可能运行到这里
        throw new KnownException(ErrorInfoEnum.SERVICE_TIMEOUT);
    }

    @Override
    public Integer getVolunteerIdByUserid(Integer userid) {
        return volunteerDao.getVolunteerIDByUserID(userid);
    }

    @Override
    public int addVolunteer(VolunteerDTO volunteerDTO) {
        // 转换为PO
        VolunteerPO volunteerPO = DTOUtil.convert(volunteerDTO, VolunteerPO.class);
        System.out.println("用户"+volunteerPO.getUserid()+"将被设置为志愿者");
        // 插入并检查结果
        Integer cnt = volunteerDao.insert(volunteerPO);
        if(cnt==null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        if(cnt == 1) {
            userDao.updateUserVolunteerAuthority(volunteerDTO.getUserid(), true);
        }
        return cnt;
    }

    @Override
    public List<VolunteerDTO> getVolunteerList() {
        // 获取原始PO列表
        List<VolunteerPO> volunteerList = volunteerDao.listVolunteers();
        // 转换为DTO列表
        List<VolunteerDTO> returnList = new Vector<>();
        for(VolunteerPO volunteerPO:volunteerList) {
            VolunteerDTO volunteerDTO = DTOUtil.convert(volunteerPO,VolunteerDTO.class);
            returnList.add(volunteerDTO);
        }
        return returnList;
    }

    @Override
    public int deleteVolunteer(int userID) {
        // 先根据用户id获取志愿者id
        int volunteerID = volunteerDao.getVolunteerIDByUserID(userID);
        // 再在用户列表中修改权限
        userDao.updateUserVolunteerAuthority(userID, false);
        // 最后删除志愿者表中的内容
        Integer cnt =  volunteerDao.deleteByID(volunteerID);
        if(cnt == null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        return cnt;
    }

    @Override
    public VolunteerDTO getVolunteerInfo(int userID) {
        // 先获取志愿者id
        int volunteerID = volunteerDao.getVolunteerIDByUserID(userID);
        // 再用志愿者id获取信息
        VolunteerPO volunteerPO = volunteerDao.getByID(volunteerID);
        if(volunteerPO == null)
            return null;
        return DTOUtil.convert(volunteerPO, VolunteerDTO.class);
    }

    @Override
    public int updateVolunteerInfo(VolunteerDTO volunteerDTO) {
        // 先获取志愿者id
        int volunteerID = volunteerDao.getVolunteerIDByUserID(volunteerDTO.getUserid());
        // 转换为PO结构
        VolunteerPO volunteerPO = DTOUtil.convert(volunteerDTO, VolunteerPO.class);
        volunteerPO.setId(volunteerID);
        // 插入并检查
        Integer cnt =  volunteerDao.update(volunteerPO);
        if(cnt==null)
            throw new KnownException(ErrorInfoEnum.WRONG_ADD_OR_DELETE);
        return cnt;
    }
}




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
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAbstractDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationAdditionDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationDetailDTO;
import team.combinatorics.shuwashuwa.model.dto.VolunteerApplicationUpdateDTO;
import team.combinatorics.shuwashuwa.model.po.VolunteerApplicationPO;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.service.VolunteerService;
import team.combinatorics.shuwashuwa.utils.DTOUtil;

import java.util.List;

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
    public void addVolunteerApplication(int userid, VolunteerApplicationAdditionDTO volunteerApplicationAdditionDTO) {
        //参数检查
        if (DTOUtil.fieldExistNull(volunteerApplicationAdditionDTO))
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);

        //重复申请检查
        if (userDao.getUserByUserid(userid).getVolunteer())
            throw new KnownException(ErrorInfoEnum.DUPLICATED_PROMOTION);

        //插入新的申请
        VolunteerApplicationPO volunteerApplicationPO =
                (VolunteerApplicationPO) DTOUtil.convert(volunteerApplicationAdditionDTO, VolunteerApplicationPO.class);

        volunteerApplicationPO.setUserId(userid);

        volunteerApplicationDao.insert(volunteerApplicationPO);

        //存档图片
        imageStorageService.setUseful(volunteerApplicationAdditionDTO.getCardPicLocation());
    }

    /**
     * 根据条件获取摘要信息列表
     *
     * @param selectApplicationCO 由controller构造的条件结构
     * @return 申请表摘要信息列表
     */
    @Override
    public List<VolunteerApplicationAbstractDTO>
    listVolunteerApplicationByCondition(int userid, SelectApplicationCO selectApplicationCO) {
        // TODO 以下是一些待完成的权限检查
        // TODO 当前用户是一般通过用户时，只能查自己的维修单，强行把CO里的user id设为用户自己的userid即可，不管传来是什么
        // TODO 以及一些其他条件？

        return volunteerApplicationDao.listApplicationAbstractByCondition(selectApplicationCO);
    }

    /**
     * 根据申请表id获取申请表的详细信息
     *
     * @param formId 申请表的id
     */
    @Override
    public VolunteerApplicationDetailDTO getApplicationDetailByFormId(int formId) {
        // 这里要检查一些什么呢？
        return volunteerApplicationDao.getApplicationDetailByFormId(formId);
    }

    /**
     * TODO 这里要完善这个update结构
     * 管理员完成维修单的填写
     *
     * @param adminUserId 管理员的用户id
     * @param updateDTO   管理员回复的结构
     */
    @Override
    public void completeApplicationByAdmin(int adminUserId, VolunteerApplicationUpdateDTO updateDTO) {
        // 获取管理员id
        int adminID = adminDao.getAdminIDByUserID(adminUserId);
        // 判断更新数据是否完整
        if (updateDTO.getFormID() == null || updateDTO.getStatus() == null || updateDTO.getReplyByAdmin() == null)
            throw new KnownException(ErrorInfoEnum.PARAMETER_LACKING);
        // TODO 这里的细节问题之后再实现
        // 随便设置个尝试上限
        for (int i = 0; i < 3; i++) {
            // 首先取出当前的结构
            VolunteerApplicationPO volunteerApplicationPO = volunteerApplicationDao.getApplicationByFormId(updateDTO.getFormID());

            // 如果状态已经被更新过，则不再更新
            if (volunteerApplicationPO.getStatus() != 0)
                throw new KnownException(ErrorInfoEnum.STATUS_UNMATCHED);
            // 更新状态，附带上次更新时间信息
            int returnValue = volunteerApplicationDao.updateApplicationByAdmin(adminID,
                    updateDTO,
                    volunteerApplicationPO.getUpdatedTime());

            // 返回值为1说明更新成功，然后进行之后的处理
            if (returnValue == 1) {
                System.out.println("成功了？");
                imageStorageService.delete(volunteerApplicationPO.getCardPicLocation());
                if (updateDTO.getStatus() == 1) {
                    Integer promotedUserId = volunteerApplicationDao.getApplicationByFormId(updateDTO.getFormID()).getUserId();
                    userDao.updateUserVolunteerAuthority(promotedUserId, true);
                }
                return;
            }
        }
        // TODO 如果运行到这里（有一说一不太应该），或许应该定义个什么错误
        throw new KnownException(ErrorInfoEnum.SERVICE_TIMEOUT);
    }
}




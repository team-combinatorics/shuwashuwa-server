package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;
import team.combinatorics.shuwashuwa.model.pojo.ServicePic;

import java.util.List;

@Component
public interface ServicePicDao {
    /**
     * 在图片库中插入一个图片位置
     * 在插入完成之后，可以用get方法读取传入的ServicePicPO获得的图片id，因此很方便
     *
     * @param servicePicPO 设置了location和service form id的对象
     *
     */
    void insertServicePic(@Param("pic")ServicePicPO servicePicPO);

    /**
     * 通过图片id获取一张图片的位置
     * @param id 图片id
     * @return 一个ServicePic结构，该结构中不包括图片关联的service form id
     */
    ServicePic selectByPicId(@Param("id") int id);

    /**
     *
     * @param serviceFormId 维修单的id
     * @return 维修单关联的图片列表
     */
    List<ServicePic> selectByServiceFormId(@Param("formID")int serviceFormId);
}

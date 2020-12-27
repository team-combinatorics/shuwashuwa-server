package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;

import java.util.List;

@Repository
public interface ServicePicDao {
    /**
     * 在图片库中插入一个图片位置
     * 在插入完成之后，可以用get方法读取传入的ServicePicPO获得的图片id，因此很方便
     *
     * @param servicePicPO 设置了location和service form id的对象
     * @return 插入成功的数量，如果为0表示不成功
     */
    int insert(@Param("pic") ServicePicPO servicePicPO);

    /**
     * @param serviceFormId 维修单的id
     * @return 维修单关联的图片列表
     */
    List<String> listServicePicsByFormId(@Param("formID") int serviceFormId);

    /**
     * 根据图片id删除
     *
     * @param id 要删除图片的id
     * @return 删除的数量，本方法中只会为0或1，返回0说明要删除的图片记录不存在，然而这种情况并不合理
     */
    int deleteByPicId(@Param("id") int id);

    /**
     * 根据维修单id删除图片信息
     *
     * @param serviceFormId 要删除图片的为u维修单id
     * @return 删除的数量
     */
    int deleteByServiceFormId(@Param("formID") int serviceFormId);
}

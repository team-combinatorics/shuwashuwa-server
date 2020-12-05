package team.combinatorics.shuwashuwa.dao;

import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;
import team.combinatorics.shuwashuwa.dao.co.CachePicCO;
import team.combinatorics.shuwashuwa.model.po.CachePicPO;

import java.util.List;

@Repository
public interface CachePicDao {
    /**
     * 插入一张图片，完成之后，cachePicPO中的id会被填充为插入后得到的id
     * 原则上，cachePicPO里面的location和userid是必填项
     *
     * @param cachePicPO 一个cachePicPO结构
     */
    void insert(@Param("cachePic") CachePicPO cachePicPO);

    /**
     * 根据cache图片id获取用户id
     *
     * @param cacheID 图片id
     * @return 用户id，当该图片不存在时会返回null
     */
    Integer getUserIDByCacheID(@Param("cacheID") int cacheID);

    /**
     * 根据id获取图片
     *
     * @param id 图片id
     * @return 图片
     */
    CachePicPO getCachePicByID(@Param("id") int id);

    /**
     * 根据location获取图片
     *
     * @param location 图片location
     * @return 图片
     */
    CachePicPO getCachePicByLocation(@Param("location") String location);

    /**
     * 返回当前cache pic中的记录数量
     * @return 记录数
     */
    int countCachePic();

    /**
     * 根据条件获取图片，条件包括：开始时间，结束时间，用户id
     * @param selectCachePicCondition 条件
     * @return 图片列表
     */
    List<CachePicPO> listCachePicsByCondition(@Param("condition") CachePicCO selectCachePicCondition);

    /**
     * 字面意思
     *
     * @param cacheID 图片id
     * @return 删除了数量，0或者1
     */
    int deleteByID(@Param("cacheID") int cacheID);

    /**
     * 根据条件删除照片，条件不允许全为null，如果全null，不会删除任何图片
     *
     * @param deleteCachePicCondition 条件
     * @return 删除的数量
     */
    int deleteByCondition(@Param("condition") CachePicCO deleteCachePicCondition);

}

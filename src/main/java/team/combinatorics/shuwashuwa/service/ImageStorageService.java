package team.combinatorics.shuwashuwa.service;

import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.model.pojo.ServicePic;

import java.security.Provider;

public interface ImageStorageService {

    /**
     * 存储一个文件
     * @param userid 发起图片上传的用户id
     * @param file 要存储的文件
     * @return 包含缓存id和文件路径的对象
     */
    ServicePic store(int userid, MultipartFile file);

    /**
     * 删除一个缓存中的图片
     * @param userid 发起图片删除的用户id
     * @param cacheId 图片在缓存表中的id
     */
    void removeFromCache(int userid, int cacheId);

    /**
     * 确认一个图片与维修单关联，从而去除缓存属性
     * @param userid 上传该图片的用户id
     * @param cacheId 图片在缓存表中的id
     */

    ServicePic confirm(int userid, int cacheId, int formId);

    /**
     * 清除一个用户的所有缓存图片
     * @param userid 释放缓存空间的用户id
     */
    void clearCache(int userid);

    /**
     * 删除指定天数前上传的所有缓存图片
     * @param days 最近days天的缓存会被保留
     */
    void clearCacheByTime(int days);

    /**
     * 删除指定天数前上传的所有图片
     * @param days 最近days天的图片会被保留
     */
    void clearAllImagesByTime(int days);
}

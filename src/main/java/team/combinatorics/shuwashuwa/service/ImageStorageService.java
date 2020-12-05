package team.combinatorics.shuwashuwa.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    /**
     * 存储一个文件
     * @param userid 发起图片上传的用户id
     * @param file 要存储的文件
     * @return 包含缓存id和文件路径的对象
     */
    String store(int userid, MultipartFile file);

    /**
     * 删除一个缓存中的图片
     * @param userid 上传该图片的用户id
     * @param path 图片路径
     */
    void removeFromCache(int userid, String path);

    /**
     * 确认一个图片与维修单关联，从而去除缓存属性
     * @param userid 上传该图片的用户id
     * @param path 图片路径
     */
    void bindWithService(int userid, String path, int formId);

    /**
     * 暴力地删除一张图片，不会进行任何检查
     * @param path 图片路径
     */
    void delete(String path);

    /**
     * 清除一个用户的所有缓存图片
     * @param userid 释放缓存空间的用户id
     */
    void clearCache(int userid);

    /**
     * 删除指定天数前上传的未使用的图片
     * @param days 最近days天的图片会被保留
     */
    void clearCacheByTime(int days);

    /**
     * 查询缓存用量
     * @return 未使用图片总数
     */
    int countCacheImages();

}

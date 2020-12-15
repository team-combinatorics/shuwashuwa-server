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
     * 将一张图片与维修单关联，若该图片在缓存表中，则会移出缓存队列
     * @param path 图片路径
     * @param formId 关联到的维修单id
     */
    void bindWithService(String path, int formId);

    /**
     * 将一张图片从缓存队列中移出，避免被清理
     * 务必保证图片可记录于其他表中再调用
     * @param path 图片路径
     */
    void setUseful(String path);

    /**
     * 不再需要一张图片，若图片在缓存队列中，则删除
     * @param userid 请求删除的用户id，用于防止恶意删除
     * @param path 图片路径
     */
    void setUseless(int userid, String path);

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

    public String[] listImages();

}

package team.combinatorics.shuwashuwa.service;

import org.springframework.web.multipart.MultipartFile;

public interface ImageStorageService {

    /**
     * 存储一个文件
     * @param userid 发起图片上传的用户id
     * @param file 要存储的文件
     * @return 文件被存储到的路径
     */
    String store(int userid, MultipartFile file);

    /**
     * 删除一个文件
     * @param userid 发起图片删除的用户id
     * @param path 待删除的文件路径
     */
    void removeFromCache(int userid, String path);

    /**
     * 确认一个文件与维修单关联，从而去除缓存属性
     * @param userid 释放缓存空间的用户id
     * @param path 待处理的文件路径
     */
    void confirm(int userid, String path);
}

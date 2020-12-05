package team.combinatorics.shuwashuwa.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.dao.CachePicDao;
import team.combinatorics.shuwashuwa.dao.ServicePicDao;
import team.combinatorics.shuwashuwa.dao.co.CachePicCO;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.po.CachePicPO;
import team.combinatorics.shuwashuwa.model.po.ServicePicPO;
import team.combinatorics.shuwashuwa.model.pojo.ServicePic;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.PropertiesConstants;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@RequiredArgsConstructor
@DependsOn("constants")
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final Path STORAGE_DIR = PropertiesConstants.PIC_STORAGE_DIR;

    private static final int SINGLE_USER_CACHE_LIMIT = 6;

    private CachePicDao cachePicDao;
    private ServicePicDao servicePicDao;

    @SuppressWarnings("")
    private static void removeFileFromDisk(String path) {
        new File(path).delete();
    }

    @Override
    public synchronized ServicePic store(int userid, MultipartFile file) throws KnownException {
        //生成随机唯一的文件名，但保留后缀
        String receivedFileName = file.getOriginalFilename();
        assert receivedFileName != null;
        String fileType = receivedFileName.substring(receivedFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+fileType;
        Path path = STORAGE_DIR.resolve(fileName);

        //尝试存储
        try {
            file.transferTo(path);
        } catch (IOException ioe) {
            throw new KnownException(ErrorInfoEnum.STORAGE_FAILURE);
        }

        //检查缓存图片队列
        List<CachePicPO> userCacheList = cachePicDao.selectByCondition(CachePicCO.builder().userId(userid).build());
        while(userCacheList.size() >= SINGLE_USER_CACHE_LIMIT) {
            CachePicPO victim = userCacheList.remove(0);
            cachePicDao.deleteByID(victim.getId());
            removeFileFromDisk(victim.getPicLocation());
        }

        //插入缓存图片队列
        CachePicPO cachePicPO = CachePicPO.builder()
                .userId(userid)
                .picLocation(path.toString())
                .build();
        cachePicDao.insertCachePic(cachePicPO);

        //构造返回对象
        return new ServicePic(cachePicPO.getUserId(),cachePicPO.getPicLocation());
    }

    @Override
    public void removeFromCache(int userid, int cacheId) {
        //检查权限
        Integer useridRequired = cachePicDao.selectUserIDByCacheID(cacheId);
        if(useridRequired == null || userid != useridRequired)
            throw new KnownException(ErrorInfoEnum.IMAGE_NOT_CACHED);

        //删除图片
        CachePicPO picPO = cachePicDao.selectByID(cacheId);
        removeFileFromDisk(picPO.getPicLocation());

        //从缓存表中移除
        cachePicDao.deleteByID(cacheId);
    }

    @Override
    public ServicePic confirm(int userid, int cacheId, int formId) {
        //检查是否已缓存
        Integer useridRequired = cachePicDao.selectUserIDByCacheID(cacheId);
        if(useridRequired == null)
            return null;
        //TODO: 确认一下图片复用时前端传回的数据结构
        else if(userid != useridRequired)
            throw new KnownException(ErrorInfoEnum.IMAGE_NOT_CACHED);

        //插入记录表
        CachePicPO cachePicPO = cachePicDao.selectByID(cacheId);
        ServicePicPO servicePicPO = ServicePicPO.builder()
                .serviceFormId(formId)
                .picLocation(cachePicPO.getPicLocation())
                .build();
        servicePicDao.insertServicePic(servicePicPO);

        //从缓存表中移除
        cachePicDao.deleteByID(cacheId);

        //返回结构
        return ServicePic.builder().id(servicePicPO.getId()).picLocation(servicePicPO.getPicLocation()).build();
    }

    @Override
    public void clearCache(int userid) {
        //获取列表
        List<CachePicPO> userCacheList = cachePicDao.selectByCondition(CachePicCO.builder().userId(userid).build());

        //逐个删除
        for (CachePicPO picPO:userCacheList) {
            removeFileFromDisk(picPO.getPicLocation());
        }

        //整理缓存表
        cachePicDao.deleteByCondition(CachePicCO.builder().userId(userid).build());
    }

    @Override
    public void clearCacheByTime(int days) {
        //获取列表
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(days));
        List<CachePicPO> userCacheList = cachePicDao.selectByCondition(CachePicCO.builder().endTime(timestamp).build());

        //逐个删除
        for (CachePicPO picPO:userCacheList) {
            removeFileFromDisk(picPO.getPicLocation());
        }

        //整理缓存表
        cachePicDao.deleteByCondition(CachePicCO.builder().endTime(timestamp).build());
    }

    @Override
    public void clearAllImagesByTime(int days) {
        //首先清缓存
        clearCacheByTime(days);

        //TODO: 其次清有用的
    }
}

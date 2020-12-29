package team.combinatorics.shuwashuwa.service.impl;

import lombok.AllArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
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
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.PropertiesConstants;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.*;

@DependsOn("constants")
@AllArgsConstructor
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final String STORAGE_DIR = PropertiesConstants.PIC_STORAGE_DIR;

    private static final int SINGLE_USER_CACHE_LIMIT = 6;

    private final CachePicDao cachePicDao;
    private final ServicePicDao servicePicDao;

    @SuppressWarnings("")
    @Override
    public void delete(String fileName) {
        new File(fullPath(fileName)).delete();
        new File(fullPath("100_"+fileName)).delete();
    }

    @Override
    public synchronized String store(int userid, MultipartFile file) throws KnownException {
        File dir = new File(STORAGE_DIR);
        if(!dir.exists())
            dir.mkdir();

        //生成随机唯一的文件名，但保留后缀
        String receivedFileName = file.getOriginalFilename();
        assert receivedFileName != null;
        String fileType = receivedFileName.substring(receivedFileName.lastIndexOf("."));
        String fileName = UUID.randomUUID().toString()+ fileType;
        String path = fullPath(fileName);

        //尝试存储
        try {
            file.transferTo(new File(path));
            Thumbnails.of(path).size(100,100).toFile(STORAGE_DIR+ "100_" + fileName);
        } catch (IOException ioe) {
            throw new KnownException(ErrorInfoEnum.STORAGE_FAILURE);
        }


        //检查缓存图片队列
        List<CachePicPO> userCacheList = cachePicDao.listCachePicsByCondition(CachePicCO.builder().userId(userid).build());
        while(userCacheList.size() >= SINGLE_USER_CACHE_LIMIT) {
            CachePicPO victim = userCacheList.remove(0);
            cachePicDao.deleteByID(victim.getId());
            delete(victim.getPicLocation());
        }

        //插入缓存图片队列
        CachePicPO cachePicPO = CachePicPO.builder()
                .userId(userid)
                .picLocation(fileName)
                .build();
        cachePicDao.insert(cachePicPO);

        return fileName;
    }

    @Override
    public void setUseful(String path) {
        CachePicPO cachePicPO = cachePicDao.getCachePicByLocation(path);
        if(cachePicPO != null)
            cachePicDao.deleteByID(cachePicPO.getId());
        else if(servicePicDao.)
    }

    @Override
    public void setUseless(int userid, String path) {
        //检查权限
        CachePicPO cachePicPO = cachePicDao.getCachePicByLocation(path);
        if(cachePicPO == null)
            return;
        if(cachePicPO.getUserId() != userid)
            throw new KnownException(ErrorInfoEnum.DATA_NOT_YOURS);

        //删除图片
        delete(path);

        //从缓存表中移除
        cachePicDao.deleteByID(cachePicPO.getId());
    }

    @Override
    public void bindWithService(String path, int formId) {
        //若在缓存表中，移除表项
        setUseful(path);

        //插入记录表
        ServicePicPO servicePicPO = ServicePicPO.builder()
                .serviceFormId(formId)
                .picLocation(path)
                .build();
        servicePicDao.insert(servicePicPO);
    }

    @Override
    public void clearCache(int userid) {
        //获取列表
        List<CachePicPO> userCacheList = cachePicDao.listCachePicsByCondition(CachePicCO.builder().userId(userid).build());

        //逐个删除
        for (CachePicPO picPO:userCacheList) {
            delete(picPO.getPicLocation());
        }

        //整理缓存表
        int deleteCnt = cachePicDao.deleteByCondition(CachePicCO.builder().userId(userid).build());
        System.out.println("删除了"+deleteCnt+"张图片");
    }

    @Override
    public void clearCacheByTime(int days) {
        //获取列表
        Timestamp timestamp = Timestamp.valueOf(LocalDateTime.now().minusDays(days));
        List<CachePicPO> userCacheList = cachePicDao.listCachePicsByCondition(CachePicCO.builder().endTime(timestamp).build());

        //逐个删除
        for (CachePicPO picPO:userCacheList) {
            delete(picPO.getPicLocation());
        }

        //整理缓存表
        int deleteCnt = cachePicDao.deleteByCondition(CachePicCO.builder().endTime(timestamp).build());
        System.out.println("删除了"+deleteCnt+"张图片");
    }

    @Override
    public int countCacheImages() {
        return cachePicDao.countCachePic();
    }

    private String fullPath(String fileName) {
        return STORAGE_DIR+fileName;
    }

}

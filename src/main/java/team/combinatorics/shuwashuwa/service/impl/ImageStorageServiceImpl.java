package team.combinatorics.shuwashuwa.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.DependsOn;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.service.ImageStorageService;
import team.combinatorics.shuwashuwa.utils.PropertiesConstants;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

@RequiredArgsConstructor
@DependsOn("constants")
@Service
public class ImageStorageServiceImpl implements ImageStorageService {

    private static final Path STORAGE_DIR = PropertiesConstants.PIC_STORAGE_DIR;

    private static final Map<Integer, List<Path>> unconfirmed = new HashMap<>();

    private static final int SINGLE_USER_CACHE_LIMIT = 6;

    @Override
    @SuppressWarnings("")
    public String store(int userid, MultipartFile file) throws KnownException {
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
        if (unconfirmed.containsKey(userid)) {
            if (unconfirmed.get(userid).size() == SINGLE_USER_CACHE_LIMIT) {
                unconfirmed.get(userid).remove(0).toFile().delete();
            }
        }
        else {
            unconfirmed.put(userid,new Vector<>());
        }

        //插入缓存图片队列
        unconfirmed.get(userid).add(path);

        return path.toString();
    }

    @Override
    @SuppressWarnings("")
    public void removeFromCache(int userid, String path) {
        //删除当然是"确认"图片没有用
        confirm(userid, path);

        Path.of(path).toFile().delete();
    }

    @Override
    public void confirm(int userid, String path) {
        if(!unconfirmed.get(userid).remove(Path.of(path)))
            throw new KnownException(ErrorInfoEnum.IMAGE_NOT_CACHED);
        if(unconfirmed.get(userid).size()==0)
            unconfirmed.remove(userid);
    }
}

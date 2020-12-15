package team.combinatorics.shuwashuwa.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.multipart.MultipartFile;
import team.combinatorics.shuwashuwa.MainApplication;
import team.combinatorics.shuwashuwa.dao.MethodsOfTesting;
import team.combinatorics.shuwashuwa.exception.KnownException;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Vector;

@RunWith(SpringJUnit4ClassRunner.class)
@PropertySource({"classpath:shuwashuwa.properties"})
@SpringBootTest(classes = MainApplication.class)
public class ImageStorageServiceTest {

    ImageStorageService imageStorageService;

    @Value("${dir.pictures:default}")
    String STORAGE_DIR;

    @Autowired
    public void setImageStorageService(ImageStorageService imageStorageService) {
        this.imageStorageService = imageStorageService;
    }

    @Autowired
    MethodsOfTesting methodsOfTesting;

    private boolean exist(String fileName) {
        return new File(STORAGE_DIR+fileName).exists();
    }

    @Test
    public void simpleTest() throws IOException {
        methodsOfTesting.truncateAllTables();
        if(!STORAGE_DIR.endsWith("/"))
            STORAGE_DIR+="/";

        File oriFile = new File(this.getClass().getClassLoader().getResource("testImage.gif").getFile());
        assert oriFile.exists();

        MultipartFile multipartFile = new MockMultipartFile("file","tmp.gif",null, FileCopyUtils.copyToByteArray(oriFile));
        List<String> ls = new Vector<>();
        for(int i=0;i<10;i++)
            ls.add(imageStorageService.store(1,multipartFile));
        assert !exist(ls.get(0));
        assert !exist(ls.get(3));
        assert exist(ls.get(4));
        assert exist("100_"+ls.get(7));
        assert exist(ls.get(8));

        imageStorageService.setUseful(ls.get(5));
        imageStorageService.setUseful(ls.get(5));
        assert exist(ls.get(5));
        ls.add(imageStorageService.store(1,multipartFile));
        assert exist(ls.get(4));
        try {
            imageStorageService.setUseless(2,ls.get(8));
            assert false;
        } catch (KnownException ke) {
            assert ke.getErrCode()==40009;
        }
        for(int i=0;i<10;i++)
            ls.add(imageStorageService.store(2,multipartFile));
        assert exist(ls.get(8));
        imageStorageService.setUseless(1,ls.get(5));
        assert exist(ls.get(5));
        imageStorageService.setUseless(1,ls.get(8));
        assert !exist(ls.get(8));
        imageStorageService.delete(ls.get(5));
        assert !exist(ls.get(5));
        imageStorageService.bindWithService(ls.get(20),33);
        imageStorageService.clearCache(2);
        imageStorageService.setUseless(2,ls.get(20));
        assert exist(ls.get(20));
        for(int i=11;i<20;i++)
            assert !exist(ls.get(i));
        imageStorageService.clearCacheByTime(0);
        imageStorageService.delete(ls.get(20));
        assert new File(STORAGE_DIR).list().length==0;
    }
}

package team.combinatorics.shuwashuwa.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;

@Component(value = "constants")
@PropertySource(value = {"classpath:shuwashuwa.properties"})
public class PropertiesConstants {
    /**
     * 微信小程序的appid
     */
    public static String WX_MINI_PROGRAM_APPID;
    /**
     * 微信小程序的secret
     */
    public static String WX_MINI_PROGRAM_SECRET;

    /**
     * 生成token的secret
     * 有一说一，可以设置每次项目启动时随机生成一个token的secret
     */
    public static String TOKEN_SECRET;

    /**
     * 图片保存的地址
     */
    public static String PIC_STORAGE_DIR;

    /**
     * 微信小程序后端的 access token
     */
    public static String WX_ACCESS_TOKEN;

    /**
     * 微信小程序的通知模板标题-template id组成的pairs
     */
    public static HashMap<String, String> WX_TEMPLATE_IDs;

    @Value("${wx.appid:default}")
    public void setWxMiniProgramAppid(String appid) {
        PropertiesConstants.WX_MINI_PROGRAM_APPID = appid;
    }

    @Value("${wx.secret:default}")
    public void setWxMiniProgramSecret(String secret) {
        PropertiesConstants.WX_MINI_PROGRAM_SECRET = secret;
    }

    @Value("${token.secret:default}")
    public void setTokenSecret(String secret) {
        PropertiesConstants.TOKEN_SECRET = secret;
    }

    @Value("${dir.pictures:default}")
    public void setPicStorageDir(String dir) {
        if(!dir.endsWith("/"))
            dir+="/";
        PropertiesConstants.PIC_STORAGE_DIR = dir;
    }
}

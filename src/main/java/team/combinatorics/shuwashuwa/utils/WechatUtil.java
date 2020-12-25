package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.WechatAppCodeDTO;
import team.combinatorics.shuwashuwa.model.dto.WechatNoticeDTO;

import java.io.*;
import java.util.*;


@Component
@DependsOn("constants")
final public class WechatUtil {

    private static final String APPID = PropertiesConstants.WX_MINI_PROGRAM_APPID;
    private static final String SECRET = PropertiesConstants.WX_MINI_PROGRAM_SECRET;
    private static final RestTemplate restTemplate = new RestTemplate();

    /**
     * 处理对微信的GET请求的通用方法
     * @param url 欲发送GET请求的URL
     * @return 返回响应的JsonNode
     * @throws Exception WECHAT_SERVER_CONNECTION_FAILURE
     */
    public static JsonNode handleGetRequest(String url) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    /**
     * 处理对微信的POST请求的通用方法
     * @param url 欲发送GET请求的URL
     * @return 返回响应的JsonNode
     * @throws Exception WECHAT_SERVER_CONNECTION_FAILURE
     */
    public static JsonNode handlePostRequest(String url, Object obj) throws Exception {
        ResponseEntity<String> response = restTemplate.postForEntity(url, obj, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

    /**
     * 获取用户的openid
     * @param code 前端传来的res.code
     * @return 用户的openid
     * @throws Exception WECHAT_SERVER_CONNECTION_FAILURE、CODE2SESSION_FAILURE
     */
    public static String getOpenID(String code) throws Exception {
        //拼接url
        String url = "https://api.weixin.qq.com/sns/jscode2session?"
                + "appid=" + APPID
                + "&secret=" + SECRET
                + "&js_code=" + code
                + "&grant_type=authorization_code";
        JsonNode root = handleGetRequest(url);
        if (root.has("errcode") && root.path("errcode").asInt() != 0) {
            System.out.println(root.path("errcode") + " " + root.path("errmsg"));
            throw new KnownException(ErrorInfoEnum.CODE2SESSION_FAILURE);
        }
        return root.path("openid").asText();
    }

    /**
     * 获取微信的access token
     * @return 当前获取的access token的有效时间
     * @throws Exception WECHAT_ACCESS_TOKEN_ERROR、WECHAT_SERVER_CONNECTION_FAILURE
     */
    public static int getWechatAccessToken() throws Exception {
        // 拼接url
        String url = "https://api.weixin.qq.com/cgi-bin/token?"
                + "grant_type=" + "client_credential"
                + "&appid=" + APPID
                + "&secret=" + SECRET;
        JsonNode root = handleGetRequest(url);
        if (root.has("errcode") && root.path("errcode").asInt() != 0) {
            System.out.println(root.path("errcode") + " " + root.path("errmsg"));
            throw new KnownException(ErrorInfoEnum.WECHAT_ACCESS_TOKEN_ERROR);
        }
        PropertiesConstants.WX_ACCESS_TOKEN = root.path("access_token").asText();
        return root.path("expires_in").asInt();
    }

    /**
     * >>>自动<<<获取Access Token，用于调用微信后端接口
     * 由于微信对Access Token的获取有每日次数限制，因此设置为定时触发
     * @throws Exception handleGetRequest可能抛出的异常
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 7000*1000)
    public void getWechatAccessTokenAutomatically() throws Exception {
        int expireTime = getWechatAccessToken();
        System.out.println("[自动]更新Access Token，更新时间为：" + new Date());
        System.out.println("当前Access Token有效期限为：" + expireTime + "秒");
    }

    /**
     * >>>主动<<<获取Access Token，用于调用微信后端接口
     * 在因为外部原因导致Access Token失效时调用
     * 由需要Access Token的方法在请求后进行判断来决定是否调用
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void getWechatAccessTokenActively() throws Exception {
        int expireTime = getWechatAccessToken();
        System.out.println("[主动]更新Access Token，更新时间为：" + new Date());
        System.out.println("当前Access Token有效期限为：" + expireTime + "秒");
    }

    /**
     * >>自动<<获取通知模板列表，每天更新一次
     * @throws Exception WECHAT_SERVER_CONNECTION_FAILURE、WECHAT_TEMPLATE_ERROR
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 86400*1000)
    public static void getTemplateListAutomatically() throws Exception {
        // 提交请求
        String url = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
        JsonNode root = handleGetRequest(url);

        // 第二次机会，用于处理access token过期的问题
        if(root.has("errcode") && root.path("errcode").asInt() != 0) {
            getWechatAccessTokenActively();
            url = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?"
                    + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
            root = handleGetRequest(url);
            if(root.has("errcode") && root.path("errcode").asInt() != 0) {
                System.out.println(root.path("errcode") + " " + root.path("errmsg"));
                throw new KnownException(ErrorInfoEnum.WECHAT_TEMPLATE_ERROR);
            }
        }

        // 缓存模板信息
        PropertiesConstants.WX_TEMPLATE_IDs = new HashMap<>();
        JsonNode data = root.path("data");
        Iterator<JsonNode> templates = data.elements();
        while(templates.hasNext()) {
            JsonNode template = templates.next();
            PropertiesConstants.WX_TEMPLATE_IDs.put(template.path("title").asText(),
                    template.path("priTmplId").asText());
        }

        // 输出模板信息
        System.out.println("[每日]刷新模板信息，今日刷新时间为：" + new Date());
        System.out.println("模板信息为：");
        for(Map.Entry<String, String> entry:PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            System.out.println("模板名称：" + entry.getKey() +
                    ", 模板ID：" + entry.getValue());
        }
        System.out.println();
    }

    /**
     * 获取通知模板id
     * @return 包含所有通知模板ID的List
     */
    public static List<String> getTemplateID () {
        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /*
     * 后续考虑把这个方法封装为一个统一的方法
     * 审核结果通知：0
     * 接单成功通知：1
     * 志愿者审核通知：2
     * 活动发起通知：3
     *
     */

    /**
     * 发送审核结果通知
     * @param wechatNoticeDTO 通知模板结构
     * @throws Exception WECHAT_SERVER_CONNECTION_FAILURE、WECHAT_NOTICE_FAILURE
     */
    public static void sendNotice(WechatNoticeDTO wechatNoticeDTO, int type) throws Exception {
        // 获取模板列表
        for(Map.Entry<String, String> entry:PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            if(type==0 && entry.getKey().equals("审核结果提醒")) {
                System.out.println(entry.getValue());
                wechatNoticeDTO.setTemplate_id(entry.getValue());
                break;
            }
            else if(type==3 && entry.getKey().equals("新活动发布提醒")) {
                System.out.println(entry.getValue());
                wechatNoticeDTO.setTemplate_id(entry.getValue());
                break;
            }
        }

        // 提交发送通知的请求
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
        JsonNode root = handlePostRequest(url, wechatNoticeDTO);

        // 第二次机会，用于处理access token过期的问题
        if(root.has("errcode") && root.path("errcode").asInt() != 0) {
            getWechatAccessTokenActively();
            url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                    + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
            root = handlePostRequest(url, wechatNoticeDTO);
            if(root.has("errcode") && root.path("errcode").asInt() != 0) {
                System.out.println(root.path("errcode") + " " + root.path("errmsg"));
                throw new KnownException(ErrorInfoEnum.WECHAT_NOTICE_FAILURE);
            }
        }
    }







    public static void generateAppCode(int activityId) throws Exception {
        // PropertiesConstants.WX_ACCESS_TOKEN = "40_vOSKPoBSs-OXSqKHqRwmXJghbfuxQhAmT4gBLQu4WkAjowabCR3f0Xd-ZaZYWUFmt1YaXrsKHUzsrFxIWcKscSXlgQZFSZkcJZxPUmcNsKjlfxFugIxDB3Mh4B9Jhi1dC6dZ0bd-6_3GSP09WIPhACASEL";

        String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
        WechatAppCodeDTO wechatAppCodeDTO = WechatAppCodeDTO.builder()
                .path("/page/index/index?acticyty="+activityId)
                .build();
        ResponseEntity<String> response = restTemplate.postForEntity(url, wechatAppCodeDTO, String.class);

        if(!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        System.out.println(root.path("contentType").asText());

        byte[] result = root.path("buffer").binaryValue();
        InputStream inputStream = new ByteArrayInputStream(result);
        File file = new File("src/test/resources/QRcode.png");
        if(!file.exists())
            file.createNewFile();

        OutputStream outputStream = new FileOutputStream(file);
        int content = 0;
        byte[] buffer = new byte[1024*8];
        while((content = inputStream.read(buffer, 0, 1024)) != -1)
            outputStream.write(buffer, 0, content);
        outputStream.flush();

    }














    /**
     * 发送活动开始通知
     * @param wechatNoticeDTO 通知模板结构
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void sendActivityNotice(WechatNoticeDTO wechatNoticeDTO) throws Exception {
        for(Map.Entry<String, String> entry:PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            if(entry.getKey().equals("新活动发布提醒")) {
                System.out.println(entry.getValue());
                wechatNoticeDTO.setTemplate_id(entry.getValue());
                break;
            }
        }

        System.out.println(wechatNoticeDTO.getTemplate_id());
        String accessToken = PropertiesConstants.WX_ACCESS_TOKEN;
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + accessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(url, wechatNoticeDTO, String.class);
        System.out.println(response.getBody());
    }

    /**
     * 发送接单通知
     * @param wechatNoticeDTO 通知模板结构
     */
    public static void sendTakeOrderNotice(WechatNoticeDTO wechatNoticeDTO) {

    }
}

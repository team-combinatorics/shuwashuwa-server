package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;


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
     * @throws Exception 如果不是因为Access Token失效导致的，抛出CODE2SESSION_FAILURE
     */
    public static JsonNode handleGetRequest(String url) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        return mapper.readTree(response.getBody());
    }

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
     * @throws Exception handleGetRequest可能抛出的异常
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
     * 获取通知模板列表
     * @return 包含所有通知模板的Iterator
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static Iterator<JsonNode> getTemplateList() throws Exception {
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
            if(root.has("errcode") && root.path("errcode").asInt() != 0)
                throw new KnownException(ErrorInfoEnum.WECHAT_TEMPLATE_ERROR);
        }

        JsonNode data = root.path("data");
        return data.elements();
    }





    /**
     * 发送审核结果通知
     * @param wechatNoticeDTO 通知模板结构
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void sendAuditResult(WechatNoticeDTO wechatNoticeDTO) throws Exception {
        // 获取模板列表
        Iterator<JsonNode> templates = getTemplateList();
        while (templates.hasNext()) {
            JsonNode t = templates.next();
            String title = t.path("title").asText();
            System.out.println(title);
            if(title.equals("审核结果提醒")) {
                System.out.println(t.path("priTmplId").asText());
                wechatNoticeDTO.setTemplate_id(t.path("priTmplId").asText());
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
            if(root.has("errcode") && root.path("errcode").asInt() != 0)
                throw new KnownException(ErrorInfoEnum.WECHAT_NOTICE_FAILURE);
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
     * 获取通知模板id
     * @return 包含所有通知模板的List
     */
    public static List<String> getTemplateID () throws Exception {

        List<String> result = new ArrayList<>();
        Iterator<JsonNode> templates = getTemplateList();
        while (templates.hasNext()) {
            JsonNode t = templates.next();
            String TmplId = t.path("priTmplId").asText();
            // System.out.println(TmplId);
            result.add(TmplId);
        }
        //result.add("Ua2MAP-UdHLHx9i_cnWf33nXwON2RgRt0XEOhzK3DNI");
        //result.add("DzU2gPVQgkKsknQ1dAXRjGoByDjphw252gBvltWir1Q");
        return result;
    }














    /**
     * 发送活动开始通知
     * @param wechatNoticeDTO 通知模板结构
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void sendActivityNotice(WechatNoticeDTO wechatNoticeDTO) throws Exception {
        Iterator<JsonNode> templates = getTemplateList();
        while (templates.hasNext()) {
            JsonNode t = templates.next();
            String title = t.path("title").asText();
            System.out.println(title);
            if(title.equals("新活动发布提醒")) {
                System.out.println(t.path("priTmplId").asText());
                wechatNoticeDTO.setTemplate_id(t.path("priTmplId").asText());
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

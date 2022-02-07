package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    private static final Logger logger = LoggerFactory.getLogger(WechatUtil.class);

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
     * 包装了handleGetRequest, 请求错误时自动刷新token
     * @param url 欲发送GET请求的URL
     * @return 返回响应的JsonNode
     * @throws KnownException WECHAT_SERVER_CONNECTION_FAILURE
     */
    public static JsonNode getWithRetry(String url) throws KnownException {
        JsonNode root = null;
        for(int i = 0; i < 3; i++) {
            try {
                root = handleGetRequest(url);
                if(!root.has("errcode") || root.get("errcode").asInt() == 0){
                    return root;  // request success
                }
                // request failed, refresh token
                String old_token = PropertiesConstants.WX_ACCESS_TOKEN;
                getWechatAccessToken();
                url = url.replace(old_token, PropertiesConstants.WX_ACCESS_TOKEN);
            } catch (Exception e) {
                logger.error("[GET] 微信服务器通信失败，URL: {}, 重试次数: {}", url, i);
            }
        }
        if (root == null)
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);
        else {
            return root;
        }
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
     * 包装了handlePostRequest, 请求错误时自动刷新token
     * @param url 欲发送GET请求的URL
     * @param payload 欲发送的数据
     * @return 返回响应的JsonNode
     * @throws KnownException WECHAT_SERVER_CONNECTION_FAILURE
     */
    public static JsonNode postWithRetry(String url, Object payload) throws KnownException {
        JsonNode root = null;
        for(int i = 0; i < 3; i++) {
            try {
                root = handlePostRequest(url, payload);
                if(!root.has("errcode") || root.get("errcode").asInt() == 0){
                    return root;  // request success
                }
                // request failed
                String old_token = PropertiesConstants.WX_ACCESS_TOKEN;
                getWechatAccessToken();
                url = url.replace(old_token, PropertiesConstants.WX_ACCESS_TOKEN);
            } catch (Exception e) {
                logger.error("[POST] 微信服务器通信失败，URL: {url}, 重试次数: {i}");
            }
        }
        if (root == null)
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);
        else {
            return root;
        }
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
            logger.error("获取OpenID失败, 错误码：" + root.path("errcode").asInt() + " " + root.path("errmsg").asText());
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
            logger.error("获取access token失败, 错误码：" + root.path("errcode").asInt() + " " + root.path("errmsg").asText());
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
        logger.info("{} 获取微信access token成功，有效时间：" + expireTime + "秒", new Date());
        logger.info("{} 微信access token：{}", new Date(), PropertiesConstants.WX_ACCESS_TOKEN);
    }

    /**
     * >>自动<<获取通知模板列表，每天更新一次
     * @throws KnownException WECHAT_SERVER_CONNECTION_FAILURE、WECHAT_TEMPLATE_ERROR
     */
    @Scheduled(initialDelay = 5000, fixedDelay = 86400*1000)
    public static void getTemplateListAutomatically() throws KnownException {
        // 提交请求
        String url = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;

        JsonNode root = getWithRetry(url);
        if(root.has("errcode") && root.path("errcode").asInt() != 0) {
            logger.error("获取消息模板列表错误 {} {}", root.path("errcode"), root.path("errmsg"));
            throw new KnownException(ErrorInfoEnum.WECHAT_TEMPLATE_ERROR);
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
        logger.info("[每日]更新模板列表，更新时间为：" + new Date());
        logger.info("模板信息为: ");
        for(Map.Entry<String, String> entry:PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            logger.info("模板名称 {}, 模板ID: {}", entry.getKey(), entry.getValue());
        }
    }

    /**
     * 获取通知模板id
     * @return 包含所有通知模板ID的List
     */
    public static List<String> getTemplateID () {
        // 如果模板列表不存在
        if ( PropertiesConstants.WX_TEMPLATE_IDs == null){
            try {
                getTemplateListAutomatically();
            } catch (KnownException e){
                logger.error("获取模板列表失败 {} {}", e.getErrCode(), e.getMessage());
            }
        }

        List<String> result = new ArrayList<>();
        for (Map.Entry<String, String> entry : PropertiesConstants.WX_TEMPLATE_IDs.entrySet()) {
            result.add(entry.getValue());
        }
        return result;
    }

    /**
     * 把发送微信通知封装为一个统一的方法
     * 审核结果通知：0
     * 接单成功通知：1
     * 志愿者审核通知：2
     * 活动发起通知：3
     */
    public enum WechatNoticeType {
        AUDIT_NOTICE,
        TAKE_NOTICE,
        VOLUNTEER_NOTICE,
        ACTIVITY_NOTICE
    }

    final static Map<WechatNoticeType, String> noticeTypeToTemplateNames = Map.of(
        WechatNoticeType.AUDIT_NOTICE, "审核结果提醒",
        WechatNoticeType.TAKE_NOTICE, "接单成功通知",
        WechatNoticeType.VOLUNTEER_NOTICE, "审核结果提醒",
        WechatNoticeType.ACTIVITY_NOTICE, "新活动发布提醒"
    );

    /**
     * 发送通知，建议忽略发送通知时发生的错误
     * @param wechatNoticeDTO 通知结构
     * @param type WechatNoticeType
     * @throws KnownException WECHAT_SERVER_CONNECTION_FAILURE、WECHAT_NOTICE_FAILURE
     */
    public static void sendNotice(WechatNoticeDTO wechatNoticeDTO, WechatNoticeType type) throws KnownException {
        // lookup template ID
        String templateName = noticeTypeToTemplateNames.get(type);
        if (templateName == null){
            logger.error("找不到对应模板 {} {}", type, noticeTypeToTemplateNames);
            throw new KnownException(ErrorInfoEnum.WECHAT_TEMPLATE_ERROR);
        }
        // 如果模板列表不存在
        if ( PropertiesConstants.WX_TEMPLATE_IDs == null){
            try {
                getTemplateListAutomatically();
            } catch (KnownException e){
                logger.error("获取模板列表失败 {} {}", e.getErrCode(), e.getMessage());
            }
        }
        String templateID = PropertiesConstants.WX_TEMPLATE_IDs.get(templateName);
        if (templateID == null){
            logger.error("在缓存中找不到templateID {} {}", templateName, PropertiesConstants.WX_TEMPLATE_IDs.values());
            throw new KnownException(ErrorInfoEnum.WECHAT_TEMPLATE_ERROR);
        }
        wechatNoticeDTO.setTemplate_id(templateID);

        // 提交发送通知的请求
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;

        JsonNode root = postWithRetry(url, wechatNoticeDTO);
        if(root.has("errcode") && root.path("errcode").asInt() != 0) {
            if (root.path("errcode").asInt() == 43101) {
                logger.warn("当前用户未接受通知消息 openid: {} 模板: {}", wechatNoticeDTO.getTouser(), templateName);
            } else {
                throw new KnownException(ErrorInfoEnum.WECHAT_NOTICE_FAILURE);
            }
        }
    }


    /**
     * 处理获取二维码的请求
     * 这里由于微信在成功时只返回图片的字节流，所以要单独写一个响应方法
     * 出错时，返回的是字符串类型的json，不能用mapper解析，故在catch时返回字节数组
     * @param url 请求路径
     * @param obj 参数结构
     * @return 如果出错，返回null；否则返回字节数组
     */
    public static byte[] handleQRCodeResponse(String url, Object obj) {
        ResponseEntity<byte[]> response = restTemplate.postForEntity(url, obj, byte[].class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        try {
            mapper.readTree(response.getBody());
            return null;
        } catch (Exception e) {
            logger.error(e.getMessage());
            return response.getBody();
        }
    }

    /**
     * 生成用于签到的小程序二维码
     * @param activityId 活动id，作为页面跳转的参数
     * @throws Exception 文件处理
     */
    public static byte[] generateAppCode(int activityId) throws Exception {
        // 设置请求需要的url和body
        String url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?"
                + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
        WechatAppCodeDTO wechatAppCodeDTO = WechatAppCodeDTO.builder()
                .path("/pages/index/index?activity="+activityId)
                .build();

        // 发送请求，并给予第二次机会
        byte[] QRCode = handleQRCodeResponse(url, wechatAppCodeDTO);
        if(QRCode == null) {
            getWechatAccessToken();
            url = "https://api.weixin.qq.com/cgi-bin/wxaapp/createwxaqrcode?"
                    + "access_token=" + PropertiesConstants.WX_ACCESS_TOKEN;
            QRCode = handleQRCodeResponse(url, wechatAppCodeDTO);
            if(QRCode == null)
                throw new KnownException(ErrorInfoEnum.WECHAT_QRCODE_FAILURE);
        }

        return QRCode;
    }
}

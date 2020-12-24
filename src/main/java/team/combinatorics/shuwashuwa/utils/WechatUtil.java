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
import team.combinatorics.shuwashuwa.model.dto.WechatNoticeDTO;

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
        JsonNode root = mapper.readTree(response.getBody());
        if (root.has("errcode") && root.path("errcode").asInt() != 0) {
            System.out.println(root.path("errcode") + " " + root.path("errmsg"));
            /* TODO: 感觉改成errmsg的匹配会好一些 */
            if(root.path("errcode").asInt()!=40001)
                throw new KnownException(ErrorInfoEnum.CODE2SESSION_FAILURE);
        }
        return root;
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
        return root.path("openid").asText();
    }

    /**
     * 获取Access Token，用于调用微信后端接口
     * 由于微信对Access Token的获取有每日次数限制，因此设置为定时触发
     * @throws Exception handleGetRequest可能抛出的异常
     */
    @Scheduled(initialDelay = 1000, fixedDelay = 7000*1000)
    public void getWechatAccessToken() throws Exception {
        // 拼接url
        String url = "https://api.weixin.qq.com/cgi-bin/token?"
                + "grant_type=" + "client_credential"
                + "&appid=" + APPID
                + "&secret=" + SECRET;
        JsonNode root = handleGetRequest(url);
        PropertiesConstants.WX_ACCESS_TOKEN = root.path("access_token").asText();
        System.out.println("[自动]更新Access Token，更新时间为：" + new Date());
        System.out.println("当前Access Token有效期限为：" + root.path("expires_in").asText() + "秒");
    }

    /**
     * >>>主动<<<获取Access Token，用于调用微信后端接口
     * 在因为外部原因导致Access Token失效时调用
     * 由需要Access Token的方法在请求后进行判断来决定是否调用
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void getWechatAccessTokenActively() throws Exception {
        // 拼接url
        System.out.println("Access Token已失效");
        String url = "https://api.weixin.qq.com/cgi-bin/token?" +
                "grant_type=client_credential"
                + "&appid=" + APPID
                + "&secret=" + SECRET;
        JsonNode root = handleGetRequest(url);
        PropertiesConstants.WX_ACCESS_TOKEN = root.path("access_token").asText();
        System.out.println("[主动]更新Access Token，更新时间为：" + new Date());
        System.out.println("当前Access Token有效期限为：" + root.path("expires_in").asText() + "秒");
    }

    /**
     * 获取通知模板列表
     * @return 包含所有通知模板的Iterator
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static Iterator<JsonNode> getTemplateList() throws Exception {
        String accessToken = PropertiesConstants.WX_ACCESS_TOKEN;
        String url = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?"
                + "access_token=" + accessToken;
        JsonNode root = handleGetRequest(url);

        /* TODO: 或许需要细化errcode原因 */
        if(root.has("errcode") && root.path("errcode").asInt() != 0)
            getWechatAccessTokenActively();

        JsonNode data = root.path("data");
        return data.elements();
    }

    /**
     * 获取通知模板id
     * @return 包含所有通知模板的List
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static List<String> getTemplateID () throws Exception {
        List<String> result = new ArrayList<>();
        Iterator<JsonNode> templates = getTemplateList();
        while (templates.hasNext()) {
            JsonNode t = templates.next();
            String TmplId = t.path("priTmplId").asText();
            System.out.println(TmplId);
            result.add(TmplId);
        }
        // result.add("Ua2MAP-UdHLHx9i_cnWf33nXwON2RgRt0XEOhzK3DNI");
        // result.add("DzU2gPVQgkKsknQ1dAXRjGoByDjphw252gBvltWir1Q");
        return result;
    }

    /**
     * 发送审核结果通知
     * @param wechatNoticeDTO 通知模板结构
     * @throws Exception handleGetRequest可能抛出的异常
     */
    public static void sendAuditResult(WechatNoticeDTO wechatNoticeDTO) throws Exception {
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

        System.out.println(wechatNoticeDTO.getTemplate_id());
        String accessToken = PropertiesConstants.WX_ACCESS_TOKEN;
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + accessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(url, wechatNoticeDTO, String.class);
        System.out.println(response.getBody());
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

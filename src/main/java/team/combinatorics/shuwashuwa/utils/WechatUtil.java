package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.DependsOn;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;
import team.combinatorics.shuwashuwa.model.dto.WechatNoticeDTO;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


@DependsOn("constants")
final public class WechatUtil {

    private static final String APPID = PropertiesConstants.WX_MINI_PROGRAM_APPID;
    private static final String SECRET = PropertiesConstants.WX_MINI_PROGRAM_SECRET;
    private static final RestTemplate restTemplate = new RestTemplate();

    String accessToken;

    public static JsonNode handleGetRequest(String url) throws Exception {
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new KnownException(ErrorInfoEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        if (root.has("errcode") && root.path("errcode").asInt() != 0) {
            System.out.println(root.path("errcode") + " " + root.path("errmsg"));
            throw new KnownException(ErrorInfoEnum.CODE2SESSION_FAILURE);
        }
        return root;
    }

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

    public static String getWechatAccessToken() throws Exception {
        // 拼接url
        String url = "https://api.weixin.qq.com/cgi-bin/token?"
                + "grant_type=" + "client_credential"
                + "&appid=" + APPID
                + "&secret=" + SECRET;
        JsonNode root = handleGetRequest(url);
        return root.path("access_token").asText();
    }

    public static Iterator<JsonNode> getTemplateList() throws Exception {

        String accessToken = getWechatAccessToken();
        String url = "https://api.weixin.qq.com/wxaapi/newtmpl/gettemplate?"
                + "access_token=" + accessToken;
        JsonNode root = handleGetRequest(url);

        JsonNode data = root.path("data");
        return data.elements();
    }

    public static List<String> getTemplateID () {
        List<String> result = new ArrayList<>();
//        Iterator<JsonNode> templates = getTemplateList();
//        while (templates.hasNext()) {
//            JsonNode t = templates.next();
//            String TmplId = t.path("priTmplId").asText();
//            System.out.println(TmplId);
//            result.add(TmplId);
//        }
        result.add("Ua2MAP-UdHLHx9i_cnWf33nXwON2RgRt0XEOhzK3DNI");
        result.add("DzU2gPVQgkKsknQ1dAXRjGoByDjphw252gBvltWir1Q");
        return result;
    }

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
        String accessToken = getWechatAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + accessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(url, wechatNoticeDTO, String.class);
        System.out.println(response.getBody());
    }

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
        String accessToken = getWechatAccessToken();
        String url = "https://api.weixin.qq.com/cgi-bin/message/subscribe/send?"
                + "access_token=" + accessToken;
        ResponseEntity<String> response = restTemplate.postForEntity(url, wechatNoticeDTO, String.class);
        System.out.println(response.getBody());
    }

    public static void sendTakeOrderNotice(WechatNoticeDTO wechatNoticeDTO) {

    }
}

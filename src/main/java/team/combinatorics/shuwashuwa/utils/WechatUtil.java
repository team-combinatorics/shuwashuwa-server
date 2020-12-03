package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;


@Component
public class WechatUtil {

    private final RestTemplate restTemplate;

    private static final String APPID = PropertiesConstants.WX_MINI_PROGRAM_APPID;

    @Value("${wx.secret:default}")
    private static final String SECRET = PropertiesConstants.WX_MINI_PROGRAM_SECRET;

    public WechatUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonNode getWechatInfo(String code) throws Exception {
        //拼接url
        String url = "https://api.weixin.qq.com/sns/jscode2session?"
                + "appid=" + APPID
                + "&secret=" + SECRET
                + "&js_code=" + code
                + "&grant_type=authorization_code";
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
}

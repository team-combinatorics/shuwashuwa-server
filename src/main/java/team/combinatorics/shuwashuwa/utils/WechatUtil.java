package team.combinatorics.shuwashuwa.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.combinatorics.shuwashuwa.exception.ErrorEnum;
import team.combinatorics.shuwashuwa.exception.GlobalException;

@PropertySource("classpath:wx.properties")
@Component
public class WechatUtil {

    private final RestTemplate restTemplate;

    @Value("${wx.appid:default}")
    private String appid;

    @Value("${wx.secret:default}")
    private String secret;

    public WechatUtil(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public JsonNode getWechatInfo(String code) throws Exception {
        //拼接url
        //System.out.println(code);
        String url = "https://api.weixin.qq.com/sns/jscode2session?"
                + "appid=" + appid
                + "&secret=" + secret
                + "&js_code=" + code
                + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new GlobalException(ErrorEnum.WECHAT_SERVER_CONNECTION_FAILURE);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        if (root.has("errcode") && root.path("errcode").asInt() != 0) {
            System.out.println(root.path("errcode") + " " + root.path("errmsg"));
            throw new GlobalException(ErrorEnum.CODE2SESSION_FAILURE);
        }
        return root;
    }
}

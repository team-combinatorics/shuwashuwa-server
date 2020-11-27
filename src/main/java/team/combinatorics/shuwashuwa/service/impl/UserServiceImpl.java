package team.combinatorics.shuwashuwa.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import team.combinatorics.shuwashuwa.dao.UserDao;
import team.combinatorics.shuwashuwa.exception.ErrorEnum;
import team.combinatorics.shuwashuwa.exception.ShuwarinException;
import team.combinatorics.shuwashuwa.model.dto.LogInInfoDto;
import team.combinatorics.shuwashuwa.model.dto.LogInSuccessDto;
import team.combinatorics.shuwashuwa.model.dto.UpdateUserInfoDto;
import team.combinatorics.shuwashuwa.model.pojo.User;
import team.combinatorics.shuwashuwa.service.UserService;
import team.combinatorics.shuwashuwa.utils.TokenUtil;

@PropertySource("classpath:wx.properties")
@Component
public class UserServiceImpl implements UserService {

    private final RestTemplate restTemplate;

    private final UserDao userDao;

    @Value("${wx.appid:default}")
    private String appid;

    @Value("${wx.secret:default}")
    private String secret;

    public UserServiceImpl(RestTemplate restTemplate, UserDao userDao) {
        this.restTemplate = restTemplate;
        this.userDao = userDao;
    }

    @Override
    public LogInSuccessDto wechatLogin(LogInInfoDto logInInfoDto) throws Exception {

        //拼接url
        String url = "https://api.weixin.qq.com/sns/jscode2session?"
                + "appid=" + appid
                + "&secret=" + secret
                + "&js_code=" + logInInfoDto.getCode()
                + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);

        if (!response.getStatusCode().equals(HttpStatus.OK))
            throw new ShuwarinException(ErrorEnum.WECHAT_SERVER_CONNECTION_FAILURE);

        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        if (root.has("errcode") && root.path("errcode").asInt() != 0)
            throw new ShuwarinException(ErrorEnum.CODE2SESSION_FAILURE);
        else {
            String openid = root.path("openid").asText();
            String sessionKey = root.path("session_key").asText();
            LogInSuccessDto logInSuccessDto = new LogInSuccessDto();
            User user = userDao.findUserByOpenid(openid);
            if(user == null) {
                logInSuccessDto.setFirstLogin(true);
                userDao.addUserOpenid(openid);
                user = userDao.findUserByOpenid(openid);
            }
            else
                logInSuccessDto.setFirstLogin(false);
            String token = TokenUtil.createToken(user.getUserid(), user.getAuthority());
            logInSuccessDto.setToken(token);
            return logInSuccessDto;
        }
    }

    @Override
    public String test(LogInInfoDto logInInfoDto) throws Exception {
        //拼接url
        //拼接url
        String url = "https://api.weixin.qq.com/sns/jscode2session?"
                + "appid=" + appid
                + "&secret=" + secret
                + "&js_code=" + logInInfoDto.getCode()
                + "&grant_type=authorization_code";
        ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(response.getBody());
        // 如果返回中有错误码且不等于零说明出错
        if (root.has("errcode") && root.path("errcode").asInt() != 0)
            return "Yes " + root.toString();
        else {
            String openid = root.path("openid").asText();
            String sessionKey = root.path("session_key").asText();
            userDao.addUserOpenid(openid);
            return userDao.findUserByOpenid(openid).toString();
        }
    }

    @Override
    public void updateUserInfo(int openid, UpdateUserInfoDto updateUserInfoDto) {

    }
}

package team.combinatorics.shuwashuwa.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TokenUtil {
    public static final String SECRET = "HappyLucky";
    public static final int EXPIRE = 60 * 60 * 4;
    public static final Map<String, Object> headerMap = new HashMap<>();
    static {
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");
    }

    /**
     * 生成token
     * @param userid userid
     * @param authority authority
     * @return token
     */
    public static String createToken(int userid, int authority) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, EXPIRE);
        Date expireDate = nowTime.getTime();

        return JWT.create()
                .withHeader(headerMap)
                .withClaim("userid", userid)
                .withClaim("authority", authority)
                .withIssuedAt(new Date())
                .withExpiresAt(expireDate)
                .sign(Algorithm.HMAC256(SECRET));
    }

    /**
     * 验证Token
     *
     */
    public static Map<String, Claim> verifyToken(String token) throws RuntimeException {
        JWTVerifier verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
        DecodedJWT jwt;
        try {
            jwt = verifier.verify(token);
        }catch (TokenExpiredException tle) {
            //TODO: 这里应该定义一个凭证过期的异常
            throw new RuntimeException("登录失效，返回标题");
        } catch (JWTVerificationException e) {
            //TODO: 这里应该定义一个签名无效的异常
            throw new RuntimeException("无效的签名");
        }

        return jwt.getClaims();
    }

    /**
     * 解析Token
     * @param token Token to be parsed
     * @return Claims contained in token
     */
    public static Map<String, Claim> parseToken(String token){
        DecodedJWT decodedJWT = JWT.decode(token);
        return decodedJWT.getClaims();
    }
}
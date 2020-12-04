package team.combinatorics.shuwashuwa.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import team.combinatorics.shuwashuwa.exception.ErrorInfoEnum;
import team.combinatorics.shuwashuwa.exception.KnownException;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class TokenUtil {
    public static final String SECRET = PropertiesConstants.TOKEN_SECRET;
    public static final int EXPIRE = 60 * 60 * 24 * 15;
    public static final Map<String, Object> headerMap = new HashMap<>();
    static {
        headerMap.put("alg", "HS256");
        headerMap.put("typ", "JWT");
    }

    /**
     * 生成token
     * @param userid userid
     * @return token
     */
    public static String createToken(int userid) {

        Calendar nowTime = Calendar.getInstance();
        nowTime.add(Calendar.SECOND, EXPIRE);
        Date expireDate = nowTime.getTime();

        return JWT.create()
                .withHeader(headerMap)
                .withClaim("userid", userid)
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
            throw new KnownException(ErrorInfoEnum.TOKEN_EXPIRED);
        } catch (JWTVerificationException e) {
            throw new KnownException(ErrorInfoEnum.TOKEN_INVALID);
        }

        return jwt.getClaims();
    }

    public static int extractUserid(String token) {
        return JWT.decode(token).getClaim("userid").asInt();
    }
}

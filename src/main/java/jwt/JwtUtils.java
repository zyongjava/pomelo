package jwt;

import com.alibaba.fastjson.JSON;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;

import java.security.Key;
import java.util.Date;
import java.util.UUID;

/**
 * <p>JWT 生成token</p>
 * <p>https://github.com/jwtk/jjwt</p>
 *
 * @author: zhengyong Date: 2018/11/29 Time: 下午4:48
 */
public class JwtUtils {

    /**
     * 利用JWT生成token
     *
     * @return token
     */
    public static String generateToken(Key key) {
        /**
         // 以下算法过程是注释伪代码步骤
         String header = "{\"alg\":\"HS256\"}";
         String body = "{\"sub\":\"Joe\"}";
         String encodedHeader = base64URLEncode( header.getBytes("UTF-8"));
         String encodedClaims = base64URLEncode( claims.getBytes("UTF-8") );
         String concatenated = encodedHeader + "." + encodedClaims;
         Key key = getMySecretKey();
         byte[] signature = hmacSha256( concatenated, key );
         String jws = concatenated + '.' + base64URLEncode( signature );
         **/
        // We need a signing key, so we'll create one just for this example. Usually
        // the key would be read from your application configuration instead.
        String jws = Jwts.builder()
                .setIssuer("me")
                .setSubject("Bob")
                .setAudience("you")
//                .setExpiration(expiration) //a java.util.Date
//                .setNotBefore(notBefore) //a java.util.Date
                .setIssuedAt(new Date()) // for example, now
                .setId(UUID.randomUUID().toString()).signWith(key).compact(); //just an example id
        return jws;
    }

    /**
     * 验证token
     */
    public static Jws validateToken(String jwsToken, Key key) {
        try {

            Jws<Claims> claimsJws = Jwts.parser().setSigningKey(key).parseClaimsJws(jwsToken);
            //OK, we can trust this JWT
            return claimsJws;
        } catch (JwtException e) {
            //don't trust the JWT!
            e.printStackTrace();
        }
        System.out.println("error");
        return null;
    }

    public static void main(String[] args) {
        Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);
        // 生成token
        String token = generateToken(key);
        System.out.println("token:" + token);

        // 解析验证token
        Jws jws = validateToken(token, key);
        System.out.println(JSON.toJSONString(jws));
    }
}

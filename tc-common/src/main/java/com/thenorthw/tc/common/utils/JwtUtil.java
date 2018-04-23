package com.thenorthw.tc.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by theNorthW on $date.
 * blog: thenorthw.com
 *
 * @autuor : theNorthW
 */
public class JwtUtil {
    private static final String secret = "iLon,asd193;1duvh9v'8i2brhbdhv9h9'2ni.;'wv98',ba]u29u3rbbcsahidhwq";
    private static final Map<String,Object> headerMap = new HashMap<String, Object>();
    private static JWTVerifier jwtV = null;

    static {
        try {
            jwtV = JWT.require(Algorithm.HMAC256(secret)).build();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    static {
        headerMap.put("alg","HS256");
        headerMap.put("typ","JWT");
    }

    public static String createToken(String uid){
        Date now = new Date();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR,24);
        Date expire = calendar.getTime();

        try {
            String token = JWT.create()
                    .withHeader(headerMap)
                    .withIssuedAt(now)
                    .withExpiresAt(expire)
                    .withClaim("u",uid)
                    .sign(Algorithm.HMAC256(secret));

            return token;
        }catch (Exception e){
            logger.error("Create Jwt error!\n Exception: {}",e.toString());
        }

        return null;
    }

    public static Map<String,Claim> verify(String token){
        if(token == null){
            return new HashMap<String, Claim>();
        }
        DecodedJWT dj = null;
        try {
             dj = jwtV.verify(token);
        }catch (Exception e){
            return null;
        }

        return dj.getClaims();

    }
}

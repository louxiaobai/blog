package com.lou.blog.utils;

import io.jsonwebtoken.Jwt;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {

    /**
     * 登录使用JWT技术。
     *
     *
     * 我的理解就是可以进行客户端与服务端之间验证的一种技术，取代了之前使用Session来验证的不安全性
     *
     * jwt 可以生成 一个加密的token，做为用户登录的令牌，当用户登录成功之后，发放给客户端。
     *
     * 请求需要登录的资源或者接口的时候，将token携带，后端验证token是否合法。
     *
     * jwt 有三部分组成：A.B.C
     *
     * A：Header，{“type”:“JWT”,“alg”:“HS256”} 固定
     *
     * B：playload，存放信息，比如，用户id，过期时间等等，可以被解密，不能存放敏感信息
     *
     * C: 签证，A和B加上秘钥 加密而成，只要秘钥不丢失，可以认为是安全的。
     *
     * jwt 验证，主要就是验证C部分 是否合法。
     *
     */

    /**
     * Session认证的缺点
     *
     * .每个用户经过我们的应用认证之后，我们的应用都要在服务端做一次记录，以方便用户下次请求的鉴别，通常而言session都是保存在内存中，而随着认证用户的增多，服务端的开销会明显增大
     *
     * 2.用户认证之后，服务端做认证记录，如果认证的记录被保存在内存中的话，这意味着用户下次请求还必须要请求在这台服务器上,这样才能拿到授权的资源，这样在分布式的应用上，相应的限制了负载均衡器的能力。这也意味着限制了应用的扩展能力。
     *
     * 3.因为是基于cookie来进行用户识别的, cookie如果被截获，用户就会很容易受到跨站请求伪造的攻击。
     *
     * 4.如果后端要做分布式集群的话，那么还要去实现session共享来保证用户一致性。
     *
     * https://www.cnblogs.com/cryface/p/14743323.html
     *
     *
     */

    //这里为 c部分的密钥
    private static final String jwtToken = "123456Mszlu!@#$$";

    public static String createToken(Long userId){
        Map<String,Object> claims = new HashMap<>();//B部分
        claims.put("userId",userId);//B部分

        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256, jwtToken) // 签发算法，秘钥为jwtToken --A部分
                .setClaims(claims) // body数据，要唯一，自行设置
                .setIssuedAt(new Date()) // 设置签发时间--保证每次生成的token都是不一样de
                .setExpiration(new Date(System.currentTimeMillis() + 24 * 60 * 60 * 60 * 1000));// 一天的有效时间
        String token = jwtBuilder.compact();
        return token;
    }

    //检测 token是否合法
    public static Map<String, Object> checkToken(String token){
        try {
            Jwt parse = Jwts.parser().setSigningKey(jwtToken).parse(token);
            return (Map<String, Object>) parse.getBody();
        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }

}


package com.js.withyou.utils;

import com.js.withyou.customClass.CustomUser;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
@Slf4j
public class JwtUtil {

    private static SecretKey key;
    //application.properteis 값
    @Value("${jwt.key}")
    private String jwtKey;

//    JWT 테스트용 키입니다. 배포시 삭제해주세요
//    private static SecretKey testKey = Keys.hmacShaKeyFor((Decoders.BASE64.decode("ac5563b16b09b6a74a5f417f70a7c09ca34509a3956bd98cb7d1b280a98ccbfa")));

    /**
     * 인증 정보로 JWT를 만드는 메서드 입니다. OAuth 로그인시 사용합니다.
     * @param providerId 인증 제공 서비스에서 유저 ID
     * @param memberRole 유저의 Role
     * @param memberName 닉네임
     * @return
     */
    public static String createJwt(String providerId, String memberRole,String memberName ){

    return     Jwts.builder()
                .claim("userName",providerId)//토큰에 들어갈 username, 보안을 위해 email 대신 providerId 사용
                .claim("role",memberRole)//유저의 role
                .claim("displayName",memberName)//사이트에서 보여질 닉네임
                .issuedAt(new Date(System.currentTimeMillis()))//발행시간
            .expiration(new Date(System.currentTimeMillis() + (1000*60*60))) //유효시간
                .signWith(key)//미리 설정한 key값으로 서명
                .compact();

    }

    // JWT 만들어주는 함수
    public static String createToken(Authentication authentication) {

        CustomUser user = (CustomUser) authentication.getPrincipal();

//.claim으로 정보 입력
        String jwt = Jwts.builder()
                .claim("username", user.getUsername())//로그인 id
                .claim("displayName", user.memberName)//닉네임
//                .claim("authorities",)//권한
                .issuedAt(new Date(System.currentTimeMillis()))//발행시간
                .expiration(new Date(System.currentTimeMillis() + (1000*60*60))) //유효시간
                .signWith(key)//미리 설정한 key값으로 서명
                .compact();
        return jwt;
    }

    // JWT 까주는 함수
    public static Claims extractToken(String token) {

        Claims claims = Jwts.parser()
                .verifyWith(key)//토큰 검증용 키 설정
//                .verifyWith(testKey)//토큰 검증용 키 설정 테스트
                .build()//Jwt parser 완성
                .parseSignedClaims(token)// JWT 토큰 파싱하여 claim 추출
                .getPayload();
        log.info("claims 반환");
        return claims;
            }

    @PostConstruct
    public void init() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(jwtKey));

    }

    public Cookie createJwtCookie(String value) {

        Cookie cookie = new Cookie("access_token", value);
        cookie.setHttpOnly(true);//HTTP에서만 쿠키전송
//        cookie.setSecure(true);//HTTPS등 보안 프로토콜에서만 쿠키 전송
        cookie.setMaxAge(60 * 20);//초단위로 설정
        cookie.setPath("/");//쿠키 유효범위

        return cookie;
    }

   


}

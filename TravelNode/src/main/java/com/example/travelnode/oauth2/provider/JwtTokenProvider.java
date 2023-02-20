package com.example.travelnode.oauth2.provider;

import com.example.travelnode.entity.Token;
import com.example.travelnode.entity.User;
import com.example.travelnode.oauth2.entity.UserPrincipal;
import com.example.travelnode.oauth2.repository.TokenRepository;
import com.example.travelnode.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletResponse;
import java.security.Key;
import java.util.*;
import java.util.stream.Collectors;

@Log4j2
@Slf4j
@Component
public class JwtTokenProvider { // 유효한 JWT Token 생성

    private final String SECRET_KEY;
    private final String COOKIE_REFRESH_TOKEN_NAME;
    private final Long ACCESS_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60;		// 1hour
    private final Long REFRESH_TOKEN_EXPIRE_LENGTH = 1000L * 60 * 60 * 24 * 7;	// 1week
    private static final String AUTHORITIES_KEY = "role";
    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;


    public JwtTokenProvider(@Value("${app.auth.token.secret-key}") final String secretKey,
                            @Value("${app.auth.token.refresh-cookie-name}") String cookieName, TokenRepository tokenRepository, UserRepository userRepository) {
        this.SECRET_KEY = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.COOKIE_REFRESH_TOKEN_NAME = cookieName;
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    public String createAccessToken(Authentication authentication) {
        Date now = new Date();
        Date valid_until = new Date(now.getTime() + ACCESS_TOKEN_EXPIRE_LENGTH);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String uniqueId = userPrincipal.getUniqueId().toString();
        String role = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        return Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setSubject(uniqueId)
                .claim(AUTHORITIES_KEY, role)
                .setIssuer("travel")
                .setIssuedAt(now)
                .setExpiration(valid_until)
                .compact();
    }

    public void createRefreshToken(Authentication authentication, HttpServletResponse response,
                                   String accessToken) throws Exception {
        Date now = new Date();
        Date valid_until = new Date(now.getTime() + REFRESH_TOKEN_EXPIRE_LENGTH);

        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        Key key = Keys.hmacShaKeyFor(keyBytes);

        String refreshToken = Jwts.builder()
                .signWith(key, SignatureAlgorithm.HS256)
                .setIssuer("travel")
                .setIssuedAt(now)
                .setExpiration(valid_until)
                .compact();

        saveRefreshToken(authentication, refreshToken, accessToken);

        ResponseCookie cookie = ResponseCookie.from(COOKIE_REFRESH_TOKEN_NAME, refreshToken)
                .httpOnly(true).secure(true).sameSite("Lax")
                .maxAge(REFRESH_TOKEN_EXPIRE_LENGTH/1000)
                .path("/").build();

        response.addHeader("Set-Cookie", cookie.toString());
    }

    private void saveRefreshToken(Authentication authentication, String refreshToken,
                                  String accessToken) throws Exception {
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        String uniqueId = userPrincipal.getUniqueId();

        User targetUser = userRepository.findByUniqueId(uniqueId);
        if(targetUser == null) {
            throw new Exception("Cannot find user");
        }

        Token refresh = Token.builder().accessToken(accessToken)
                .user(targetUser).refreshToken(refreshToken).build();

        Token toDelete = tokenRepository.findByUniqueId(uniqueId);
        if(toDelete != null) {
            tokenRepository.delete(toDelete); // 토큰을 새로 발급받은 경우, 기존에 있던 토큰 삭제
        }
        tokenRepository.save(refresh); // 새로 발급받은 토큰 저장
    }

    // Request에 포함된 JWT Token (Access Token)으로 인증 정보 조회 및 Authentication 객체 생성
    public Authentication getAuthentication(String accessToken) {
        Claims claims = parseClaims(accessToken);

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new).toList();

        User user = userRepository.findByUniqueId(claims.getSubject());
        UserPrincipal principal = UserPrincipal.create(user);

        return new UsernamePasswordAuthenticationToken(principal, accessToken, authorities);
        // JwtAuthenticationFilter에서 입력받은 Access Token + 유저 정보로 Authentication 객체 생성 및 리턴
    }

    public Boolean isValidToken(String token) {
        try{
            Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(token);
            return true;
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token");
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token");
        } catch (IllegalStateException e) {
            log.info("Wrong JWT Token");
        }
        return false;
    }

    // Access Token 만료 -> Token 갱신에 사용할 정보를 얻기 위해 Claim 값(JWT Token = Access Token을 복호화한 값)을 구함
    private Claims parseClaims(String accessToken) {
        try{
            return Jwts.parserBuilder().setSigningKey(SECRET_KEY)
                    .build().parseClaimsJws(accessToken).getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }
}

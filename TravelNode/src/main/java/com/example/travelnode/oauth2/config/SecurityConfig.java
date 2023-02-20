package com.example.travelnode.oauth2.config;

import com.example.travelnode.oauth2.properties.CorsProperties;
import com.example.travelnode.oauth2.exception.JwtAuthenticationEntryPoint;
import com.example.travelnode.oauth2.filter.JwtAuthenticationFilter;
import com.example.travelnode.oauth2.handler.JwtAccessDeniedHandler;
import com.example.travelnode.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.travelnode.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.example.travelnode.oauth2.provider.JwtTokenProvider;
import com.example.travelnode.oauth2.repository.CookieAuthorizationRequestRepository;
import com.example.travelnode.oauth2.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomOAuth2UserService customOAuth2UserService;
    private final JwtAccessDeniedHandler jwtAccessDeniedHandler;
    private final JwtTokenProvider jwtTokenProvider;
    private final CorsProperties corsProperties;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception{

        http
                .cors().and()
                .csrf().disable()
                .formLogin().disable()
                .httpBasic().disable()
                .sessionManagement()
                // 인증에 JWT 토큰을 사용하므로 Session을 생성하지 않음
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //.exceptionHandling()
                // 잘못된/만료된/지원되지 않는 JWT 토큰을 사용했을 때의 예외처리
                //.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                // 사용자가 허가받지 않은 API에 접근한 경우
                //.accessDeniedHandler(jwtAccessDeniedHandler).and()
                .authorizeRequests()  // 인증된 유저만 접근 가능
                // CORS Preflight Request에 대해서는 spring security를 적용하지 않음
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/*", "/api/v1/**", "/login/oauth2/**").permitAll()
                // 로그인 URL과 Home URL에는 모든 사용자가 접근 가능
                .antMatchers(HttpMethod.POST, "/api/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Home과 로그인 URL을 제외한 페이지는 관리자 혹은 가입된 유저만 접근 가능
                .anyRequest().authenticated().and() // 이외에는 인증된 사용자만 접근 가능
                .oauth2Login() // oauth2Login 설정
                .authorizationEndpoint().baseUri("/login/oauth2").and()
                .redirectionEndpoint().baseUri("/*/oauth2/code/*").and()
                .userInfoEndpoint() // oauth2Login 성공 이후의 설정
                .userService(customOAuth2UserService).and()// 로그인 성공 이후의 task는 customOAuth2UserService에서 처리
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler()).and()
                // JWT 토큰 인증을 위한 Filter 등록
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                .logout().logoutSuccessUrl("/");

        return http.build();
    }

    private AuthenticationFailureHandler oAuth2AuthenticationFailureHandler() {
        return new OAuth2AuthenticationFailureHandler(cookieAuthorizationRequestRepository());
    }

    private AuthenticationSuccessHandler oAuth2AuthenticationSuccessHandler() {
        return new OAuth2AuthenticationSuccessHandler(jwtTokenProvider);
    }

    // Token Filter 설정
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    // Cookie 기반 인가 repository -> 인가 응답을 토큰과 연계 및 응답 검증 시 사용
    @Bean
    public CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new CookieAuthorizationRequestRepository();
    }

    // CORS 설정
    public UrlBasedCorsConfigurationSource corsConfigurationSource() {
        UrlBasedCorsConfigurationSource corsConfigurationSource = new UrlBasedCorsConfigurationSource();
        CorsConfiguration corsConfiguration = new CorsConfiguration();

        corsConfiguration.setAllowedHeaders(Arrays.asList(corsProperties.getAllowedHeaders().split(",")));
        corsConfiguration.setAllowedMethods(Arrays.asList(corsProperties.getAllowedMethods().split(",")));
        corsConfiguration.setAllowedOrigins(Arrays.asList(corsProperties.getAllowedOrigins().split(",")));
        corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setMaxAge(corsConfiguration.getMaxAge());
        corsConfigurationSource.registerCorsConfiguration("/**", corsConfiguration);

        return corsConfigurationSource;
    }
}

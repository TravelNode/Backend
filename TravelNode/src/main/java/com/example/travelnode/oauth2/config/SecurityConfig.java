package com.example.travelnode.oauth2.config;

import com.example.travelnode.oauth2.handler.OAuth2AuthenticationFailureHandler;
import com.example.travelnode.oauth2.handler.OAuth2AuthenticationSuccessHandler;
import com.example.travelnode.oauth2.properties.CorsProperties;
import com.example.travelnode.oauth2.filter.JwtAuthenticationFilter;
import com.example.travelnode.oauth2.handler.JwtAccessDeniedHandler;

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
                // ????????? JWT ????????? ??????????????? Session??? ???????????? ??????
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                //.exceptionHandling()
                // ?????????/?????????/???????????? ?????? JWT ????????? ???????????? ?????? ????????????
                //.authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                // ???????????? ???????????? ?????? API??? ????????? ??????
                //.accessDeniedHandler(jwtAccessDeniedHandler).and()
                .authorizeRequests()  // ????????? ????????? ?????? ??????
                // CORS Preflight Request??? ???????????? spring security??? ???????????? ??????
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()

                .antMatchers("/*","/**", "/api/v1/**", "/login/oauth2/**").permitAll()

                .antMatchers("/**", "/api/v1/**", "/login/oauth2/**").permitAll()

                // ????????? URL??? Home URL?????? ?????? ???????????? ?????? ??????
                .antMatchers(HttpMethod.POST, "/api/**")
                .hasAnyRole("USER", "ADMIN")
                .antMatchers("/admin/**").hasRole("ADMIN")
                // Home??? ????????? URL??? ????????? ???????????? ????????? ?????? ????????? ????????? ?????? ??????
                .anyRequest().authenticated().and() // ???????????? ????????? ???????????? ?????? ??????
                .oauth2Login() // oauth2Login ??????
                .authorizationEndpoint().baseUri("/login/oauth2").and()
                .redirectionEndpoint().baseUri("/*/oauth2/code/*").and()
                .userInfoEndpoint() // oauth2Login ?????? ????????? ??????
                .userService(customOAuth2UserService).and()// ????????? ?????? ????????? task??? customOAuth2UserService?????? ??????
                .successHandler(oAuth2AuthenticationSuccessHandler())
                .failureHandler(oAuth2AuthenticationFailureHandler()).and()
                // JWT ?????? ????????? ?????? Filter ??????
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

    // Token Filter ??????
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider);
    }

    // Cookie ?????? ?????? repository -> ?????? ????????? ????????? ?????? ??? ?????? ?????? ??? ??????
    @Bean
    public CookieAuthorizationRequestRepository cookieAuthorizationRequestRepository() {
        return new CookieAuthorizationRequestRepository();
    }

    // CORS ??????
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



package com.example.travelnode.service;

import com.example.travelnode.dto.UserInfoDto;
import com.example.travelnode.entity.OAuthAttributes;
import com.example.travelnode.entity.User;
import com.example.travelnode.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.DefaultOAuth2User;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpSession;
import java.util.Collections;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;
    private final HttpSession httpSession;

    @SneakyThrows
    @Override
    // 발급받은 Access Token을 사용해 인증 서버에서 사용자 정보를 받아오는 기능
    // 회원가입이 되어있지 않은 사용자인 경우 DB에 정보 저장(회원가입), 이미 회원가입을 한 사용자인 경우 로그인만
    // DefaultOAuth2User 객체 리턴
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest);

        // Naver, Kakao, Google 중 어느 계정인지
        String provider = userRequest.getClientRegistration().getRegistrationId();
        // 각 계정의 고유 id (소셜 서비스마다 고유 id를 저장하는 필드의 이름이 다르므로, 먼저 해당 필드명을 찾고 데이터를 가져와야 함)
        String uniqueId = userRequest.getClientRegistration().getProviderDetails().
                getUserInfoEndpoint().getUserNameAttributeName();

        OAuthAttributes attributes = OAuthAttributes.of(provider, uniqueId, oAuth2User.getAttributes());
        if(attributes == null){
            return null; // attributes에서 NullPointerException이 발생하는 경우 예외 처리 필요
        }

        User isAlready = userRepository.findByEmail(attributes.getEmail()); // DB에서 찾을 수 있는 이미 가입된 유저인지
        if(isAlready != null) {
            throw new Exception("이미 가입된 계정입니다.");
        }
        else{
            isAlready = createUser(attributes);
        }

        httpSession.setAttribute("user", new UserInfoDto(isAlready));

        return new DefaultOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(isAlready.getRoleType().getCode())),
                attributes.getAttributes(), attributes.getNameAttributeKey());
    }

    private User createUser(OAuthAttributes attributes) {
        User user = attributes.toEntity();

        return userRepository.save(user);
    }
}
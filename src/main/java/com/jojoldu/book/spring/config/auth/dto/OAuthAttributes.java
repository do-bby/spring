package com.jojoldu.book.spring.config.auth.dto;
import com.jojoldu.book.spring.domain.user.Role;
import com.jojoldu.book.spring.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@Getter
public class OAuthAttributes {

    private Map<String, Object> attributes;
    private String nameAttributeKey;
    private String name;
    private String email;
    private String picture;

    @Builder
    public OAuthAttributes(Map<String, Object> attributes, String nameAttributeKey, String name, String email, String picture){
        this.attributes = attributes;
        this.nameAttributeKey = nameAttributeKey;
        this.name = name;
        this.email = email;
        this.picture = picture;
    }

    public static OAuthAttributes of(String registrationId, String userNameAttributeName, Map<String, Object> attributes) {

        if("naver".equals(registrationId)) {
            return ofNaver("id", attributes);
        }

        return ofGoogle(userNameAttributeName, attributes);
    }
    //of() : OAuth2User에서 반환하는 사용자 정보는 map이기 때문에 값 하나하나를 변환해야한다.

    private static OAuthAttributes ofNaver(String userNameAttributeName, Map<String, Object> attributes){
        MultiValueMap<String, Object> response = (MultiValueMap<String, Object>) attributes.get("response");

        /*response.add("name", response.get("name"));
        response.add("email", response.get("email"));
        response.add("picture", response.get("profile_image"));
        return new RestTemplate().postForEntity("https://nid.naver.com/oauth2.0/token", response, Map.class);*/

        return OAuthAttributes.builder()
                //.name((String) response.get("name"))
                //.email((String) response.get("email"))
                //.picture((String) response.get("profile_image"))
                //.attributes(response)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }
    private static OAuthAttributes ofGoogle(String userNameAttributeName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .picture((String) attributes.get("picture"))
                .attributes(attributes)
                .nameAttributeKey(userNameAttributeName)
                .build();
    }

    public User toEntity(){
        return User.builder()
                .name(name)
                .email(email)
                .picture(picture)
                .role(Role.GUEST)
                .build();
    }
}

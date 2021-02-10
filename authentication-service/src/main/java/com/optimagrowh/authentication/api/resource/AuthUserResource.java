package com.optimagrowh.authentication.api.resource;

import com.optimagrowh.authentication.api.dto.AuthUserDto;
import com.optimagrowh.authentication.api.dto.AuthUserDto.UserData;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;

@RequiredArgsConstructor
@RestController
@RequestMapping(AuthUserResource.BASE_URL)
public class AuthUserResource {

    static final String BASE_URL = "/user";

    private final ModelMapper mapper;

    @GetMapping
    public AuthUserDto getUser(OAuth2Authentication authentication) {
        Authentication userAuthentication = authentication.getUserAuthentication();
        User user = (User) userAuthentication.getPrincipal();
        Set<String> authorities = AuthorityUtils.authorityListToSet(userAuthentication.getAuthorities());

        return new AuthUserDto(
                mapper.map(user, UserData.class),
                authorities);
    }
}

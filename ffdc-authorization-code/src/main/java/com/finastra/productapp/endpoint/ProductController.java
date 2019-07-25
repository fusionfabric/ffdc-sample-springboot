package com.finastra.productapp.endpoint;

import com.finastra.productapp.api.ffdc.FfdcApi;
import jdk.nashorn.internal.objects.annotations.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

import java.io.UnsupportedEncodingException;

@Controller
class ProductController {

    @Autowired
    private FfdcApi ffdcApi;

    @GetMapping("/")
    public String getProducts(Model model, @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                              @AuthenticationPrincipal OAuth2User oauth2User) throws UnsupportedEncodingException {
        model.addAttribute("userName", oauth2User.getName());
        model.addAttribute("clientName", authorizedClient.getClientRegistration().getClientName());
        model.addAttribute("accessTokenScopes", authorizedClient.getAccessToken().getScopes());
        model.addAttribute("accessTokenValue", authorizedClient.getAccessToken().getTokenValue());
        model.addAttribute("userAttributes", oauth2User.getAttributes());
        model.addAttribute("userAuthorities", oauth2User.getAuthorities());
        return "index";
    }

    @GetMapping("/reference-resources/{entitiesName}")
    public ResponseEntity getReferences(@RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient authorizedClient,
                                        @AuthenticationPrincipal OAuth2User oauth2User,
                                        @PathVariable String entitiesName){
        return ffdcApi.getReferenceSources(entitiesName);
    }

}
package com.finastra.productapp.config;

import com.finastra.productapp.api.JwtClientAuthenticationHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;

@Configuration
@EnableWebSecurity
public class FfdcConfig extends WebSecurityConfigurerAdapter {

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().antMatchers("/**").permitAll();
    }

    @Value("${auth.strong}")
    private boolean authStrong;

    @Value("${auth.keyId}")
    private String keyId;

    @Value("${ffdcapi.loginUrl}")
    private String aud;

    @Bean
    @ConfigurationProperties("finastra.oauth2.client")
    public ClientCredentialsResourceDetails oAuthDetails() {
        return new ClientCredentialsResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate restTemplate() {
        return authStrong ? clientJwtRestTemplate() : clientSecretRestTemplate();
    }

    private OAuth2RestTemplate clientSecretRestTemplate(){
        return new OAuth2RestTemplate(oAuthDetails());
    }

    private OAuth2RestTemplate clientJwtRestTemplate() {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuthDetails());
        ClientCredentialsAccessTokenProvider accessTokenProvider = new ClientCredentialsAccessTokenProvider();
        accessTokenProvider.setAuthenticationHandler(jwtClientAuthenticationHandler());
        restTemplate.setAccessTokenProvider(accessTokenProvider);
        CloseableHttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        restTemplate.setRetryBadAccessTokens(false);
        return restTemplate;
    }

    private JwtClientAuthenticationHandler jwtClientAuthenticationHandler() {
        JwtClientAuthenticationHandler authenticationHandler = new JwtClientAuthenticationHandler();
        authenticationHandler.setKeyId(keyId);
        authenticationHandler.setAud(aud);
        authenticationHandler.afterPropertiesSet();
        return authenticationHandler;
    }
}

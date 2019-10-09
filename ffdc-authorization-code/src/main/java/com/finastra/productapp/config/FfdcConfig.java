package com.finastra.productapp.config;

import com.finastra.productapp.api.JwtClientAuthenticationHandler;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.auth.DefaultClientAuthenticationHandler;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeAccessTokenProvider;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.store.jwk.JwkTokenStore;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.ExceptionTranslationFilter;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

@Configuration
@EnableWebSecurity
@EnableOAuth2Client
public class FfdcConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private OAuth2ClientContext oauth2ClientContext;

    @Autowired
    private OAuth2ClientContextFilter oauth2ClientContextFilter;

    @Value("${auth.strong}")
    private boolean authStrong;

    @Value("${auth.keyId}")
    private String keyId;

    @Value("${ffdcapi.loginUrl}")
    private String aud;

    @Value("${oauth2.callbackPath}")
    private String oauth2CallbackPath;

    @Value("${oauth2.jwkSetUri}")
    private String jwkSetUri;

    @Bean
    @ConfigurationProperties("finastra.oauth2.client")
    public AuthorizationCodeResourceDetails oAuthDetails() {
        return new AuthorizationCodeResourceDetails();
    }

    @Bean
    public OAuth2RestTemplate restTemplate() {
        return authStrong ? clientJwtRestTemplate() : clientSecretRestTemplate();
    }

    private OAuth2RestTemplate clientSecretRestTemplate(){
        OAuth2RestTemplate restTemplate = new  OAuth2RestTemplate(oAuthDetails(), oauth2ClientContext);
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider ();
        accessTokenProvider.setAuthenticationHandler(new DefaultClientAuthenticationHandler());
        restTemplate.setAccessTokenProvider(accessTokenProvider);
        CloseableHttpClient httpClient = HttpClientBuilder.create().useSystemProperties().build();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory(httpClient));
        restTemplate.setRetryBadAccessTokens(false);
        return restTemplate;
    }

    private OAuth2RestTemplate clientJwtRestTemplate() {
        OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(oAuthDetails(),oauth2ClientContext);
        AuthorizationCodeAccessTokenProvider accessTokenProvider = new AuthorizationCodeAccessTokenProvider ();
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

    @Bean
    public DefaultTokenServices tokenService (){
        DefaultTokenServices services = new DefaultTokenServices();
        services.setTokenStore(tokenStore());
        return services;
    }

    @Bean
    public OAuth2ClientAuthenticationProcessingFilter oauth2ClientAuthenticationProcessingFilter() {;
        OAuth2ClientAuthenticationProcessingFilter filter =
                new OAuth2ClientAuthenticationProcessingFilter(oauth2CallbackPath);
        filter.setRestTemplate(restTemplate());
        filter.setTokenServices(tokenService());
        return filter;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return new LoginUrlAuthenticationEntryPoint(oauth2CallbackPath);
    }

    @Bean
    public JwkTokenStore tokenStore() {
        return new JwkTokenStore(jwkSetUri);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.exceptionHandling()
                .authenticationEntryPoint(authenticationEntryPoint())
                .and()
                .authorizeRequests()
                .antMatchers("/results").authenticated()
                .anyRequest().permitAll()
                .and()
                .addFilterAfter(oauth2ClientContextFilter, ExceptionTranslationFilter.class)
                .addFilterBefore(oauth2ClientAuthenticationProcessingFilter(), FilterSecurityInterceptor.class)
                .logout()
                .logoutUrl("/appLogout")
                .logoutSuccessUrl("/logout");
    }
}

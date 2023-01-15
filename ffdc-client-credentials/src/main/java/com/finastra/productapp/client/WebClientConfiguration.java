package com.finastra.productapp.client;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.security.oauth2.client.AuthorizedClientServiceOAuth2AuthorizedClientManager;
import org.springframework.security.oauth2.client.ClientCredentialsOAuth2AuthorizedClientProvider;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.endpoint.DefaultClientCredentialsTokenResponseClient;
import org.springframework.security.oauth2.client.http.OAuth2ErrorResponseErrorHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction;
import org.springframework.security.oauth2.core.http.converter.OAuth2AccessTokenResponseHttpMessageConverter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.netty.http.client.HttpClient;

import java.time.Duration;
import java.util.Arrays;
import java.util.function.Consumer;

@Configuration
@RequiredArgsConstructor
class WebClientConfiguration {
    public static final String FFDC_API_CLIENT = "ffdcApiWebClient";
    private static final int CONNECT_TIMEOUT_MILLISECONDS = 2000;
    private static final int READ_TIMEOUT_MILLISECONDS = 30_000;
    private static final String CLIENT_REGISTRATION_ID = "finastra";

    private final RestTemplateBuilder restTemplateBuilder;


    @Bean(name = FFDC_API_CLIENT)
    public WebClient ffdcWebClient(ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService,
            WebClient.Builder webClientBuilder,
            @Value("${ffdcapi.baseUrl}") String baseUrl) {
        return webClientBuilder
                .baseUrl(baseUrl)
                .clientConnector(configureHttpConnector())
                .apply(prepareOAuth2ConfigurationWithAuthorizedClientServiceManager(
                        clientRegistrationRepository, authorizedClientService))
                .build();
    }

    private ReactorClientHttpConnector configureHttpConnector() {
        HttpClient httpClient = HttpClient.create();
        return new ReactorClientHttpConnector(httpClient);
    }

    private Consumer<WebClient.Builder> prepareOAuth2ConfigurationWithAuthorizedClientServiceManager(
            ClientRegistrationRepository clientRegistrationRepository,
            OAuth2AuthorizedClientService authorizedClientService) {

        final ServletOAuth2AuthorizedClientExchangeFilterFunction oauth2 =
                composeOAuth2FilterWithAuthorizedClientServiceManager(authorizedClientService,
                        clientRegistrationRepository);

        oauth2.setDefaultClientRegistrationId(CLIENT_REGISTRATION_ID);

        return oauth2.oauth2Configuration();
    }

    private ServletOAuth2AuthorizedClientExchangeFilterFunction composeOAuth2FilterWithAuthorizedClientServiceManager(
            OAuth2AuthorizedClientService authorizedClientService,
            ClientRegistrationRepository clientRegistrationRepository) {

        final DefaultClientCredentialsTokenResponseClient clientCredentialsTokenResponseClient =
                new DefaultClientCredentialsTokenResponseClient();

        RestTemplate restTemplate = prepareRestTemplate();
        clientCredentialsTokenResponseClient.setRestOperations(restTemplate);

        ClientCredentialsOAuth2AuthorizedClientProvider
                clientCredentialsOAuth2AuthorizedClientProvider =
                new ClientCredentialsOAuth2AuthorizedClientProvider();
        clientCredentialsOAuth2AuthorizedClientProvider.setAccessTokenResponseClient(
                clientCredentialsTokenResponseClient);

        AuthorizedClientServiceOAuth2AuthorizedClientManager
                authorizedClientServiceOAuth2AuthorizedClientManager =
                new AuthorizedClientServiceOAuth2AuthorizedClientManager(
                        clientRegistrationRepository, authorizedClientService);

        authorizedClientServiceOAuth2AuthorizedClientManager.setAuthorizedClientProvider(
                clientCredentialsOAuth2AuthorizedClientProvider);

        return new ServletOAuth2AuthorizedClientExchangeFilterFunction(
                authorizedClientServiceOAuth2AuthorizedClientManager);
    }

    private RestTemplate prepareRestTemplate() {
        return restTemplateBuilder
                .messageConverters(Arrays.asList(new FormHttpMessageConverter(),
                        new OAuth2AccessTokenResponseHttpMessageConverter()))
                .interceptors(new RestTemplateLoggerInterceptor())
                .errorHandler(new OAuth2ErrorResponseErrorHandler())
                .setConnectTimeout(Duration.ofMillis(CONNECT_TIMEOUT_MILLISECONDS))
                .setReadTimeout(Duration.ofMillis(READ_TIMEOUT_MILLISECONDS))
                .build();
    }
}

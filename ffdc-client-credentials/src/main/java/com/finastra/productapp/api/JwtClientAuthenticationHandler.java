package com.finastra.productapp.api;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpHeaders;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaSigner;
import org.springframework.security.jwt.crypto.sign.Signer;
import org.springframework.security.oauth2.client.resource.OAuth2ProtectedResourceDetails;
import org.springframework.security.oauth2.client.token.auth.ClientAuthenticationHandler;
import org.springframework.util.Assert;
import org.springframework.util.MultiValueMap;

import java.security.PrivateKey;
import java.security.interfaces.RSAPrivateKey;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
public class JwtClientAuthenticationHandler implements ClientAuthenticationHandler, InitializingBean {

    public static final String CLIENT_ASSERTION_TYPE = "client_assertion_type";
    public static final String CLIENT_ASSERTION = "client_assertion";
    public static final String CLIENT_ASSERTION_TYPE_JWT = "urn:ietf:params:oauth:client-assertion-type:jwt-bearer";

    public static final int DEFAULT_EXPIRATION = 60 * 60;

    private Signer signer;
    private String keyId;
    private String aud;

    private int expiration = DEFAULT_EXPIRATION;

    private ObjectMapper objectMapper = new ObjectMapper();

    public JwtClientAuthenticationHandler() {}

    @Override
    public void authenticateTokenRequest(OAuth2ProtectedResourceDetails resource, MultiValueMap<String, String> form, HttpHeaders headers) {
        if (resource.isAuthenticationRequired()) {
            JwtGrant grant = newGrant(resource);
            Jwt jwt = null;
            try {
                Map<String, String> jwtEncodeHeader = new HashMap<>(1);
                jwtEncodeHeader.put("kid", keyId);
                jwt = JwtHelper.encode(objectMapper.writeValueAsString(grant), signer, jwtEncodeHeader);
                form.set(CLIENT_ASSERTION_TYPE, CLIENT_ASSERTION_TYPE_JWT);
                form.set(CLIENT_ASSERTION, jwt.getEncoded());
            } catch (JsonProcessingException e) {
                throw new IllegalStateException(e);
            }
        }
    }

    private JwtGrant newGrant(OAuth2ProtectedResourceDetails resource) {
        int currentTimeSeconds = (int) (System.currentTimeMillis() / 1000);
        JwtGrant jwtGrant = new JwtGrant();
        jwtGrant.setJti(UUID.randomUUID().toString());
        jwtGrant.setIssuer(resource.getClientId());
        jwtGrant.setSubject(resource.getClientId());
        jwtGrant.setAudience(aud);
        jwtGrant.setExpires(currentTimeSeconds + expiration);
        jwtGrant.setIssuedAt(currentTimeSeconds);
        return jwtGrant;
    }

    public void setKeyId(String keyId) {
        this.keyId = keyId;
    }

    public void setExpiration(int expiration) {
        this.expiration = expiration;
    }

    public void setAud(String aud) {
        this.aud = aud;
    }

    @Override
    public void afterPropertiesSet() {
        if (keyId == null) {
            throw new IllegalArgumentException("keystore property is required");
        }
        try {
            PrivateKey privateKey = PrivateKeyReader.get("pkcs8_private.pem");
            Assert.state(privateKey instanceof RSAPrivateKey, "KeyPair must be an RSA");
            signer = new RsaSigner((RSAPrivateKey) privateKey);
        } catch (Exception e) {
            log.error("failed to generate private key", e);
        }
    }

    private static class JwtGrant {

        @JsonProperty("jti")
        private String jti;

        @JsonProperty("iss")
        private String issuer;

        @JsonProperty("sub")
        private String subject;

        @JsonProperty("aud")
        private String audience;

        @JsonProperty("exp")
        private int expires;

        @JsonProperty("iat")
        private int issuedAt;

        public String getJti() { return jti;}

        public void setJti(String jti) { this.jti = jti; }

        public String getIssuer() {
            return issuer;
        }

        public void setIssuer(String issuer) {
            this.issuer = issuer;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getAudience() {
            return audience;
        }

        public void setAudience(String audience) {
            this.audience = audience;
        }

        public int getExpires() {
            return expires;
        }

        public void setExpires(int expires) {
            this.expires = expires;
        }

        public int getIssuedAt() {
            return issuedAt;
        }

        public void setIssuedAt(int issuedAt) {
            this.issuedAt = issuedAt;
        }


    }
}

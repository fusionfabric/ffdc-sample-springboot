package com.finastra.productapp.api;


import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;

public class PrivateKeyReader {

  public static PrivateKey get(String resourceName) throws Exception {
    String privateKeyPEM  = new String(Files.readAllBytes(Paths.get(ClassLoader.getSystemResource(resourceName).toURI())), StandardCharsets.UTF_8);
    privateKeyPEM  = privateKeyPEM
            .replace("-----BEGIN PRIVATE KEY-----", "")
            .replace("-----END PRIVATE KEY-----", "");
    byte[] privateKeyDER = new Base64().decode(privateKeyPEM );
    PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(privateKeyDER);
    return KeyFactory.getInstance("RSA").generatePrivate(spec);
  }

}
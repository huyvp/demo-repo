package dev.samsung.jwtKey;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.interfaces.RSAPublicKey;

public class ExportJwks {

    public static void main(String[] args) throws Exception {

        String keystore = "ssia.jks";
        String password = "ssia123";
        String alias = "ssia";

        // Load keystore
        KeyStore ks = KeyStore.getInstance("JKS");
        ks.load(new java.io.FileInputStream(keystore), password.toCharArray());

        // Get public key
        Certificate cert = ks.getCertificate(alias);
        RSAPublicKey publicKey = (RSAPublicKey) cert.getPublicKey();

        // Build JWKS
        RSAKey rsaKey = new RSAKey.Builder(publicKey)
                .keyID("key-1")
                .build();

        JWKSet jwkSet = new JWKSet(rsaKey);

        // Write file
        try (FileOutputStream fos = new FileOutputStream("jwks.json")) {
            fos.write(jwkSet.toJSONObject().toString()
                    .getBytes(StandardCharsets.UTF_8));
        }

        System.out.println("✅ Exported jwks.json");
    }
}
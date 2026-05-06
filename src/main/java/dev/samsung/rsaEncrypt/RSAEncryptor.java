package dev.samsung.rsaEncrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAEncryptor {
    private static final Cipher cipher;

    static {
        try {
            cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
        } catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
            throw new RuntimeException(e);
        }
    }

    public void decrypt(String privateKeyStr, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PrivateKey privateKey = KeyFactory.getInstance("RSA").generatePrivate(
                new PKCS8EncodedKeySpec(Base64.getDecoder().decode(privateKeyStr))
        );

        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        System.out.println("Decrypted: " + new String(cipher.doFinal(Base64.getDecoder().decode(data))));
    }

    public String encrypt(String publicKeyStr, String data) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
                new X509EncodedKeySpec(Base64.getDecoder().decode(publicKeyStr))
        );
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encryptedBytes = cipher.doFinal(data.getBytes());
        String encryptedBase64 = Base64.getEncoder().encodeToString(encryptedBytes);
        System.out.println("Encrypted: " + encryptedBase64);
        return encryptedBase64;
    }

    public static void main(String[] args) {
        try {
            String publicKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/pem/public_key.pem")));
            String publicKeyStr = extractKey(publicKeyPem);

            String privateKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/pem/private_key.pem")));
            String privateKeyStr = extractKey(privateKeyPem);

            RSAEncryptor rsa = new RSAEncryptor();

            String data = "Xin chào RSA!";
            System.out.println("Original: " + data);

            String encryptedBase64 = rsa.encrypt(publicKeyStr, data);

            rsa.decrypt(privateKeyStr, encryptedBase64);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String extractKey(String pem) {
        return pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s+", "");
    }
}

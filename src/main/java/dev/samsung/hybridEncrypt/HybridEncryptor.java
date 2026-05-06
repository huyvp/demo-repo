package dev.samsung.hybridEncrypt;

import javax.crypto.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class HybridEncryptor {

    public static void main(String[] args) throws Exception {
        String publicKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/pem/public_key.pem")));
        PublicKey publicKey = loadPublicKey(publicKeyPem);

        SecretKey aesKey = generateSecretKey();

        String jsonPayload = "{\"message\":\"Hello from HuyVP!\",\"amount\":12345}";

        String encryptedKey = encryptAESKeyWithRSA(aesKey, publicKey);
        String encryptedPayload = encryptData(aesKey, jsonPayload); // Gắn IV vào trong

        System.out.println("Encrypted AES Key:");
        System.out.println(encryptedKey);
        System.out.println("Encrypted Payload:");
        System.out.println(encryptedPayload);
    }

    private static PublicKey loadPublicKey(String pemContent) throws Exception {
        String publicKey = pemContent
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");
        byte[] bytes = Base64.getDecoder().decode(publicKey);
        X509EncodedKeySpec spec = new X509EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePublic(spec);
    }

    private static SecretKey generateSecretKey() throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance("AES");
        keyGen.init(256);
        return keyGen.generateKey();
    }

    private static String encryptAESKeyWithRSA(SecretKey aesKey, PublicKey publicKey) throws Exception {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] encrypted = cipher.doFinal(aesKey.getEncoded());
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private static String encryptData(SecretKey aesKey, String data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, aesKey);
        byte[] bytes = cipher.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(bytes);
    }
//    private static String encryptData(SecretKey aesKey, String data) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//
//        byte[] iv = new byte[GCM_IV_LENGTH];
//        new SecureRandom().nextBytes(iv);
//        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
//
//        cipher.init(Cipher.ENCRYPT_MODE, aesKey, spec);
//        byte[] encrypted = cipher.doFinal(data.getBytes());
//
//        // Gắn IV vào đầu encrypted data
//        byte[] combined = new byte[iv.length + encrypted.length];
//        System.arraycopy(iv, 0, combined, 0, iv.length);
//        System.arraycopy(encrypted, 0, combined, iv.length, encrypted.length);
//
//        return Base64.getEncoder().encodeToString(combined);
//    }
}




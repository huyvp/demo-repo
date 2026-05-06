package dev.samsung.hybridEncrypt;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Base64;

public class HybridDecryptor {

    private static final int GCM_IV_LENGTH = 12;

    public static void main(String[] args) throws Exception {
        String privateKeyPem = new String(Files.readAllBytes(Paths.get("src/main/resources/pem/private_key.pem")));
        PrivateKey privateKey = loadPrivateKey(privateKeyPem);

        // Copy từ kết quả mã hóa
        String encryptedKey = "HLHuAvTfno2mF1D5w21/QEgk883l54b+ANjyYbJcMKnteJL6t1voBgDb7B5qUciJuzXlj1PHqn2A1Bl8NqBcTzg0tVMbH98XFNqMAUjabaZSY0P1SlQoItFn2PqwrLgY49e570SoL8wnXE+t5MhMvqLK7yTv9+i2szNSmy3CxWEnLvYiz3zE6FTJ+Zw3dkbQIynVb1HHNOQehYDXxbTDvT9BI6bSZcMN70aHIOcMTKMusIPDdfFX/3GxuBd5fUYp1J08Mq9r+7hXSvbS99/GKrwbqW8RlvkebTGDwxBk8vIBuot1G8anvxjosJFIsaAU1kljMnS+rAiVaucPEOiZLw==";
        String encryptedPayload = "Y54JXH+ZnQnEDUWddr4QbV18vDyFwdLDwqAgQVdSL9L2Ma/zsuafFrk/SdNkpJK2t2EYtHTep1uWNUSptQI=";

        SecretKey aesKey = decryptEncryptedAESKey(encryptedKey, privateKey);
        String decryptedData = decryptData(encryptedPayload, aesKey);

        System.out.println("✅ Decrypted Payload:");
        System.out.println(decryptedData);
    }

    private static PrivateKey loadPrivateKey(String pemContent) throws Exception {
        String privateKey = pemContent
                .replace("-----BEGIN PRIVATE KEY-----", "")
                .replace("-----END PRIVATE KEY-----", "")
                .replaceAll("\\s", "");
        byte[] bytes = Base64.getDecoder().decode(privateKey);
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(bytes);
        return KeyFactory.getInstance("RSA").generatePrivate(spec);
    }

//    private static SecretKey decryptEncryptedAESKey(String encryptedKey, PrivateKey privateKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("RSA");
//        cipher.init(Cipher.DECRYPT_MODE, privateKey);
//        byte[] decodedKey = Base64.getDecoder().decode(encryptedKey);
//        byte[] bytes = cipher.doFinal(decodedKey);
//        return new SecretKeySpec(bytes, "AES");
//    }
    private static SecretKey decryptEncryptedAESKey(String encryptedKey, PrivateKey privateKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] decodedKey = Base64.getDecoder().decode(encryptedKey);
        byte[] bytes = cipher.doFinal(decodedKey);
        return new SecretKeySpec(bytes, "AES/GCM/NoPadding");
    }

    private static String decryptData(String data, SecretKey aesKey) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, aesKey);
        byte[] decodedData = Base64.getDecoder().decode(data);
        byte[] bytes = cipher.doFinal(decodedData);
        return new String(bytes);
    }

//    private static String decryptData(String data, SecretKey aesKey) throws Exception {
//        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
//        byte[] decoded = Base64.getDecoder().decode(data);
//
//        byte[] iv = Arrays.copyOfRange(decoded, 0, GCM_IV_LENGTH);
//        byte[] ciphertext = Arrays.copyOfRange(decoded, GCM_IV_LENGTH, decoded.length);
//
//        GCMParameterSpec spec = new GCMParameterSpec(128, iv);
//        cipher.init(Cipher.DECRYPT_MODE, aesKey, spec);
//
//        byte[] bytes = cipher.doFinal(ciphertext);
//        return new String(bytes);
//    }
}


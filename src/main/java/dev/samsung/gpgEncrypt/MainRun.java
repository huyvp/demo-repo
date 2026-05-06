package dev.samsung.gpgEncrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Security;
import java.util.Base64;

public class MainRun {

    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // B1: Đọc public & private key từ file .asc rồi encode thành base64
        String base64PublicKey = encodeFileToBase64("../public.asc");
        String base64PrivateKey = encodeFileToBase64("../private.asc");

        System.out.println(base64PublicKey);
        System.out.println(base64PrivateKey);

        // B2: Đọc ảnh rồi encode thành base64
        String base64Image = encodeFileToBase64("src/main/resources/image.png");

        // B3: Encrypt ảnh
        String offerId = "offer-test-01";
        String result = GPGUtil.encrypt(base64PublicKey, offerId, base64Image);
        String[] paths = result.split(",");
        String tempDir = paths[0];
        String encryptedFilePath = paths[1];

        System.out.println("✅ Đã mã hóa, file tại: " + encryptedFilePath);

        // B4: Decrypt ảnh
        char[] passphrase = "@huyvp88".toCharArray();
        String decryptedBase64 = GPGUtil.decryptAsBase64(base64PrivateKey, encryptedFilePath, passphrase);

        // B5: Ghi ảnh đã giải mã thành file
        byte[] imageBytes = Base64.getDecoder().decode(decryptedBase64);
        Files.write(Paths.get(tempDir + "/restored_idcard.png"), imageBytes);
        System.out.println("✅ Đã giải mã xong: " + tempDir + "/restored_idcard.jpg");
    }

    private static String encodeFileToBase64(String filePath) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(filePath));
        return Base64.getEncoder().encodeToString(content);
    }
}

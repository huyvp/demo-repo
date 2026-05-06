package dev.samsung.gpgEncrypt;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

public class KeyBase64Converter {
    public static void main(String[] args) throws Exception {
        byte[] pubBytes = Files.readAllBytes(Paths.get("../public.asc"));
        byte[] privBytes = Files.readAllBytes(Paths.get("../private.asc"));

        String pubBase64 = Base64.getEncoder().encodeToString(pubBytes);
        String privBase64 = Base64.getEncoder().encodeToString(privBytes);

        System.out.println("Public Key Base64:\n" + pubBase64);
        System.out.println("Private Key Base64:\n" + privBase64);
    }
}


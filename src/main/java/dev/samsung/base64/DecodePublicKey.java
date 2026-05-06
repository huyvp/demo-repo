package dev.samsung.base64;

import java.util.Base64;

public class DecodePublicKey {
    public static void main(String[] args) {
        String publicKey = "-----BEGIN PUBLIC KEY-----\n" +
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA1HX/3PPA58BTwuBCt516\n" +
                "E+edxxLwefmcmzjpqVExjLc+gITOsEecyBwUHSMY7O3E9K8S04MqBU72ptcYOTxX\n" +
                "tp2+Gsz+tYl8NBBD84EsXYoX9hMBUpRrQe9gyH+xiqE8A+JNO4y+sHd1fpVLNmhD\n" +
                "FgqApMKgqftWrezgRSGwn48hWDfjUzlZLTqsp/V9S+6MCXJPKbnp5kL7mUx5uJQI\n" +
                "P+XAy4Cjtfbi5ARbzOahKwEBNE3688a7qW6KzgKI8wc8Ej1ekF1pbL8QUkbvw58r\n" +
                "n2sFjFo13ZnVC4roe/Q0eLQ2lpbaiCfPDg4Tf/l4czixDS1rAnssj8PBpqrq1rRg\n" +
                "WQIDAQAB\n" +
                "-----END PUBLIC KEY-----";

        String newPk = publicKey.replace("-----BEGIN PUBLIC KEY-----", "")
                        .replace("-----END PUBLIC KEY-----", "")
                        .replace("\r", "")
                        .replace("\n", "");
        System.out.println(newPk);
        byte[] bytes = Base64.getDecoder().decode(newPk);
    }
}

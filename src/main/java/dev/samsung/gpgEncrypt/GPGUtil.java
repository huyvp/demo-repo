package dev.samsung.gpgEncrypt;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.openpgp.*;
import org.bouncycastle.openpgp.jcajce.JcaPGPObjectFactory;
import org.bouncycastle.openpgp.jcajce.JcaPGPPublicKeyRingCollection;
import org.bouncycastle.openpgp.jcajce.JcaPGPSecretKeyRingCollection;
import org.bouncycastle.openpgp.operator.jcajce.*;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.security.SecureRandom;
import java.security.Security;
import java.util.Base64;
import java.util.Date;
import java.util.Iterator;

public class GPGUtil {
    private static PGPPublicKey publicKey;
    private static PGPPrivateKey privKey;
    private static final Base64.Decoder DECODER = Base64.getDecoder();

    public static String encrypt(String base64PublicKey, String offerId, String base64Img) throws PGPException, IOException {
        Security.addProvider(new BouncyCastleProvider());
        if (publicKey == null) {
            publicKey = getPublicKeyFromEncodedString(base64PublicKey);
        }
        PGPEncryptedDataGenerator encGen = new PGPEncryptedDataGenerator(
                new JcePGPDataEncryptorBuilder(PGPEncryptedData.CAST5)
                        .setSecureRandom(new SecureRandom()).setProvider("BC"));
        encGen.addMethod(new JcePublicKeyKeyEncryptionMethodGenerator(publicKey).setProvider("BC"));

        Path tempPath = Files.createTempDirectory(offerId);
        String fileName = "img_idcard";
        Path filePath = tempPath.resolve(fileName);
        Files.createFile(filePath);

        OutputStream output = new BufferedOutputStream(new FileOutputStream(filePath.toFile()));
        OutputStream encryptedOut = encGen.open(output, new byte[1024]);

        PGPLiteralDataGenerator literalDataGenerator = new PGPLiteralDataGenerator();
        OutputStream literalOutput = literalDataGenerator.open(encryptedOut, PGPLiteralData.BINARY, PGPLiteralData.CONSOLE, base64Img.getBytes().length, new Date());
        literalOutput.write(base64Img.getBytes());
        literalOutput.close();
        encryptedOut.close();
        output.close();

        return tempPath + "," + filePath;
    }

    public static String decryptAsBase64(String base64PrivateKey, String inputFilePath, char[] passphrase) throws IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());

        if (privKey == null) {
            privKey = getPrivateKeyFromEncodedString(base64PrivateKey, passphrase);
        }

        ByteArrayOutputStream fileOutput = null;
        try {
            fileOutput = new ByteArrayOutputStream();
            try (InputStream fileInput = PGPUtil.getDecoderStream(new FileInputStream(inputFilePath))) {

                JcaPGPObjectFactory pgpObjectFactory = new JcaPGPObjectFactory(fileInput);
                PGPEncryptedDataList encryptedDataList = (PGPEncryptedDataList) pgpObjectFactory.nextObject();

                Iterator<PGPEncryptedData> it = encryptedDataList.getEncryptedDataObjects();
                PGPPrivateKey privateKey = null;
                PGPPublicKeyEncryptedData publicKeyEncryptedData = null;

                while (privateKey == null && it.hasNext()) {
                    publicKeyEncryptedData = (PGPPublicKeyEncryptedData) it.next();

                    if (publicKeyEncryptedData.getKeyID() == privKey.getKeyID()) {
                        privateKey = privKey;
                    }
                }

                if (privateKey == null) {
                    throw new IllegalArgumentException("Secret key for message not found.");
                }

                InputStream clear = publicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().build(privateKey));

                JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);

                Object message = plainFact.nextObject();

                if (message instanceof PGPCompressedData) {
                    PGPCompressedData cData = (PGPCompressedData) message;
                    plainFact = new JcaPGPObjectFactory(cData.getDataStream());

                    message = plainFact.nextObject();
                }

                if (message instanceof PGPLiteralData) {
                    PGPLiteralData ld = (PGPLiteralData) message;

                    try (InputStream unc = ld.getInputStream()) {

                        int ch;
                        while ((ch = unc.read()) >= 0) {
                            fileOutput.write(ch);
                        }
                    } catch (IOException e) {
                        // Log or re-throw the exception as necessary
                    }
                } else {
                    throw new PGPException("Message is not a simple file - type unknown.");
                }

                if (publicKeyEncryptedData.isIntegrityProtected()) {
                    if (!publicKeyEncryptedData.verify()) {
                        System.err.println("Data is corrupt.");
                    }
                }
            } catch (PGPException | IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (fileOutput != null) {
                fileOutput.close();
            }
        }

        return fileOutput.toString();
    }

    public static byte[] decrypt(String base64PrivateKey, String inputFilePath, char[] passphrase) throws IOException, PGPException {
        Security.addProvider(new BouncyCastleProvider());
        if (privKey == null) {
            privKey = getPrivateKeyFromEncodedString(base64PrivateKey, passphrase);
        }

        ByteArrayOutputStream fileOutput = null;
        try {
            fileOutput = new ByteArrayOutputStream();
            try (InputStream fileInput = PGPUtil.getDecoderStream(new FileInputStream(inputFilePath))) {

                JcaPGPObjectFactory pgpObjectFactory = new JcaPGPObjectFactory(fileInput);
                PGPEncryptedDataList encryptedDataList = (PGPEncryptedDataList) pgpObjectFactory.nextObject();

                Iterator<PGPEncryptedData> it = encryptedDataList.getEncryptedDataObjects();
                PGPPrivateKey privateKey = null;
                PGPPublicKeyEncryptedData publicKeyEncryptedData = null;

                while (privateKey == null && it.hasNext()) {
                    publicKeyEncryptedData = (PGPPublicKeyEncryptedData) it.next();

                    if (publicKeyEncryptedData.getKeyID() == privKey.getKeyID()) {
                        privateKey = privKey;
                    }
                }

                if (privateKey == null) {
                    throw new IllegalArgumentException("Secret key for message not found.");
                }

                InputStream clear = publicKeyEncryptedData.getDataStream(new JcePublicKeyDataDecryptorFactoryBuilder().build(privateKey));

                JcaPGPObjectFactory plainFact = new JcaPGPObjectFactory(clear);

                Object message = plainFact.nextObject();

                if (message instanceof PGPCompressedData) {
                    PGPCompressedData cData = (PGPCompressedData) message;
                    plainFact = new JcaPGPObjectFactory(cData.getDataStream());

                    message = plainFact.nextObject();
                }

                if (message instanceof PGPLiteralData) {
                    PGPLiteralData ld = (PGPLiteralData) message;

                    try (InputStream unc = ld.getInputStream()) {

                        int ch;
                        while ((ch = unc.read()) >= 0) {
                            fileOutput.write(ch);
                        }
                    } catch (IOException e) {
                        // Log or re-throw the exception as necessary
                    }
                } else {
                    throw new PGPException("Message is not a simple file - type unknown.");
                }

                if (publicKeyEncryptedData.isIntegrityProtected()) {
                    if (!publicKeyEncryptedData.verify()) {
                        System.err.println("Data is corrupt.");
                    }
                }
            } catch (PGPException | IOException e) {
                throw new RuntimeException(e);
            }
        } finally {
            if (fileOutput != null) {
                fileOutput.close();
            }
        }
//        System.out.println(fileOutput.toString());

        byte[] data = decodeAsByte(fileOutput.toString());
//        try (OutputStream stream = new FileOutputStream(outputFilePath)) {
//            stream.write(data);
//        }
        return data;
    }

    protected static PGPPublicKey getPublicKeyFromEncodedString(String base64PublicKey) throws IOException, PGPException {
        String asciiPubKey = decodeAsString(base64PublicKey, StandardCharsets.UTF_8);
        try (InputStream keyInputStream = PGPUtil.getDecoderStream(new ByteArrayInputStream(asciiPubKey.getBytes(StandardCharsets.UTF_8)))) {
            JcaPGPPublicKeyRingCollection pgpPub = new JcaPGPPublicKeyRingCollection(keyInputStream);
            PGPPublicKeyRing keyRing = pgpPub.getKeyRings().next();
            return keyRing.getPublicKey();
        }
    }

    protected static PGPPrivateKey getPrivateKeyFromEncodedString(String base64PrivateKey, char[] passPhrase) throws IOException, PGPException {
        String asciiPrivKey = decodeAsString(base64PrivateKey, StandardCharsets.UTF_8);

        try (InputStream keyInputStream = PGPUtil.getDecoderStream(new ByteArrayInputStream(asciiPrivKey.getBytes(StandardCharsets.UTF_8)))) {

            // Initialize secret key ring collection
            JcaPGPSecretKeyRingCollection pgpSec = new JcaPGPSecretKeyRingCollection(keyInputStream);

            // Loop through all secret key rings
            for (PGPSecretKeyRing keyRing : pgpSec) {
                // Extract the private key
                PGPPrivateKey myKey = keyRing.getSecretKey().extractPrivateKey(new JcePBESecretKeyDecryptorBuilder().setProvider("BC").build(passPhrase));
                if (myKey != null) {
                    return myKey;
                }
            }
        }
        throw new IllegalArgumentException("Can't decode private key.");
    }

    public static String decodeAsString(String str, Charset charset) {
        return new String(DECODER.decode(str), charset);
    }

    public static byte[] decodeAsByte(String str) {
        return DECODER.decode(str);
    }
}

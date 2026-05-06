# 🔐 Hướng dẫn tạo `ssia.jks` và export `jwks.json` (chuẩn cho IAM + Resource Server)

## 🎯 Mục tiêu

* Tạo **keystore chuẩn (`ssia.jks`) bằng `keytool`**
* Chỉ chứa **key pair cần thiết (RSA)**
* Dùng code Java để **export `jwks.json`** (public key cho CMS)

---

# 🧱 1. Tạo file `ssia.jks` bằng keytool

## ✅ Lệnh chuẩn (QUAN TRỌNG)

```bash
keytool -genkeypair \
  -alias ssia \
  -keyalg RSA \
  -keysize 2048 \
  -storetype JKS \
  -keystore ssia.jks \
  -storepass ssia123 \
  -keypass ssia123 \
  -validity 3650 \
  -dname "CN=iam-service"
```

---

## 🧠 Giải thích tối giản

| Param                | Ý nghĩa                             |
| -------------------- | ----------------------------------- |
| `-alias ssia`        | tên key                             |
| `-keyalg RSA`        | dùng RSA (bắt buộc cho JWT RS256)   |
| `-keysize 2048`      | độ dài key                          |
| `-storetype JKS`     | ép dùng JKS (tránh lỗi PKCS12)      |
| `-keystore ssia.jks` | file output                         |
| `-storepass`         | password keystore                   |
| `-keypass`           | password private key                |
| `-dname`             | metadata (không quan trọng với JWT) |

---

## 📦 Kết quả

```text
ssia.jks
```

---

## 🔍 Verify file

```bash
keytool -list -keystore ssia.jks -storepass ssia123
```

---

## ✅ Output đúng

```text
Keystore type: JKS
Your keystore contains 1 entry
```

---

# ⚠️ Lưu ý cực quan trọng

* ❌ Không dùng default (vì có thể thành PKCS12)
* ❌ Không đổi password lung tung
* ❌ Không commit `.jks` lên git (prod)

---

# 🧩 2. Code Java export `jwks.json`

## 📦 Dependency

```xml
<dependency>
  <groupId>com.nimbusds</groupId>
  <artifactId>nimbus-jose-jwt</artifactId>
  <version>9.37</version>
</dependency>
```

---

## 🚀 Code

```java
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.security.Key;
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
```

---

# 📄 Output

```text
jwks.json
```

---

## 📌 Format chuẩn

```json
{
  "keys": [
    {
      "kty": "RSA",
      "kid": "key-1",
      "n": "...",
      "e": "AQAB"
    }
  ]
}
```

---

# 🔄 3. Flow sử dụng trong hệ thống

## IAM Service

* Load `ssia.jks`
* Dùng private key → ký JWT

---

## CMS BE Service

* Gọi:

```text
http://iam/.well-known/jwks.json
```

* Lấy public key → verify JWT

---

# 🎯 Tổng kết

| Thành phần  | Vai trò                       |
| ----------- | ----------------------------- |
| `ssia.jks`  | chứa private key để ký JWT    |
| `jwks.json` | chứa public key để verify JWT |

---

# ✅ Best practice

* ✔ Generate `.jks` bằng keytool
* ✔ Không viết tay `jwks.json`
* ✔ Generate JWKS từ public key
* ✔ IAM giữ private key
* ✔ Resource server chỉ dùng JWKS

---

# 🚀 Done

Bạn chỉ cần:

```text
1. chạy keytool → có ssia.jks
2. chạy Java → có jwks.json
3. plug vào Spring Security → chạy
```

---

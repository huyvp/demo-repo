
---

## ✅ Dùng `OpenSSL` để tạo RSA Key (2048 bit)

### 📌 Bước 1: Tạo private key (`private_key.pem`)

```bash
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
```

### 📌 Bước 2: Tạo public key từ private key (`public_key.pem`)

```bash
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

---

## ✅ Sử dụng trong Java

Khi dùng với Java, bạn cần chắc chắn rằng:

* Public key là **X.509**
* Private key là **PKCS#8**

Mặc định, khi bạn chạy lệnh `openssl` tạo file mà không chỉ định đường dẫn đầy đủ, thì file sẽ được **lưu ở thư mục hiện tại (current working directory)** – chính là **nơi bạn đang mở terminal hoặc command prompt**.

---

## 📍 Ví dụ minh họa:

### ✅ Trên **Windows CMD** hoặc **PowerShell**:

```bash
cd C:\Users\huyvp\Documents\rsa-keys
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

* File `private_key.pem` và `public_key.pem` sẽ được lưu tại:

```
C:\Users\huyvp\Documents\rsa-keys\
```

---

### ✅ Trên **macOS/Linux terminal**:

```bash
cd ~/rsa-keys
openssl genpkey -algorithm RSA -out private_key.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in private_key.pem -out public_key.pem
```

* Sẽ lưu tại:

```
/home/your-user-name/rsa-keys/
```

hoặc

```
/Users/your-user-name/rsa-keys/
```

---

## 🧠 Mẹo: Chỉ định đường dẫn cụ thể

Bạn có thể lưu vào thư mục bạn muốn như sau:

```bash
openssl genpkey -algorithm RSA -out ./keys/private_key.pem -pkeyopt rsa_keygen_bits:2048
openssl rsa -pubout -in ./keys/private_key.pem -out ./keys/public_key.pem
```

> Thư mục `keys/` phải tồn tại trước khi chạy lệnh (tạo bằng `mkdir keys` nếu chưa có).

---

## ✅ Kiểm tra file đã tạo:

```bash
dir keys  # Windows
ls keys   # macOS/Linux
```

---
### 🔐 **RSA là gì? (Rivest–Shamir–Adleman)**

RSA là một thuật toán **mã hóa bất đối xứng (asymmetric encryption)** rất phổ biến trong bảo mật hiện đại. Nó dùng **hai khóa khác nhau**:

* **Public Key** (khóa công khai): dùng để mã hóa
* **Private Key** (khóa riêng): dùng để giải mã

---

## ✅ Ứng dụng của RSA:

| Ứng dụng                           | Vai trò của RSA                                      |
| ---------------------------------- | ---------------------------------------------------- |
| HTTPS / TLS                        | Mã hóa khóa phiên (AES) khi bắt tay                  |
| Chữ ký số                          | Private key ký, Public key xác minh                  |
| JWT (JSON Web Token)               | Dùng để ký token                                     |
| Mã hóa kết hợp (Hybrid encryption) | Mã hóa khóa AES                                      |
| SSH / OpenSSL                      | Dùng để xác thực danh tính và truyền dữ liệu an toàn |

---

## 🧠 Nguyên lý hoạt động cơ bản:

1. **Khóa được tạo từ 2 số nguyên tố lớn**
   (số `n = p × q`)

2. **Mã hóa**:

   ```
   ciphertext = (message ^ e) mod n
   ```

3. **Giải mã**:

   ```
   plaintext = (ciphertext ^ d) mod n
   ```

    * `e` là public exponent (thường là 65537)
    * `d` là private exponent

---

## 📦 Mã hóa bất đối xứng: RSA vs AES

| Thuật toán | Đối xứng/Bất đối xứng | Tốc độ    | Dữ liệu lớn?                 | Dùng trong thực tế     |
| ---------- | --------------------- | --------- | ---------------------------- | ---------------------- |
| **RSA**    | Bất đối xứng          | Chậm hơn  | ❌ Không (chỉ vài trăm bytes) | Mã hóa khóa AES, ký số |
| **AES**    | Đối xứng              | Rất nhanh | ✅ Có (hàng MB, GB)           | Mã hóa dữ liệu thực tế |

➡️ Do đó, thực tế **người ta thường dùng RSA để mã hóa khóa AES**, sau đó dùng AES để mã hóa nội dung → gọi là **hybrid encryption**.

---

## 📁 File định dạng khóa RSA:

### 📌 Public Key (X.509 PEM)

```pem
-----BEGIN PUBLIC KEY-----
MIIBIjANBgkqhkiG9w0BAQEFAAO...
-----END PUBLIC KEY-----
```

### 📌 Private Key (PKCS#8 PEM)

```pem
-----BEGIN PRIVATE KEY-----
MIIEvAIBADANBgkqhkiG9w0BA...
-----END PRIVATE KEY-----
```

---

## 🔐 Mã hóa RSA trong Java

```java
Cipher cipher = Cipher.getInstance("RSA");
cipher.init(Cipher.ENCRYPT_MODE, publicKey);
byte[] encrypted = cipher.doFinal(data.getBytes());
```

---

## 📌 Tóm tắt

| Tính năng        | RSA                                     |
| ---------------- | --------------------------------------- |
| Loại mã hóa      | Bất đối xứng                            |
| Dùng khóa gì?    | Public / Private                        |
| Ưu điểm          | An toàn, xác thực tốt                   |
| Nhược điểm       | Mã hóa chậm, không dùng với dữ liệu lớn |
| Thực tế dùng cho | Mã hóa khóa, ký số, JWT, TLS            |

---

Rất hay! Mình sẽ giải thích kỹ **vì sao phải kết hợp RSA và AES trong mã hóa**, thay vì chỉ dùng 1 loại.

---

## 🎯 Mục tiêu của bạn:

> Bạn đang thấy Java dùng `SecretKey aesKey = generateSecretKey()` rồi lại `encryptAESKeyWithRSA(aesKey, publicKey)`.
> Tức là dùng cả **AES (đối xứng)** và **RSA (bất đối xứng)** → tại sao?

---

## ✅ Lý do kết hợp: "Hybrid Encryption" (mã hóa lai)

| AES                                              | RSA                                                     |
| ------------------------------------------------ | ------------------------------------------------------- |
| 🔐 Đối xứng: 1 khóa cho cả mã hóa & giải mã      | 🔐 Bất đối xứng: 2 khóa (public + private)              |
| ⚡ Rất nhanh                                      | 🐢 Rất chậm                                             |
| ❌ Cần 2 bên biết trước cùng 1 khóa               | ✅ Có thể gửi public key công khai                       |
| ✅ Phù hợp để mã hóa nội dung lớn (file, JSON...) | ❌ Không dùng để mã hóa nội dung lớn (chỉ vài trăm byte) |

---

## 🤯 Tại sao không dùng mỗi RSA?

RSA:

* Dữ liệu đầu vào bị giới hạn (với key 2048 bit, chỉ mã hóa được \~245 byte)
* Rất chậm nếu xử lý dữ liệu lớn
* RSA private key **phải giữ bí mật tuyệt đối**, nhưng lại cần được dùng để giải mã trực tiếp nếu không có AES

→ Không phù hợp để mã hóa **payload lớn**, ví dụ: file, JSON, dữ liệu giao dịch.

---

## 💡 Giải pháp: Kết hợp cả 2 (Hybrid)

| Giai đoạn                                              | Dùng RSA hay AES? | Giải thích                        |
| ------------------------------------------------------ | ----------------- | --------------------------------- |
| 1. Client tạo khóa AES ngẫu nhiên                      | –                 | Tạo một lần dùng cho phiên đó     |
| 2. Client dùng **RSA Public Key** để mã hóa khóa AES   | ✅ RSA             | Gửi an toàn cho server            |
| 3. Server dùng **RSA Private Key** để giải mã khóa AES | ✅ RSA             | Lấy ra khóa AES                   |
| 4. Dữ liệu (payload) mã hóa bằng khóa AES              | ✅ AES             | Nhanh, mạnh, dùng cho dữ liệu lớn |

---

## 🔒 Kết quả:

* **Bảo mật như RSA**
* **Hiệu năng như AES**
* → Tốt nhất của cả hai thế giới

---

## 🧠 Ví dụ đơn giản hóa

1. Bạn muốn gửi 1 JSON `{"card": "1234", "amount": 1000}` cho server
2. Bạn:

    * Sinh 1 khóa AES: `k1`
    * Dùng AES (`k1`) mã hóa JSON
    * Dùng RSA public key của server mã hóa `k1`
3. Gửi:

    * `encryptedAESKey`
    * `encryptedPayload`

Server:

* Dùng RSA private key giải mã `k1`
* Dùng `k1` để giải mã payload

---

## 📦 Tóm gọn

| Lý do kết hợp                              | Vì sao?                                                 |
| ------------------------------------------ | ------------------------------------------------------- |
| **RSA không mã hóa dữ liệu lớn được**      | Giới hạn byte                                           |
| **AES cần key bí mật truyền đi an toàn**   | Không thể hard-code hoặc gửi lộ                         |
| **Kết hợp = giải pháp an toàn + hiệu quả** | Dùng RSA để bảo vệ AES key, dùng AES để mã hóa nội dung |

---

Nếu bạn muốn, mình có thể:

* Vẽ sơ đồ luồng dữ liệu
* Mô phỏng luồng mã hóa/giải mã như client/server
* Hoặc tạo REST API demo

Bạn cần kiểu nào để hiểu sâu hơn?



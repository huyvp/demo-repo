gpg --full-generate-key
#gpg (GnuPG) 2.4.5-unknown; Copyright (C) 2024 g10 Code GmbH
#This is free software: you are free to change and redistribute it.
#There is NO WARRANTY, to the extent permitted by law.
#
#gpg: directory '/c/Users/huyvp/.gnupg' created
#Please select what kind of key you want:
#   (1) RSA and RSA
#   (2) DSA and Elgamal
#   (3) DSA (sign only)
#   (4) RSA (sign only)
#   (9) ECC (sign and encrypt) *default*
#  (10) ECC (sign only)
#  (14) Existing key from card
#Your selection? 1
#RSA keys may be between 1024 and 4096 bits long.
#What keysize do you want? (3072) 4096
#Requested keysize is 4096 bits
#Please specify how long the key should be valid.
#         0 = key does not expire
#      <n>  = key expires in n days
#      <n>w = key expires in n weeks
#      <n>m = key expires in n months
#      <n>y = key expires in n years
#Key is valid for? (0) 0
#Key does not expire at all
#Is this correct? (y/N) y
#
#GnuPG needs to construct a user ID to identify your key.
#
#Real name: huyvp
#Email address: nvh1892kw@gmail.com
#Comment: for me
#You selected this USER-ID:
#    "huyvp (for me) <nvh1892kw@gmail.com>"
#
#Change (N)ame, (C)omment, (E)mail or (O)kay/(Q)uit? o
#We need to generate a lot of random bytes. It is a good idea to perform
#some other action (type on the keyboard, move the mouse, utilize the
#disks) during the prime generation; this gives the random number
#generator a better chance to gain enough entropy.
#We need to generate a lot of random bytes. It is a good idea to perform
#some other action (type on the keyboard, move the mouse, utilize the
#disks) during the prime generation; this gives the random number
#generator a better chance to gain enough entropy.
#gpg: /c/Users/huyvp/.gnupg/trustdb.gpg: trustdb created
#gpg: directory '/c/Users/huyvp/.gnupg/openpgp-revocs.d' created
#gpg: revocation certificate stored as '/c/Users/huyvp/.gnupg/openpgp-revocs.d/BF3EB0478263AADED8F401600C4039A1EAD97E1A.rev'
#public and secret key created and signed.
#
#pub   rsa4096 2025-06-16 [SC]
#      BF3EB0478263AADED8F401600C4039A1EAD97E1A
#uid                      huyvp (for me) <nvh1892kw@gmail.com>
#sub   rsa4096 2025-06-16 [E]


gpg --export --armor nvh1892kw@gmail.com > public.asc
gpg --export-secret-keys --armor nvh1892kw@gmail.com > private.asc
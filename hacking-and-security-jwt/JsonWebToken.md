## Lab Setup
- Install Virtual Box.
- Kali Linux as one VM.
  - Browser
  - Terminal
- Securestore VM - Using the download link.
- POSTMAN chrome extension/desktop application.

# What is JSON Web Token
- JWT is an open standard (RFC 7519) that defines a compact and self-contained way for securely transmitting information between parties as a JSON object.

- In simple words, JWT is standardized format that is used to securely(Authorization) transfer information between two parties.
  - Authorization
  - Information Exchange

- JWT Structure
  - header.payload.signature
  - header, payload & signature 
  - Header and payload can be converted to simple text as these two are base64 URL encoded.
  - Signature is used to validate the Payload on the resource server.
  - Header and payload are not encrypted but encoded. So anyone can see what's inside them.
- Base64URL
  - Base64
  - Characters that are used in Base64 sometimes do not work well when passed using GET parameters or HTTP headers.
  - A variant of Base64 encoding with the following differences:
    - + (plus) is replaced by - (minus)
    - / (slash) is replaced by _ 
    - = (Padding characters) are omitted

- Signature:
  - Generated using HS 256 and RS 256.

- How JWT Works
  - Client         Authorization Server        
  - Resource Server 1

# 12. JWT Headers
{"typ":"JWT","alg":"HS256"}
- Type of token
- Algorithm used

This information is essential for the resource server to be able to validate the signature, by using an appropriate algorithm before providing access to the requested user

# 13. JWT Payload
{"iat":Issued At time in numeric format(in seconds since epoch), "iss":"localhost","exp":Expiry time in numeric format,"userId":"securestore","isAdmin":"false"}
- Usually containes user identification information for authorization
- Issued At, issuer, Expiry

# 14. JWT Signature
HS256 Signature => Header, Payload, Key, Algorithm
- Signature part of JWT contains information for the resource server in order to verify if the token is valid and not tampered.
- HS256 and RS256 are commonly used algorithms.

# 15. HS256
- HS256 uses SHA256-HMAC (Hash-Based Message Authentication Code).
- This means SHA256 Hash is generated but by using a secret key.
- So, HS256 is SHA256 hash of Header, Payload and a Secret => A digital signature
- One can produce this hash only if he/she has the secret.
### The Resource Server as well as the Authorization Server should have the exact key.
- Potential Security Issues:
  - 1. Key must be distributed to all the servers.
  - 2. If one server is compromsed, the shared key can be stolen.
  - 3. Brute forcing shared key.

# 17. RS256
- RS256 uses RSA and SHA256 algorithms.
- First, SHA256 Hash and Payload is generated.
- This hash is signed using RSA Private key.
- So, RS256 Signature is a signed SHA256 hash of Header, Payload => A digital signature.
- This is appended to the Header and Payload as 3rd part of JWT token.
- One can reproduce this token only if he/she has the private key used to encrypt the hash.

# RS256 in Action

Client > Login Credentials -> Authorization Server
-> 2. JWT Token -> CLient -> 3. Accesss Reources -> Resource Server.

Attacker needs
	- Login Credentials
	- Private Key

- Benefits
  - 1. Private key is known only to authorization server.
  - 2. Only the server with private key can create a new token.
  - 3. RSA keys are used in process and they are long enough to prevent brute force attacks.

- openssl genrsa -out private.pem 2048
- ls
- openssl rsa -in private.pem -out public.pem -pubout -outform PEM
- cat private.pem

# RS256 Signature Using CMD line
- Acess 192.168.1.79/c2/rs256.php
- mkdir RS256
- Copy JWT header and payload.
- echo -n "paste copied string" | openssl dgst -sha256 -sign private.key | openssl base64 -e -A | sed 's/\+/-/g' | sed 's/\//_/g' | sed -E s/=+$//


open other terminal
- $cat /var/www/html/v3/private.pem
- rm.private.key
- vim private.key
- paste rsa
- save

# Possible Security Issues
- Use of None Algorithm
- Signature Stripping - Use a whitelist of allowed algorithms['HS256','None']
- Cracking weak shared secrets- Useof strong shared secrets
- Substitution attack - Use ne method instead of whitelist ['RS256']

# JWT Attacks
Use of None Algorithm for signature
Goa: See admin's Credit Card Number Using Burpsuite

# Defenses
- 
# web-hook-token

Exemplary project to explore a concept for a token to secure web hooks for SaaS applications.

## Web Hook Tokens
A Web Hook Token is a token that consists of two parts:

1. The Token Payload that contains the `dbId` of a Web Hook, the `stackId` of the receiver and the `expirationDate`.
2. The Token Signature - an HMAC signature verifying the Token Payload

Simply by parsing the Web Hook Token, the recipient is able to verify the authenticity of the token, as the token signature must be signed with a secret key that is only available to the issuer of the Web Hook Token - the SaaS application. By parsing the Web Hook Token, the recipient is also able to reject expired tokens and forward token to the designated target (via the `stackId`).

## Structure of a Web Hook Token

The structure of a Web Hook Token is as follows:

```
${base64url(tokenPayload)}.${base64url(tokenSignature)}
```

Both parts are Base64 ((URL safe, RFC4648)[https://docs.oracle.com/en/java/javase/17/docs/api/java.base/java/util/Base64.html]) encoded and concatenated with a `. (dot).

The Token Payload is structured as follows:
```
${stackId}|${expirationDate}|${dbId}
```

The Token Payload parts are separated with a `|` (pipe).

The `expirationDate must have the format:
```
YYYY-MM-DD --> match (\d){4}-(0[1-9]|1[0-2]){1}-([0-3]{1}[0-9]{1}){1}
```

## Example
```
Token: YTFiMmMzZDR8MjAyNC0wMS0xNXwzMTU.kn2VJDoPd7y1h64-f5bZ77hzGYMA3bKGT02GjQU6O4k
Token Payload: a1b2c3d4|2024-01-15|315
Stack Id: a1b2c3d4
Expiration Date: 2024-01-15
Web Hook DB ID: 315
```
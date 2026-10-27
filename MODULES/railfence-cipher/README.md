# Rail Fence Cipher

This Spring Boot module encodes and decodes text with the Rail Fence cipher. From this directory, start it with `./mvnw spring-boot:run` (or `mvnw.cmd spring-boot:run` on Windows); the module reads its port and context path from the repository-level environment files. With the service running, send a JSON body containing `text` and `key` to either endpoint. For example, a key of `3` encodes `WEAREDISCOVEREDFLEEATONCE` as `WECRLTEERDSOEEFEAOCAIVDEN`:

The runtime help endpoint at `http://localhost:8003/railfence/help` returns the usage and both examples as plain text.

```powershell
curl.exe -X POST http://localhost:8003/railfence/encode `
  -H "Content-Type: application/json" `
  -d '{"text":"WEAREDISCOVEREDFLEEATONCE","key":3}'
```

The response is plain text. Decode by sending the ciphertext to `/decode` with the same key:

```powershell
curl.exe -X POST http://localhost:8003/railfence/decode `
  -H "Content-Type: application/json" `
  -d '{"text":"WECRLTEERDSOEEFEAOCAIVDEN","key":3}'
```

Input text must be nonblank, and the key must be at least `2` and less than the text length.

## Adding a Module

For another cipher in this module, implement `railfence.service.CipherService` with its `encode` and `decode` operations, expose those operations through a Spring REST controller accepting the `text` and `key` request fields, and add tests for known encode/decode vectors and round trips. Keep the module's Spring Boot configuration and service registration consistent with the existing application setup.
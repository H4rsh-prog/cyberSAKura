package railfence.service;

public interface CipherService {
    String encode(String plainText, int key);
    String decode(String cipherText, int key);
}

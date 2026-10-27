package railfence.service.impl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class CipherServiceImplTest {

    private final CipherServiceImpl cipherService = new CipherServiceImpl();

    @Test
    void encodeProducesExpectedCiphertextForKnownVectors() {
        assertEquals("WECRLTEERDSOEEFEAOCAIVDEN",
                cipherService.encode("WEAREDISCOVEREDFLEEATONCE", 3));
        assertEquals("ACEGBDFH", cipherService.encode("ABCDEFGH", 2));
        assertEquals("ABCED", cipherService.encode("ABCDE", 4));
    }

    @Test
    void decodeProducesExpectedPlaintextForKnownVectors() {
        assertEquals("WEAREDISCOVEREDFLEEATONCE",
                cipherService.decode("WECRLTEERDSOEEFEAOCAIVDEN", 3));
        assertEquals("ABCDEFGH", cipherService.decode("ACEGBDFH", 2));
        assertEquals("ABCDE", cipherService.decode("ABCED", 4));
    }

    @Test
    void encodeAndDecodeRoundTripAcrossRailCounts() {
        assertRoundTrip("HELLOWORLD", 2);
        assertRoundTrip("WEAREDISCOVEREDFLEEATONCE", 3);
        assertRoundTrip("ABCDE", 4);
    }

    @Test
    void rejectsKeysOutsideSupportedRange() {
        assertThrows(IllegalArgumentException.class, () -> cipherService.encode("ABC", 1));
        assertThrows(IllegalArgumentException.class, () -> cipherService.decode("ABC", 1));
        assertThrows(IllegalArgumentException.class, () -> cipherService.encode("ABC", 3));
        assertThrows(IllegalArgumentException.class, () -> cipherService.decode("ABC", 3));
    }

    @Test
    void rejectsBlankInput() {
        assertThrows(IllegalArgumentException.class, () -> cipherService.encode("   ", 2));
        assertThrows(IllegalArgumentException.class, () -> cipherService.decode("   ", 2));
    }

    @Test
    void rejectsNullInput() {
        assertThrows(NullPointerException.class, () -> cipherService.encode(null, 2));
        assertThrows(NullPointerException.class, () -> cipherService.decode(null, 2));
    }

    private void assertRoundTrip(String plainText, int key) {
        String cipherText = cipherService.encode(plainText, key);
        assertEquals(plainText, cipherService.decode(cipherText, key));
    }
}
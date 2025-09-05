package marconae.web_hook_token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObfuscatedStringTest {

    @Test
    void test_stringObfuscation() {
        final String baseString = "hello";
        final String expected = "aZVVr";

        final ObfuscatedString obfuscatedString = new ObfuscatedString(baseString);

        final String obfuscated = obfuscatedString.toObfuscatedString();
        assertEquals(expected, obfuscated);

        // ToString should return base string
        assertEquals(baseString, obfuscatedString.toString());

        final String deobfuscated = ObfuscatedString.convertToBaseString(obfuscated);
        assertEquals(baseString, deobfuscated);
    }
}
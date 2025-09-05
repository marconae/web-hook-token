package marconae.web_hook_token;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collector;

@RequiredArgsConstructor
public class ObfuscatedString {

    private static final String REGULAR_CHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstovwxyz0123456789";
    private static final String PADDED_CHARS = "pTHIbkt8zqLme3l4A9PnNRDvKW0o5oZGSaQCJV1frBOFdiY7h2sXwMUjg6Ecyx";

    @Getter
    private final String baseString;

    public String toObfuscatedString() {
        return baseString.chars()
                .mapToObj(i -> (char) i)
                .map(ObfuscatedString::padChar)
                .collect(createCharacterCollector());
    }

    @Override
    public String toString() {
        return getBaseString();
    }

    public static String convertToBaseString(String obfuscatedString) {
        return obfuscatedString.chars()
                .mapToObj(i -> (char) i)
                .map(ObfuscatedString::unpadChar)
                .collect(createCharacterCollector());
    }

    private static Collector<Character, StringBuilder, String> createCharacterCollector() {
        return Collector.of(
                StringBuilder::new,
                StringBuilder::append,
                StringBuilder::append,
                StringBuilder::toString);
    }

    private static char padChar(char sourceChar) {
        final int index = REGULAR_CHARS.indexOf(sourceChar);
        return PADDED_CHARS.charAt(index);
    }

    private static char unpadChar(char paddedChar) {
        final int index = PADDED_CHARS.indexOf(paddedChar);
        return REGULAR_CHARS.charAt(index);
    }

}

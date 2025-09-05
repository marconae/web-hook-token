package marconae.web_hook_token;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class HMACSignatureTest {

    private static final String KEY = "I am very secret";

    private static final Long WEB_HOOK_DB_ID = 315L;
    private static final String STACK_ID = "a1b2c3d4";
    private static final String EXP_DATE = "20240115";
    private static final String LABEL = "My Web Hook";

    @Test
    void test_signature_SHA256() {
        final String data = getData();
        final HMACSignature hmacSignature = new HMACSignature(KEY, data);
        final String signature = hmacSignature.getAsString();

        assertNotNull(signature);

        System.out.println("SHA256: " + signature);
        System.out.println("Length: " + signature.length());
    }

    @Test
    void test_signature_SHA512() {
        final String data = getData();
        final HMACSignature hmacSignature = new HMACSignature(KEY, data, HMACSignature.Algorithm.SHA512);
        final String signature = hmacSignature.getAsString();

        assertNotNull(signature);

        System.out.println("SHA512: " + signature);
        System.out.println("Length: " + signature.length());
    }

    private String getData() {
        return String.format("%d-%s-%s-%s", WEB_HOOK_DB_ID, STACK_ID, EXP_DATE, LABEL);
    }
}
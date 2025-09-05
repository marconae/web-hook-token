package marconae.web_hook_token;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class WebHookTokenTest {

    private static final String KEY = "I am very secret";

    private static final Long WEB_HOOK_DB_ID = 315L;
    private static final String STACK_ID = "a1b2c3d4";
    private static final String EXP_DATE = "2024-01-15";


    @Test
    void test_decode_encode() {
        final WebHookToken underTest = new WebHookToken(KEY);

        final WebHookTokenPayload expectedPayload = new WebHookTokenPayload(STACK_ID, EXP_DATE, WEB_HOOK_DB_ID);

        final String decodedWebHookToken = underTest.encode(expectedPayload);
        assertNotNull(decodedWebHookToken);

        System.out.println("Web Hook Token: " + decodedWebHookToken);
        System.out.println("Web Hook Token Payload: " + expectedPayload);

        // Decoded WebHookTokenPayload should match
        final WebHookTokenPayload receivedPayload = underTest.decodePayload(decodedWebHookToken);
        assertEquals(receivedPayload, expectedPayload);

        assertEquals(WEB_HOOK_DB_ID, receivedPayload.dbId());
        assertEquals(STACK_ID, receivedPayload.stackId());
        assertEquals(EXP_DATE, receivedPayload.expirationDate());

        // Signature should be valid
        assertTrue(underTest.verifySignature(decodedWebHookToken));

        // Payload should be valid
        final LocalDate localDate = LocalDate.parse("2024-01-14");
        final Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());
        underTest.isValidToken(decodedWebHookToken, date);
    }
}

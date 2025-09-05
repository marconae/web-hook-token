package marconae.web_hook_token;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Base64;
import java.util.Date;
import java.util.HexFormat;

import static com.google.common.base.Preconditions.*;

@RequiredArgsConstructor
public class WebHookToken {

    private static final String STACK_ID_PATTERN = "( [a-z]{1}\\d{1} | \\d{1}[a-z]{1} ){6}";
    private static final String EXPIRATION_DATE_PATTERN = "(\\d){4}-(0[1-9]|1[0-2]){1}-([0-3]{1}[0-9]{1}){1}";
    private static final SimpleDateFormat DATE_FORMATTER = new SimpleDateFormat("yyyy-MM-dd");

    protected static final String KEY = "I am secret";
    protected static final Base64.Encoder ENCODER = Base64.getUrlEncoder().withoutPadding();
    protected static final Base64.Decoder DECODER = Base64.getUrlDecoder();

    private final String key;

    public WebHookToken() {
        this.key = KEY;
    }

    public String encode(final @NonNull WebHookTokenPayload tokenPayload) {
        final String tokenPayloadString = tokenPayload.toString();

        final HMACSignature hmacSignature = new HMACSignature(key, tokenPayloadString);

        final String payloadEncoded = new String(ENCODER.encode(tokenPayloadString.getBytes()));
        final String signatureEncoded = new String(ENCODER.encode(hmacSignature.get()));

        return String.format("%s.%s", payloadEncoded, signatureEncoded);
    }

    public WebHookTokenPayload decodePayload(final @NonNull String decodedToken) {
        final String[] tokenParts = getTokenParts(decodedToken);

        final String payloadString = new String(DECODER.decode(tokenParts[0]));

        return parseWebHookTokenPayload(payloadString);
    }

    public boolean verifySignature(final @NonNull String decodedToken) {
        final WebHookTokenPayload tokenPayload = decodePayload(decodedToken);
        final String[] tokenParts = getTokenParts(decodedToken);

        final String receivedSignature = decodeAndToHexString(tokenParts[1]);
        final String expectedSignature = new HMACSignature(key, tokenPayload.toString()).getAsString();

        return expectedSignature.equals(receivedSignature);
    }

    private static String decodeAndToHexString(String tokenSignature) {
        return HexFormat.of().formatHex(DECODER.decode(tokenSignature));
    }

    public boolean isValidToken(final @NonNull String decodedToken, final @NonNull Date date) {
        final WebHookTokenPayload tokenPayload = decodePayload(decodedToken);

        if(!verifySignature(decodedToken)) {
            return false;
        }

        try {
            final Date expirationDate = DATE_FORMATTER.parse(tokenPayload.expirationDate());

            return tokenPayload.stackId().matches(STACK_ID_PATTERN)
                    && tokenPayload.expirationDate().matches(EXPIRATION_DATE_PATTERN)
                    && tokenPayload.dbId() > 0L
                    && ( date.before(expirationDate) || date.equals(expirationDate) );
        } catch (ParseException e) {
            return false;
        }
    }

    private static String[] getTokenParts(String decodedToken) {
        final String[] tokenParts = decodedToken.split("\\.");
        checkArgument(tokenParts.length == 2, "Invalid token");
        return tokenParts;
    }

    private static WebHookTokenPayload parseWebHookTokenPayload(String payloadString) {
        final String[] payloadParts = payloadString.split("\\|");
        checkArgument(payloadParts.length == 3, "Invalid token");

        final Long dbId = Long.valueOf(payloadParts[2]);
        return new WebHookTokenPayload(payloadParts[0], payloadParts[1], dbId);
    }

}

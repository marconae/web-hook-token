package marconae.web_hook_token;

import com.google.common.hash.Hashing;
import lombok.RequiredArgsConstructor;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;

@RequiredArgsConstructor
public class HMACSignature {

    protected static final Charset UTF_8 = StandardCharsets.UTF_8;
    private final String key;
    private final String data;
    private final Algorithm algorithm;

    enum Algorithm {
        SHA256,
        SHA512
    }

    public HMACSignature(final String key, final String data) {
        this.key = key;
        this.data = data;
        this.algorithm = Algorithm.SHA256;
    }

    @Override
    public String toString() {
        return HexFormat.of().formatHex(get());
    }

    public String getAsString() {
        return toString();
    }

    public byte[] get() {
        if(algorithm.equals(Algorithm.SHA256)) {
            return getSHA256();
        } else {
            return getSHA512();
        }
    }

    public byte[] getSHA256() {
        final byte[] keyBytes = getKeyBytes();

        return Hashing.hmacSha256(keyBytes)
                .hashString(data, UTF_8)
                .asBytes();
    }

    public byte[] getSHA512() {
        final byte[] keyBytes = getKeyBytes();

        return Hashing.hmacSha512(keyBytes)
                .hashString(data, UTF_8)
                .asBytes();
    }

    private byte[] getKeyBytes() {
        return key.getBytes(UTF_8);
    }
}

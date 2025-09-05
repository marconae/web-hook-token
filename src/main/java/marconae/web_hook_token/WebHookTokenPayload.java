package marconae.web_hook_token;

public record WebHookTokenPayload(String stackId, String expirationDate, Long dbId) {
    @Override
    public String toString() {
        return String.format("%s|%s|%d", stackId, expirationDate, dbId);
    }
}
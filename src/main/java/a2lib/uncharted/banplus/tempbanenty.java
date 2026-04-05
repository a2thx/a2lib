package a2lib.uncharted.banplus;

public class tempbanenty {
    private String reason;
    private String bannedBy;
    private long expiryTimeMs;

    public tempbanenty() {
    }

    public tempbanenty(String reason, String bannedBy, long expiryTimeMs) {
        this.reason = reason;
        this.bannedBy = bannedBy;
        this.expiryTimeMs = expiryTimeMs;
    }

    public boolean isexpired() {
        return System.currentTimeMillis() > expiryTimeMs;
    }

    public String getreason() {
        return reason;
    }

    public String getBannedBy() {
        return bannedBy;
    }

    public long getExpiryTimeMs() {
        return expiryTimeMs;
    }
}

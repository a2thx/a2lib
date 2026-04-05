package a2lib.uncharted.banplus;

public class tempmuteenty {
    private String reason;
    private String mutedBy;
    private long expiryTimeMs;

    public tempmuteenty() {
    }

    public tempmuteenty(String reason, String mutedBy, long expiryTimeMs) {
        this.reason = reason;
        this.mutedBy = mutedBy;
        this.expiryTimeMs = expiryTimeMs;
    }

    public boolean isexpired() {
        return System.currentTimeMillis() > expiryTimeMs;
    }

    public String getreason() {
        return reason;
    }
    
    public long getExpiryTimeMs() {
        return expiryTimeMs;
    }
}

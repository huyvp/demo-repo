package dev.samsung.gpgEncrypt;

public class EventRequest {
    private String languageCode;
    private String countryCode;
    private String event;
    private String soId;
    private String encryptedKey;
    private String payload;

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final EventRequest req = new EventRequest();
        public Builder languageCode(String val) { req.languageCode = val; return this; }
        public Builder countryCode(String val) { req.countryCode = val; return this; }
        public Builder event(String val) { req.event = val; return this; }
        public Builder soId(String val) { req.soId = val; return this; }
        public Builder encryptedKey(String val) { req.encryptedKey = val; return this; }
        public Builder payload(String val) { req.payload = val; return this; }
        public EventRequest build() { return req; }
    }

    @Override
    public String toString() {
        return "EventRequest{\n" +
                "  languageCode='" + languageCode + "',\n" +
                "  countryCode='" + countryCode + "',\n" +
                "  event='" + event + "',\n" +
                "  soId='" + soId + "',\n" +
                "  encryptedKey='" + encryptedKey + "',\n" +
                "  payload='" + payload + "'\n" +
                '}';
    }
}


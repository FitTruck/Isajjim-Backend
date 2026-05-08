package kr.co.isajjim.domains.chat.domain.constant;

public enum MessageType {
    TEXT(null),
    IMAGE("사진");

    public final String label;

    MessageType(String label) {
        this.label = label;
    }
}
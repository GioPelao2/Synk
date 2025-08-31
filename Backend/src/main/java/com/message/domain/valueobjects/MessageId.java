package com.message.domain.valueobjects;

import java.util.Objects;

public final class MessageId {
    private final Long value;

    private MessageId(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("ConversationId must be a positive number");
        }
        this.value = value;
    }

    public static MessageId from(Long value) {
        return new MessageId(value);
    }

    public Long value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this==object) return true;
        if (object==null || getClass() != object.getClass()) return false;
        MessageId messageId = (MessageId) object;
        return Objects.equals(value, messageId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "UserId{" + value + "}";
    }


}

package com.message.domain.valueobjects;

import java.util.Objects;

public final class MessageId {
    private final Long value;

    private MessageId(Long value) {
        this.value = Objects.requireNonNull(value, "MessageId value cannot be null");
    }

    public static MessageId from(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("MessageId value cannot be null");
        }
        return new MessageId(value);
    }

    public Long value() {
        return value;
    }
    
    public boolean isTemporary() {
        return value.equals(-1L);
    }
    
    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        MessageId messageId = (MessageId) object;
        return Objects.equals(value, messageId.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "MessageId{" + value + "}";
    }
}

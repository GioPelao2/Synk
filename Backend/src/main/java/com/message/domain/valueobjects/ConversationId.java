package com.message.domain.valueobjects;

import java.util.Objects;

public class ConversationId {
    private final Long value;

    private ConversationId(Long value) {
        this.value = Objects.requireNonNull(value, "ConversationId value cannot be null");
    }

    public static ConversationId from(Long value) {
        if (value == null) {
            throw new IllegalArgumentException("ConversationId value cannot be null");
        }
        return new ConversationId(value);
    }

    public Long value() {
        return value;
    }

    public boolean isTemporary() {
        return value.equals(-1L);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ConversationId that = (ConversationId) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConversationId{" + value + '}';
    }
}

package com.message.domain.valueobjects;

import java.util.Objects;

public final class ConversationId {
    private final Long value;

    private ConversationId(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("ConversationId must be a positive number");
        }
        this.value = value;
    }

    public static ConversationId from(Long value) {
        return new ConversationId(value);
    }

    public static ConversationId newId() {
        return new ConversationId(-1L); // TEMPORAL se le asigna EN base a la DB
    }

    public Long value() {
        return value;
    }

    public boolean isTemporary() {
        return value <= 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        ConversationId that = (ConversationId) object;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return "ConversationId{" + value + "}";
    }
}

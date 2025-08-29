package com.mesagge.domain.valueobjects;

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

    public Long value() {
        return value;
    }
    
    @Override
    public boolean equals(Object object) {
        if (this==object) return true;
        if (object==null || getClass() != object.getClass()) return false;
        ConversationId conversationId = (ConversationId) object;
        return Objects.equals(value, conversationId.value);
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

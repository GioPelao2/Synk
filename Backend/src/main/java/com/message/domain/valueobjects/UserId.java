package com.message.domain.valueobjects;

import java.util.Objects;

public final class UserId {
    private final Long value;

    private UserId(Long value, boolean allowTemporary) {
        if (value == null) {
            throw new IllegalArgumentException("UserId cannot be null");
        }

        if (!allowTemporary && value <= 0) {
            throw new IllegalArgumentException("UserId must be a positive number");
        }

        this.value = value;
    }

    private UserId(Long value) {
        this(value, false);
    }

    public static UserId from(Long value) {
        return new UserId(value);
    }

    public static UserId temporary() {
        return new UserId(-1L, true);
    }

    public Long value() {
        return value;
    }

    public Long getValue() {
        return value;
    }

    public boolean isTemporary() {
        return value <= 0;
    }

    @Override
    public boolean equals(Object object) {
        if (this==object) return true;
        if (object==null || getClass() != object.getClass()) return false;
        UserId userId = (UserId) object;
        return Objects.equals(value, userId.value);
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


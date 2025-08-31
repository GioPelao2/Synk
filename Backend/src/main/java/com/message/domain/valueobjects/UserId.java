package com.mesagge.domain.valueobjects;

import java.util.Objects;

public final class UserId {
    private final Long value;

    private UserId(Long value) {
        if (value == null || value < 0) {
            throw new IllegalArgumentException("UserId must be a positive number");
        }
        this.value = value;
    }

    public static UserId from(Long value) {
        return new UserId(value);
    }

    public Long value() {
        return value;
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

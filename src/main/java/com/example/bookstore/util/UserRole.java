package com.example.bookstore.util;

import com.example.bookstore.exception.RecordNotFoundException;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.function.Predicate;

@AllArgsConstructor
@Getter
public enum UserRole {

    USER(1, "USER", "ROLE_USER"),
    ADMIN(2, "ADMIN", "ROLE_ADMIN");

    private final long code;
    private final String value;
    private final String role;

    private static UserRole find(final Predicate<UserRole> predicate) {
        return StreamUtil.find(Arrays.stream(UserRole.values()), predicate)
                .orElseThrow(() -> new RecordNotFoundException("No Role Found for this argument"));
    }

    public static String getValueByCode(final long code){
        return find(x -> x.code == code)
                .getValue();
    }

    public static String getRoleByCode(final long code){
        return find(x -> x.code == code)
                .getRole();
    }

    public static boolean isValidRole(final long code){
        return Arrays
                .stream(UserRole.values())
                .anyMatch(x -> x.getCode() == code);
    }

    public static boolean isInvalidRole(final long code){
        return !isValidRole(code);
    }
}
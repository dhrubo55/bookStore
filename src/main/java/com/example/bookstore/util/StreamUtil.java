package com.example.bookstore.util;

import lombok.experimental.UtilityClass;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Stream;

@UtilityClass
public class StreamUtil {

    public static <T> Optional<T> find(Stream<T> stream, Predicate<T> predicate){
        return stream
                .filter(predicate)
                .findFirst();
    }
}
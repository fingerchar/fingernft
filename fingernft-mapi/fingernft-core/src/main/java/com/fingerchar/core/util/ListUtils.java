package com.fingerchar.core.util;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ListUtils {

    public static <T extends Object> T getByPredicate(List<T> objs, Predicate<T> predicate) {
        return objs.stream()
                .filter(predicate)
                .findAny()
                .orElse(null);
    }

    public static <T extends Object> List<T> unrepeated(List<T> list){
        return list.stream().collect(Collectors.toSet()).stream().collect(Collectors.toList());
    }
}

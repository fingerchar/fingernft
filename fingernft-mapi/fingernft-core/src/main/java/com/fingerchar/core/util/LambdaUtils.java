package com.fingerchar.core.util;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class LambdaUtils {
	
	public static <T> Consumer<T> consumerWithIndex(BiConsumer<T, Integer> consumer) {
        class Obj {
            int i;
        }
        Obj obj = new Obj();
        return t -> {
            int index = obj.i++;
            consumer.accept(t, index);
        };
    }
	
	public static <E> void forEach(
            Iterable<? extends E> elements, BiConsumer<Integer, ? super E> action) {
        int index = 0;
        for (E element : elements) {
            action.accept(index++, element);
        }
    }
}

package com.fingerchar.api.utils;

import java.util.List;
import java.util.function.Predicate;

public class ListUtils {

	public static <T extends Object> T getByPredicate(List<T> objs, Predicate<T> predicate) {
		return objs.stream()
				.filter(predicate)
				.findAny()
				.orElse(null);
	}
}

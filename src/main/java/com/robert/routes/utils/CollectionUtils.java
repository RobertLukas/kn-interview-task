package com.robert.routes.utils;

import com.robert.routes.models.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * Provides utility methods and decorators for Collection instances.
 */
public class CollectionUtils {

    /**
     * Creates list of {@link Pair} from a list
     *
     * @param list - the source list
     * @param <T>  - the type of object the Collection contains
     * @return list of Pairs
     */
    private <T> List<Pair<T, T>> twoGrams(List<T> list) {
        return IntStream.range(0, list.size() - 3)
                .mapToObj(i -> new ArrayList<>(list.subList(i, i + 2)))
                .map(arr -> new Pair<>(arr.get(0), arr.get(1)))
                .collect(Collectors.toList());
    }
}

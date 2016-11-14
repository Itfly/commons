package com.itfly.commons.map;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoufeiyu on 05/11/2016.
 */
public class MapUtils {

    public static <K extends Comparable<? super K>, V> Map<K, V> sortByKey(Map<K, V> map, boolean desc) {
        Comparator<Map.Entry<K,V>> comparator = Map.Entry.comparingByKey();
        if (desc) {
            comparator = Collections.reverseOrder(comparator);
        }

        return sort(map, comparator);
    }

    public static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map, boolean desc) {
        Comparator<Map.Entry<K,V>> comparator = Map.Entry.comparingByValue();
        if (desc) {
            comparator = Collections.reverseOrder(comparator);
        }

        return sort(map, comparator);
    }

    public static <K, V> Map<K, V> sort(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        return map.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },
                        LinkedHashMap::new));
    }

}

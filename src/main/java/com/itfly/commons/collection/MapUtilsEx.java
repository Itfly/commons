package com.itfly.commons.collection;

import org.apache.commons.collections.MapUtils;
import org.apache.commons.collections.map.LinkedMap;
import org.apache.commons.collections.map.UnmodifiableMap;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by zhoufeiyu on 05/11/2016.
 */
public class MapUtilsEx extends MapUtils {

    public static final Map EMPTY_LINKEDHASH_MAP = UnmodifiableMap.decorate(new LinkedMap(1));

    public static <K extends Comparable<? super K>, V>
    Map<K, V> sortByKey(Map<K, V> map, boolean desc) {
        Comparator<Map.Entry<K,V>> comparator = Map.Entry.comparingByKey();
        if (desc) {
            comparator = Collections.reverseOrder(comparator);
        }

        return sort(map, comparator);
    }

    /**
     * Sort the map by value's comparator, return a LinkedHashMap
     * @param map
     * @param desc
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V extends Comparable<? super V>>
    Map<K, V> sortByValue(Map<K, V> map, boolean desc) {
        Comparator<Map.Entry<K,V>> comparator = Map.Entry.comparingByValue();
        if (desc) {
            comparator = Collections.reverseOrder(comparator);
        }

        return sort(map, comparator);
    }


    /**
     * Sort the map by the giving comparator, return a LinkedHashMap
     * @param map
     * @param comparator
     * @param <K>
     * @param <V>
     * @return
     */
    public static <K, V>
    Map<K, V> sort(Map<K, V> map, Comparator<Map.Entry<K, V>> comparator) {
        Objects.requireNonNull(comparator);

        if (isEmpty(map)) {
            return EMPTY_LINKEDHASH_MAP;
        }

        return map.entrySet().stream()
                .sorted(comparator)
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        Map.Entry::getValue,
                        (u,v) -> { throw new IllegalStateException(String.format("Duplicate key %s", u)); },
                        LinkedHashMap::new));
    }

    // TODO : support other collection
    public static <K, V>
    Map<V, List<K>> reverse(Map<K, V> map) {
        if (isEmpty(map)) {
            return EMPTY_MAP;
        }

        return map.entrySet().stream()
                .collect(Collectors.groupingBy(Map.Entry::getValue,
                        Collectors.mapping(Map.Entry::getKey, Collectors.toList())));
    }

}

package com.itfly.commons.collection;

import org.apache.commons.collections.SetUtils;
import org.apache.commons.lang3.ArrayUtils;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhoufeiyu on 14/11/2016.
 */
public class SetUtilsEx {

    public static int[] toIntArray(Set<Integer> set) {
        return ArrayUtils.toPrimitive(set.toArray(ArrayUtils.EMPTY_INTEGER_OBJECT_ARRAY));
    }

    public static long[] toLongArray(Set<Long> set) {
        return ArrayUtils.toPrimitive(set.toArray(ArrayUtils.EMPTY_LONG_OBJECT_ARRAY));
    }

    public static Set<Integer> toSet(int[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return SetUtils.EMPTY_SET;
        }

        return Arrays.stream(array).boxed()
                .collect(Collectors.toSet());
    }

    public static Set<Long> toSet(long[] array) {
        if (ArrayUtils.isEmpty(array)) {
            return SetUtils.EMPTY_SET;
        }

        return Arrays.stream(array).boxed()
                .collect(Collectors.toSet());
    }

    public static <T> Set<T> intersect(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<T>();
        result.addAll(set1);
        result.retainAll(set2);
        return result;
    }

    public static <T> Set<T> union(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<T>();
        result.addAll(set1);
        result.addAll(set2);
        return result;
    }

    public static <T> Set<T> sub(Set<T> set1, Set<T> set2) {
        Set<T> result = new HashSet<T>();
        result.addAll(set1);
        result.removeAll(set2);
        return result;
    }
}

package com.gy.util;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class SetUtil {
    public static <T> Set<T> toSet(T... obj){
        return new HashSet<>(Arrays.asList(obj));
    }
}

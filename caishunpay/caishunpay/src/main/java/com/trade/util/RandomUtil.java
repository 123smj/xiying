package com.trade.util;

import org.apache.commons.lang3.RandomUtils;

import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;

public class RandomUtil {
    public static <K> Optional<K> weightRandom(Map<K, Integer> entryWithWeight) {
        TreeMap<Integer, K> weightMap = new TreeMap<>();
        Integer count = 0;
        if (entryWithWeight.size() == 0) {
            return Optional.empty();
        }
        for (Map.Entry<K, Integer> i : entryWithWeight.entrySet()) {
            if(i.getValue()<=0){
                continue;
            }
            count += i.getValue();
            weightMap.put(count, i.getKey());
        }
        if (count == 0) {
            return Optional.empty();
        }
        Integer val = RandomUtils.nextInt(0, count);
        return Optional.of(weightMap.higherEntry(val).getValue());
    }
}


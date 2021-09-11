package com.example.demo.util;

import org.springframework.stereotype.Component;
import java.util.Map.Entry;

import java.util.*;

@Component
public class MapUtilsImpl implements MapUtils{

    public  <K, V extends Comparable<V>> K maxValueFromMap(Map<K, V> map) {
        Optional<Map.Entry<K, V>> maxEntry = map.entrySet()
                .stream()
                .max(Map.Entry.comparingByValue()
                );

        return maxEntry.get().getKey();
    }

    public HashMap<Long, Integer> sortByValue(HashMap<Long, Integer> hm) {
        List<Map.Entry<Long, Integer> > list =
                new LinkedList<>(hm.entrySet());

        list.sort(Map.Entry.comparingByValue());

        HashMap<Long, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<Long, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    public Map<Long, Integer> sortByValue2(boolean order, HashMap<Long, Integer> map)
    {
        List<Entry<Long, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort((o1, o2) -> {
            if (order) {
                return o1.getValue().compareTo(o2.getValue());
            } else {
                return o2.getValue().compareTo(o1.getValue());
            }
        });

        Map<Long, Integer> sortedMap = new LinkedHashMap<>();
        for (Entry<Long, Integer> entry : list)
        {
            sortedMap.put(entry.getKey(), entry.getValue());
        }
        return sortedMap;
    }
}

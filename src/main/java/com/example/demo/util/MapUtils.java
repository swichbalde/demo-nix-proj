package com.example.demo.util;

import java.util.HashMap;
import java.util.Map;

public interface MapUtils {

    <K, V extends Comparable<V>> K maxValueFromMap(Map<K, V> map);

    HashMap<Long, Integer> sortByValue(HashMap<Long, Integer> hm);

    Map<Long, Integer> sortByValue2(boolean order, HashMap<Long, Integer> map);
}

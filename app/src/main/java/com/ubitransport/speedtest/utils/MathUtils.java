package com.ubitransport.speedtest.utils;

import java.util.List;

public class MathUtils {
    public static long calculateAverage(List<Long> speedList) {
        long sum = 0;
        if (!speedList.isEmpty()) {
            for (Long mark : speedList) {
                sum += mark;
            }
            return sum / speedList.size();
        }
        return sum;
    }
}

package com.ubitransport.speedtest;

import com.ubitransport.speedtest.utils.MathUtils;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;


public class MathUnitTest {

    @Test
    public void averageIsCorrect(){
        List<Long> speedList = new ArrayList<>();
        speedList.add((long)20);
        speedList.add((long)30);
        speedList.add((long)40);
        long average = MathUtils.calculateAverage(speedList);
        assertEquals(average,30);
    }
}

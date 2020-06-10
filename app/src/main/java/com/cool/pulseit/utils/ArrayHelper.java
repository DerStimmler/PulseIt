package com.cool.pulseit.utils;

import java.util.Collection;

public class ArrayHelper {
    public static int[] toArray(Collection<Integer> integerList){
        int[] ints = new int[integerList.size()];

        for(int i = 0; i < integerList.size(); i++){
            ints[i] = integerList.toArray(new Integer[integerList.size()])[i];
        }

        return ints;
    }
}

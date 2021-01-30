package com.yaroslavgorbach.counter;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Utility {
    /*delete the same groups*/
    static public List<String> deleteTheSameGroups(List<String> strings){
        Set<String> set = new HashSet<>(strings);
        return Arrays.asList(set.toArray(new String[0]));
    }
}

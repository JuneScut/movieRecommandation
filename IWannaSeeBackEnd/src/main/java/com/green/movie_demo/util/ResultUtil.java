package com.green.movie_demo.util;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResultUtil
{
    public static Object total(int total, List list)
    {
        Map<String, Object>map = new HashMap<>();
        map.put("total", total);
        map.put("list", list);
        return map;
    }
}

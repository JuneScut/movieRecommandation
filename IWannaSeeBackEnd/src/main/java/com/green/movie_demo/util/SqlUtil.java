package com.green.movie_demo.util;

public class SqlUtil
{
    public static int offset(int page, int per_page)
    {
        return (page - 1) * per_page;
    }
    
    public static final String LIMIT_OFFSET = " limit #{limit} offset #{offset} ";
}

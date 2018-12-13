package com.green.movie_demo.util;

public class SqlUtil
{
    public static int pageLimit(int page, int per_page)
    {
        return (page - 1) * per_page;
    }
}

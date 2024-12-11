package com.epam.esm.service.util;

public class PaginationUtil {

    public static int getStartPosition(int page, int size) {
        return ((page - 1) * size + 1) - 1;
    }
}

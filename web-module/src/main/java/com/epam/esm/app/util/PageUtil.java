package com.epam.esm.app.util;

import com.epam.esm.app.model.Page;

public class PageUtil {

    public static Page getPageInfo(int page, int size, long countTags) {
        return Page.builder()
                .number(page)
                .size(size)
                .totalElements(countTags)
                .totalPages((int) Math.ceil((double) countTags / size))
                .build();
    }
}
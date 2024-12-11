package com.epam.esm.app.model;

import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Page {

    private int number;
    private int totalPages;
    private int size;
    private long totalElements;
}
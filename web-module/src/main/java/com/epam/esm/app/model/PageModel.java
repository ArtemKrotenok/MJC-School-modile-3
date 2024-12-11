package com.epam.esm.app.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.Link;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class PageModel<T> {

    public PageModel(T data) {
        this.data = data;
    }

    private T data;
    private List<Link> links = new ArrayList();
    private Page page;
}

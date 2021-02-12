package com.tags.popuplibrary.models;

import java.io.Serializable;

public class Tag implements Serializable {
    private String id;
    private String name;

    public Tag(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
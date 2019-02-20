package com.cardium.cardieflash;

public class Tag {
    private int tagId;
    private String name;

    public Tag(int id, String name) {
        this.tagId = id;
        this.name = name;
    }

    public int getTagId() {
        return this.tagId;
    }

    public String getName() {
        return this.name;
    }
}

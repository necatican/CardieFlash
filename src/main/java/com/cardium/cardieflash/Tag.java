package com.cardium.cardieflash;

public class Tag {

    private int tagId;
    private String name;

    public Tag(int id, String name) {
        this.tagId = id;
        this.name = name;
    }

    public Tag(String name) {
        this.name = name;
    }

    public int getTagId() {
        return tagId;
    }

    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        } else if (obj == null || getClass() != obj.getClass()) {
            return false;
        } else {
            Tag tag = (Tag) obj;
            return this.getName() == tag.getName();
        }
    }
}
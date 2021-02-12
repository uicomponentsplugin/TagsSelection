package com.tags.popuplibrary.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Tags implements Serializable {
    private List<Tag> allTags = new ArrayList<>();
    private List<Tag> selectedTags = new ArrayList<>();

    public List<Tag> getAllTags() {
        return allTags;
    }

    public void setAllTags(List<Tag> allTags) {
        this.allTags = allTags;
    }

    public List<Tag> getSelectedTags() {
        return selectedTags;
    }

    public void setSelectedTags(List<Tag> selectedTags) {
        this.selectedTags = selectedTags;
    }
}

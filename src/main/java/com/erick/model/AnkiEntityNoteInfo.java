package com.erick.model;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AnkiEntityNoteInfo {
    public Long noteId;
    public String modelName;
    public Map<String, AnkiEntityNoteInfoField> fields = new LinkedHashMap<>();
    public List<String> tags = new ArrayList<>();
    public List<Long> cards = new ArrayList<>();

    public Long getNoteId() {
        return noteId;
    }

    public void setNoteId(Long noteId) {
        this.noteId = noteId;
    }

    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public Map<String, AnkiEntityNoteInfoField> getFields() {
        return fields;
    }

    public void setFields(Map<String, AnkiEntityNoteInfoField> fields) {
        this.fields = fields;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public List<Long> getCards() {
        return cards;
    }

    public void setCards(List<Long> cards) {
        this.cards = cards;
    }

    @Override
    public String toString() {
        return String.valueOf(fields);
    }
}

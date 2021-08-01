package com.erick.model;

import java.util.LinkedHashMap;
import java.util.Map;

public class AnkiEntityUpdateNoteField {
    public Long id;
    public Map<String, String> fields = new LinkedHashMap<>();

    public AnkiEntityUpdateNoteField(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Map<String, String> getFields() {
        return fields;
    }

    public void setFields(Map<String, String> fields) {
        this.fields = fields;
    }
}

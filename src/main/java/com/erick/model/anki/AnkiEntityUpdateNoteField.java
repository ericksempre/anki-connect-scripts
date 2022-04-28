package com.erick.model.anki;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public class AnkiEntityUpdateNoteField {
    public Long id;
    public Map<String, String> fields = new LinkedHashMap<>();

    public AnkiEntityUpdateNoteField(Long id) {
        this.id = id;
    }
}

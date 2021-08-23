package com.erick.model;

import lombok.Data;
import lombok.ToString;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Data
public class AnkiEntityNoteInfo {
    public Long noteId;
    public String modelName;
    public Map<String, AnkiEntityNoteInfoField> fields = new LinkedHashMap<>();
    public List<String> tags = new ArrayList<>();
    public List<Long> cards = new ArrayList<>();

    @Override
    public String toString() {
        return String.valueOf(tags);
    }
}

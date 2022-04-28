package com.erick.model.anki;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

@Data
public class AnkiEntityAddNote {
    public String deckName;
    public String modelName;
    public Map<String, String> fields = new LinkedHashMap<>();
    public Map<String, String> options = new LinkedHashMap<>();
    public Set<String> tags = new LinkedHashSet<>();

    @Override
    public String toString() {
        return fields == null ? "" : String.join(" ", fields.values().stream().toList().get(0));
    }
}

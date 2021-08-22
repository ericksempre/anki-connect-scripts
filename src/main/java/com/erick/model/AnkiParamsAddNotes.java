package com.erick.model;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class AnkiParamsAddNotes {
    public List<AnkiEntityAddNote> notes = new ArrayList<>();

    public void addNote(AnkiEntityAddNote ankiEntityAddNote) {
        notes.add(ankiEntityAddNote);
    }
}

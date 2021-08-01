package com.erick.model;

import java.util.ArrayList;
import java.util.List;

public class AnkiParamsAddNotes {
    public List<AnkiEntityAddNote> notes = new ArrayList<>();

    public void addNote(AnkiEntityAddNote ankiEntityAddNote) {
        notes.add(ankiEntityAddNote);
    }

    public List<AnkiEntityAddNote> getNotes() {
        return notes;
    }

    public void setNotes(List<AnkiEntityAddNote> notes) {
        this.notes = notes;
    }
}

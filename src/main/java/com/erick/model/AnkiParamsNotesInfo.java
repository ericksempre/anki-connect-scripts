package com.erick.model;

import java.util.List;

public class AnkiParamsNotesInfo {
    public List<Long> notes;

    public AnkiParamsNotesInfo(List<Long> notes) {
        this.notes = notes;
    }

    public List<Long> getNotes() {
        return notes;
    }

    public void setNotes(List<Long> notes) {
        this.notes = notes;
    }
}

package com.erick.model;

public class AnkiParamsUpdateNoteFields {
    public AnkiEntityUpdateNoteField note;

    public AnkiParamsUpdateNoteFields(AnkiEntityUpdateNoteField note) {
        this.note = note;
    }

    public AnkiEntityUpdateNoteField getNote() {
        return note;
    }

    public void setNote(AnkiEntityUpdateNoteField note) {
        this.note = note;
    }
}

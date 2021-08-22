package com.erick.model;

import lombok.Getter;
import lombok.Setter;

import static com.erick.constants.AnkiConnectConstants.DELETE_NOTES;
import static com.erick.constants.AnkiConnectConstants.NOTES_INFO;

@Getter
@Setter
public class AnkiRequestDeleteNotes extends AnkiRequest {
    public AnkiParamsNoteIds params;

    public AnkiRequestDeleteNotes(AnkiParamsNoteIds params) {
        super(DELETE_NOTES);
        this.params = params;
    }
}

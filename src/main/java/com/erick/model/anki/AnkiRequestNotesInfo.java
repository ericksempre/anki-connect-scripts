package com.erick.model.anki;

import lombok.Getter;
import lombok.Setter;

import static com.erick.constants.AnkiConnectConstants.NOTES_INFO;

@Getter
@Setter
public class AnkiRequestNotesInfo extends AnkiRequest {
    public AnkiParamsNoteIds params;

    public AnkiRequestNotesInfo(AnkiParamsNoteIds params) {
        super(NOTES_INFO);
        this.params = params;
    }
}

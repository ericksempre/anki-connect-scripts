package com.erick.model;

import lombok.Getter;
import lombok.Setter;

import static com.erick.constants.AnkiConnectConstants.UPDATE_NOTE_FIELDS;

@Getter
@Setter
public class AnkiRequestUpdateNoteFields extends AnkiRequest {
    public AnkiParamsUpdateNoteFields params;

    public AnkiRequestUpdateNoteFields(AnkiParamsUpdateNoteFields params) {
        super(UPDATE_NOTE_FIELDS);
        this.params = params;
    }
}

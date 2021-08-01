package com.erick.model;

import static com.erick.constants.AnkiConnectActions.UPDATE_NOTE_FIELDS;

public class AnkiRequestUpdateNoteFields extends AnkiRequest {
    public AnkiParamsUpdateNoteFields params;

    public AnkiRequestUpdateNoteFields(AnkiParamsUpdateNoteFields params) {
        super(UPDATE_NOTE_FIELDS);
        this.params = params;
    }

    public AnkiParamsUpdateNoteFields getParams() {
        return params;
    }

    public void setParams(AnkiParamsUpdateNoteFields params) {
        this.params = params;
    }
}

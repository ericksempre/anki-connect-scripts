package com.erick.model;

import static com.erick.constants.AnkiConnectActions.ADD_NOTES;

public class AnkiRequestAddNotes extends AnkiRequest {
    public AnkiParamsAddNotes params;

    public AnkiRequestAddNotes(AnkiParamsAddNotes params) {
        super(ADD_NOTES);
        this.params = params;
    }

    public AnkiParamsAddNotes getParams() {
        return params;
    }

    public void setParams(AnkiParamsAddNotes params) {
        this.params = params;
    }
}

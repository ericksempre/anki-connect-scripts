package com.erick.model;

import static com.erick.constants.AnkiConnectActions.FIND_NOTES;

public class AnkiRequestFindNotes extends AnkiRequest {
    public AnkiParamsFindNotes params;

    public AnkiRequestFindNotes(AnkiParamsFindNotes params) {
        super(FIND_NOTES);
        this.params = params;
    }

    public AnkiParamsFindNotes getParams() {
        return params;
    }

    public void setParams(AnkiParamsFindNotes params) {
        this.params = params;
    }
}

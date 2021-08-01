package com.erick.model;

import static com.erick.constants.AnkiConnectActions.NOTES_INFO;

public class AnkiRequestNotesInfo extends AnkiRequest {
    public AnkiParamsNotesInfo params;

    public AnkiRequestNotesInfo(AnkiParamsNotesInfo params) {
        super(NOTES_INFO);
        this.params = params;
    }

    public AnkiParamsNotesInfo getParams() {
        return params;
    }

    public void setParams(AnkiParamsNotesInfo params) {
        this.params = params;
    }
}

package com.erick.model.anki;

import lombok.Getter;
import lombok.Setter;

import static com.erick.constants.AnkiConnectConstants.FIND_NOTES;

@Getter
@Setter
public class AnkiRequestFindNotes extends AnkiRequest {
    public AnkiParamsFindNotes params;

    public AnkiRequestFindNotes(AnkiParamsFindNotes params) {
        super(FIND_NOTES);
        this.params = params;
    }
}

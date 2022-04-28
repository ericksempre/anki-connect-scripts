package com.erick.model.anki;

import lombok.Getter;
import lombok.Setter;

import static com.erick.constants.AnkiConnectConstants.ADD_NOTES;

@Getter
@Setter
public class AnkiRequestAddNotes extends AnkiRequest {
    public AnkiParamsAddNotes params;

    public AnkiRequestAddNotes(AnkiParamsAddNotes params) {
        super(ADD_NOTES);
        this.params = params;
    }
}

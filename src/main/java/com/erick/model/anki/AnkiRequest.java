package com.erick.model.anki;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnkiRequest {
    public  String action;

    public Integer version = 6;

    public AnkiRequest(String action) {
        this.action = action;
    }
}

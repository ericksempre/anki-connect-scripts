package com.erick.model;

import lombok.Data;

@Data
public class AnkiEntityNoteInfoField {
    public String value;
    public Integer order;

    @Override
    public String toString() {
        return value;
    }
}

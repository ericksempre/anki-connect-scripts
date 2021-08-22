package com.erick.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class AnkiParamsNoteIds {
    public List<Long> notes;
}

package com.erick.model.anki;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class AnkiResultNotesInfo {
    public List<AnkiEntityNoteInfo> result = new ArrayList<>();
    public String error;

    @Override
    public String toString() {
        return result == null ? "NULL" : result.size() + " notes";
    }
}

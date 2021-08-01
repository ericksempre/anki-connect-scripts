package com.erick.model;

import java.util.ArrayList;
import java.util.List;

public class AnkiResultNotesInfo {
    public List<AnkiEntityNoteInfo> result = new ArrayList<>();
    public String error;

    public List<AnkiEntityNoteInfo> getResult() {
        return result;
    }

    public void setResult(List<AnkiEntityNoteInfo> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    @Override
    public String toString() {
        return result == null ? "NULL" : result.size() + " notes";
    }
}

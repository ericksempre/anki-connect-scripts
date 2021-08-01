package com.erick.model;

import java.util.List;

public class AnkiResultFindNotes {
    public List<Long> result;
    public String error;

    public List<Long> getResult() {
        return result;
    }

    public void setResult(List<Long> result) {
        this.result = result;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }
}

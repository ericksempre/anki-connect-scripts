package com.erick.model;

public class AnkiEntityNoteInfoField {
    public String value;
    public Integer order;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }

    @Override
    public String toString() {
        return value;
    }
}

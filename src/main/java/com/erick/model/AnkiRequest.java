package com.erick.model;

class AnkiRequest {
    public  String action;
    public  String version = "6";

    public AnkiRequest(String action) {
        this.action = action;
    }
}
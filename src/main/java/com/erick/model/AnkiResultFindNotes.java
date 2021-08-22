package com.erick.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnkiResultFindNotes {
    public List<Long> result;
    public String error;
}

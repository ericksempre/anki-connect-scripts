package com.erick.scripts;

import com.erick.model.AnkiEntityAddNote;
import com.erick.model.AnkiEntityNoteInfo;
import com.erick.model.AnkiEntityNoteInfoField;
import com.erick.services.AnkiConnectService;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.*;

import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static java.util.stream.Collectors.toList;

@RequiredArgsConstructor
public class ScriptReOrderDeckByKanjiFrequency {
    private static final Type MAP_SRING_LONG_TYPE = new TypeToken<Map<String, Long>>() {
    }.getType();
    private static final String DECK_NAME = "中学校の漢字";
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private final AnkiConnectService service;

    public void run() {
        var kanjiFrequency = loadKanjiFrequency();
        var notes = service.findNewNotes(DECK_NAME);
        notes.sort(orderByKanjiFrequency(kanjiFrequency));
        service.deleteNotesByIds(toIds(notes));
        service.addNotes(toAnkiAddNote(notes));
    }

    @SneakyThrows
    private Map<String, Long> loadKanjiFrequency() {
        var json = Resources.toString(getResource("kanji_frequency_sorted.json"), UTF_8);
        return GSON.fromJson(json, MAP_SRING_LONG_TYPE);
    }

    private Comparator<AnkiEntityNoteInfo> orderByKanjiFrequency(Map<String, Long> kanjiFrequency) {
        return (a, b) -> kanjiFrequency.get(b.getTags().get(0)).compareTo(kanjiFrequency.get(a.getTags().get(0)));
    }

    private List<Long> toIds(List<AnkiEntityNoteInfo> notes) {
        return notes.stream()
                .map(AnkiEntityNoteInfo::getNoteId)
                .collect(toList());
    }

    private List<AnkiEntityAddNote> toAnkiAddNote(List<AnkiEntityNoteInfo> notes) {
        return notes.stream()
                .map(info -> {
                    var addNote = new AnkiEntityAddNote();
                    addNote.setDeckName(DECK_NAME);
                    addNote.setModelName(info.getModelName());
                    addNote.setFields(convertFieldsToAddNoteFormat(info.fields));
                    addNote.setTags(new LinkedHashSet<>(info.getTags()));
                    return addNote;
                })
                .collect(toList());
    }

    private Map<String, String> convertFieldsToAddNoteFormat(Map<String, AnkiEntityNoteInfoField> fields) {
        var fieldNewFormat = new LinkedHashMap<String, String>();
        for (var entry : fields.entrySet()) {
            fieldNewFormat.put(entry.getKey(), entry.getValue().getValue());
        }
        return fieldNewFormat;
    }
}

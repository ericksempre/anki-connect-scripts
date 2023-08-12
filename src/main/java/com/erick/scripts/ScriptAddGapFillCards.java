package com.erick.scripts;

import com.erick.model.PhraseDTO;
import com.erick.model.anki.AnkiEntityAddNote;
import com.erick.services.AnkiConnectService;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static com.erick.util.GsonUtil.buildGson;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;

@RequiredArgsConstructor
public class ScriptAddGapFillCards {
    private static final Type TYPE = new TypeToken<List<PhraseDTO>>() {
    }.getType();
    private static final String JSON_FILE_PATH = "gap_fill_exercises.json";
    private static final String ANKI_DECK_NAME = "at, it, on";
    public static final String ANKI_MODEL_NAME = "at, it, on";

    private final Gson GSON = buildGson();
    private final AnkiConnectService service;

    public void run() {
        var phrases = loadPhrases();
        var notesToAdd = new ArrayList<AnkiEntityAddNote>();
        for (var entry : phrases) {
            notesToAdd.add(buildAnkiNote(entry.getSentenceWithGap(), entry.getSentenceWithAnswer(), entry.getAnswer()));
        }
        service.addNotes(notesToAdd);
    }

    @SneakyThrows
    private List<PhraseDTO> loadPhrases() {
        var json = Resources.toString(getResource(JSON_FILE_PATH), UTF_8);
        return GSON.fromJson(json, TYPE);
    }

    private AnkiEntityAddNote buildAnkiNote(String question, String answer, String tag) {
        var ankiNote = new AnkiEntityAddNote();
        ankiNote.deckName = ANKI_DECK_NAME;
        ankiNote.modelName = ANKI_MODEL_NAME;
        ankiNote.fields.put("Front", question);
        ankiNote.fields.put("Back", answer);
        ankiNote.tags.add(tag);
        return ankiNote;
    }
}

package com.erick.scripts;

import static com.erick.constants.AnkiConnectActions.ANKI_CONNECT_URL;

import com.erick.model.AnkiRequestAddNotes;
import com.erick.model.AnkiParamsAddNotes;
import com.erick.model.AnkiEntityAddNote;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


public class ScriptAddKanjiNotesWithJustSentenceAndFurigana {
    private static final String CHUUGAKKOU_KANJI_SENTENCES_FILE_PATH = "chuugakkou_kanji_example_sentences_for_anki.txt";
    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    public void run() throws Exception {
        var addNoteParams = new AnkiParamsAddNotes();
        var addNoteRequest = new AnkiRequestAddNotes(addNoteParams);

        try (Stream<String> stream = Files.lines(Paths.get(CHUUGAKKOU_KANJI_SENTENCES_FILE_PATH))) {
            stream.forEach(line -> {
                var fields = line.split(";");
                var sentence = fields[0].trim();
                var sentenceWithFurigana = fields[1].trim();
                var kanji = fields[2].trim();
                addNoteToRequest(addNoteRequest, sentence, sentenceWithFurigana, kanji);
            });
        }

        var request = HttpRequest.newBuilder()
                .uri(URI.create(ANKI_CONNECT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(addNoteRequest)))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private void addNoteToRequest(AnkiRequestAddNotes addNoteRequest, String sentence, String sentenceWithFurigana, String kanji) {
        var ankiNote = new AnkiEntityAddNote();
        ankiNote.deckName = "中学校の漢字";
        ankiNote.modelName = "Chuugakkou Japanese";
        ankiNote.fields.put("Expression", sentence);
        ankiNote.fields.put("Reading", sentenceWithFurigana);
        ankiNote.tags.add(kanji);
        addNoteRequest.getParams().addNote(ankiNote);
    }
}

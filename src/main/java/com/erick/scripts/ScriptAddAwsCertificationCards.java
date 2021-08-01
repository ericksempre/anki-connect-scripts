package com.erick.scripts;

import com.erick.model.AnkiEntityAddNote;
import com.erick.model.AnkiParamsAddNotes;
import com.erick.model.AnkiRequestAddNotes;
import com.erick.model.BookQuestion;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;

import static com.erick.constants.AnkiConnectActions.ANKI_CONNECT_URL;


public class ScriptAddAwsCertificationCards {
    private static final Type BOOK_QUESTIONS_TYPE = new TypeToken<Map<String, List<BookQuestion>>>() {
    }.getType();
    private static final String AWS_CERTIFICATION_QUESTIONS_JSON_FILE_PATH = "aws_certification_book_questions.json";
    private static final String ANKI_DECK_NAME = "AWS Certification";
    public static final String ANKI_MODEL_NAME = "AWS Certification";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private Map<String, List<BookQuestion>> kanjiSentences;

    public void run() throws Exception {
        kanjiSentences = loadAwsCertificationBookQuestions();
        var addNoteParams = new AnkiParamsAddNotes();
        var addNoteRequest = new AnkiRequestAddNotes(addNoteParams);
        for (var entry : kanjiSentences.entrySet()) {
            for (var bookQuestion : entry.getValue()) {
                var formattedQuestion = formatQuestion(bookQuestion.question);
                var formattedAnswer = formatAnswer(bookQuestion.answer);
                addNoteToRequest(addNoteRequest, formattedQuestion, formattedAnswer, entry.getKey());
            }
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(ANKI_CONNECT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(addNoteRequest)))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private Map<String, List<BookQuestion>> loadAwsCertificationBookQuestions() throws FileNotFoundException {
        var reader = new JsonReader(new FileReader(AWS_CERTIFICATION_QUESTIONS_JSON_FILE_PATH));
        return GSON.fromJson(reader, BOOK_QUESTIONS_TYPE);
    }

    private String formatQuestion(String answer) {
        return answer.replaceAll("\r\n(?=([A-Z]\\.))", "<br>")
                .replace("A. ", "<br>A. ");
    }

    private String formatAnswer(String answer) {
        return answer.replaceAll("\r\n(?=([A-Z]\\.))", "<br>");
    }

    private void addNoteToRequest(AnkiRequestAddNotes addNoteRequest, String question, String answer, String chapter) {
        var ankiNote = new AnkiEntityAddNote();
        ankiNote.deckName = ANKI_DECK_NAME;
        ankiNote.modelName = ANKI_MODEL_NAME;
        ankiNote.fields.put("Front", question);
        ankiNote.fields.put("Back", answer);
        ankiNote.tags.add(chapter);
        addNoteRequest.getParams().addNote(ankiNote);
    }
}

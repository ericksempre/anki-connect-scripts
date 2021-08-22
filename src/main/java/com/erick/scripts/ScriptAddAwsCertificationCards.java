package com.erick.scripts;

import com.erick.model.AnkiEntityAddNote;
import com.erick.model.BookQuestion;
import com.erick.services.AnkiConnectService;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.erick.util.GsonUtil.buildGson;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;

@RequiredArgsConstructor
public class ScriptAddAwsCertificationCards {
    private static final Type BOOK_QUESTIONS_TYPE = new TypeToken<Map<String, List<BookQuestion>>>() {
    }.getType();
    private static final String AWS_CERTIFICATION_QUESTIONS_JSON_FILE_PATH = "aws_certification_book_questions.json";
    private static final String ANKI_DECK_NAME = "AWS Certification";
    public static final String ANKI_MODEL_NAME = "AWS Certification";

    private final Gson GSON = buildGson();
    private final AnkiConnectService service;

    private Map<String, List<BookQuestion>> kanjiSentences;

    public void run() {
        kanjiSentences = loadAwsCertificationBookQuestions();
        var notesToAdd = new ArrayList<AnkiEntityAddNote>();
        for (var entry : kanjiSentences.entrySet()) {
            for (var bookQuestion : entry.getValue()) {
                var formattedQuestion = formatQuestion(bookQuestion.question);
                var formattedAnswer = formatAnswer(bookQuestion.answer);
                notesToAdd.add(buildAnkiNote(formattedQuestion, formattedAnswer, entry.getKey()));
            }
        }
        service.addNotes(notesToAdd);
    }

    @SneakyThrows
    private Map<String, List<BookQuestion>> loadAwsCertificationBookQuestions() {
        var json = Resources.toString(getResource(AWS_CERTIFICATION_QUESTIONS_JSON_FILE_PATH), UTF_8);
        return GSON.fromJson(json, BOOK_QUESTIONS_TYPE);
    }

    private String formatQuestion(String answer) {
        return answer.replaceAll("\r\n(?=([A-Z]\\.))", "<br>")
                .replace("A. ", "<br>A. ");
    }

    private String formatAnswer(String answer) {
        return answer.replaceAll("\r\n(?=([A-Z]\\.))", "<br>");
    }

    private AnkiEntityAddNote buildAnkiNote(String question, String answer, String chapter) {
        var ankiNote = new AnkiEntityAddNote();
        ankiNote.deckName = ANKI_DECK_NAME;
        ankiNote.modelName = ANKI_MODEL_NAME;
        ankiNote.fields.put("Front", question);
        ankiNote.fields.put("Back", answer);
        ankiNote.tags.add(chapter);
        return ankiNote;
    }
}

package com.erick.scripts.legacy;

import com.erick.model.AnkiEntityAddNote;
import com.erick.model.AnkiParamsAddNotes;
import com.erick.model.AnkiRequestAddNotes;
import com.erick.model.KanjiSentenceCard;
import com.erick.util.KanjiDict;
import com.erick.util.KanjiWordDict;
import com.google.common.io.Resources;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import lombok.SneakyThrows;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static com.erick.constants.AnkiConnectConstants.ANKI_CONNECT_URL;
import static com.erick.util.KanjiUtil.getAllKanjisInSentence;
import static com.erick.util.StringUtil.isBlank;
import static com.google.common.base.Charsets.UTF_8;
import static com.google.common.io.Resources.getResource;
import static java.lang.String.*;
import static java.util.stream.Collectors.toList;

@Deprecated
public class ScriptAddKanjiCardsWithMeanings {
    private static final Type LIST_LIST_TYPE = new TypeToken<List<List<Object>>>() {
    }.getType();
    private static final Type KANJI_SENTENCE_CARDS_TYPE = new TypeToken<List<KanjiSentenceCard>>() {
    }.getType();
    private static final String KANJI_DICT_JSON_FILE_PATH = "C:\\coding\\kanjidic_english_modified\\kanji_bank_1.json";
    private static final String KANJI_WORDS_DICT_JSON_FILE_PATH = "D:\\coding\\jmdict-english\\term_bank_%d.json";
    private static final String KANJI_SENTENCES_JSON_FILE_PATH = "chuugakkou_kanji_sentence_cards.json";
    private static final String ANKI_DECK_NAME = "中学校の漢字";
    private static final String KANJIS_INFO_FIELD_TEMPLATE = "<span class=\"kanji-info-kanji\">${KANJI_CHARACTER}</span><br><span class=\"kanji-info-onyomi\">${ON_YOMIS}</span><br><span class=\"kanji-info-kunyomi\">${KUN_YOMIS}</span><br><span class=\"kanji-info-meanings\">${KANJI_MEANINGS}</span><br><br>";
    private static final String WORD_MEANING_FIELD_TEMPLATE = "<span class=\"word-meaning-word\">${KANJI_WORD}</span><br>${WORD_MEANINGS}";
    private static final String WORD_MEANING_SUB_FIELD_TEMPLATE = "<span class=\"word-meaning-meaning\">${WORD_MEANING_INDEX}. ${WORD_MEANING}</span><br>";
    public static final String ANKI_MODEL_NAME = "Chuugakkou Japanese";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson GSON = new GsonBuilder().setPrettyPrinting().create();

    private KanjiDict kanjiDict;
    private KanjiWordDict kanjiWordDict;
    private List<KanjiSentenceCard> kanjiSentences;

    public void run() throws Exception {
        kanjiDict = loadKanjiDict();
        kanjiWordDict = loadKanjiWordDict();
        kanjiSentences = loadKanjiSentences();
        var addNoteParams = new AnkiParamsAddNotes();
        var addNoteRequest = new AnkiRequestAddNotes(addNoteParams);
        for (int i = 105, kanjiSentencesSize = kanjiSentences.size(); i < kanjiSentencesSize; i++) {
            var kanjiSentence = kanjiSentences.get(i);
            try {
                var sentenceWithKanjis = kanjiSentence.sentence;
                var japaneseWordToRecognize = kanjiSentence.word;
                var kanjisInJapaneseWordToRecognize = japaneseWordToRecognize.chars().mapToObj(e -> (char) e).collect(toList());
                var allKanjisInSentence = getAllKanjisInSentence(sentenceWithKanjis);
                var difficultKanjis = getAllKanjisWithGradeHigherThan(allKanjisInSentence, kanjisInJapaneseWordToRecognize, List.of("小6", "小5", "中"));
                var kanjisInfoFieldValue = "";
                for (var kanjiCharacter : difficultKanjis) {
                    kanjisInfoFieldValue += KANJIS_INFO_FIELD_TEMPLATE
                            .replace("${KANJI_CHARACTER}", valueOf(kanjiCharacter))
                            .replace("${ON_YOMIS}", join("、", kanjiDict.getOnYomi(kanjiCharacter)))
                            .replace("${KUN_YOMIS}", join("、", kanjiDict.getKunYomi(kanjiCharacter)))
                            .replace("${KANJI_MEANINGS}", join(", ", kanjiDict.getMeanings(kanjiCharacter)));
                }
                kanjisInfoFieldValue = removeLastBreakLineElements(kanjisInfoFieldValue);
                kanjisInfoFieldValue = removeEmptyOnKunElements(kanjisInfoFieldValue);
                var wordMeaningFieldValue = "";
                var kanjiWordMeaningElements = buildKanjiWordMeaningElements(japaneseWordToRecognize);
                if (!isBlank(kanjiWordMeaningElements)) {
                    wordMeaningFieldValue = WORD_MEANING_FIELD_TEMPLATE
                            .replace("${KANJI_WORD}", japaneseWordToRecognize)
                            .replace("${WORD_MEANINGS}", kanjiWordMeaningElements);
                    wordMeaningFieldValue = removeLastBreakLineElements(wordMeaningFieldValue);
                }
                addNoteToRequest(addNoteRequest, sentenceWithKanjis, kanjiSentence.sentenceWithAnkiStyleFurigana, kanjisInfoFieldValue, wordMeaningFieldValue, kanjiSentence.kanji);
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        var request = HttpRequest.newBuilder()
                .uri(URI.create(ANKI_CONNECT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(addNoteRequest)))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
    }

    private KanjiDict loadKanjiDict() throws FileNotFoundException {
        var reader = new JsonReader(new FileReader(KANJI_DICT_JSON_FILE_PATH));
        return new KanjiDict(GSON.fromJson(reader, LIST_LIST_TYPE));
    }

    private KanjiWordDict loadKanjiWordDict() throws FileNotFoundException {
        var allKanjiWordEntries = new ArrayList<List<Object>>();
        for (int i = 1; i <= 32; i++) {
            var reader = new JsonReader(new FileReader(format(KANJI_WORDS_DICT_JSON_FILE_PATH, i)));
            var kanjiWordEntries = (List<List<Object>>) GSON.fromJson(reader, LIST_LIST_TYPE);
            allKanjiWordEntries.addAll(kanjiWordEntries);
        }
        return new KanjiWordDict(allKanjiWordEntries);
    }

    @SneakyThrows
    private List<KanjiSentenceCard> loadKanjiSentences() {
        var json = Resources.toString(getResource(KANJI_SENTENCES_JSON_FILE_PATH), UTF_8);
        return GSON.fromJson(json, KANJI_SENTENCE_CARDS_TYPE);
    }

    private List<Character> getAllKanjisWithGradeHigherThan(List<Character> kanjis, List<Character> exceptThisKanjis, List<String> targetGrades) {
        var kanjisWithTargetGrade = new ArrayList<>(kanjis);
        kanjisWithTargetGrade.removeIf(kanji -> !isKanjiWithTargetGrade(kanji, targetGrades) && !exceptThisKanjis.contains(kanji));
        return kanjisWithTargetGrade;
    }

    private boolean isKanjiWithTargetGrade(Character kanji, List<String> targetGrades) {
        return targetGrades.contains(kanjiDict.getGrade(kanji));
    }

    private String removeLastBreakLineElements(String str) {
        var result = str;
        while (result.endsWith("<br>")) {
            var index = result.lastIndexOf("<br>");
            result = result.substring(0, index);
        }
        return result;
    }

    private String removeEmptyOnKunElements(String kanjisInfoFieldValue) {
        var emptyKunYomiElement = "<span class=\"kanji-info-kunyomi\"></span><br>";
        var emptyOnYomiElement = "<span class=\"kanji-info-onyomi\"></span><br>";
        return kanjisInfoFieldValue
                .replace(emptyOnYomiElement, "")
                .replace(emptyKunYomiElement, "");
    }

    private String buildKanjiWordMeaningElements(String kanjiWord) {
        var result = new StringBuilder();
        var meaningGroups = kanjiWordDict.getMeaningsGroups(kanjiWord);
        if (meaningGroups == null) {
            return "";
        }
        var loopIndex = new AtomicInteger(0);
        meaningGroups.stream()
                .sequential()
                .forEach(meanings -> {
                    var meaningsJoined = join(", ", meanings);
                    result.append(WORD_MEANING_SUB_FIELD_TEMPLATE
                            .replace("${WORD_MEANING_INDEX}", valueOf(loopIndex.addAndGet(1)))
                            .replace("${WORD_MEANING}", meaningsJoined));
                });
        return result.toString();
    }

    private void addNoteToRequest(AnkiRequestAddNotes addNoteRequest, String sentence, String sentenceWithFurigana, String kanjisInfo, String kanjiWordMeaning, String kanji) {
        var ankiNote = new AnkiEntityAddNote();
        ankiNote.deckName = ANKI_DECK_NAME;
        ankiNote.modelName = ANKI_MODEL_NAME;
        ankiNote.fields.put("KanjiSentence", sentence);
        ankiNote.fields.put("KanjiSentenceWithFurigana", sentenceWithFurigana);
        ankiNote.fields.put("KanjisInfo", kanjisInfo);
        ankiNote.fields.put("WordMeaning", kanjiWordMeaning);
        ankiNote.tags.add(kanji);
        addNoteRequest.getParams().addNote(ankiNote);
    }
}

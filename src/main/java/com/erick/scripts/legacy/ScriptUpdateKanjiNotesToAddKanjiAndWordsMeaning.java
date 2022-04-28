package com.erick.scripts.legacy;

import static com.erick.constants.AnkiConnectConstants.ANKI_CONNECT_URL;
import static com.erick.util.GsonUtil.buildGson;
import static com.erick.util.KanjiUtil.getAllKanjisInSentence;
import static com.erick.util.StringUtil.isBlank;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.lang.String.valueOf;
import static java.util.stream.Collectors.toList;

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

import com.erick.model.anki.AnkiEntityUpdateNoteField;
import com.erick.model.anki.AnkiParamsFindNotes;
import com.erick.model.anki.AnkiParamsNoteIds;
import com.erick.model.anki.AnkiParamsUpdateNoteFields;
import com.erick.model.anki.AnkiRequestFindNotes;
import com.erick.model.anki.AnkiRequestNotesInfo;
import com.erick.model.anki.AnkiRequestUpdateNoteFields;
import com.erick.model.anki.AnkiResultFindNotes;
import com.erick.model.anki.AnkiResultGeneric;
import com.erick.model.anki.AnkiResultNotesInfo;
import com.erick.util.KanjiDict;
import com.erick.util.KanjiWordDict;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

@Deprecated
public class ScriptUpdateKanjiNotesToAddKanjiAndWordsMeaning {
    private static final Type LIST_LIST_TYPE = new TypeToken<List<List<Object>>>() {
    }.getType();
    private static final String KANJI_DICT_JSON_FILE_PATH = "D:\\coding\\kanjidic_english_modified\\kanji_bank_1.json";
    private static final String KANJI_WORDS_DICT_JSON_FILE_PATH = "C:\\coding\\jmdict-english\\term_bank_%d.json";
    private static final String DECK_NAME = "中学校の漢字";
    private static final String KANJIS_INFO_FIELD_TEMPLATE = "<span class=\"kanji-info-kanji\">${KANJI_CHARACTER}</span><br><span class=\"kanji-info-onyomi\">${ON_YOMIS}</span><br><span class=\"kanji-info-kunyomi\">${KUN_YOMIS}</span><br><span class=\"kanji-info-meanings\">${KANJI_MEANINGS}</span><br><br>";
    private static final String WORD_MEANING_FIELD_TEMPLATE = "<br><br><span class=\"word-meaning-word\">${KANJI_WORD}</span><br>${WORD_MEANINGS}";
    private static final String WORD_MEANING_SUB_FIELD_TEMPLATE = "<span class=\"word-meaning-meaning\">${WORD_MEANING_INDEX}. ${WORD_MEANING}</span><br>";

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private final Gson GSON = buildGson();

    private KanjiDict kanjiDict;
    private KanjiWordDict kanjiWordDict;

    public void run() throws Exception {
        kanjiDict = loadKanjiDict();
        kanjiWordDict = loadKanjiWordDict();
        var ankiParamsFindNotes = new AnkiParamsFindNotes("deck:" + DECK_NAME);
        var ankiRequestFindNotes = new AnkiRequestFindNotes(ankiParamsFindNotes);
        var request = HttpRequest.newBuilder()
                .uri(URI.create(ANKI_CONNECT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(ankiRequestFindNotes)))
                .build();
        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var ankiNoteIds = GSON.fromJson(response.body(), AnkiResultFindNotes.class).result;
        var ankiParamsNotesInfo = new AnkiParamsNoteIds(ankiNoteIds);
        var ankiRequestNotesInfo = new AnkiRequestNotesInfo(ankiParamsNotesInfo);
        var request2 = HttpRequest.newBuilder()
                .uri(URI.create(ANKI_CONNECT_URL))
                .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(ankiRequestNotesInfo)))
                .build();
        var response2 = httpClient.send(request2, HttpResponse.BodyHandlers.ofString());
        var ankiNotes = GSON.fromJson(response2.body(), AnkiResultNotesInfo.class).result;
        for (var ankiNote : ankiNotes) {
            try {
                var sentenceWithKanjis = ankiNote.fields.get("KanjiSentence").value;
                var sentenceWithKanjisAndFurigana = ankiNote.fields.get("KanjiSentenceWithFurigana").value;
                var japaneseWordToRecognize = sentenceWithKanjisAndFurigana.substring(sentenceWithKanjisAndFurigana.indexOf(" ") + 1, sentenceWithKanjisAndFurigana.indexOf("["));
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
                var ankiEntityUpdateNoteField = new AnkiEntityUpdateNoteField(ankiNote.getNoteId());
                var ankiParamsUpdateNoteFields = new AnkiParamsUpdateNoteFields(ankiEntityUpdateNoteField);
                var ankiRequestUpdateNoteFields = new AnkiRequestUpdateNoteFields(ankiParamsUpdateNoteFields);
                ankiEntityUpdateNoteField.fields.put("KanjisInfo", kanjisInfoFieldValue);
                if (japaneseWordToRecognize.length() >= 2) {
                    var wordMeaningFieldValue = "";
                    var kanjiWordMeaningElements = buildKanjiWordMeaningElements(japaneseWordToRecognize);
                    if (!isBlank(kanjiWordMeaningElements)) {
                        wordMeaningFieldValue = WORD_MEANING_FIELD_TEMPLATE
                                .replace("${KANJI_WORD}", japaneseWordToRecognize)
                                .replace("${WORD_MEANINGS}", kanjiWordMeaningElements);
                        wordMeaningFieldValue = removeLastBreakLineElements(wordMeaningFieldValue);
                        ankiEntityUpdateNoteField.fields.put("WordMeaning", wordMeaningFieldValue);
                    }
                }
                var request3 = HttpRequest.newBuilder()
                        .uri(URI.create(ANKI_CONNECT_URL))
                        .POST(HttpRequest.BodyPublishers.ofString(GSON.toJson(ankiRequestUpdateNoteFields)))
                        .build();
                var response3 = httpClient.send(request3, HttpResponse.BodyHandlers.ofString());
                var result = GSON.fromJson(response3.body(), AnkiResultGeneric.class);
            } catch (StringIndexOutOfBoundsException ignored) {
            }
        }
        System.out.println("Finished!");
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
}

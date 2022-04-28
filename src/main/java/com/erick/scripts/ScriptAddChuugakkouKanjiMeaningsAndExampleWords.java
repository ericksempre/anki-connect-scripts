package com.erick.scripts;

import static com.erick.util.GsonUtil.buildGson;
import static java.util.stream.Collectors.summarizingDouble;
import static java.util.stream.Collectors.toList;

import com.erick.model.KanjiDTO;
import com.erick.model.WordDTO;
import com.erick.model.anki.AnkiEntityAddNote;
import com.erick.services.AnkiConnectService;
import com.google.common.base.Charsets;
import com.google.common.io.Files;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;

@RequiredArgsConstructor
public class ScriptAddChuugakkouKanjiMeaningsAndExampleWords {
  private static final String DECK_NAME = "中学校の漢字【改】";
  private static final String MODEL_NAME = "Chuugakkou no Kanji Kai";
  private static final Gson GSON = buildGson();
  private static final Type KANJI_LIST_TYPE = new TypeToken<List<KanjiDTO>>(){}.getType();

  private final AnkiConnectService service;

  public void run() {
    var kanjis = getKanjis();
    var ankiNotes = new ArrayList<AnkiEntityAddNote>();
    for (var kanji : kanjis) {
      var ankiNote = new AnkiEntityAddNote();
      ankiNote.deckName = DECK_NAME;
      ankiNote.modelName = MODEL_NAME;
      ankiNote.fields.put("KanjiCharacter", kanji.kanji);
      ankiNote.fields.put("OnYomi", buildOnYomi(kanji));
      ankiNote.fields.put("KunYomi", buildKunYomi(kanji));
      ankiNote.fields.put("KanjiMeanings", buildKanjiMeanings(kanji));
      ankiNote.fields.put("ExampleWords", buildExampleWords(kanji));
      ankiNote.fields.put("ExampleWordsDetails", buildExampleWordsDetails(kanji));
      ankiNotes.add(ankiNote);
    }
    service.addNotes(ankiNotes);
  }

  private String buildOnYomi(KanjiDTO kanji) {
    return String.join("、", kanji.on);
  }

  private String buildKunYomi(KanjiDTO kanji) {
    return String.join("、", kanji.kun);
  }

  private String buildKanjiMeanings(KanjiDTO kanji) {
    return String.join(", ", kanji.meanings);
  }

  private String buildExampleWords(KanjiDTO kanji) {
    var words = kanji.exampleWords.stream()
        .map(WordDTO::getWord)
        .collect(toList());
    return String.join("、", words);
  }

  private String buildExampleWordsDetails(KanjiDTO kanji) {
    var result = new StringBuilder();
    for (var exampleWord : kanji.exampleWords) {
      result.append(exampleWord.reading).append(": ").append(String.join(", ", exampleWord.meanings)).append("</br></br>");
    }
    return result.toString();
  }

  private List<KanjiDTO> getKanjis() {
    var json = getResourceFile("chuugakkou_kanjis_sorted_by_frequency_and_example_words.json");
    return GSON.fromJson(json, KANJI_LIST_TYPE);
  }

  @SneakyThrows
  private String getResourceFile(String resourceFilePath) {
    var url = getClass().getClassLoader().getResource(resourceFilePath);
    var file = Paths.get(url.toURI()).toFile();
    return Files.asCharSource(file, Charsets.UTF_8).read();
  }
}

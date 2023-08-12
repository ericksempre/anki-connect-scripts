package com.erick.scripts.adhoc;

import com.erick.model.PhraseDTO;
import lombok.SneakyThrows;

import java.io.FileWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;

import static com.erick.util.GsonUtil.buildGson;
import static java.lang.Math.min;
import static java.lang.String.format;
import static java.util.Collections.shuffle;
import static java.util.stream.Collectors.toSet;

public class ScriptBuildGapFillExercisesFromBooks {
    private static final List<String> BOOK_PATHS = List.of(
            "C:\\Users\\erick\\Downloads\\Sapiens.txt",
            "C:\\Users\\erick\\Downloads\\Where the Crawdads Sing.txt"
    );
    private static final List<String> TERMS = List.of("on", "at", "in");
    private static final String OUTPUT_FILE_PATH = "gap_fill_exercises.json";

    @SneakyThrows
    public static void main(String[] args) {
        var phrasesPerTerm = new HashMap<String, Set<String>>();
        var equalizedPhrasesPerTerm = new HashMap<String, Set<String>>();
        var minTermCount = Integer.MAX_VALUE;
        var result = new ArrayList<PhraseDTO>();
        for (var bookPath : BOOK_PATHS) {
            var bookContent = Files.readString(Path.of(bookPath), StandardCharsets.UTF_8);
            for (var term : TERMS) {
                var pattern = Pattern.compile(format("[^.\n*–\"]* %s [^.\n*–\"]*\\.", term));
                var phrases = pattern.matcher(bookContent)
                        .results()
                        .map(MatchResult::group)
                        .map(String::trim)
                        .map(phrase -> phrase.substring(0, 1).toUpperCase() + phrase.substring(1))
                        .collect(toSet());
                if (!phrasesPerTerm.containsKey(term)) {
                    phrasesPerTerm.put(term, new HashSet<>());
                }
                phrasesPerTerm.get(term).addAll(phrases);
            }
        }
        for (var entry : phrasesPerTerm.entrySet()) {
            minTermCount = min(minTermCount, entry.getValue().size());
        }
        for (var entry : phrasesPerTerm.entrySet()) {
            var list = new ArrayList<>(entry.getValue());
            shuffle(list);
            var newSet = list.stream().limit(minTermCount).collect(toSet());
            equalizedPhrasesPerTerm.put(entry.getKey(), newSet);
        }
        for (var entry : equalizedPhrasesPerTerm.entrySet()) {
            var answer = entry.getKey();
            var gapFillPhrases = entry.getValue()
                    .stream()
                    .map(phrase -> PhraseDTO.builder()
                            .sentenceWithGap(phrase.replace(" " + answer + " ", " __ "))
                            .sentenceWithAnswer(phrase)
                            .answer(answer)
                            .build()
                    )
                    .collect(toSet());
            result.addAll(gapFillPhrases);
        }
        shuffle(result);
        saveFile(result);
    }

    @SneakyThrows
    private static void saveFile(List<PhraseDTO> data) {
        try (var writer = new FileWriter(OUTPUT_FILE_PATH)) {
            buildGson().toJson(data, writer);
        }
    }
}

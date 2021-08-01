package com.erick.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;

public class KanjiWordDict {
    private final Map<String, LinkedHashSet<LinkedHashSet<String>>> kanjiWordMeanings;

    public KanjiWordDict(List<List<Object>> kanjiWordDictionary) {
        kanjiWordMeanings = new HashMap<>();
        for (List<Object> kanjiWordEntry : kanjiWordDictionary) {
            var currentKanjiWord = (String) kanjiWordEntry.get(0);
            var currentMeanings = new LinkedHashSet<>((ArrayList<String>) kanjiWordEntry.get(5));
            if (!kanjiWordMeanings.containsKey(currentKanjiWord)) {
                kanjiWordMeanings.put(currentKanjiWord, new LinkedHashSet<>());
            }
            kanjiWordMeanings.get(currentKanjiWord).add(currentMeanings);
        }
    }

    public LinkedHashSet<LinkedHashSet<String>> getMeaningsGroups(String kanjiWord) {
        return kanjiWordMeanings.get(kanjiWord);
    }
}

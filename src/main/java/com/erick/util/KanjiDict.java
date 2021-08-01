package com.erick.util;

import static com.erick.util.StringUtil.isBlank;
import static java.util.Arrays.asList;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.internal.LinkedTreeMap;

public class KanjiDict {
    private final Map<Character, List<Object>> kanjiInfos;

    public KanjiDict(List<List<Object>> kanjiDict) {
        kanjiInfos = new HashMap<>();
        for (List<Object> kanjiDictEntry : kanjiDict) {
            var currentKanji = (String) kanjiDictEntry.get(0);
            var currentKanjiChar = currentKanji.charAt(0);
            kanjiInfos.put(currentKanjiChar, kanjiDictEntry);
        }
    }

    public List<String> getKunYomi(char kanji) {
        var kunYomisSeparetedBySpace = (String) kanjiInfos.get(kanji).get(2);
        if (isBlank(kunYomisSeparetedBySpace)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(asList(kunYomisSeparetedBySpace.split(" ")));
    }

    public List<String> getOnYomi(char kanji) {
        var onYomisSeparetedBySpace = (String) kanjiInfos.get(kanji).get(1);
        if (isBlank(onYomisSeparetedBySpace)) {
            return new ArrayList<>();
        }
        return new ArrayList<>(asList(onYomisSeparetedBySpace.split(" ")));
    }

    public List<String> getMeanings(char kanji) {
        return (ArrayList<String>) kanjiInfos.get(kanji).get(4);
    }

    public String getGrade(char kanji) {
        return ((LinkedTreeMap<String, String>) kanjiInfos.get(kanji).get(5)).get("grade");
    }
}

package com.erick.util;

import lombok.experimental.UtilityClass;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class KanjiUtil {
    private static final int UNICODE_OF_FIRST_KANJI_CHARACTER_IN_CJK = 19968;
    private static final int UNICODE_OF_LAST_KANJI_CHARACTER_IN_CJK = 40879;

    public static List<Character> getAllKanjisInSentence(String japaneseSentence) {
        var kanjis = new ArrayList<Character>();
        var japaneseChars = japaneseSentence.toCharArray();
        for (char japaneseChar : japaneseChars) {
            if (!isKanji(japaneseChar)) {
                continue;
            }
            kanjis.add(japaneseChar);
        }
        return kanjis;
    }

    public static boolean isKanji(char character) {
        return character >= UNICODE_OF_FIRST_KANJI_CHARACTER_IN_CJK && character <= UNICODE_OF_LAST_KANJI_CHARACTER_IN_CJK;
    }
}

package com.erick.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class KanjiSentenceCard {
    public String kanji = "";
    public String sentence = "";
    public String sentenceWithAnkiStyleFurigana = "";
    public String word = "";
}

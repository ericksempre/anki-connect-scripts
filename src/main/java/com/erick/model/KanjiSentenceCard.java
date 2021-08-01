package com.erick.model;

public class KanjiSentenceCard {
    public String kanji = "";
    public String sentence = "";
    public String sentenceWithAnkiStyleFurigana = "";
    public String word = "";

    public String getKanji() {
        return kanji;
    }

    public void setKanji(String kanji) {
        this.kanji = kanji;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }

    public String getSentenceWithAnkiStyleFurigana() {
        return sentenceWithAnkiStyleFurigana;
    }

    public void setSentenceWithAnkiStyleFurigana(String sentenceWithAnkiStyleFurigana) {
        this.sentenceWithAnkiStyleFurigana = sentenceWithAnkiStyleFurigana;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}

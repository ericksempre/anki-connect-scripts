package com.erick.model;

import java.util.Objects;

public class BookQuestion {
    public String question;
    public String answer;

    public BookQuestion() {
    }

    public BookQuestion(String question) {
        this.question = question;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (other == null || getClass() != other.getClass()) {
            return false;
        }
        BookQuestion that = (BookQuestion) other;
        return Objects.equals(question, that.question) && Objects.equals(answer, that.answer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(question, answer);
    }

    @Override
    public String toString() {
        if (question == null || question.isBlank()) {
            return question;
        }
        if (question.length() > 10) {
            return question;
        }
        return question.substring(0, 10);
    }
}

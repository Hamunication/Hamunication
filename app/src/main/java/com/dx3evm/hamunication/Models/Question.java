package com.dx3evm.hamunication.Models;

import java.io.Serializable;
import java.util.Map;

public class Question implements Serializable {

    private String questionID;
    private String questionText;
    private String correctAnswer;
    private Map<String, String> choices;
    private boolean answered;


    public Question() {
    }

    public Question(String questionID, String questionText, String correctAnswer, Map<String, String> choices) {
        this.questionID = questionID;
        this.questionText = questionText;
        this.correctAnswer = correctAnswer;
        this.choices = choices;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        this.correctAnswer = correctAnswer;
    }

    public Map<String, String> getChoices() {
        return choices;
    }

    public void setChoices(Map<String, String> choices) {
        this.choices = choices;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}

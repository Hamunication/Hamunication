package com.dx3evm.hamunication.Models;

import java.io.Serializable;
import java.util.Map;

public class QuestionModel implements Serializable {

    private String questionID;
    private String QuestionTitle;
    private String CorrectAnswer;
    private Map<String, String> Choices;
    private boolean answered;


    public QuestionModel() {
    }

    public QuestionModel(String QuestionTitle, String CorrectAnswer, Map<String, String> Choices) {
        this.QuestionTitle = QuestionTitle;
        this.CorrectAnswer = CorrectAnswer;
        this.Choices = Choices;
    }

    public String getQuestionID() {
        return questionID;
    }

    public void setQuestionID(String questionID) {
        this.questionID = questionID;
    }

    public String getQuestionTitle() {
        return QuestionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        QuestionTitle = questionTitle;
    }

    public String getCorrectAnswer() {
        return CorrectAnswer;
    }

    public void setCorrectAnswer(String correctAnswer) {
        CorrectAnswer = correctAnswer;
    }

    public Map<String, String> getChoices() {
        return Choices;
    }

    public void setChoices(Map<String, String> choices) {
        Choices = choices;
    }

    public boolean isAnswered() {
        return answered;
    }

    public void setAnswered(boolean answered) {
        this.answered = answered;
    }
}

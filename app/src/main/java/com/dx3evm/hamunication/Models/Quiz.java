package com.dx3evm.hamunication.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz implements Serializable {
    private String quizID, quizTitle;
    private Map<String, Object> questions = new HashMap<>();
    public Quiz(){}

    public Quiz(String quizID, String quizTitle, Map<String, Object> questions) {
        this.quizID = quizID;
        this.quizTitle = quizTitle;
        this.questions = questions;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public Map<String, Object> getQuestions() {
        return questions;
    }

    public void setQuestions(Map<String, Object> questions) {
        this.questions = questions;
    }
}


package com.dx3evm.hamunication.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Quiz implements Serializable {
    private String quizID, quizTitle;

    public Quiz(){}

    public Quiz(String quizID, String quizTitle, Map<String, Object> questions) {
        this.quizID = quizID;
        this.quizTitle = quizTitle;
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

}


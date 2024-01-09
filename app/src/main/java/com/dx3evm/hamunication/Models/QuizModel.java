package com.dx3evm.hamunication.Models;

public class QuizModel {
    private String quizTitle;

    public QuizModel() {
        // Default constructor required for Firebase
    }

    public QuizModel(String quizTitle) {
        this.quizTitle = quizTitle;
    }

    public String getQuizTitle() {
        return quizTitle;
    }

    public void setQuizTitle(String quizTitle) {
        this.quizTitle = quizTitle;
    }
}

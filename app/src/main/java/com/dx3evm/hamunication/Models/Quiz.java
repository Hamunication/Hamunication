package com.dx3evm.hamunication.Models;

import java.util.ArrayList;

public class Quiz {
    private String quizID, quizQuestion, quizCorrectAnswer;
    private ArrayList<String> quizChoices = new ArrayList<>();
    public Quiz(){}

    public Quiz(String quizID, String quizQuestion, String quizCorrectAnswer, ArrayList<String> quizChoices){
        this.quizID = quizID;
        this.quizQuestion = quizQuestion;
        this.quizCorrectAnswer = quizCorrectAnswer;
        this.quizChoices = quizChoices;
    }

    public String getQuizID() {
        return quizID;
    }

    public void setQuizID(String quizID) {
        this.quizID = quizID;
    }

    public String getQuizQuestion() {
        return quizQuestion;
    }

    public void setQuizQuestion(String quizQuestion) {
        this.quizQuestion = quizQuestion;
    }

    public String getQuizCorrectAnswer() {
        return quizCorrectAnswer;
    }

    public void setQuizCorrectAnswer(String quizCorrectAnswer) {
        this.quizCorrectAnswer = quizCorrectAnswer;
    }

    public ArrayList<String> getChoices() {
        return quizChoices;
    }

    public void setChoices(ArrayList<String> choices) {
        this.quizChoices = choices;
    }
}

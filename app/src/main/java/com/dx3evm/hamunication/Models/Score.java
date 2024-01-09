package com.dx3evm.hamunication.Models;

public class Score {
    private String scoreId;
    private String score;
    private String totalScore;

    private String userFullName;

    public Score(){}

    public Score(String scoreId, String score, String totalScore, String userFullName) {
        this.scoreId = scoreId;
        this.score = score;
        this.totalScore = totalScore;
        this.userFullName = userFullName;
    }

    public String getScoreId() {
        return scoreId;
    }

    public void setScoreId(String scoreId) {
        this.scoreId = scoreId;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public String getTotalScore() {
        return totalScore;
    }

    public void setTotalScore(String totalScore) {
        this.totalScore = totalScore;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

}

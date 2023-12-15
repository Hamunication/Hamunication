package com.dx3evm.hamunication.Models;

public class Score {
    private String scoreId, score, totalScore;

    public Score(){}

    public Score(String scoreId, String score, String totalScore) {
        this.scoreId = scoreId;
        this.score = score;
        this.totalScore = totalScore;
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
}

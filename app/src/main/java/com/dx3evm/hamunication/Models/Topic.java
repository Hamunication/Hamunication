package com.dx3evm.hamunication.Models;

import java.io.Serializable;

public class Topic implements Serializable {
    private String topicID, topicTitle, topicDescription;

    public Topic(){}
    public Topic(String topicID, String topicTitle, String topicDescription) {
        this.topicID = topicID;
        this.topicTitle = topicTitle;
        this.topicDescription = topicDescription;
    }

    public String getTopicID() {
        return topicID;
    }

    public void setTopicID(String topicID) {
        this.topicID = topicID;
    }

    public String getTopicTitle() {
        return topicTitle;
    }

    public void setTopicTitle(String topicTitle) {
        this.topicTitle = topicTitle;
    }

    public String getTopicDescription() {
        return topicDescription;
    }

    public void setTopicDescription(String topicDescription) {
        this.topicDescription = topicDescription;
    }
}

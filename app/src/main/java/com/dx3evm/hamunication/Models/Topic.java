package com.dx3evm.hamunication.Models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;

public class Topic implements Serializable {
    private String topicID, topicTitle, topicDescription;
    private Map<String, Map<String, String>> urlList;

    public Topic(){}
    public Topic(String topicID, String topicTitle, String topicDescription, Map<String, Map<String, String>> urlList) {
        this.topicID = topicID;
        this.topicTitle = topicTitle;
        this.topicDescription = topicDescription;
        this.urlList = urlList;
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

    public Map<String, Map<String, String>> getUrlList() {
        return urlList;
    }

    public void setUrlList(Map<String, Map<String, String>> urlList) {
        this.urlList = urlList;
    }
}

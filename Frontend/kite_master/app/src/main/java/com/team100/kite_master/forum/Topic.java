package com.team100.kite_master.forum;

public class Topic {

    private String topicName;
    private String topicDescription;

    public Topic(String name, String description){
        topicName = name;
        topicDescription = description;
    }

    public String getName(){
        return topicName;
    }

    public String getDescription(){
        return topicDescription;
    }
}

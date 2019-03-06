package com.team100.kite_master.forum;

public class Topic implements Comparable<Topic>{

    private String topicName;
    private String topicDescription;
    private String topicID;

    //constructor for topics
    public Topic(String name, String description) {
        topicID = name;
        topicName = formatName(name);
        topicDescription = description;

    }
    //returns topic id (unformatted name)
    public String getTopicID() { return topicID; }

    //returns topic name (formatted)
    public String getName() { return topicName; }

    //returns description of topic
    public String getDescription() {
        return topicDescription;
    }

    //overrides compare to to allow alphabetical sorting of the topic list
    @Override
    public int compareTo(Topic comparetopic) {
        int cmp = this.getName().compareTo(comparetopic.getName());
        return cmp;
    }

    //returns a string containing the topic id and description
    public String toString(){
        return "|" + topicID+" -> "+topicDescription+"|";
    }

    //formats the name so only the first letter of each word is uppercase
    private String formatName(String name) {
        String str = name.toLowerCase();
        StringBuilder s = new StringBuilder(str.length());
        String words[] = str.split(" ");
        for (int i = 0; i < words.length; i++) {
            s.append(Character.toUpperCase(words[i].charAt(0))).append(words[i].substring(1)).append(" ");
        }
        return s.toString();
    }

}

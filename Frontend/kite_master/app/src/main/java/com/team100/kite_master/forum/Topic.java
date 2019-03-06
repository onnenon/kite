package com.team100.kite_master.forum;

public class Topic {

    private String topicName;
    private String topicDescription;
    private String topicID;

    public Topic(String name, String description) {
        topicID = name;
        topicName = formatName(name);
        topicDescription = description;

    }

    public String getTopicID() {
        return topicID;
    }

    public String getName() {
        return topicName;
    }


    public String getDescription() {
        return topicDescription;
    }


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

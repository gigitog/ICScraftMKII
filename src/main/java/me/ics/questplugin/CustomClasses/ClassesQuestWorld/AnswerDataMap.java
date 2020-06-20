package me.ics.questplugin.CustomClasses.ClassesQuestWorld;

import java.util.HashMap;
import java.util.Map;

public class AnswerDataMap {
    private Map<Integer, String> map;

    public AnswerDataMap() {
        this.map = new HashMap<>();
    }

    public void add(int checkpoint, String answer){
        map.put(checkpoint, answer);
    }

    public String getAnswer(int checkpoint){
        return map.get(checkpoint);
    }
}

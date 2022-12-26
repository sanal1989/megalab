package com.example.megalab.entity;

import com.fasterxml.jackson.annotation.JsonValue;

public enum Rubric {
    SPORT("Спорт");

    private String value;

    @JsonValue
    public String getValue(){
        return value;
    }

    Rubric(String value) {
        this.value = value;
    }

    public static Rubric getRubric(String string){
        Rubric[] arrayRubric = Rubric.values();
        for (Rubric i: arrayRubric) {
            if(i.getValue().equals(string)) return i;
        }
        return null;
    }
}

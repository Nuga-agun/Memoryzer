package com.nugaterie.memoryzer.data.local;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "question_sets")
public class QuestionSetEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public String name;

    public QuestionSetEntity(String name) {
        this.name = name;
    }
}

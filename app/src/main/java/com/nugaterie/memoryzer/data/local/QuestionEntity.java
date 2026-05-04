package com.nugaterie.memoryzer.data.local;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "questions",
        foreignKeys = @ForeignKey(entity = QuestionSetEntity.class,
                parentColumns = "id",
                childColumns = "setId",
                onDelete = ForeignKey.CASCADE))
public class QuestionEntity {
    @PrimaryKey(autoGenerate = true)
    public long id;
    public long setId;
    public String questionText;
    public String answer;

    public QuestionEntity(long setId, String questionText, String answer) {
        this.setId = setId;
        this.questionText = questionText;
        this.answer = answer;
    }
}

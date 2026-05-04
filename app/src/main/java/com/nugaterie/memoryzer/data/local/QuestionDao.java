package com.nugaterie.memoryzer.data.local;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface QuestionDao {
    @Insert
    long insertSet(QuestionSetEntity set);

    @Insert
    void insertQuestion(QuestionEntity question);

    @Query("SELECT * FROM question_sets")
    LiveData<List<QuestionSetEntity>> getAllSets();

    @Query("SELECT * FROM questions WHERE setId = :setId")
    List<QuestionEntity> getQuestionsForSet(long setId);

    @Query("DELETE FROM question_sets WHERE id = :setId")
    void deleteSet(long setId);
}

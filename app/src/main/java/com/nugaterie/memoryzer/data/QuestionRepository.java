package com.nugaterie.memoryzer.data;

import androidx.lifecycle.LiveData;

import com.nugaterie.memoryzer.data.local.QuestionDao;
import com.nugaterie.memoryzer.data.local.QuestionEntity;
import com.nugaterie.memoryzer.data.local.QuestionSetEntity;

import java.util.ArrayList;
import java.util.List;

public class QuestionRepository {

    private final QuestionBank questionBank;
    private final QuestionDao questionDao;

    public QuestionRepository(QuestionBank questionBank, QuestionDao questionDao) {
        this.questionBank = questionBank;
        this.questionDao = questionDao;
    }

    public List<Question> getDefaultQuestions() {
        return questionBank.getDefaultQuestions();
    }

    public LiveData<List<QuestionSetEntity>> getAllCustomSets() {
        return questionDao.getAllSets();
    }

    public List<Question> getQuestionsForSet(long setId) {
        List<QuestionEntity> entities = questionDao.getQuestionsForSet(setId);
        List<Question> questions = new ArrayList<>();
        for (QuestionEntity entity : entities) {
            questions.add(new Question(entity.questionText, entity.answer));
        }
        return questions;
    }

    public void createSet(String name, List<Question> questions) {
        // This should be done on a background thread in a real app
        new Thread(() -> {
            long setId = questionDao.insertSet(new QuestionSetEntity(name));
            for (Question q : questions) {
                questionDao.insertQuestion(new QuestionEntity(setId, q.getQuestionText(), q.getAnswer()));
            }
        }).start();
    }

    public void deleteSet(long setId) {
        new Thread(() -> questionDao.deleteSet(setId)).start();
    }
}

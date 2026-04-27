package com.nugaterie.memoryzer.ui.quiz;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.data.QuestionRepository;
import com.nugaterie.memoryzer.injection.ViewModelFactory;

import java.util.List;

public class QuizViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private List<Question> questionList;

    MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    MutableLiveData<Integer> score = new MutableLiveData<>();
    MutableLiveData<Integer> questionIndex = new MutableLiveData<>();
    MutableLiveData<Integer> numberOfQuestions = new MutableLiveData<>();

    public QuizViewModel (QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }


    public void StartQuiz() {
        questionList = questionRepository.getDefaultQuestions();
        numberOfQuestions.postValue(questionList.size());
        questionIndex.postValue(0);
        currentQuestion.postValue(questionList.get(0));
    }

    public void nextQuestion() {
        Integer current = questionIndex.getValue();
        if (current == null || current >= questionList.size() - 1) {
            return;
        }
        current++;
        questionIndex.postValue(current);
        currentQuestion.postValue(questionList.get(current));
    }

    public void previousQuestion() {
        Integer current = questionIndex.getValue();
        if (current == null || current <= 0) {
            return;
        }
        current--;
        questionIndex.postValue(current);
        currentQuestion.postValue(questionList.get(current));
    }

    public boolean isAnswerValid(String answer) {
        Integer index = questionIndex.getValue();
        if (index == null || questionList.get(index) == null || answer == null) {
            return false;
        }
        return questionList.get(index).getAnswer().equalsIgnoreCase(answer);
    }
}

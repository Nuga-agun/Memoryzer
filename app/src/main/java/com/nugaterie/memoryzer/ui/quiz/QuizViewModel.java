package com.nugaterie.memoryzer.ui.quiz;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.data.QuestionRepository;

import java.util.List;

public class QuizViewModel extends ViewModel {
    private final QuestionRepository questionRepository;
    private List<Question> questionList;

    MutableLiveData<Question> currentQuestion = new MutableLiveData<>();
    MutableLiveData<Integer> questionIndex = new MutableLiveData<>();
    MutableLiveData<Integer> numberOfQuestions = new MutableLiveData<>();

    public QuizViewModel (QuestionRepository questionRepository) {
        this.questionRepository = questionRepository;
    }

    public void StartQuiz(long setId) {
        new Thread(() -> {
            if (setId == -1) {
                questionList = questionRepository.getDefaultQuestions();
            } else {
                questionList = questionRepository.getQuestionsForSet(setId);
            }
            
            if (questionList != null && !questionList.isEmpty()) {
                numberOfQuestions.postValue(questionList.size());
                questionIndex.postValue(0);
                currentQuestion.postValue(questionList.get(0));
            }
        }).start();
    }

    public void nextQuestion() {
        Integer current = questionIndex.getValue();
        if (current == null || questionList == null || current >= questionList.size() - 1) {
            return;
        }
        current++;
        questionIndex.postValue(current);
        currentQuestion.postValue(questionList.get(current));
    }

    public void previousQuestion() {
        Integer current = questionIndex.getValue();
        if (current == null || questionList == null || current <= 0) {
            return;
        }
        current--;
        questionIndex.postValue(current);
        currentQuestion.postValue(questionList.get(current));
    }

    public boolean isAnswerValid(String answer) {
        Integer index = questionIndex.getValue();
        if (index == null || questionList == null || questionList.get(index) == null || answer == null) {
            return false;
        }
        return questionList.get(index).getAnswer().equalsIgnoreCase(answer);
    }
}

package com.nugaterie.memoryzer.ui.create;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.data.QuestionRepository;

import java.util.ArrayList;
import java.util.List;

public class CreateSetViewModel extends ViewModel {
    private final QuestionRepository repository;

    private final MutableLiveData<List<Question>> _questions = new MutableLiveData<>(new ArrayList<>());
    public LiveData<List<Question>> questions = _questions;

    private final MutableLiveData<Integer> _currentIndex = new MutableLiveData<>(-1);
    public LiveData<Integer> currentIndex = _currentIndex;

    private final MutableLiveData<Boolean> _isSaveEnabled = new MutableLiveData<>(false);
    public LiveData<Boolean> isSaveEnabled = _isSaveEnabled;

    private String currentSetName = "";

    public CreateSetViewModel(QuestionRepository repository) {
        this.repository = repository;
    }

    public void setSetName(String name) {
        this.currentSetName = name;
        updateSaveEnabled();
    }

    public void addQuestion(String text, String answer) {
        List<Question> currentList = new ArrayList<>(_questions.getValue());
        currentList.add(new Question(text, answer));
        _questions.setValue(currentList);
        _currentIndex.setValue(-1);
        updateSaveEnabled();
    }

    public void nextQuestion() {
        Integer index = _currentIndex.getValue();
        List<Question> currentList = _questions.getValue();
        if (index != null && currentList != null) {
            if (index < currentList.size() - 1) {
                _currentIndex.setValue(index + 1);
            } else {
                _currentIndex.setValue(-1);
            }
        }
    }

    public void previousQuestion() {
        Integer index = _currentIndex.getValue();
        List<Question> currentList = _questions.getValue();
        if (index != null && currentList != null) {
            if (index == -1) {
                if (!currentList.isEmpty()) {
                    _currentIndex.setValue(currentList.size() - 1);
                }
            } else if (index > 0) {
                _currentIndex.setValue(index - 1);
            }
        }
    }

    private void updateSaveEnabled() {
        List<Question> currentList = _questions.getValue();
        boolean hasName = currentSetName != null && !currentSetName.trim().isEmpty();
        boolean hasQuestions = currentList != null && !currentList.isEmpty();
        _isSaveEnabled.setValue(hasName && hasQuestions);
    }

    public void saveSet() {
        List<Question> currentList = _questions.getValue();
        if (currentList != null && !currentList.isEmpty() && !currentSetName.isEmpty()) {
            repository.createSet(currentSetName, currentList);
        }
    }
}

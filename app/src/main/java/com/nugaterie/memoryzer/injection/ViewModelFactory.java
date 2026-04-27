package com.nugaterie.memoryzer.injection;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.data.QuestionBank;
import com.nugaterie.memoryzer.data.QuestionRepository;
import com.nugaterie.memoryzer.ui.quiz.QuizViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance() {
        synchronized (ViewModelFactory.class) {
            if (factory == null) {
                factory = new ViewModelFactory();
            }
        }
        return factory;
    }

    private ViewModelFactory() {
        QuestionBank questionBank = QuestionBank.getInstance();
        this.questionRepository = new QuestionRepository(questionBank);
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create (Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QuizViewModel.class)) {
            return (T) new QuizViewModel(questionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

package com.nugaterie.memoryzer.injection;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.data.QuestionBank;
import com.nugaterie.memoryzer.data.QuestionRepository;
import com.nugaterie.memoryzer.data.local.AppDatabase;
import com.nugaterie.memoryzer.ui.create.CreateSetViewModel;
import com.nugaterie.memoryzer.ui.quiz.QuizViewModel;
import com.nugaterie.memoryzer.ui.welcome.WelcomeViewModel;

public class ViewModelFactory implements ViewModelProvider.Factory {
    private final QuestionRepository questionRepository;
    private static ViewModelFactory factory;

    public static ViewModelFactory getInstance(Context context) {
        synchronized (ViewModelFactory.class) {
            if (factory == null) {
                factory = new ViewModelFactory(context.getApplicationContext());
            }
        }
        return factory;
    }

    private ViewModelFactory(Context context) {
        QuestionBank questionBank = QuestionBank.getInstance();
        AppDatabase database = AppDatabase.getDatabase(context);
        this.questionRepository = new QuestionRepository(questionBank, database.questionDao());
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create (Class<T> modelClass) {
        if (modelClass.isAssignableFrom(QuizViewModel.class)) {
            return (T) new QuizViewModel(questionRepository);
        }
        if (modelClass.isAssignableFrom(CreateSetViewModel.class)) {
            return (T) new CreateSetViewModel(questionRepository);
        }
        if (modelClass.isAssignableFrom(WelcomeViewModel.class)) {
            return (T) new WelcomeViewModel(questionRepository);
        }
        throw new IllegalArgumentException("Unknown ViewModel class");
    }
}

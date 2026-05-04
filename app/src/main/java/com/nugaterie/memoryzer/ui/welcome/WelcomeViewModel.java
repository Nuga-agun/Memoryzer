package com.nugaterie.memoryzer.ui.welcome;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.nugaterie.memoryzer.data.QuestionRepository;
import com.nugaterie.memoryzer.data.local.QuestionSetEntity;

import java.util.List;

public class WelcomeViewModel extends ViewModel {
    private final QuestionRepository repository;

    private final MutableLiveData<Long> _selectedSetId = new MutableLiveData<>(-1L);
    public LiveData<Long> selectedSetId = _selectedSetId;

    public WelcomeViewModel(QuestionRepository repository) {
        this.repository = repository;
    }

    public LiveData<List<QuestionSetEntity>> getCustomSets() {
        return repository.getAllCustomSets();
    }

    public void selectSet(long setId) {
        _selectedSetId.setValue(setId);
    }

    public void deleteSelectedSet() {
        Long id = _selectedSetId.getValue();
        if (id != null && id != -1L) {
            repository.deleteSet(id);
            _selectedSetId.setValue(-1L);
        }
    }
}

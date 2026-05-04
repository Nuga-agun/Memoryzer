package com.nugaterie.memoryzer.ui.create;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.R;
import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.databinding.FragmentCreateSetBinding;
import com.nugaterie.memoryzer.injection.ViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class CreateSetFragment extends Fragment {

    private FragmentCreateSetBinding binding;
    private CreateSetViewModel viewModel;

    public static CreateSetFragment newInstance() {
        return new CreateSetFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(CreateSetViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCreateSetBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.etSetName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                viewModel.setSetName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        TextWatcher fieldWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                updateAddButtonState();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        };
        binding.etQuestion.addTextChangedListener(fieldWatcher);
        binding.etAnswer.addTextChangedListener(fieldWatcher);

        binding.btnPrevious.setOnClickListener(v -> viewModel.previousQuestion());

        binding.btnAdd.setOnClickListener(v -> {
            Integer index = viewModel.currentIndex.getValue();
            if (index != null && index == -1) {
                String q = binding.etQuestion.getText() != null ? binding.etQuestion.getText().toString() : "";
                String a = binding.etAnswer.getText() != null ? binding.etAnswer.getText().toString() : "";
                if (q.isEmpty() || a.isEmpty()) {
                    Toast.makeText(requireContext(), "Please fill both question and answer", Toast.LENGTH_SHORT).show();
                    return;
                }
                viewModel.addQuestion(q, a);
            } else {
                viewModel.nextQuestion();
            }
        });

        binding.btnSaveSet.setOnClickListener(v -> {
            viewModel.saveSet();
            Toast.makeText(requireContext(), "Set saved!", Toast.LENGTH_SHORT).show();
            getParentFragmentManager().popBackStack();
        });

        viewModel.currentIndex.observe(getViewLifecycleOwner(), index -> {
            if (index == -1) {
                binding.etQuestion.setText("");
                binding.etAnswer.setText("");
                binding.btnAdd.setText(R.string.add);
                binding.etQuestion.setEnabled(true);
                binding.etAnswer.setEnabled(true);
                binding.etQuestion.requestFocus();
            } else {
                List<Question> questions = viewModel.questions.getValue();
                if (questions != null && index < questions.size()) {
                    Question q = questions.get(index);
                    binding.etQuestion.setText(q.getQuestionText());
                    binding.etAnswer.setText(q.getAnswer());
                    binding.btnAdd.setText(R.string.next);
                    binding.etQuestion.setEnabled(false);
                    binding.etAnswer.setEnabled(false);
                }
            }
            updatePreviousButtonState();
            updateAddButtonState();
        });

        viewModel.questions.observe(getViewLifecycleOwner(), questions -> {
            updatePreviousButtonState();
        });

        viewModel.isSaveEnabled.observe(getViewLifecycleOwner(), enabled -> {
            binding.btnSaveSet.setEnabled(enabled);
        });
    }

    private void updatePreviousButtonState() {
        Integer index = viewModel.currentIndex.getValue();
        List<Question> questions = viewModel.questions.getValue();
        if (index != null && questions != null) {
            if (index == -1) {
                binding.btnPrevious.setEnabled(!questions.isEmpty());
            } else {
                binding.btnPrevious.setEnabled(index > 0);
            }
        }
    }

    private void updateAddButtonState() {
        Integer index = viewModel.currentIndex.getValue();
        if (index != null && index == -1) {
            String q = binding.etQuestion.getText() != null ? binding.etQuestion.getText().toString() : "";
            String a = binding.etAnswer.getText() != null ? binding.etAnswer.getText().toString() : "";
            binding.btnAdd.setEnabled(!q.trim().isEmpty() && !a.trim().isEmpty());
        } else {
            binding.btnAdd.setEnabled(true);
        }
    }
}

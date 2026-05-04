package com.nugaterie.memoryzer.ui.quiz;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.R;
import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.databinding.FragmentQuizBinding;
import com.nugaterie.memoryzer.injection.ViewModelFactory;
import com.nugaterie.memoryzer.ui.welcome.WelcomeFragment;

public class QuizFragment extends Fragment {
    private static final String ARG_SET_ID = "set_id";
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance(long setId) {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        args.putLong(ARG_SET_ID, setId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(QuizViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentQuizBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        long setId = -1;
        if (getArguments() != null) {
            setId = getArguments().getLong(ARG_SET_ID, -1);
        }
        
        viewModel.StartQuiz(setId);
        viewModel.currentQuestion.observe(getViewLifecycleOwner(), this::updateQuestion);
        
        binding.btnSubmit.setOnClickListener(v -> {
            String answer = binding.answer.getText() != null ? binding.answer.getText().toString() : "";
            if (viewModel.isAnswerValid(answer)) {
                binding.tvFeedback.setText(R.string.correct);
                binding.tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
                binding.btnNext.setEnabled(true);
            } else {
                binding.tvFeedback.setText(R.string.wrong);
                binding.tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_red_dark, null));
            }
            binding.tvFeedback.setVisibility(View.VISIBLE);
        });
        
        binding.btnNext.setOnClickListener(v -> viewModel.nextQuestion());
        binding.btnPrevious.setOnClickListener(v -> viewModel.previousQuestion());
    }

    private void updateQuestion(Question question) {
        if (question != null) {
            binding.question.setText(question.getQuestionText());
            binding.answer.setText("");
            binding.tvFeedback.setVisibility(View.INVISIBLE);
            binding.btnNext.setEnabled(false);
        }
        
        Integer numberOfQuestions = viewModel.numberOfQuestions.getValue();
        Integer questionIndex = viewModel.questionIndex.getValue();
        
        if (numberOfQuestions != null && questionIndex != null && numberOfQuestions == questionIndex + 1) {
            binding.btnNext.setText(R.string.finish);
            binding.btnNext.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WelcomeFragment welcomeFragment = WelcomeFragment.newInstance();
                fragmentTransaction.replace(R.id.fragmentContainerView, welcomeFragment);
                fragmentTransaction.commit();
            });
        } else {
            binding.btnNext.setText(R.string.next);
            binding.btnNext.setOnClickListener(v -> viewModel.nextQuestion());
        }
    }
}

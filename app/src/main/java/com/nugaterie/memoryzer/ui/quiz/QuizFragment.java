package com.nugaterie.memoryzer.ui.quiz;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nugaterie.memoryzer.R;
import com.nugaterie.memoryzer.data.Question;
import com.nugaterie.memoryzer.databinding.FragmentQuizBinding;
import com.nugaterie.memoryzer.injection.ViewModelFactory;
import com.nugaterie.memoryzer.ui.welcome.WelcomeFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link QuizFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class QuizFragment extends Fragment {
    private QuizViewModel viewModel;
    private FragmentQuizBinding binding;

    public QuizFragment() {
        // Required empty public constructor
    }

    public static QuizFragment newInstance() {
        QuizFragment fragment = new QuizFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance()).get(QuizViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_quiz, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        binding = FragmentQuizBinding.bind(view);
        super.onViewCreated(view, savedInstanceState);
        viewModel.StartQuiz();
        viewModel.currentQuestion.observe(getViewLifecycleOwner(), this::updateQuestion);
        binding.btnSubmit.setOnClickListener(v -> {
            if (viewModel.isAnswerValid(binding.answer.getText() == null ? null : binding.answer.getText().toString())) {
                binding.tvFeedback.setText(R.string.correct);
                binding.tvFeedback.announceForAccessibility("Correct answer !");
                binding.tvFeedback.setTextColor(getResources().getColor(android.R.color.holo_green_dark, null));
                binding.btnNext.setEnabled(true);
            } else {
                binding.tvFeedback.setText(R.string.wrong);
                binding.tvFeedback.announceForAccessibility("Wrong answer !");
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
        if (numberOfQuestions != null && questionIndex != null && numberOfQuestions == questionIndex+1) {
            binding.btnNext.setText(R.string.finish);
            binding.btnNext.setOnClickListener(v -> {
                FragmentManager fragmentManager = getParentFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                WelcomeFragment welcomeFragment = WelcomeFragment.newInstance();
                fragmentTransaction.add(R.id.fragmentContainerView, welcomeFragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.detach(this);
                fragmentTransaction.commit();
            });
        }
    }
}
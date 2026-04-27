package com.nugaterie.memoryzer.ui.welcome;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nugaterie.memoryzer.R;
import com.nugaterie.memoryzer.databinding.FragmentWelcomeBinding;
import com.nugaterie.memoryzer.ui.quiz.QuizFragment;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link WelcomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class WelcomeFragment extends Fragment {

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance() {
        WelcomeFragment fragment = new WelcomeFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_welcome, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        FragmentWelcomeBinding binding = FragmentWelcomeBinding.bind(view);
        binding.newQuiz.setOnClickListener(v -> {
            //TODO : Ecran de création d'un quizz
        });
        binding.setDefault.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            QuizFragment quizFragment = QuizFragment.newInstance();
            fragmentTransaction.add(R.id.fragmentContainerView, quizFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.detach(this);
            fragmentTransaction.commit();
        });
    }
}
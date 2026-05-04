package com.nugaterie.memoryzer.ui.welcome;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;

import com.nugaterie.memoryzer.R;
import com.nugaterie.memoryzer.data.local.QuestionSetEntity;
import com.nugaterie.memoryzer.databinding.FragmentWelcomeBinding;
import com.nugaterie.memoryzer.injection.ViewModelFactory;
import com.nugaterie.memoryzer.ui.create.CreateSetFragment;
import com.nugaterie.memoryzer.ui.quiz.QuizFragment;

import java.util.List;

public class WelcomeFragment extends Fragment {

    private FragmentWelcomeBinding binding;
    private WelcomeViewModel viewModel;

    public WelcomeFragment() {
        // Required empty public constructor
    }

    public static WelcomeFragment newInstance() {
        return new WelcomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this, ViewModelFactory.getInstance(requireContext())).get(WelcomeViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentWelcomeBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        binding.btnCreateSet.setOnClickListener(v -> {
            FragmentManager fragmentManager = getParentFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            CreateSetFragment createSetFragment = CreateSetFragment.newInstance();
            fragmentTransaction.replace(R.id.fragmentContainerView, createSetFragment);
            fragmentTransaction.addToBackStack(null);
            fragmentTransaction.commit();
        });

        binding.btnRemove.setOnClickListener(v -> viewModel.deleteSelectedSet());

        viewModel.getCustomSets().observe(getViewLifecycleOwner(), this::updateSetButtons);
        
        viewModel.selectedSetId.observe(getViewLifecycleOwner(), selectedId -> {
            if (selectedId == -1L) {
                binding.btnRemove.setVisibility(View.GONE);
            } else {
                binding.btnRemove.setVisibility(View.VISIBLE);
            }
            updateButtonsAppearance(selectedId);
        });
    }

    private void updateSetButtons(List<QuestionSetEntity> sets) {
        binding.rvSquares.removeAllViews();
        binding.rvSquares.addView(binding.setsLayout);

        int[] ids = new int[sets.size()];
        for (int i = 0; i < sets.size(); i++) {
            QuestionSetEntity set = sets.get(i);
            Button btn = new Button(requireContext());
            btn.setId(View.generateViewId());
            btn.setText(set.name);
            btn.setTag(set.id); // Store ID to find it later
            
            btn.setOnClickListener(v -> {
                if (viewModel.selectedSetId.getValue() != -1L) {
                    viewModel.selectSet(-1L); // Deselect if clicking another one
                } else {
                    startQuiz(set.id);
                }
            });
            
            btn.setOnLongClickListener(v -> {
                viewModel.selectSet(set.id);
                return true;
            });
            
            binding.rvSquares.addView(btn);
            ids[i] = btn.getId();
        }
        
        binding.setsLayout.setReferencedIds(ids);
        Long selectedId = viewModel.selectedSetId.getValue();
        updateButtonsAppearance(selectedId != null ? selectedId : -1L);
    }

    private void updateButtonsAppearance(long selectedId) {
        for (int i = 0; i < binding.rvSquares.getChildCount(); i++) {
            View child = binding.rvSquares.getChildAt(i);
            if (child instanceof Button && child.getTag() instanceof Long) {
                long setId = (long) child.getTag();
                if (setId == selectedId) {
                    child.setAlpha(0.5f);
                } else {
                    child.setAlpha(1.0f);
                }
            }
        }
    }

    private void startQuiz(long setId) {
        FragmentManager fragmentManager = getParentFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        QuizFragment quizFragment = QuizFragment.newInstance(setId);
        fragmentTransaction.replace(R.id.fragmentContainerView, quizFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}

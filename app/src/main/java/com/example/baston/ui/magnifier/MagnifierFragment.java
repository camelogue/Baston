package com.example.baston.ui.magnifier;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.baston.databinding.FragmentMagnifierBinding;

public class MagnifierFragment extends Fragment {

    private FragmentMagnifierBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        MagnifierViewModel galleryViewModel =
                new ViewModelProvider(this).get(MagnifierViewModel.class);

        binding = FragmentMagnifierBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textMagnifier;
        galleryViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
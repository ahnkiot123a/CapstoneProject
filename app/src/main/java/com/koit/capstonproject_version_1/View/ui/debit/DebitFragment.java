package com.koit.capstonproject_version_1.View.ui.debit;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.koit.capstonproject_version_1.R;

public class DebitFragment extends Fragment {

    private DebitViewModel debitViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        debitViewModel =
                ViewModelProviders.of(this).get(DebitViewModel.class);
        View root = inflater.inflate(R.layout.fragment_debit, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        debitViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
package com.koit.capstonproject_version_1.View.ui.account;

import android.content.Intent;
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

import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private  TextView tvNameProfile;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
    //    final TextView textView = root.findViewById(R.id.text_notifications);
       tvNameProfile = root.findViewById(R.id.tvNameProfile);

        return root;
    }
}
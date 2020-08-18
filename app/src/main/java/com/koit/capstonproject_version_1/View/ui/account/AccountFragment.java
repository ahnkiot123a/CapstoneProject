package com.koit.capstonproject_version_1.View.ui.account;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.koit.capstonproject_version_1.Model.User;
import com.koit.capstonproject_version_1.R;
import com.koit.capstonproject_version_1.dao.UserDAO;

import de.hdodenhof.circleimageview.CircleImageView;

public class AccountFragment extends Fragment {

    private AccountViewModel accountViewModel;
    private TextView tvNameProfile;
    private FirebaseUser currentUser;
    private CircleImageView profile_img;
    private Button btnAccountInfo;
    private Button btnChangePassword;
    private UserDAO userDAO;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        accountViewModel =
                ViewModelProviders.of(this).get(AccountViewModel.class);
        View root = inflater.inflate(R.layout.fragment_account, container, false);
        //    final TextView textView = root.findViewById(R.id.text_notifications);
        tvNameProfile = root.findViewById(R.id.tvNameProfile);
        profile_img = root.findViewById(R.id.profile_img);
//        User user = (User)getActivity().getIntent().getSerializableExtra("currentUser");
        userDAO = new UserDAO();
        User user = userDAO.getUser();
        currentUser = FirebaseAuth.getInstance().getCurrentUser();
        btnAccountInfo = root.findViewById(R.id.accountInfo);
        btnChangePassword = root.findViewById(R.id.changePassword);
        // tvNameProfile.setText(currentUser.getDisplayName());
        if (user != null && currentUser == null) tvNameProfile.setText(user.getFullName());
        else if (currentUser != null) {
            tvNameProfile.setText(currentUser.getDisplayName());
            Glide.with(this).load(currentUser.getPhotoUrl()).into(profile_img);
            Log.d("kiemtra", currentUser.getEmail());

            btnAccountInfo.setEnabled(false);
            btnChangePassword.setEnabled(false);
        }


        return root;
    }


}
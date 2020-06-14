package com.koit.capstonproject_version_1.View.ui.account;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AccountViewModel() {
        mText = new MutableLiveData<>();
       // mText.setValue("Đây là cá nhân");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
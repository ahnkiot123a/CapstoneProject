package com.koit.capstonproject_version_1.view.ui.debit;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DebitViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public DebitViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Đây là sổ nợ");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
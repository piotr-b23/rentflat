package com.example.rentflat.ui.myAccount;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyAccountViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyAccountViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Zmień dane swojego konta");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
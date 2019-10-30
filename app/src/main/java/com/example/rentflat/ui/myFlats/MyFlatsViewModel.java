package com.example.rentflat.ui.myFlats;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MyFlatsViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public MyFlatsViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("Przeglądaj swoje ogłoszenia");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
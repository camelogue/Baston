package com.example.baston.ui.magnifier;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MagnifierViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public MagnifierViewModel() {
        mText = new MutableLiveData<>();
//        mText.setValue("BUYUTEC SAYFASI");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
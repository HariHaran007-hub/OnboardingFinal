package com.example.onboarding.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class UserViewModel extends ViewModel {
    private final MutableLiveData<User> userMutableLiveData = new MutableLiveData<>();

    public LiveData<User> getUserLiveData(){
        return userMutableLiveData;
    }

    public void setUserLiveData(User user){
        userMutableLiveData.setValue(user);
    }
}

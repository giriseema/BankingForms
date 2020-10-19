package com.giriseematechme.bankingforms.ViewModel;

import android.app.Application;
import android.util.Log;


import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.giriseematechme.bankingforms.Repository.LoginRepository;
import com.giriseematechme.bankingforms.RoomDatabase.LoginTable;
import com.giriseematechme.bankingforms.utils.AESUtils;

import java.util.List;

public class LoginViewModel extends AndroidViewModel {

    private LoginRepository repository;
    private LiveData<List<LoginTable>> getAllData;

    public LoginViewModel(@NonNull Application application) {
        super(application);

        repository = new LoginRepository(application);
        getAllData = repository.getAllData();

    }

    public void insert(LoginTable data) {
        repository.insertData(data);
    }

    public LiveData<List<LoginTable>> getGetAllData() {
        return getAllData;
    }

    public int checkUserExist(String userName, String password) {
        try {
            password = AESUtils.encrypt(password);
            Log.d("TEST", "encrypted:" + password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return repository.checkUserExist(userName,password);
    }

    public int checkUserAlreadyExist(String userName) {
        return repository.checkUserAlreadyExist(userName);
    }

}


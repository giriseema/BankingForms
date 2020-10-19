package com.giriseematechme.bankingforms.Repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.giriseematechme.bankingforms.RoomDatabase.LoginDao;
import com.giriseematechme.bankingforms.RoomDatabase.LoginDatabase;
import com.giriseematechme.bankingforms.RoomDatabase.LoginTable;

import java.util.List;

public class LoginRepository {

    private LoginDao loginDao;
    private LiveData<List<LoginTable>> allData;

    public LoginRepository(Application application) {
        LoginDatabase db = LoginDatabase.getDatabase(application);
        loginDao = db.loginDao();
        allData = loginDao.getDetails();

    }

    public void deleteData() {
        loginDao.deleteAllData();
    }

    public LiveData<List<LoginTable>> getAllData() {
        return allData;
    }

    public void insertData(LoginTable data) {
        new LoginInsertion(loginDao).execute(data);
    }

    public int checkUserExist(String userName, String password) {
        return loginDao.checkUserExist(userName,password);
    }

    public int checkUserAlreadyExist(String userName) {
       return loginDao.checkUserAlreadyExist(userName);
    }

    private static class LoginInsertion extends AsyncTask<LoginTable, Void, Void> {

        private LoginDao loginDao;

        private LoginInsertion(LoginDao loginDao) {

            this.loginDao = loginDao;

        }

        @Override
        protected Void doInBackground(LoginTable... data) {

            loginDao.deleteAllData();

            loginDao.insertDetails(data[0]);
            return null;

        }

    }

}

package com.giriseematechme.bankingforms.RoomDatabase;


import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface LoginDao {

    @Insert
    void insertDetails(LoginTable data);

    @Query("select * from LoginDetails")
    LiveData<List<LoginTable>> getDetails();

    @Query("delete from LoginDetails")
    void deleteAllData();

    @Delete
    void deleteUserRecord(LoginTable data);

    @Query("SELECT COUNT(Email) from LoginDetails where Email = :userName and Password=:password")
    int checkUserExist(String userName, String password);

    @Query("SELECT COUNT(Email) from LoginDetails where Email = :userName")
    int checkUserAlreadyExist(String userName);
}

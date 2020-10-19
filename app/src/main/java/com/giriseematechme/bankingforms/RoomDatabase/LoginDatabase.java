package com.giriseematechme.bankingforms.RoomDatabase;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {LoginTable.class,Form.class}, version = 2, exportSchema = false)
public abstract class LoginDatabase extends RoomDatabase {

    public abstract LoginDao loginDao();
    public abstract FormDao formDao();

    private static LoginDatabase INSTANCE;

    public static LoginDatabase getDatabase(final Context context) {

        if (INSTANCE == null) {

            synchronized (LoginDatabase.class) {

                if (INSTANCE == null) {

                    INSTANCE = Room.databaseBuilder(
                            context, LoginDatabase.class, "LOGIN_DATABASE")
                            .fallbackToDestructiveMigration()
                            .build();

                }

            }

        }

        return INSTANCE;

    }

}

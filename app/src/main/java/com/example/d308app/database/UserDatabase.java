package com.example.d308app.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.d308app.entities.User;
import com.example.d308app.dao.UserDAO;


@Database(entities = {User.class}, version = 4)
public abstract class UserDatabase extends RoomDatabase {
    private static final String dbName = "user";
    private static UserDatabase userDatabase;

    public static synchronized UserDatabase getUserDatabase(Context context) {
        if (userDatabase == null) {
            userDatabase = Room.databaseBuilder(context.getApplicationContext(), UserDatabase.class, dbName)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return userDatabase;
    }

    public abstract UserDAO userDao();

}

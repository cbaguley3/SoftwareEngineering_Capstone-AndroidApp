package com.example.d308app.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.d308app.entities.User;

@Dao
public interface UserDAO {
    @Insert
    void registerUser(User user);
    @Query("SELECT * FROM users WHERE userName=(:userName) and password =(:password)")
    User login(String userName, String password);
}

package com.example.d308app.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308app.entities.User;

@Dao
public interface UserDAO {

    @Insert
    void registerUser(User user);

    @Query("SELECT * FROM users WHERE userName = :userName AND password = :password")
    User login(String userName, String password);

//  Testing code

    @Query("SELECT * FROM users WHERE id = :id")
    User getUserById(int id);

    @Update
    void update(User user);
}

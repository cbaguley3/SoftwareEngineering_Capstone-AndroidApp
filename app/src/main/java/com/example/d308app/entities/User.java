package com.example.d308app.entities;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

@Entity(tableName= "users")
public class User {
    @PrimaryKey(autoGenerate = true)
    Integer id;

    @ColumnInfo(name = "userName")
    String userName;

    @ColumnInfo(name = "password")
    String password;

    @ColumnInfo(name = "name")
    String name;

    @Ignore
    public User() {}

    public User(int id, String userName, String password) {
        this.id = id;
        this.userName = userName;
        this.password = password;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

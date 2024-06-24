package com.example.d308app.entities;
import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "excursions")
public class Excursion {
    @PrimaryKey(autoGenerate = true)
    private int excursionID;
    private String excursionName;
    private String date;
    private int vacationID;


    //Constructor
    public Excursion(int excursionID, String excursionName, String date, int vacationID) {
        this.excursionID = excursionID;
        this.excursionName = excursionName;
        this.date = date;
        this.vacationID = vacationID;

    }

    //Getters and Setters

    public int getExcursionID() {
        return excursionID;
    }

    public void setExcursionID(int excursionID) {
        this.excursionID = excursionID;
    }

    public String getExcursionName() {
        return excursionName;
    }

    public void setExcursionName(String excursionName) {
        this.excursionName = excursionName;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getVacationID() {
        return vacationID;
    }

    public void setVacationID(int vacationID) {
        this.vacationID = vacationID;
    }

    @NonNull
    public String toString() {
        return date;
    }

    public String getNote() {

        return toString();
    }
}

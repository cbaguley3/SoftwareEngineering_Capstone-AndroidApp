package com.example.d308app.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.d308app.entities.Vacation;

import java.util.List;


@Dao
public interface VacationDAO {
    @Insert(onConflict = OnConflictStrategy.IGNORE) // <<<  database insert issue
    void insert(Vacation vacation);
    @Update
    void update(Vacation vacation);

    @Delete
    void delete (Vacation vacation);

    @Query("SELECT * FROM vacations ORDER BY startDate ASC")
    List<Vacation> getAllVacations();
}

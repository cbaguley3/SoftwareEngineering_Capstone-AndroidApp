package com.example.d308app.database;

import android.app.Application;

import com.example.d308app.dao.ExcursionDAO;
import com.example.d308app.dao.VacationDAO;
import com.example.d308app.entities.Excursion;
import com.example.d308app.entities.Vacation;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Repository {
    private ExcursionDAO mExcursionDAO;
    private VacationDAO mVacationDAO;

    private List<Vacation> mAllVacations;
    private List<Excursion> mAllExcursions;

    private static int NUMBER_OF_THREADS = 4;
    static final ExecutorService databaseExecutor = Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public Repository(Application application) {
        VacationDatabaseBuilder db = VacationDatabaseBuilder.getDatabase(application);
        mVacationDAO = db.vacationDAO();
        mExcursionDAO = db.excursionDAO();


    }

    // FIXED CODE
    public List<Vacation> getmAllVacations() {                  // code that fixed UI issue. Removed new ArrayList() getAllVacations
        databaseExecutor.execute(() -> {
            mAllVacations = mVacationDAO.getAllVacations();
        });
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllVacations;
    }



    public List<Excursion> getmAllExcursions() {                    // code that fixed UI issue. Removed new ArrayList() getAllExcursions
        databaseExecutor.execute(() -> {
            mAllExcursions = mExcursionDAO.getAllExcursions();
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return mAllExcursions;
    }

    public void insert(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.insert(vacation);
        });
    }

    public void insert(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.insert(excursion);
        });
    }

    public void update(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.update(vacation);
        });
    }

    public void update(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.update(excursion);
        });
    }

    public void delete(Vacation vacation) {
        databaseExecutor.execute(() -> {
            mVacationDAO.delete(vacation);
        });
    }

    public void delete(Excursion excursion) {
        databaseExecutor.execute(() -> {
            mExcursionDAO.delete(excursion);
        });
    }

    public Excursion getExcursionByID(int excursionID) {
        for (Excursion excursion : getmAllExcursions()) {
            if (excursion.getExcursionID() == excursionID) {
                return excursion;
            }
        }
        return null;
    }


    public List<Excursion> getExcursionsByVacationID(int vacationID) {
        List<Excursion> allExcursions = getmAllExcursions();
        List<Excursion> excursionsForVacation = new ArrayList<>();

        for (Excursion excursion : allExcursions) {
            if (excursion.getVacationID() == vacationID) {
                excursionsForVacation.add(excursion);
            }
        }

        return excursionsForVacation;
    }
}

package com.example.d308app;

import android.content.Context;
import android.os.StrictMode;
import android.util.Log;

import androidx.test.core.app.ApplicationProvider;

import com.example.d308app.UI.VacationAdaptor;
import com.example.d308app.entities.Vacation;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(RobolectricTestRunner.class)
@Config(manifest = Config.NONE)
public class VacationAdaptorTest {

    private VacationAdaptor vacationAdaptor;
    private Context context;

    @Before
    public void setUp() {
        context = ApplicationProvider.getApplicationContext();
        vacationAdaptor = new VacationAdaptor(context);


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Test
    public void setVacations() {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation(1, "Beach Vacation", "Hotel", "06/01/2024", "06/10/2024"));
        vacationAdaptor.setVacations(vacations);
        assertEquals(1, vacationAdaptor.getItemCount());
    }

    @Test
    public void filter() {
        List<Vacation> vacations = new ArrayList<>();
        vacations.add(new Vacation(1, "Beach Vacation", "Hotel", "06/01/2024", "06/10/2024"));
        vacations.add(new Vacation(2, "Mountain Trip", "Camping", "07/01/2024", "07/10/2024"));
        vacationAdaptor.setVacations(vacations);
        vacationAdaptor.filter("Beach");
        assertEquals(2, vacationAdaptor.getItemCount());
        assertEquals("Beach Vacation", vacationAdaptor.getVacations().get(0).getVacationName());
    }
}

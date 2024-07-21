package com.example.d308app.UI;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308app.R;
import com.example.d308app.database.Repository;
import com.example.d308app.entities.Excursion;
import com.example.d308app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {
    String vacationName;
    String lodging;
    String startDate;
    String endDate;
    int vacationID;
    EditText editName;
    EditText editLodging;
    TextView editStartDate;
    TextView editEndDate;
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    Excursion currentExcursion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);

        vacationName = getIntent().getStringExtra("name");
        lodging = getIntent().getStringExtra("lodging");
        startDate = getIntent().getStringExtra("startDate");
        endDate = getIntent().getStringExtra("endDate");
        vacationID = getIntent().getIntExtra("id", -1);

        editName = findViewById(R.id.vacationName);
        editLodging = findViewById(R.id.lodging);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);

        editName.setText(vacationName);
        editLodging.setText(lodging);
        editStartDate.setText(startDate);
        editEndDate.setText(endDate);

        repository = new Repository(getApplication());

        setupRecyclerView();
        setupFloatingActionButton();
        setupTextWatchers();
    }

    private void setupRecyclerView() {
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdaptor excursionAdapter = new ExcursionAdaptor(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        updateExcursionList(excursionAdapter);
    }

    private void setupFloatingActionButton() {
        FloatingActionButton fab = findViewById(R.id.addExcursions);
        fab.setOnClickListener(view -> {
            if (vacationID == -1) {
                Toast.makeText(VacationDetails.this, "Please save vacation before adding excursions", Toast.LENGTH_LONG).show();
            } else {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });
    }

    private void setupTextWatchers() {
        editStartDate.addTextChangedListener(createDateTextWatcher(editStartDate));
        editEndDate.addTextChangedListener(createDateTextWatcher(editEndDate));
    }

    private TextWatcher createDateTextWatcher(TextView textView) {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!isValidDate(s.toString())) {
                    textView.setError("Invalid date format. Use MM/dd/yy");
                } else {
                    textView.setError(null); // Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        };
    }

    private boolean isValidDate(String date) {
        String datePattern = "^\\d{2}/\\d{2}/\\d{2}$";
        return date.matches(datePattern);
    }

    private boolean isEndDateAfterStartDate(String startDate, String endDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date start = sdf.parse(startDate);
            Date end = sdf.parse(endDate);
            return end.after(start);
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    private void updateExcursionList(ExcursionAdaptor excursionAdapter) {
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) filteredExcursions.add(excursion);
        }
        excursionAdapter.setExcursions(filteredExcursions);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            this.finish();
            return true;
        } else if (id == R.id.vacationsave) {
            saveVacation();
            return true;
        } else if (id == R.id.vacationdelete) {
            deleteVacation();
            return true;
        } else if (id == R.id.addExcursions) {
            if (vacationID == -1) {
                Toast.makeText(VacationDetails.this, "Please save vacation before adding excursions", Toast.LENGTH_LONG).show();
            }
            return true;
        } else if (id == R.id.deleteExcursions) {
            deleteExcursions();
            return true;
        }
        if (item.getItemId() == R.id.share) {
            ArrayList<String> excursionNamesList = new ArrayList<>();
            List<Excursion> excursions = repository.getExcursionsByVacationID(vacationID);

            for (Excursion excursion : excursions) {
                excursionNamesList.add(excursion.getExcursionName());
            }

            StringBuilder excursionsBuilder = new StringBuilder();
            for (String name : excursionNamesList) {
                excursionsBuilder.append(name).append(",\n");
            }
            String excursionsFormatted = excursionsBuilder.toString();

            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String formattedText = String.format("Lodging: %s Date: %s - %s. Excursions: %s", lodging, startDate, endDate, excursionsFormatted );
            sendIntent.putExtra(Intent.EXTRA_TEXT, formattedText);
            sendIntent.putExtra(Intent.EXTRA_TITLE, vacationName);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true; // Added return statement to prevent falling through
        }
        if (item.getItemId() == R.id.notify) {
            String startDateString = editStartDate.getText().toString();
            String endDateString = editEndDate.getText().toString();
            setNotification(startDateString, vacationName + " Starts Today");
            setNotification(endDateString, vacationName + " Ends Today");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setNotification(String dateString, String message) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (myDate != null) {
            // Check if the date is today
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(myDate);
            int dayOfYear = calendar.get(Calendar.DAY_OF_YEAR);
            int currentDayOfYear = Calendar.getInstance().get(Calendar.DAY_OF_YEAR);

            if (dayOfYear == currentDayOfYear) {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
                intent.putExtra("key", message);
                PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            }
        }
    }

    private void saveVacation() {
        Vacation vacation;
        String name = editName.getText().toString();
        String lodging = editLodging.getText().toString();
        String startDate = editStartDate.getText().toString();
        String endDate = editEndDate.getText().toString();

        if (!isEndDateAfterStartDate(startDate, endDate)) {
            Toast.makeText(this, "End date must be after start date", Toast.LENGTH_LONG).show();
            return; // Prevent saving
        }

        if (vacationID == -1) {
            if (repository.getmAllVacations().isEmpty()) {
                vacationID = 1;
            } else {
                vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
            }
            vacation = new Vacation(vacationID, name, lodging, startDate, endDate);
            repository.insert(vacation);
        } else {
            try {
                vacation = new Vacation(vacationID, name, lodging, startDate, endDate);
                repository.update(vacation);
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "Error updating vacation", Toast.LENGTH_SHORT).show();
                return;
            }
        }
        Toast.makeText(this, vacation.getVacationName() + " saved", Toast.LENGTH_SHORT).show();
    }


    private void deleteVacation() {
        for (Vacation vacation : repository.getmAllVacations()) {
            if (vacation.getVacationID() == vacationID) currentVacation = vacation;
        }

        numExcursions = 0;
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) ++numExcursions;
        }

        if (numExcursions == 0) {
            repository.delete(currentVacation);
            Toast.makeText(VacationDetails.this, currentVacation.getVacationName() + " was deleted", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
        }
    }

    private void deleteExcursions() {
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) {
                currentExcursion = excursion;
                repository.delete(excursion);
                Toast.makeText(VacationDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        setupRecyclerView();
        refreshExcursionList();
    }


    private void refreshExcursionList() {
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdaptor excursionAdaptor = new ExcursionAdaptor(this);
        recyclerView.setAdapter(excursionAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) {
                filteredExcursions.add(excursion);
            }
        }
        excursionAdaptor.setExcursions(filteredExcursions);
    }

}

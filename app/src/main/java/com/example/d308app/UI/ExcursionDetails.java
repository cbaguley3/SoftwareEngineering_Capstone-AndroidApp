package com.example.d308app.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.d308app.R;
import com.example.d308app.database.Repository;
import com.example.d308app.entities.Excursion;
import com.example.d308app.entities.Vacation;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class ExcursionDetails extends AppCompatActivity {
    String vacationName;
    String excursionName;
    String date;
    String note;
    int excursionID;
    int vacationID;
    EditText editName;
    EditText editNote;
    TextView editDate;
    Repository repository;
    Excursion currentExcursion;

    DatePickerDialog.OnDateSetListener startDate;
    final Calendar myCalendarStart = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        repository = new Repository(getApplication());
        excursionName = getIntent().getStringExtra("name");
        date = getIntent().getStringExtra("date");
        note = getIntent().getStringExtra("note");
        editName = findViewById(R.id.name);
        editName.setText(excursionName);
        excursionID = getIntent().getIntExtra("excursionID", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        editNote = findViewById(R.id.note);
        editDate = findViewById(R.id.date);

        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        editDate.setText(sdf.format(myCalendarStart.getTime()));

        ArrayList<Vacation> vacationArrayList = new ArrayList<>(repository.getmAllVacations());
        ArrayAdapter<Vacation> vacationAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, vacationArrayList);

        Spinner spinner = findViewById(R.id.spinner);
        spinner.setAdapter(vacationAdapter);
        spinner.setSelection(0);

        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, monthOfYear);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }
        };

        editDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String info = editDate.getText().toString();
                if (info.equals("")) info = "05/01/24";
                try {
                    myCalendarStart.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        // Add TextWatcher to editDate for validation
        editDate.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Validate the date format when the text changes
                if (!isValidDate(s.toString())) {
                    editDate.setError("Invalid date format. Use MM/dd/yy");
                } else {
                    editDate.setError(null); // Clear the error
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                // No action needed here
            }
        });
    }

    private void updateLabelStart() {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        editDate.setText(sdf.format(myCalendarStart.getTime()));
    }

    // Method to validate date format
    private boolean isValidDate(String date) {
        String datePattern = "^\\d{2}/\\d{2}/\\d{2}$";
        return date.matches(datePattern);
    }

    // Method to check if the date is within the vacation period
    private boolean isDateWithinVacationPeriod(String date, String vacationStartDate, String vacationEndDate) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy", Locale.US);
        try {
            Date excursionDate = sdf.parse(date);
            Date startDate = sdf.parse(vacationStartDate);
            Date endDate = sdf.parse(vacationEndDate);
            return (excursionDate.equals(startDate) || excursionDate.equals(endDate) ||
                    (excursionDate.after(startDate) && excursionDate.before(endDate)));
        } catch (ParseException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.excursionsave) {
            Excursion excursion;
            if (excursionID == -1) {
                if (repository.getmAllExcursions().isEmpty())
                    excursionID = 1;
                else
                    excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
            }

            String excursionDate = editDate.getText().toString();

            // Get vacation details for validation
            Vacation vacation = repository.getmAllVacations().get(vacationID);
            if (vacation != null) {
                String vacationStartDate = vacation.getStartDate();
                String vacationEndDate = vacation.getEndDate();


                // Check if excursion date is within vacation period
                if (!isDateWithinVacationPeriod(excursionDate, vacationStartDate, vacationEndDate)) {
                    Toast.makeText(this, "Excursion date must be within the vacation period.", Toast.LENGTH_LONG).show();
                    return false; // Prevent saving
                }
            }

            excursion = new Excursion(excursionID, editName.getText().toString(), excursionDate, vacationID);
            if (excursionID == -1) {
                repository.insert(excursion);
            } else {
                repository.update(excursion);
            }
            return true;
        }
        if (item.getItemId() == R.id.deleteExcursion) {
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getExcursionID() == excursionID) currentExcursion = excursion;
                repository.delete(excursion);
                Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.share) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            String formattedText = String.format("Excursion: %s\nDate: %s Note: %s", excursionName, date, note);
            sendIntent.putExtra(Intent.EXTRA_TEXT, formattedText);

            sendIntent.putExtra(Intent.EXTRA_TITLE, vacationName);
            sendIntent.setType("text/plain");
            Intent shareIntent = Intent.createChooser(sendIntent, null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify) {
            String dateFromScreen = editDate.getText().toString();
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
            Date myDate = null;
            try {
                myDate = sdf.parse(dateFromScreen);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            try {
                Long trigger = myDate.getTime();
                Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                intent.putExtra("key", excursionName + " Today");
                PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_ONE_SHOT | PendingIntent.FLAG_IMMUTABLE);
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

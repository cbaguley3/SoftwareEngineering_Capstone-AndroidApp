package com.example.d308app.UI;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        String excursionName;
        int excursionID;
        int vacationID;
        EditText editName;
        EditText editNote;
        TextView editDate;
        Repository repository;
        DatePickerDialog.OnDateSetListener startDate;
        final Calendar myCalendarStart = Calendar.getInstance();

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_excursion_details);
            //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            repository=new Repository(getApplication());
            excursionName = getIntent().getStringExtra("name");
            editName = findViewById(R.id.name);
            editName.setText(excursionName);
            excursionID = getIntent().getIntExtra("excursionID", -1);
            vacationID = getIntent().getIntExtra("vacationID", -1);
            editNote=findViewById(R.id.note);
            editDate=findViewById(R.id.date);
            String myFormat = "MM/dd/yy"; //In which you need put here
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            ArrayList<Vacation> productArrayList= new ArrayList<>();
            productArrayList.addAll(repository.getmAllVacations());
            ArrayList<Integer> vacationIdList= new ArrayList<>();
            for(Vacation vacation:productArrayList){
                vacationIdList.add(vacation.getVacationID());
            }
            ArrayAdapter<Integer> productIdAdapter= new ArrayAdapter<Integer>(this, android.R.layout.simple_spinner_item,vacationIdList);
            Spinner spinner=findViewById(R.id.spinner);
            spinner.setAdapter(productIdAdapter);

            startDate = new DatePickerDialog.OnDateSetListener() {

                @Override
                public void onDateSet(DatePicker view, int year, int monthOfYear,
                                      int dayOfMonth) {
                    // TODO Auto-generated method stub

                    myCalendarStart.set(Calendar.YEAR, year);
                    myCalendarStart.set(Calendar.MONTH, monthOfYear);
                    myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);


                    updateLabelStart();
                }

            };

            editDate.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    // TODO Auto-generated method stub
                    Date date;

                    String info=editDate.getText().toString();
                    if(info.equals(""))info="02/10/22";
                    try{
                        myCalendarStart.setTime(sdf.parse(info));
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    new DatePickerDialog(ExcursionDetails.this, startDate, myCalendarStart
                            .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                            myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();
                }
            });

        }
        private void updateLabelStart() {
            String myFormat = "MM/dd/yy";
            SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

            editDate.setText(sdf.format(myCalendarStart.getTime()));
        }


        public boolean onCreateOptionsMenu(Menu menu) {
            getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
            return true;
        }

        public boolean onOptionsItemSelected(MenuItem item) {

            if (item.getItemId()== android.R.id.home){
                this.finish();
                return true;
            }

            // return true;
//                Intent intent=new Intent(PartDetails.this,MainActivity.class);
//                startActivity(intent);
//                return true;

            if (item.getItemId()== R.id.excursionsave){
                Excursion excursion;
                if (excursionID == -1) {
                    if (repository.getmAllExcursions().isEmpty())
                        excursionID = 1;
                    else
                        excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                    excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                    repository.insert(excursion);
                } else {
                    excursion = new Excursion(excursionID, editName.getText().toString(), editDate.getText().toString(), vacationID);
                    repository.update(excursion);
                }
                return true;}
            if (item.getItemId()== R.id.share) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, editNote.getText().toString());
                sendIntent.putExtra(Intent.EXTRA_TITLE, "Message Title");
                sendIntent.setType("text/plain");
                Intent shareIntent = Intent.createChooser(sendIntent, null);
                startActivity(shareIntent);
                return true;
            }
            if(item.getItemId()== R.id.notify) {
                String dateFromScreen = editDate.getText().toString();
                String myFormat = "MM/dd/yy"; //In which you need put here
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                Date myDate = null;
                try {
                    myDate = sdf.parse(dateFromScreen);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                try{
                    Long trigger = myDate.getTime();
                    Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
                    intent.putExtra("key", "message I want to see");
                    PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, ++MainActivity.numAlert, intent, PendingIntent.FLAG_IMMUTABLE);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);}
                catch (Exception e){

                }
                return true;
            }

            return super.onOptionsItemSelected(item);
        }
    }

package com.example.d308app.UI;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

import java.util.ArrayList;
import java.util.List;


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
    Excursion excursionName;
    Excursion excursionDate;
    Excursion currentExcursion;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        vacationName = getIntent().getStringExtra("name");
        editName = findViewById(R.id.vacationName);
        editName.setText(vacationName);
        lodging = getIntent().getStringExtra("lodging");
        editLodging = findViewById(R.id.lodging);
        editLodging.setText((lodging));
        editStartDate = findViewById(R.id.startDate);
        editStartDate.setText(startDate);
        editEndDate = findViewById(R.id.endDate);
        editEndDate.setText(endDate);
        vacationID = getIntent().getIntExtra("id", -1);
        // getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());
        final ExcursionAdaptor excursionAdapter = new ExcursionAdaptor(this);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) filteredExcursions.add(excursion);
        }
        excursionAdapter.setExcursions(filteredExcursions);

        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                startActivity(intent);
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacationdetails, menu);
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }
        if (item.getItemId() == R.id.vacationsave) {
            Vacation vacation;
            if (vacationID == -1) {
                if (repository.getmAllVacations().isEmpty()) vacationID = 1;
                else
                    vacationID = repository.getmAllVacations().get(repository.getmAllVacations().size() - 1).getVacationID() + 1;
                vacation = new Vacation(vacationID, editName.getText().toString(), editLodging.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
            } else {
                try {
                    vacation = new Vacation(vacationID, editName.getText().toString(), editLodging.getText().toString(), editStartDate.getText().toString(), editEndDate.getText().toString());
                    repository.update(vacation);
                } catch (Exception e) {

                }
            }
            return true;
        }
        if (item.getItemId() == R.id.vacationdelete) {
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
            return true;
        }
        if (item.getItemId() == R.id.addExcursions) {
            if (vacationID == -1)
                Toast.makeText(VacationDetails.this, "Please save vacation before adding excursions", Toast.LENGTH_LONG).show(); }
        if (item.getItemId() == R.id.deleteExcursions) {
            for (Excursion excursion : repository.getmAllExcursions()) {
                if (excursion.getVacationID() == vacationID) currentExcursion = excursion;
                repository.delete(excursion);
                Toast.makeText(VacationDetails.this, currentExcursion.getExcursionName() + " was deleted", Toast.LENGTH_LONG).show();
            }

        }

            else {
                int excursionID;

                if (repository.getmAllExcursions().size() == 0) excursionID = 1;
                else
                    excursionID = repository.getmAllExcursions().get(repository.getmAllExcursions().size() - 1).getExcursionID() + 1;
                Excursion excursion = new Excursion(excursionID, excursionName.getExcursionName(), excursionDate.getDate(), vacationID);
                repository.insert(excursion);
                excursion = new Excursion(++excursionID, excursionName.getExcursionName(), excursionDate.getDate(), vacationID);
                repository.insert(excursion);
                RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
                final ExcursionAdaptor excursionAdaptor = new ExcursionAdaptor(this);
                recyclerView.setAdapter(excursionAdaptor);
                recyclerView.setLayoutManager(new LinearLayoutManager(this));
                List<Excursion> filteredExcursions = new ArrayList<>();
                for (Excursion excursion2 : repository.getmAllExcursions()) {
                    if (excursion2.getVacationID() == vacationID)
                        filteredExcursions.add(excursion2);
                }
                excursionAdaptor.setExcursions(filteredExcursions);
                return true;
            }
            return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {

        super.onResume();
        RecyclerView recyclerView = findViewById(R.id.excursionrecyclerview);
        final ExcursionAdaptor excursionAdaptor = new ExcursionAdaptor(this);
        recyclerView.setAdapter(excursionAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion excursion : repository.getmAllExcursions()) {
            if (excursion.getVacationID() == vacationID) filteredExcursions.add(excursion);
        }
        excursionAdaptor.setExcursions(filteredExcursions);

        //Toast.makeText(ProductDetails.this,"refresh list",Toast.LENGTH_LONG).show();
    }

}

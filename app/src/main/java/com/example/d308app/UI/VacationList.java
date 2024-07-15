package com.example.d308app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.d308app.R;
import com.example.d308app.database.Repository;
import com.example.d308app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;
    private VacationAdaptor vacationAdaptor;

    //search bar code
    private List<Vacation> allVacations = new ArrayList<>();
    private List<Vacation> filteredVacations = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });

        // search bar code block
//        listView = findViewById(R.id.recyclerview);
//        arrayAdapter = new ArrayAdapter<String>(this, R.layout.vacation_list_item);
//        listView.setAdapter(arrayAdapter);

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        allVacations = repository.getmAllVacations();
        filteredVacations.addAll(allVacations); // Initially show all vacations
        vacationAdaptor = new VacationAdaptor(this);
        recyclerView.setAdapter(vacationAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdaptor.setVacations(filteredVacations);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        MenuItem menuItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setQueryHint("Search for a vacation");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (vacationAdaptor != null) {
                    filterVacations(newText);
                    return true;
                }
                return false;
            }
        });
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish(); // Close the current activity
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void filterVacations(String query) {
        filteredVacations.clear();
        if (query.isEmpty()) {
            filteredVacations.addAll(allVacations);
        } else {
            for (Vacation vacation : allVacations) {
                if (vacation.getVacationName().toLowerCase().contains(query.toLowerCase())) {
                    filteredVacations.add(vacation);
                }
            }
        }
        vacationAdaptor.setVacations(filteredVacations);
    }
}

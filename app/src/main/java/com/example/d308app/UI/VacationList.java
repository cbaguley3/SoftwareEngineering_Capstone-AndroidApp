package com.example.d308app.UI;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.d308app.R;
import com.example.d308app.database.Repository;
import com.example.d308app.entities.Excursion;
import com.example.d308app.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab=findViewById(R.id.floatingActionButton);

        fab.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            Intent intent= new Intent(VacationList.this, VacationDetails.class);
            startActivity(intent);
            }
        }));

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
//            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
//            return insets;
//        });

        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdaptor vacationAdaptor = new VacationAdaptor(this);
        recyclerView.setAdapter(vacationAdaptor);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdaptor.setVacations(allVacations);
        // System.out.println(getIntent().getStringExtra("test"));
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.sample) {
            repository=new Repository(getApplication());
           // Toast.makeText(VacationList.this,"put in sample data",Toast.LENGTH_LONG).show();
            Vacation vacation= new Vacation(0, "San Francisco", "SF Hotel Downtown","07/08/24","07/12/24");
            repository.insert(vacation);
            vacation = new Vacation(1, "Miami", "Ocean Drive Hotel", "09/22/24","09/25/24");
            repository.insert(vacation);
            Excursion excursion = new Excursion(0,"Fishing Boat","9/23/24",1);
            repository.insert(excursion);
            excursion = new Excursion(1,"Beach Resort", "09/24/24", 1);
            repository.insert(excursion);

            return true;
        }
        if(item.getItemId()==android.R.id.home) {
            this.finish();
            return true;
        }
        return true;
    }
}
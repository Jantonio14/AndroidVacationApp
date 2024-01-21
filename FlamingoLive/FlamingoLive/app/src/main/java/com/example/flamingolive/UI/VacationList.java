package com.example.flamingolive.UI;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.flamingolive.R;
import com.example.flamingolive.database.Repository;
import com.example.flamingolive.entities.Excursion;
import com.example.flamingolive.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class VacationList extends AppCompatActivity {
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_list);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationList.this, VacationDetails.class);
                startActivity(intent);
            }
        });
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        repository = new Repository(getApplication());
        List<Vacation> allVacations = repository.getmAllVacations();
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_vacation_list, menu);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Vacation> allVacations = repository.getmAllVacations();
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        final VacationAdapter vacationAdapter = new VacationAdapter(this);
        recyclerView.setAdapter(vacationAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        vacationAdapter.setVacations(allVacations);
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId()==R.id.mysample) {
            repository = new Repository(getApplication());


            Vacation vacation = new Vacation(0, "Grand Canyon", "Bellagio", "12/22/23", "12/24/23");
            repository.insert(vacation);
            vacation = new Vacation(0, "Las Vegas", "Caesar's Palace", "01/05/24", "01/10/24");
            repository.insert(vacation);
            Excursion excursion = new Excursion(0, "Canyon Tour", "12/23/23", 2);
            repository.insert(excursion);
            excursion = new Excursion(0, "Segway Tours", "01/08/24", 2);
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

package com.example.flamingolive.UI;

import static android.app.ProgressDialog.show;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.widget.Toast;

import com.example.flamingolive.R;
import com.example.flamingolive.database.Repository;
import com.example.flamingolive.entities.Excursion;
import com.example.flamingolive.entities.Vacation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class VacationDetails extends AppCompatActivity {

    String vacationTitle;
    String hotelName;
    int vacationID;
    EditText editTitle;
    EditText editHotel;

    TextView editStartDate;
    TextView editEndDate;
    DatePickerDialog.OnDateSetListener startDate;
    DatePickerDialog.OnDateSetListener endDate;
    final Calendar myCalendarStart = Calendar.getInstance();
    final Calendar myCalendarEnd = Calendar.getInstance();
    Repository repository;
    Vacation currentVacation;
    int numExcursions;
    String vacationStart;
    String vacationEnd;
    RecyclerView recyclerView;
    String myFormat = "MM/dd/yy"; //In which you need put here
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacation_details);
        FloatingActionButton fab = findViewById(R.id.floatingActionButton2);
        editStartDate = findViewById(R.id.startDate);
        editEndDate = findViewById(R.id.endDate);
        editTitle = findViewById(R.id.vacationTitle);
        editHotel = findViewById(R.id.hotelName);

        vacationID = getIntent().getIntExtra("id", -1);
        vacationTitle = getIntent().getStringExtra("title");
        hotelName = getIntent().getStringExtra("hotel");
        vacationStart = getIntent().getStringExtra("checkInDate");
        vacationEnd = getIntent().getStringExtra("checkOutDate");







        editStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String info = editStartDate.getText().toString();
                if(info.equals(""))
                    info= sdf.format(date);
                try{
                    myCalendarStart.setTime(sdf.parse(info));

                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, startDate, myCalendarStart
                        .get(Calendar.YEAR), myCalendarStart.get(Calendar.MONTH),
                        myCalendarStart.get(Calendar.DAY_OF_MONTH)).show();

            }
        });



        startDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarStart.set(Calendar.YEAR, year);
                myCalendarStart.set(Calendar.MONTH, month);
                myCalendarStart.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                String formattedDate = sdf.format(myCalendarStart.getTime());
                editStartDate.setText(formattedDate);

            }
        };

        endDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarEnd.set(Calendar.YEAR, year);
                myCalendarEnd.set(Calendar.MONTH, month);
                myCalendarEnd.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                if (myCalendarEnd.after(myCalendarStart)) {

                    String formattedDate = sdf.format(myCalendarEnd.getTime());
                    editEndDate.setText(formattedDate);
                } else {
                    Toast.makeText(VacationDetails.this, "End date must be after start date", Toast.LENGTH_SHORT).show();
                }



            }
        };

        editEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Date date = new Date();
                String info = editEndDate.getText().toString();
                if(info.equals(""))
                    info = sdf.format(date);;
                try{
                    myCalendarEnd.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(VacationDetails.this, endDate, myCalendarEnd
                        .get(Calendar.YEAR), myCalendarEnd.get(Calendar.MONTH),
                        myCalendarEnd.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(VacationDetails.this, ExcursionDetails.class);
                intent.putExtra("vacationID", vacationID);
                intent.putExtra("vacationStartDate", editStartDate.getText().toString());
                intent.putExtra("vacationEndDate", editEndDate.getText().toString());
                startActivity(intent);
            }
        });
        recyclerView = findViewById(R.id.excursionrecyclerview);
        repository = new Repository(getApplication());


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

        if(item.getItemId() == R.id.vacationsave){
            Vacation vacation;
            if (vacationID == -1){
                vacation = new Vacation(0, editTitle.getText().toString(), editHotel.getText().toString(),
                        editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.insert(vacation);
                finish();
            }
            else{
                vacation = new Vacation(vacationID, editTitle.getText().toString(),editHotel.getText().toString(),
                        editStartDate.getText().toString(), editEndDate.getText().toString());
                repository.update(vacation);
                finish();
            }
        }
        if(item.getItemId()== R.id.vacationdelete){
            for(Vacation vacation : repository.getmAllVacations()){
                if(vacation.getVacationID() == vacationID) currentVacation = vacation;
            }
            numExcursions = 0;
            for(Excursion excursion : repository.getAllExcursions()){
                if(excursion.getVacationID()== vacationID)++ numExcursions;
            }
            if(numExcursions == 0) {
                repository.delete(currentVacation);
                Toast.makeText(VacationDetails.this, currentVacation.getVacationTitle() + " was deleted",Toast.LENGTH_LONG).show();
                VacationDetails.this.finish();
            }
            else{
                Toast.makeText(VacationDetails.this, "Can't delete a vacation with excursions", Toast.LENGTH_LONG).show();
            }
        }

        if (item.getItemId() == R.id.share) {
            Intent sentIntent= new Intent();
            sentIntent.setAction(Intent.ACTION_SEND);
            sentIntent.putExtra(Intent.EXTRA_TEXT,
                    "Title: " + editTitle.getText().toString() + "\n" +
                    "Hotel: " + editHotel.getText().toString() + "\n" +
                    "Start Date: " + editStartDate.getText().toString() + "\n" +
                    "End Date: " + editEndDate.getText().toString());
            sentIntent.setType("text/plain");
            Intent shareIntent=Intent.createChooser(sentIntent,null);
            startActivity(shareIntent);
            return true;
        }
        if (item.getItemId() == R.id.notify) {

            setAlert(editStartDate.getText().toString(), "Vacation Start Date: " + editTitle.getText().toString(), vacationID * 10);
            setAlert(editEndDate.getText().toString(), "Vacation End Date: " + editTitle.getText().toString(), vacationID * 10 + 1);
            return true;

        }

        return true;
    }

    private void setAlert(String dateFromScreen, String key, int requestCode) {
        String myFormat = "MM/dd/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date myDate = null;
        try {
            myDate = sdf.parse(dateFromScreen);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (myDate != null) {

            Long trigger = myDate.getTime();
            Intent intent = new Intent(VacationDetails.this, MyReceiver.class);
            intent.putExtra("key", key);
            PendingIntent sender = PendingIntent.getBroadcast(VacationDetails.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

        }
    }
    @Override
    protected void onResume() {

        super.onResume();
        editTitle.setText(vacationTitle);
        editHotel.setText(hotelName);
        editStartDate.setText(vacationStart);
        editEndDate.setText(vacationEnd);

        final ExcursionAdapter excursionAdapter = new ExcursionAdapter(this, vacationStart, vacationEnd);
        recyclerView.setAdapter(excursionAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        List<Excursion> filteredExcursions = new ArrayList<>();
        for (Excursion e : repository.getAllExcursions()) {
            if (e.getVacationID() == vacationID) filteredExcursions.add(e);
        }
        excursionAdapter.setExcursions(filteredExcursions);


    }
}
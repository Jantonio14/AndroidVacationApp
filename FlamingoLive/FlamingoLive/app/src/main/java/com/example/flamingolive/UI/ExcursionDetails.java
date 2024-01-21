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
import android.util.Log;
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

public class ExcursionDetails extends AppCompatActivity {

    String excursionTitle;

    int excursionID;
    int vacationID;
    EditText editTitle;
    TextView editDate;
    String date;
    String vacationStartDate;
    String vacationEndDate;

    String selectedExcursionDateStr;



    Excursion currentExcursion;
    Repository repository;
    DatePickerDialog.OnDateSetListener excursionDate;
    final Calendar myCalendarExcursion = Calendar.getInstance();
    String myFormat = "MM/dd/yy";
    SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excursion_details);
        repository = new Repository(getApplication());


        excursionTitle = getIntent().getStringExtra("excursion_title");
        excursionID = getIntent().getIntExtra("id", -1);
        vacationID = getIntent().getIntExtra("vacationID", -1);
        date = getIntent().getStringExtra("date");
        vacationStartDate = getIntent().getStringExtra("vacationStartDate");
        vacationEndDate = getIntent().getStringExtra("vacationEndDate");


        editTitle = findViewById(R.id.titletext);
        editDate = findViewById(R.id.datetext);

        editTitle.setText(excursionTitle);
        editDate.setText(date);


        editDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Date date = new Date();
                //get value from other screen,but I'm going to hard code it right now
                String info=editDate.getText().toString();
                if(info.equals(""))
                    info = sdf.format(date);
                try{
                    myCalendarExcursion.setTime(sdf.parse(info));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                new DatePickerDialog(ExcursionDetails.this, excursionDate, myCalendarExcursion
                        .get(Calendar.YEAR), myCalendarExcursion.get(Calendar.MONTH),
                        myCalendarExcursion.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        excursionDate = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                myCalendarExcursion.set(Calendar.YEAR, year);
                myCalendarExcursion.set(Calendar.MONTH, month);
                myCalendarExcursion.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                String formattedDate = sdf.format(myCalendarExcursion.getTime());
                editDate.setText(formattedDate);
                date = getIntent().getStringExtra("excursion_date");
            }
        };
        Spinner spinner=findViewById(R.id.spinner);
        ArrayList<Vacation> vacationArrayList=new ArrayList<>();

        vacationArrayList.addAll(repository.getmAllVacations());

        ArrayAdapter<Vacation> vacationAdapter=new ArrayAdapter<>(this,android.R.layout.simple_spinner_item,vacationArrayList);
        spinner.setAdapter(vacationAdapter);
        spinner.setSelection(0);
    }



    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_excursiondetails, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        if (item.getItemId() == R.id.notifyexcursion) {

            setAlert(editDate.getText().toString(), "Excursion starts today!: " + editTitle.getText().toString(), excursionID * 10);
            return true;

        }

        if (item.getItemId() == R.id.excursiondelete) {
            for(Excursion excursion : repository.getAllExcursions()){
                if(excursion.getExcursionID() == excursionID) currentExcursion = excursion;
            }
            repository.delete(currentExcursion);
            Toast.makeText(ExcursionDetails.this, currentExcursion.getExcursionTitle() + " was deleted",Toast.LENGTH_LONG).show();
            ExcursionDetails.this.finish();
        } else {
            Log.d("ExcursionDetails", "currentExcursion is null, cannot delete");
        }


        if (item.getItemId() == R.id.excursionsave) {
            String title = editTitle.getText().toString();
            String selectedExcursionDateStr = editDate.getText().toString().trim();

            if (title.isEmpty() || selectedExcursionDateStr.isEmpty()) {
                Toast.makeText(ExcursionDetails.this, "Title or Date is empty", Toast.LENGTH_SHORT).show();
                return true;
            }

            try {
                Date excursionDate = sdf.parse(selectedExcursionDateStr);
                Date startDate = sdf.parse(vacationStartDate);
                Date endDate = sdf.parse(vacationEndDate);

                if (excursionDate.before(startDate) || excursionDate.after(endDate)) {
                    Toast.makeText(ExcursionDetails.this, "Excursion date must be within the vacation start and end dates", Toast.LENGTH_SHORT).show();
                    return true;
                }

                if (excursionID == -1) {
                    Excursion excursion = new Excursion(0, title, selectedExcursionDateStr, vacationID);
                    repository.insert(excursion);
                } else {
                    Excursion excursion = new Excursion(excursionID, title, selectedExcursionDateStr, vacationID);
                    repository.update(excursion);
                }

                finish();
                return true;

            } catch (ParseException e) {
                e.printStackTrace();
                Toast.makeText(ExcursionDetails.this, "Error parsing dates", Toast.LENGTH_SHORT).show();
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(ExcursionDetails.this, "Error occurred", Toast.LENGTH_SHORT).show();
                return true;
            }
        }


        return super.onOptionsItemSelected(item);
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
            Intent intent = new Intent(ExcursionDetails.this, MyReceiver.class);
            intent.putExtra("key", key);
            PendingIntent sender = PendingIntent.getBroadcast(ExcursionDetails.this, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, trigger, sender);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
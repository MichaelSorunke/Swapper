package comp3350.srsys.presentation;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.example.swapper.R;

import java.util.Calendar;

import comp3350.srsys.presentation.parcelableobjects.ParcelableListing;


public class ChooseTimePageUI extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private Button timeButton;
    private Button dateButton;
    private ParcelableListing listingParcel1;
    private ParcelableListing listingParcel2;
    private Intent intent;
    private int hourOfDay;
    private int minute;
    private static int year;
    private static int month;
    private static int date;
    private boolean datePickedFlag = false;
    private boolean timePickedFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        timeButton = (Button) findViewById(R.id.meetupTimeButton); //sets actions to take after pressing the meetupTime button
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "time picker");
            }
        });

        dateButton = (Button) findViewById(R.id.meetupDateButton); //sets actions to take after pressing the meetupDate button
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragement();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });

        dateButton = (Button) findViewById(R.id.confirmTimeButton); //sets actions to take after pressing the confirmTimeButton
        dateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToConfirmInformation();
            }
        });

        intent = getIntent();
        listingParcel1 = intent.getParcelableExtra("choosenownlisting1");
        listingParcel2 = intent.getParcelableExtra("choosenownlisting2");
    }

    @Override
    public void onTimeSet(TimePicker view, int hourPick, int minutePick) {
        Calendar currTime = Calendar.getInstance();

        hourOfDay = hourPick;
        minute = minutePick;

        boolean dateSelectedCheck = year >= currTime.YEAR;

        boolean currHourCheck = currTime.get(currTime.HOUR_OF_DAY) == hourOfDay && currTime.get(currTime.MINUTE) < minute && currTime.get(currTime.DAY_OF_MONTH) == date;
        boolean laterHourSameDayCheck = currTime.get(currTime.HOUR_OF_DAY) < hourOfDay && currTime.get(currTime.DAY_OF_MONTH) == date ;
        boolean laterDayCheck = currTime.get(currTime.DAY_OF_MONTH) < date;
        if(dateSelectedCheck) {
            if (currHourCheck || laterHourSameDayCheck || laterDayCheck) {
                TextView textView = (TextView) findViewById(R.id.chooseTimePrompt);
                textView.setText(hourOfDay + ":" + String.format("%02d", minute));
                timePickedFlag = true;
            }
            else {
                TextView textView = (TextView) findViewById(R.id.chooseTimePrompt);
                textView.setText("Can't choose a time from the past");
            }
        }
        else {
            TextView textView = (TextView) findViewById(R.id.chooseTimePrompt);
            textView.setText("Please choose a date first");
        }
    }

    @Override
    public void onDateSet(DatePicker view, int yearPick, int monthPick, int datePick) {
        Calendar currDate = Calendar.getInstance();

        year = yearPick;
        month = monthPick + 1;
        date = datePick;

        boolean currYearNotCurrMonthCheck = currDate.get(currDate.MONTH) < (month - 1) && currDate.get(currDate.YEAR) == year;
        boolean currYearCurrMonthCheck= currDate.get(currDate.MONTH) == (month - 1) && currDate.get(currDate.DAY_OF_MONTH) < date && currDate.get(currDate.YEAR) == year;
        boolean currYearMonthDayCheck = currDate.get(currDate.MONTH) == (month - 1) && currDate.get(currDate.DAY_OF_MONTH) == date && currDate.get(currDate.YEAR) == year;
        boolean futureYearCheck = currDate.get(currDate.YEAR) < year;
        if (currYearNotCurrMonthCheck || currYearCurrMonthCheck || currYearMonthDayCheck || futureYearCheck) {
            TextView textView = (TextView) findViewById(R.id.chooseDatePrompt);
            textView.setText("m." + month + " d." + date + " y." + year);
            datePickedFlag = true;
        }
        else {
            TextView textView = (TextView) findViewById(R.id.chooseDatePrompt);
            textView.setText("Can't choose a date from the past");
        }
    }

    private void goToConfirmInformation() {
        if(datePickedFlag && timePickedFlag) {
            Intent intent = new Intent(this, ConfirmInformationUI.class);
            intent.putExtra("choosenownlisting1", listingParcel1);
            intent.putExtra("choosenownlisting2", listingParcel2);
            intent.putExtra("choosenhourofday", hourOfDay);
            intent.putExtra("choosenminute", minute);
            intent.putExtra("choosenyear", year);
            intent.putExtra("choosenmonth", month);
            intent.putExtra("choosendate", date);
            startActivity(intent);
        }
        else {
            String toShow = "Please pick a valid date and time";
            Toast.makeText(this, toShow, Toast.LENGTH_SHORT).show();
        }
    }
}

/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	Allows for Event information to be edited and reserved the the database.

 */

package com.nillawaffer.hue.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class EventEdit extends AppCompatActivity {

    TextView firstHourDisplay;
    TextView firstMinuteDisplay;
    Button pickTime;

    String endMinString;
    String startHourString;
    String startMinString;

    TextView secondHourDisplay;
    TextView secondMinuteDisplay;

    int pHour;
    int pMinute;
    int startMin;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

    TextView firstMonthDisplay;
    TextView firstDayDisplay;
    TextView firstYearDisplay;
    Button pickDate;

    private int year2;
    private int month3;
    private int day2;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int DATE_DIALOG_ID = 2;
    static final int DATE_DIALOG_ID2 = 3;

    TextView secondMonthDisplay;
    TextView secondDayDisplay;
    TextView secondYearDisplay;
    Button pickDate2;

    Button addButton;
    Button cancelButton;

    int startHour;
    int endMin;

    String tag = "0";
    String name = "0";
    String finalStartHour = "0";
    String finalEndHour = "0";
    String startMinute = "0";
    String endHour = "0";
    String endMinute = "0";
    String startMonth;
    String startDay;
    String startYear;
    String endMonth;
    String endDay;
    String endYear;

    String finalEndMonth;
    String finalStartMonth;

    public void setTextOnLabels(){

        Bundle extras = getIntent().getExtras();
        name = extras.getString("pushName");

        startHourString = extras.getString("pushStartName");
        startDay = extras.getString("pushStartDay");
        startMinString = extras.getString("pushStartMin");
        startYear = extras.getString("pushStartYear");
        startMonth = extras.getString("pushStartMonth");

        endHour = extras.getString("pushEndHour");
        endDay = extras.getString("pushEndDay");
        endMinString = extras.getString("pushEndMin");
        endYear = extras.getString("pushEndYear");
        endMonth = extras.getString("pushEndMonth");

        TextView nameField = (TextView) findViewById(R.id.textfieldone);
        nameField.setText(name);

        firstHourDisplay = (TextView) findViewById(R.id.firstHourDisplay);
        String time = finalStartHour;
        firstHourDisplay.setText(time);

        firstMinuteDisplay = (TextView) findViewById(R.id.firstMinuteDisplay);
        String time2 = startMinute;
        firstMinuteDisplay.setText(time2);

        secondHourDisplay = (TextView) findViewById(R.id.secondHourDisplay);
        String time3 = endHour;
        secondHourDisplay.setText(time3);

        secondMinuteDisplay = (TextView) findViewById(R.id.secondMinuteDisplay);
        String time4 = endMinute;
        secondMinuteDisplay.setText(time4);

        firstMonthDisplay = (TextView) findViewById(R.id.firstMonthDisplay);
        String date  = startMonth;
        firstMonthDisplay.setText(date);

        firstDayDisplay = (TextView) findViewById(R.id.firstDayDisplay);
        String date2  = startDay;
        firstDayDisplay.setText(date2);

        firstYearDisplay = (TextView) findViewById(R.id.firstYearDisplay);
        String date3  = startYear;
        firstYearDisplay.setText(date3);

        secondMonthDisplay = (TextView) findViewById(R.id.secondMonthDisplay);
        String date4  = endMonth;
        secondMonthDisplay.setText(date4);

        secondDayDisplay = (TextView) findViewById(R.id.secondDayDisplay);
        String date5  = endDay;
        secondDayDisplay.setText(date5);

        secondYearDisplay = (TextView) findViewById(R.id.secondYearDisplay);
        String date6  = endYear;
        secondYearDisplay.setText(date6);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventaddition);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        //add tappablity
        addListenerOnButton();

        /** Capture our View elements */
        firstHourDisplay = (TextView) findViewById(R.id.firstHourDisplay);
        firstMinuteDisplay = (TextView) findViewById(R.id.firstMinuteDisplay);
        pickTime = (Button) findViewById(R.id.pickTime);

        firstMonthDisplay = (TextView) findViewById(R.id.firstMonthDisplay);
        firstDayDisplay = (TextView) findViewById(R.id.firstDayDisplay);
        firstYearDisplay = (TextView) findViewById(R.id.firstYearDisplay);
        pickDate = (Button) findViewById(R.id.pickDate);

        /** Listener for click event of the button */
        pickTime.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {showDialog(TIME_DIALOG_ID);
            }
        });

        /** Listener for click event of the button */
        pickDate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {showDialog(DATE_DIALOG_ID);
            }
        });

        /** Get the current time */
        final Calendar cal = Calendar.getInstance();
        pHour = cal.get(Calendar.HOUR_OF_DAY);
        pMinute = cal.get(Calendar.MINUTE);

        /** Get the current date */
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        /** Display the current time in the TextView */
        String aMpM = "AM";
        if (pHour > 11) {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if (pHour > 11) {
            currentHour = pHour - 12;
        } else {
            currentHour = pHour;
        }
        startMin = pMinute;
        startHour = pHour;

        //Set a message for user
        firstHourDisplay.setText(
                new StringBuilder()
                        .append(pad(currentHour)));
        firstMinuteDisplay.setText(
                new StringBuilder()
                        .append(pad(pMinute)));

        /** Display the current time in the TextView */
        String month2 = " ";
        if (month == 0) {
            month2 = "January";
        } else if (month == 1) {
            month2 = "February";
        } else if (month == 2) {
            month2 = "March";
        } else if (month == 3) {
            month2 = "April";
        } else if (month == 4) {
            month2 = "May";
        } else if (month == 5) {
            month2 = "June";
        } else if (month == 6) {
            month2 = "July";
        } else if (month == 7) {
            month2 = "August";
        } else if (month == 8) {
            month2 = "September";
        } else if (month == 9) {
            month2 = "October";
        } else if (month == 10) {
            month2 = "November";
        } else {
            month2 = "December";
        }

        firstMonthDisplay.setText(
                new StringBuilder()
                        .append(month2));
        firstDayDisplay.setText(
                new StringBuilder()
                        .append(day));
        firstYearDisplay.setText(
                new StringBuilder()
                        .append(year));

        /** Capture our View elements */
        secondHourDisplay = (TextView) findViewById(R.id.secondHourDisplay);
        secondMinuteDisplay = (TextView) findViewById(R.id.secondMinuteDisplay);
        pickTime2 = (Button) findViewById(R.id.pickTime2);

        /** Capture our View elements */
        secondMonthDisplay = (TextView) findViewById(R.id.secondMonthDisplay);
        secondDayDisplay = (TextView) findViewById(R.id.secondDayDisplay);
        secondYearDisplay = (TextView) findViewById(R.id.secondYearDisplay);
        pickDate2 = (Button) findViewById(R.id.pickDate2);

        /** Listener for click event of the button */
        pickTime2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(TIME_DIALOG_ID2);
            }
        });

        /** Listener for click event of the button */
        pickDate2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                showDialog(DATE_DIALOG_ID2);
            }
        });

        /** Get the current time */
        final Calendar cal2 = Calendar.getInstance();
        pHour2 = cal2.get(Calendar.HOUR_OF_DAY)+1;
        pMinute2 = cal2.get(Calendar.MINUTE);

        /** Get the current date */
        final Calendar c2 = Calendar.getInstance();
        int year2 = c2.get(Calendar.YEAR);
        int month3 = c2.get(Calendar.MONTH);
        int day2 = c2.get(Calendar.DAY_OF_MONTH);

        /** Display the current time in the TextView */
        String aMpM2 = "AM";
        if (pHour2 > 11) {
            aMpM2 = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour2;
        if (pHour2 > 11) {
            currentHour2 = pHour2 - 12;
        } else {
            currentHour2 = pHour2;
        }
        endMin = pMinute2;
        //endHour = pHour2;
        //Set a message for user
        secondHourDisplay.setText(
                new StringBuilder()
                        .append(pad(currentHour2)));
        secondMinuteDisplay.setText(
                new StringBuilder()
                        .append(pad(pMinute2)));

        /** Display the current date in the TextView */
        String monthh2 = " ";
        if (month3 == 0) {
            monthh2 = "January";
        } else if (month3 == 1) {
            monthh2 = "February";
        } else if (month3 == 2) {
            monthh2 = "March";
        } else if (month3 == 3) {
            monthh2 = "April";
        } else if (month3 == 4) {
            monthh2 = "May";
        } else if (month3 == 5) {
            monthh2 = "June";
        } else if (month3 == 6) {
            monthh2 = "July";
        } else if (month3 == 7) {
            monthh2 = "August";
        } else if (month3 == 8) {
            monthh2 = "September";
        } else if (month3 == 9) {
            monthh2 = "October";
        } else if (month3 == 10) {
            monthh2 = "November";
        } else {
            monthh2 = "December";
        }

        int endYear = year2;
        int endMonth = month3;
        int endDay = day2;

        secondMonthDisplay.setText(
                new StringBuilder()
                        .append(monthh2));
        secondDayDisplay.setText(
            new StringBuilder()
                .append(day2));
        secondYearDisplay.setText(
            new StringBuilder()
                .append(year2));

        setTextOnLabels();

    }

    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String aMpM = "AM";
                    if (hourOfDay > 11) {
                        aMpM = "PM";
                    }

                    //Make the 24 hour time format to 12 hour time format
                    int currentHour;
                    if (hourOfDay > 11) {
                        currentHour = hourOfDay - 12;
                    } else {
                        currentHour = hourOfDay;
                    }
                    //Set a message for user
                    pHour = hourOfDay;
                    int finalStartHour = pHour;
                    pMinute = minute;

                    firstHourDisplay.setText(
                            new StringBuilder()
                                    .append(pad(currentHour)));
                    firstMinuteDisplay.setText(
                            new StringBuilder()
                                    .append(pad(pMinute)));
                }
            };

    /** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener mDateSetListener2 =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {

                    String month2 = " ";
                    if (month == 0) {
                        month2 = "January";
                    } else if (month == 1) {
                        month2 = "February";
                    } else if (month == 2) {
                        month2 = "March";
                    } else if (month == 3) {
                        month2 = "April";
                    } else if (month == 4) {
                        month2 = "May";
                    } else if (month == 5) {
                        month2 = "June";
                    } else if (month == 6) {
                        month2 = "July";
                    } else if (month == 7) {
                        month2 = "August";
                    } else if (month == 8) {
                        month2 = "September";
                    } else if (month == 9) {
                        month2 = "October";
                    } else if (month == 10) {
                        month2 = "November";
                    } else {
                        month2 = "December";
                    }

                    //Set a message for user
                    year2 = year;
                    month3 = month;
                    day2 = day;

                    int finalEndMonth = month3;

                    secondMonthDisplay.setText(
                            new StringBuilder()
                                    .append(month2));
                    secondDayDisplay.setText(
                            new StringBuilder()
                                    .append(day));
                    secondYearDisplay.setText(
                            new StringBuilder()
                                    .append(year));


                }
            };



    /** Add padding to numbers less than ten */
    private static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }


    /** Create a new dialog for time picker */

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case TIME_DIALOG_ID:
                return new TimePickerDialog(this,
                        mTimeSetListener, pHour, pMinute, false);

            case TIME_DIALOG_ID2:
                return new TimePickerDialog(this,
                        mTimeSetListener2, pHour2, pMinute2, false);

            case DATE_DIALOG_ID:
                return new DatePickerDialog(this,
                        mDateSetListener, month3, day2, year2);

            case DATE_DIALOG_ID2:
                return new DatePickerDialog(this,
                        mDateSetListener2, month3, day2, year2);
        }
        return null;
    }

    private TextView displayTime2;
    private Button pickTime2;

    private int pHour2;
    private int pMinute2;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID2 = 1;

    /** Callback received when the user "picks" a time in the dialog */
    private TimePickerDialog.OnTimeSetListener mTimeSetListener2 =
            new TimePickerDialog.OnTimeSetListener() {
                public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                    String aMpM = "AM";
                    if (hourOfDay > 11) {
                        aMpM = "PM";
                    }

                    //Make the 24 hour time format to 12 hour time format
                    int currentHour;
                    if (hourOfDay > 11) {
                        currentHour = hourOfDay - 12;
                    } else {
                        currentHour = hourOfDay;
                    }
                    //Set a message for user
                    pHour2 = hourOfDay;
                    pMinute2 = minute;

                    secondHourDisplay.setText(
                            new StringBuilder()
                                    .append(pad(currentHour)));
                    secondMinuteDisplay.setText(
                            new StringBuilder()
                                    .append(pad(pMinute2)));
                }
            };

    /** Callback received when the user "picks" a date in the dialog */
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
                public void onDateSet(DatePicker view, int year, int month, int day) {

                    String month2 = " ";
                    if (month == 0) {
                        month2 = "January";
                    } else if (month == 1) {
                        month2 = "February";
                    } else if (month == 2) {
                        month2 = "March";
                    } else if (month == 3) {
                        month2 = "April";
                    } else if (month == 4) {
                        month2 = "May";
                    } else if (month == 5) {
                        month2 = "June";
                    } else if (month == 6) {
                        month2 = "July";
                    } else if (month == 7) {
                        month2 = "August";
                    } else if (month == 8) {
                        month2 = "September";
                    } else if (month == 9) {
                        month2 = "October";
                    } else if (month == 10) {
                        month2 = "November";
                    } else {
                        month2 = "December";
                    }

                    //Set a message for user
                    year2 = year;
                    month3 = month;
                    day2 = day;

                    int finalStartMonth = month3;

                    firstMonthDisplay.setText(
                            new StringBuilder()
                                    .append(month2));
                    firstDayDisplay.setText(
                            new StringBuilder()
                                    .append(day));
                    firstYearDisplay.setText(
                            new StringBuilder()
                                    .append(year));
                }
            };

    /** Add padding to numbers less than ten */
    private static String pad2(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + String.valueOf(c);
    }

    public void addListenerOnButton() {

        cancelButton = (Button) findViewById(R.id.buttoncancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        addButton = (Button) findViewById(R.id.buttonadd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(), "Event Added", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        return super.onOptionsItemSelected(item);
    }
}
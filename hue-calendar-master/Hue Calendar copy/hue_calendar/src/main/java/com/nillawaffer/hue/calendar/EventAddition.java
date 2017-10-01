/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	Allows for events to be added to the calendar though the use of a date and time picker.
	The activity stores the data taken from the user and stores it in the database and
	also schedules the lights to turn on and off based upon the event they have just created.

 */

package com.nillawaffer.hue.calendar;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.database.sqlite.SQLiteDatabase;
import android.content.ContentValues;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.os.Handler;

import com.philips.lighting.hue.listener.PHGroupListener;
import com.philips.lighting.hue.listener.PHLightListener;
import com.philips.lighting.hue.listener.PHScheduleListener;
import com.philips.lighting.hue.sdk.PHHueSDK;
import com.philips.lighting.hue.sdk.PHMessageType;
import com.philips.lighting.model.PHBridge;
import com.philips.lighting.model.PHBridgeResource;
import com.philips.lighting.model.PHBridgeResourcesCache;
import com.philips.lighting.model.PHGroup;
import com.philips.lighting.model.PHHueError;
import com.philips.lighting.model.PHLight;
import com.philips.lighting.model.PHLightState;
import com.philips.lighting.model.PHSchedule;

public class EventAddition extends AppCompatActivity {

    private PHHueSDK phHueSDK;
    private static final int MAX_HUE=65535;
    public static final String TAG = "Hue Calendar";

    public int startMin;
    private int startHour;
    private int endMin;
    private int endHour;
    private int startYear;
    private int startMonth;
    private int startDay;
    private int endYear;
    private int endMonth;
    private int endDay;

    int finalStartMonth = 4;
    int finalEndMonth = 4;

    int finalStartHour;
    int finalEndHour;


    EditText eventName;
    EditText tagName;
    TextView firstHourDisplay;
    TextView firstMinuteDisplay;
    TextView firstaMpMDisplay;
    Button pickTime;

    int pHour;
    int pMinute;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID = 0;

    TextView firstMonthDisplay;
    TextView firstDayDisplay;
    TextView firstYearDisplay;
    Button pickDate;

    private int year;
    private int month;
    private int day;

    private int year2;
    private int month3;
    private int day2;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int DATE_DIALOG_ID = 2;
    static final int DATE_DIALOG_ID2 = 3;

    TextView secondMonthDisplay;
    TextView secondDayDisplay;
    TextView secondYearDisplay;
    TextView secondaMpMDisplay;
    Button pickDate2;

    TextView secondHourDisplay;
    TextView secondMinuteDisplay;
    Button pickTime2;

    int pHour2;
    int pMinute2;
    /** This integer will uniquely define the dialog to be used for displaying time picker.*/
    static final int TIME_DIALOG_ID2 = 1;

    Button addButton;
    Button cancelButton;

    StringBuilder Builder = new StringBuilder();

    private Handler mUserLocationHandler = null;
    private Handler handler = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.eventaddition);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        addButton = (Button) findViewById(R.id.buttonadd);

        PHBridge bridge = PHHueSDK.getInstance().getSelectedBridge();

        phHueSDK = PHHueSDK.getInstance();
        phHueSDK.enableHeartbeat(bridge, PHHueSDK.HB_INTERVAL);

        getBridgeInfo();

        //add tappablity
        addListenerOnButton();

        addButton = (Button) findViewById(R.id.buttonadd);

        eventName = (EditText) findViewById(R.id.textfieldone);
        tagName = (EditText) findViewById(R.id.tagField);

        /** Capture our View elements */
        firstHourDisplay = (TextView) findViewById(R.id.firstHourDisplay);
        firstMinuteDisplay = (TextView) findViewById(R.id.firstMinuteDisplay);
        firstaMpMDisplay = (TextView) findViewById(R.id.firstaMpMDisplay);
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
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);

        /** Display the current time in the TextView */
        String aMpM = "AM";
        if (pHour > 11) {
            aMpM = "PM";
        }

        //Make the 24 hour time format to 12 hour time format
        int currentHour;
        if (pHour > 12) {
            currentHour = pHour - 12;
        }
        else if(pHour == 0){
            currentHour = 12;
        } else {
            currentHour = pHour;
        }
        startMin = pMinute;
        startHour = pHour;
        //Set a message for user
        firstHourDisplay.setText(new StringBuilder().append(pad(currentHour)));
        firstMinuteDisplay.setText(new StringBuilder().append(pad(pMinute)));
        firstaMpMDisplay.setText(new StringBuilder().append(aMpM));

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


        //finalStartMonth = month;
        Log.w(TAG, "Month = ");
        Log.w(TAG, Integer.toString(month));
        //startYear = year;
        //startMonth = month;
        //startDay = day;

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
        secondaMpMDisplay = (TextView) findViewById(R.id.secondaMpMDisplay);
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
        if (pHour2 > 12) {
            currentHour2 = pHour2 - 12;
        }
        else if(pHour == 0){
            currentHour2 = 12;
        } else {
            currentHour2 = pHour2;
        }
        endMin = pMinute2;
        endHour = pHour2;
        //Set a message for user
        secondHourDisplay.setText(
                new StringBuilder()
                        .append(pad(currentHour2)));
        secondMinuteDisplay.setText(
                new StringBuilder()
                        .append(pad(pMinute2)));
        secondaMpMDisplay.setText(
                new StringBuilder()
                        .append(aMpM2));

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

        //finalEndMonth = month3;

        endYear = year2;
        endMonth = month3;
        endDay = day2;

        secondMonthDisplay.setText(
                new StringBuilder()
                        .append(monthh2));
        secondDayDisplay.setText(
                new StringBuilder()
                        .append(day2));
        secondYearDisplay.setText(
                new StringBuilder()
                        .append(year2));

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
                    if (hourOfDay > 12) {
                        currentHour = hourOfDay - 12;
                    }
                    else if(pHour == 0){
                        currentHour = 12;
                    } else {
                        currentHour = hourOfDay;
                    }
                    //Set a message for user
                    pHour = hourOfDay;
                    //finalStartHour = pHour;
                    pMinute = minute;
                    firstHourDisplay.setText(
                            new StringBuilder()
                                    .append(pad(currentHour)));
                    firstMinuteDisplay.setText(
                            new StringBuilder()
                                    .append(pad(pMinute)));
                    firstaMpMDisplay.setText(
                            new StringBuilder()
                                    .append(aMpM));
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
                    day2 = day;


                    secondMonthDisplay.setText(
                            new StringBuilder()
                                    .append(month2));
                    secondDayDisplay.setText(
                            new StringBuilder()
                                    .append(day));
                    secondYearDisplay.setText(
                            new StringBuilder()
                                    .append(year));
                    //onDateSet(R.id., year2, month3, day2);
                    //onDateSet(R.);

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
                        mDateSetListener, year, month, day);

            case DATE_DIALOG_ID2:
                return new DatePickerDialog(this,
                        mDateSetListener2, year, month, day);
        }
        return null;
    }



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
                    if (hourOfDay > 12) {
                        currentHour = hourOfDay - 12;
                    }
                    else if(hourOfDay == 0){
                        currentHour = 12;
                    } else {
                        currentHour = hourOfDay;
                    }
                    //Set a message for user
                    pHour2 = hourOfDay;
                    //finalEndHour = pHour2;
                    pMinute2 = minute;

                    secondHourDisplay.setText(
                            new StringBuilder()
                                    .append(pad(currentHour)));
                    secondMinuteDisplay.setText(
                            new StringBuilder()
                                    .append(pad(pMinute2)));
                    secondaMpMDisplay.setText(
                            new StringBuilder()
                                    .append(aMpM));
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
                    day2 = day;


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

    //Handle adding an event as well as closing the addition view.
    public void addListenerOnButton() {

        cancelButton = (Button) findViewById(R.id.buttoncancel);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EventAddition.this, BasicActivity.class);
                startActivity(intent);

            }
        });

        addButton = (Button) findViewById(R.id.buttonadd);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                EventDB myDbHelper = new EventDB(getApplicationContext());
                SQLiteDatabase db = myDbHelper.getReadableDatabase();
                ContentValues values = new ContentValues();

                values.put(Events.SubmitEvent.COLUMN_EVENT_NAME, eventName.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_START_MINUTE, firstMinuteDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_START_HOUR, Integer.toString(pHour));
                values.put(Events.SubmitEvent.COLUMN_EVENT_END_MINUTE, secondMinuteDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_END_HOUR, Integer.toString(pHour2));
                values.put(Events.SubmitEvent.COLUMN_EVENT_START_YEAR, firstYearDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_START_MONTH, Integer.toString(finalStartMonth));
                values.put(Events.SubmitEvent.COLUMN_EVENT_START_DAY, firstDayDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_END_YEAR, secondYearDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_END_MONTH, Integer.toString(finalEndMonth));
                values.put(Events.SubmitEvent.COLUMN_EVENT_END_DAY, secondDayDisplay.getText().toString());
                values.put(Events.SubmitEvent.COLUMN_EVENT_TAGS, tagName.getText().toString());

                // insert the values into the database
                long newRowId = db.insert(Events.SubmitEvent.TABLE_NAME, null, values);

                // toast for new data
                int duration = Toast.LENGTH_LONG;
                String result;

                // check if data was inserted
                if (newRowId != -1) {
                    result = "New Event Added";
                } else {
                    result = "Error";
                }

                Toast toast = Toast.makeText(getApplicationContext(), result, duration);
                toast.show();

                //eventName.setText("");
                tagName.setText("");

                scheduleLightsOff();
                scheduleLightsOn();
                //remove();
                //scheduleLightsOn();
                Intent intent = new Intent(EventAddition.this, BasicActivity.class);
                startActivity(intent);

            }
        });
    }

    //Schedules the lights to turn off
    public void scheduleLightsOff(){
        //Set Calendar from Event Objects
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, pHour);
        calendar.set(Calendar.MINUTE, Integer.parseInt(firstMinuteDisplay.getText().toString()));
        calendar.set(Calendar.MONTH, finalStartMonth);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(firstDayDisplay.getText().toString()));
        Date date = calendar.getTime();

        PHBridge bridge = PHHueSDK.getInstance().getSelectedBridge();

        PHSchedule schedule = new PHSchedule("schedule");

        PHLightState lightState = new PHLightState();

        lightState.setOn(false);
        schedule.setIdentifier("scheduler");
        schedule.setLightState(lightState);
        schedule.setLightIdentifier("ID");
        schedule.setDate(date);
        schedule.setStartTime(date);
        schedule.setLocalTime(true);
        schedule.setAutoDelete(true);
        bridge.createSchedule(schedule, scheduleListener);

    }


    //Schedules the lights to turn off
    public void scheduleLightsOn(){
        //Set Calendar from Event Objects
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, pHour);
        calendar.set(Calendar.MINUTE, Integer.parseInt(secondMinuteDisplay.getText().toString()));
        calendar.set(Calendar.MONTH, finalEndMonth);
        calendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(secondDayDisplay.getText().toString()));
        Date date = calendar.getTime();

        PHBridge bridge = PHHueSDK.getInstance().getSelectedBridge();

        PHSchedule schedule = new PHSchedule(eventName.getText().toString());

        PHLightState lightState = new PHLightState();

        lightState.setOn(false);
        schedule.setIdentifier("scheduler");
        schedule.setLightState(lightState);
        schedule.setLightIdentifier("ID");
        schedule.setDate(date);
        schedule.setStartTime(date);
        schedule.setLocalTime(true);
        schedule.setAutoDelete(true);
        bridge.updateSchedule(schedule, scheduleListener);

    }

    //Listens for a schedule to be created
    PHScheduleListener scheduleListener = new PHScheduleListener() {
        @Override
        public void onCreated(PHSchedule phSchedule) {

            StringBuilder sb = new StringBuilder();

            sb.append(phSchedule.getName());
            sb.append(", ");
            sb.append(phSchedule.getLightState());
            sb.append(", ");
            sb.append(phSchedule.getLocalTime());
            sb.append(", ");
            sb.append(phSchedule.getStartTime());
            Log.w(TAG, sb.toString());
        }

        @Override
        public void onSuccess() {

            Log.w(TAG, "Success");
        }

        @Override
        public void onError(int i, String s) {
            StringBuilder sb = new StringBuilder();
            sb.append(i);
            sb.append(", ");
            sb.append(s);
            Log.w(TAG, sb.toString());
        }

        @Override
        public void onStateUpdate(Map<String, String> map, List<PHHueError> list) {
            StringBuilder sb = new StringBuilder();
            sb.append(map);
            sb.append(", ");
            sb.append(list);

            Log.w(TAG, sb.toString());
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);

        //Hide some irrelevant menu items
        menu.getItem(0).setVisible(false);
        menu.getItem(1).setVisible(false);
        menu.getItem(2).setVisible(false);
        menu.getItem(3).setVisible(false);
        menu.getItem(4).setVisible(false);
        menu.getItem(5).setVisible(false);
        menu.getItem(8).setVisible(false);
        menu.getItem(7).setVisible(false);
        menu.getItem(6).setVisible(false);

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

    // If you want to handle the response from the bridge, create a PHLightListener object.
    PHLightListener listener = new PHLightListener() {

        @Override
        public void onSuccess() {
        }

        @Override
        public void onStateUpdate(Map<String, String> arg0, List<PHHueError> arg1) {
            Log.w(TAG, "Light has updated");
        }

        @Override
        public void onError(int arg0, String arg1) {}

        @Override
        public void onReceivingLightDetails(PHLight arg0) {}

        @Override
        public void onReceivingLights(List<PHBridgeResource> arg0) {}

        @Override
        public void onSearchComplete() {}
    };

    public void getBridgeInfo() {

        PHBridgeResourcesCache cache = phHueSDK.getSelectedBridge().getResourceCache();
        Log.w(TAG, "Time:");
        Log.w(TAG, cache.getBridgeConfiguration().getTime());
        Log.w(TAG, "Local Time:");
        Log.w(TAG, cache.getBridgeConfiguration().getLocalTime());

        List<PHSchedule> mySchedules = cache.getAllSchedules(true);
        StringBuilder sb = new StringBuilder();

        sb.append("Schedules: ");

        for (PHSchedule schedule : mySchedules){


            sb.append(schedule.getName());
            sb.append(", ");
        }

        List<PHGroup> myGroups = cache.getAllGroups();

        sb.append("Groups: ");

        for (PHGroup group : myGroups){


            sb.append(group.getName());
            sb.append(", ");
        }

        String result = sb.toString();

        Log.w(TAG, result);

    }

    @Override
    protected void onDestroy() {
        PHBridge bridge = phHueSDK.getSelectedBridge();
        if (bridge != null) {

            if (phHueSDK.isHeartbeatEnabled(bridge)) {
                phHueSDK.disableHeartbeat(bridge);
            }

            phHueSDK.disconnect(bridge);
            super.onDestroy();
        }
    }




}

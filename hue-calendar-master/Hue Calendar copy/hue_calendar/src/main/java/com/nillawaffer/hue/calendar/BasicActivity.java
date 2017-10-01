/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	This activity provides the structure of the calendar as well
	as sets up the floating action button. It handles the interactions
	between the calendar and the individual events and the rest of the application
	itself. Also configures the menu and allows for switching between a  day, 3 day and
	week view for the calendar.

 */

package com.nillawaffer.hue.calendar;

import com.alamkanak.weekview.WeekViewEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.nfc.Tag;
import android.util.Log;


//TO DO
//create a global array
//load array
//create add method
//clear array of none-repeating events
//find repeating events
//events = repeating events

//NOTES
//Use setRandomColor() as a means of randomly creating a color

public class BasicActivity extends BaseActivity {

    int randomNumber;
    Random rand = new Random();

    @Override
    public List<? extends WeekViewEvent> onMonthChange(int newYear, int newMonth) {
        // Populate the week view with some events.
        List<WeekViewEvent> events = new ArrayList<WeekViewEvent>();

        //addFillerEvent();

        EventDB dbHelper = new EventDB(getApplicationContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String[] bind = {
        Events.SubmitEvent._ID,
        Events.SubmitEvent.COLUMN_EVENT_NAME,
        Events.SubmitEvent.COLUMN_EVENT_START_MINUTE,
        Events.SubmitEvent.COLUMN_EVENT_START_HOUR,
        Events.SubmitEvent.COLUMN_EVENT_END_MINUTE,
        Events.SubmitEvent.COLUMN_EVENT_END_HOUR,
        Events.SubmitEvent.COLUMN_EVENT_START_YEAR,
        Events.SubmitEvent.COLUMN_EVENT_START_MONTH,
        Events.SubmitEvent.COLUMN_EVENT_START_DAY,
        Events.SubmitEvent.COLUMN_EVENT_END_YEAR,
        Events.SubmitEvent.COLUMN_EVENT_END_MONTH,
        Events.SubmitEvent.COLUMN_EVENT_END_DAY,
        Events.SubmitEvent.COLUMN_EVENT_TAGS
        };

        final Cursor cursor = db.query(Events.SubmitEvent.TABLE_NAME, //table to query
                bind,
                null,
                null,
                null,
                null,
                null
        );

        //while (cursor.moveToNext()) {
            //
            // set start time

            //cursor.moveToNext();

        /*
        String[] names = cursor.getColumnNames();

        StringBuilder sb = new StringBuilder();

        for(String name: names) {

            sb.append(name);
            sb.append(", ");
        }

        Log.w(TAG, sb.toString());
        */
         while(cursor.moveToNext()){

            //Log.w(TAG, "IM IN HERE");
                 Calendar startTime = Calendar.getInstance();
                 startTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_START_DAY))));
                 startTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_START_HOUR))));
                 startTime.set(Calendar.MINUTE, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_START_MINUTE))));
                 startTime.set(Calendar.MONTH, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_START_MONTH))));
                 //startTime.set(Calendar.MONTH, 4);
                 startTime.set(Calendar.YEAR, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_START_YEAR))));


                 // set end time
                 Calendar endTime = (Calendar) startTime.clone();
                 endTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_END_DAY))));
                 endTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_END_HOUR))));
                 //endTime.set(Calendar.HOUR_OF_DAY, 20);
                 endTime.set(Calendar.MINUTE, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_END_MINUTE))));
                 int month = Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_END_MONTH)));
                 endTime.set(Calendar.MONTH, month);
                 //endTime.set(Calendar.MONTH, 4);
                 endTime.set(Calendar.YEAR, Integer.parseInt(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_END_YEAR))));

             if (month == newMonth){

                 // set event name
                 WeekViewEvent event = new WeekViewEvent(1, cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_NAME)), startTime, endTime);
                 event.setColor(getResources().getColor(setRandomColor()));
                 event.setID((cursor.getString(cursor.getColumnIndex(Events.SubmitEvent._ID))));
                 event.setTag(cursor.getString(cursor.getColumnIndex(Events.SubmitEvent.COLUMN_EVENT_TAGS)));
                 events.add(event);
                 Log.w(TAG, "ADDED");

             }else{

                 Log.w(TAG, "Not Equal");
             }
        }


        cursor.close();
        db.close();

        StringBuilder builder = new StringBuilder();

        for (WeekViewEvent event: events){
            builder.append(event.getName());
            builder.append(event.getStartTime());
            builder.append(event.getEndTime());
        }

        Log.w(TAG, builder.toString());

        return events;
    }



    public int setRandomColor(){

        int color1 = R.color.event_color_01;
        int color2 = R.color.event_color_02;
        int color3 = R.color.event_color_03;
        int color4 = R.color.event_color_04;
        int color5 = R.color.event_color_05;
        int color6 = R.color.event_color_06;
        int color7 = R.color.event_color_07;
        int color8 = R.color.event_color_08;
        int color9 = R.color.event_color_09;
        int color10 = R.color.event_color_010;

        ArrayList<Integer> colorArray = new ArrayList<Integer>(10);
        colorArray.add(color1);
        colorArray.add(color2);
        colorArray.add(color3);
        colorArray.add(color4);
        colorArray.add(color5);
        colorArray.add(color6);
        colorArray.add(color7);
        colorArray.add(color8);
        colorArray.add(color9);
        colorArray.add(color10);

        randomNumber = rand.nextInt(9 - 0) + 0;

        int returnColor = colorArray.get(randomNumber);
        //remove this color form the array so it isnt used again for awhile
        colorArray.remove(randomNumber);

        //once we run out of colors, refill them
        if (colorArray.size() == 0) {

            colorArray.add(color1);
            colorArray.add(color2);
            colorArray.add(color3);
            colorArray.add(color4);
            colorArray.add(color5);
            colorArray.add(color6);
            colorArray.add(color7);
            colorArray.add(color8);
            colorArray.add(color9);
            colorArray.add(color10);
        }

        return returnColor;
    }

    public void addFillerEvent() {

        EventDB myDbHelper = new EventDB(getApplicationContext());
        SQLiteDatabase db = myDbHelper.getReadableDatabase();
        ContentValues values = new ContentValues();


        values.put(Events.SubmitEvent.COLUMN_EVENT_NAME, "");
        values.put(Events.SubmitEvent.COLUMN_EVENT_START_MINUTE, "0");
        values.put(Events.SubmitEvent.COLUMN_EVENT_START_HOUR, "1");
        values.put(Events.SubmitEvent.COLUMN_EVENT_END_MINUTE, "0");
        values.put(Events.SubmitEvent.COLUMN_EVENT_END_HOUR, "3");
        values.put(Events.SubmitEvent.COLUMN_EVENT_START_YEAR, "1900");
        values.put(Events.SubmitEvent.COLUMN_EVENT_START_MONTH, "2");
        values.put(Events.SubmitEvent.COLUMN_EVENT_START_DAY, "1");
        values.put(Events.SubmitEvent.COLUMN_EVENT_END_YEAR, "1900");
        values.put(Events.SubmitEvent.COLUMN_EVENT_END_MONTH, "2");
        values.put(Events.SubmitEvent.COLUMN_EVENT_END_DAY, "3");
        values.put(Events.SubmitEvent.COLUMN_EVENT_TAGS, "");

        // insert the values into the database
        long newRowId = db.insert(Events.SubmitEvent.TABLE_NAME, null, values);
    }

}

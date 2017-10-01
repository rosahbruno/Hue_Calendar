/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

	Handles the creation of the database.

 */

package com.nillawaffer.hue.calendar;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.alamkanak.weekview.WeekViewEvent;
import com.alamkanak.weekview.sample.apiclient.Event;

public class EventDB extends SQLiteOpenHelper{

    public static final int DATABASE_VERSION=7;
    public static final String DATABASE_NAME="events.db";
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP=",";

    private static final String SQL_CREATE_EVENT = "CREATE TABLE " + Events.SubmitEvent.TABLE_NAME + " (" +
            Events.SubmitEvent._ID + " INTEGER PRIMARY KEY" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_NAME + TEXT_TYPE + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_START_MINUTE + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_START_HOUR + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_END_MINUTE + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_END_HOUR + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_START_YEAR + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_START_MONTH + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_START_DAY + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_END_YEAR + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_END_MONTH + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_END_DAY + " TEXT" + COMMA_SEP +

            Events.SubmitEvent.COLUMN_EVENT_TAGS + TEXT_TYPE + ")";


    // deleting a table
    private static final String SQL_DELETE = "DROP TABLE IF EXISTS " + Events.SubmitEvent.TABLE_NAME;

    private static final String SQL_EVENT_DELETE = "DELETE * FROM EVENTS WHERE ID = " + Events.SubmitEvent._ID;

    public EventDB (Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public EventDB (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL(SQL_CREATE_EVENT);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion){
        db.execSQL(SQL_DELETE);
        onCreate(db);
    }
}










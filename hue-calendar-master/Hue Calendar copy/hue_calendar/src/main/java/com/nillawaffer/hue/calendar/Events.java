/*
Author: Blair Altland, Bruno Rosa, Nate DeCriscio, Kyle Bargo
Date: 5/2/2016

    Instantiates the database.

 */

package com.nillawaffer.hue.calendar;

import android.provider.BaseColumns;

public final class Events{

    public Events(){}

    public static abstract class SubmitEvent implements BaseColumns{


        public static final String TABLE_NAME="Events";
        public static final String COLUMN_EVENT_NAME="eventName";
        public static final String COLUMN_EVENT_START_MINUTE="firstMinuteDisplay";
        public static final String COLUMN_EVENT_START_HOUR="firstHourDisplay";
        public static final String COLUMN_EVENT_END_MINUTE="secondMinuteDisplay";
        public static final String COLUMN_EVENT_END_HOUR="secondHourDisplay";
        public static final String COLUMN_EVENT_START_YEAR="firstYearDisplay";
        public static final String COLUMN_EVENT_START_MONTH="firstMonthDisplay";
        public static final String COLUMN_EVENT_START_DAY="firstDayDisplay";
        public static final String COLUMN_EVENT_END_YEAR="secondYearDisplay";
        public static final String COLUMN_EVENT_END_MONTH="secondMonthDisplay";
        public static final String COLUMN_EVENT_END_DAY="secondDayDisplay";
        public static final String COLUMN_EVENT_TAGS="eventTags";

    }

}









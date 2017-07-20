package com.example.android.habittrackerapp.Data;

import android.provider.BaseColumns;

/**
 * Created by luisp on 20/07/2017.
 */

public final class HabitContract {

    //To prevent someone from accidentally instantiating the contract class,
    // give it an empty constructor.
    private HabitContract() {
    }

    /**
     * Inner class that defines constant values for the habits track database table.
     * Each entry in the table represents a single habit.
     */
    public static final class HabitEntry implements BaseColumns {

        /** Name of database table for pets */
        // TODO: PAY ATTENTION TO "habits" table name in plural, not "habit"
        public final static String TABLE_NAME = "habits";

        /**
         * Unique ID number for the habit (only for use in the database table).
         *
         * Type: INTEGER
         */
        public final static String _ID = BaseColumns._ID;

        /**
         * Name of the habit.
         *
         * Type: TEXT
         */
        public final static String COLUMN_HABIT_NAME = "name";

        /**
         * Time spent in the habit (in minuts)
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_TIME = "time";

        /**
         * Kinf of habit
         *
         * The only possible values are {@link #HABIT_INDIVIDUAL}, {@link #HABIT_COLLECTIVE}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_KIND = "kind";

        /**
         * Activity type which the habit refers to.
         *
         * The only possible values are {@link #HABIT_SPORTIVE}, {@link #HABIT_INTELLECTUAL}.
         *  {@link #HABIT_SOCIAL}, {@link #HABIT_OTHERS}.
         *
         * Type: INTEGER
         */
        public final static String COLUMN_HABIT_ACTIVITY = "activity";

        /**
         * Possible values for the kind of habit.
         */
        public static final int HABIT_INDIVIDUAL = 0;
        public static final int HABIT_COLLECTIVE = 1;

        /**
         * Possible values for the type of activity which the habit is referred.
         */

        public static final int HABIT_OTHERS = 0;
        public static final int HABIT_SPORTIVE = 1;
        public static final int HABIT_INTELLECTUAL = 2;
        public static final int HABIT_SOCIAL = 3;

    }
}

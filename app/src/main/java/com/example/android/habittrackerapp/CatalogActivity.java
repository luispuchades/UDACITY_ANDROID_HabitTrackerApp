package com.example.android.habittrackerapp;

import android.content.ContentValues;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.android.habittrackerapp.Data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.Data.HabitDbHelper;

public class CatalogActivity extends AppCompatActivity {

    /** Database helper that will provide us access to the database */
    private HabitDbHelper mDbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalog);

        // Setup FAB to open EditorActivity
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CatalogActivity.this, EditorActivity.class);
                startActivity(intent);
            }
        });

        // To access our database, we instantiate our subclass of SQLiteOpenHelper
        // and pass the context, which is the current activity.
        mDbHelper = new HabitDbHelper(this);
        }

        @Override
        protected void onStart() {
            super.onStart();
            displayDatabaseInfo();
        }

        // Correction from evaluator
        public Cursor queryAllHabits(){
            // Create and/or open a database to read from it
            SQLiteDatabase db = mDbHelper.getReadableDatabase();

            // Define a projection that specifies which columns from the database
            // you will actually use after this query.
            String[] projection = {
                    HabitEntry._ID,
                    HabitEntry.COLUMN_HABIT_NAME,
                    HabitEntry.COLUMN_HABIT_TIME,
                    HabitEntry.COLUMN_HABIT_KIND,
                    HabitEntry.COLUMN_HABIT_ACTIVITY
            };

            // Perform a query on the pets table
            Cursor cursor = db.query(
                    HabitEntry.TABLE_NAME,   // The table to query
                    projection,              // The columns to return
                    null,                  // The columns for the WHERE clause
                    null,                  // The values for the WHERE clause
                    null,                  // Don't group the rows
                    null,                  // Don't filter by row groups
                    null);                   // The sort order
            return cursor;
        }


        /**
         * Temporary helper method to display information in the onscreen TextView about the state
         * of the habits database.
         */
        private void displayDatabaseInfo() {

            Cursor cursor = queryAllHabits();

            TextView displayView = (TextView) findViewById(R.id.text_view_habit);

            try {
                // Create a header in the Text View that looks like this:
                //
                // The habit table contains <number of rows in Cursor> habits.
                // _id - name - time - kind of habit - Type of Activity
                //
                // In the while loop below, iterate through the rows of the cursor and display
                // the information from each column in this order.
                displayView.setText("The habit table contains " + cursor.getCount() + "habits.\n\n");

                displayView.append(HabitEntry._ID + " -" +
                        HabitEntry.COLUMN_HABIT_NAME + " - " +
                        HabitEntry.COLUMN_HABIT_TIME + " - " +
                        HabitEntry.COLUMN_HABIT_KIND + " - " +
                        HabitEntry.COLUMN_HABIT_ACTIVITY);

                // Figure out the index of each column
                int idColumnIndex = cursor.getColumnIndex(HabitEntry._ID);
                int nameColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_NAME);
                int timeColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_TIME);
                int kindColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_KIND);
                int activityColumnIndex = cursor.getColumnIndex(HabitEntry.COLUMN_HABIT_ACTIVITY);

                while (cursor.moveToNext()) {
                    // Use that index to extract the String or Int value of the word
                    // at the current row the cursor is on.
                    int currentID = cursor.getInt(idColumnIndex);
                    String currentName = cursor.getString(nameColumnIndex);
                    int currentTime = cursor.getInt(timeColumnIndex);
                    int currentKind = cursor.getInt(kindColumnIndex);
                    int currentActivity = cursor.getInt(activityColumnIndex);

                    // Display the values from each column of the current row in the cursor in the TextView
                    displayView.append("\n" + currentID + " - " +
                            currentName + " - " +
                            currentTime + " - " +
                            currentKind + " - " +
                            currentActivity);
                }

            } finally {
                // Always close the cursor when you're done reading from it. This releases all its
                // resources and makes it invalid.
                cursor.close();
            }
        }

    /**
     * Helper method to insert hardcoded habit data into the database. For debugging purposes only.
     */
    private void insertHabit() {
        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and snap habit (siesta) attributes are the values.
        ContentValues values = new ContentValues();

        values.put(HabitEntry.COLUMN_HABIT_NAME, "snap");
        values.put(HabitEntry.COLUMN_HABIT_TIME, 15);
        values.put(HabitEntry.COLUMN_HABIT_KIND, HabitEntry.HABIT_INDIVIDUAL);
        values.put(HabitEntry.COLUMN_HABIT_ACTIVITY, HabitEntry.HABIT_INTELLECTUAL);

        // Insert a new row for snap (siesta) in the database, returning the ID of that new row.
        // The first argument for db.insert() is the habit table name.
        // The second argument provides the name of a column in which the framework
        // can insert NULL in the event that the ContentValues is empty (if
        // this is set to "null", then the framework will not insert a row when
        // there are no values).
        // The third argument is the ContentValues object containing the info for snap.
        long newRowId = db.insert(HabitEntry.TABLE_NAME, null, values);

        // Show a toast message depending on whether or not the insertion was successful
        if (newRowId == -1) {
            // If the row ID is -1, then there was an error with insertion.
            Toast.makeText(this, "Error with saving habit", Toast.LENGTH_SHORT).show();
        } else {
            // Otherwise, the insertion was successful and we can display a toast with the row ID.
            Toast.makeText(this, "Habit saved with row id " + newRowId, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu options from the res/menu/menu_catalog.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_catalog, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()) {
            case R.id.action_insert_dummy_data:
                insertHabit();
                displayDatabaseInfo();
                return true;

            case R.id.action_delete_all_entries:
                //Do nothing for now
                return true;
        }
        return onOptionsItemSelected(item);
    }
}

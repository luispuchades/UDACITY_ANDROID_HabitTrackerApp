package com.example.android.habittrackerapp;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import com.example.android.habittrackerapp.Data.HabitContract.HabitEntry;
import com.example.android.habittrackerapp.Data.HabitDbHelper;

import static android.R.attr.setupActivity;
import static android.icu.lang.UCharacter.GraphemeClusterBreak.T;
import static android.icu.text.UnicodeSet.CASE;

/**
 * Allows user to create a new pet or edit an existing one.
 */
public class EditorActivity extends AppCompatActivity {

    /** EditText field to enter the habit's name */
    private EditText mNameEditText;

    /** EditText field to enter the habit's time */
    private EditText mTimeEditText;

    /** EditText field to enter the kind of habit */
    private Spinner mKindSpinner;

    /** EditText field to enter the type of the activity */
    private Spinner mActivitySpinner;

    /**
     * Kind of habit. The possible valid values are in the HabitContract.java file:
     * {@link HabitEntry#HABIT_INDIVIDUAL}, {@link HabitEntry#HABIT_COLLECTIVE}
     */
    private int mKind = HabitEntry.HABIT_INDIVIDUAL;

    /**
     * Type of habit activity. The possible valid values are in the HabitContract.java file:
     * {@link HabitEntry#HABIT_OTHERS}, {@link HabitEntry#HABIT_SPORTIVE}
     * {@link HabitEntry#HABIT_INTELLECTUAL}, {@link HabitEntry#HABIT_SOCIAL}
     */
    private int mActivity = HabitEntry.HABIT_OTHERS;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editor);

        // Find all relevant views that we will need to read user input from
        mNameEditText = (EditText) findViewById(R.id.edit_habit_name);
        mTimeEditText = (EditText) findViewById(R.id.edit_habit_time);
        mKindSpinner = (Spinner) findViewById(R.id.spinner_kind_of_habit);
        mActivitySpinner = (Spinner) findViewById(R.id.spinner_type_of_activity);

        setupKindSpinner();
        setupActivitySpinner();
    }

    /**
     * Setup the dropdown spinner that allows the user to select the kind of the habit.
     */
    private void setupKindSpinner() {
        // Create adapter for kind of habit spinner. The list options are from the String array
        // it will use the spinner will use the default layout
        ArrayAdapter kindSpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_kind_habit_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        kindSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mKindSpinner.setAdapter(kindSpinnerAdapter);

        // Set the integer mSelected to the constant values
        mKindSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.kind_habit_individual))) {
                        mKind = HabitEntry.HABIT_INDIVIDUAL;
                    } else if (selection.equals(getString(R.string.kind_habit_collective))){
                        mKind = HabitEntry.HABIT_COLLECTIVE;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mKind = HabitEntry.HABIT_INDIVIDUAL;
            }
        });
    }

    /**
     * Setup the dropdown spinner that allows the user to select the type of activity.
     */
    private void setupActivitySpinner() {
        // Create adapter for type of activity spinner. The list options are from the String array
        // it will use the spinner will use the default layout
        ArrayAdapter activitySpinnerAdapter = ArrayAdapter.createFromResource(this,
                R.array.array_type_activity_options, android.R.layout.simple_spinner_item);

        // Specify dropdown layout style - simple list view with 1 item per line
        activitySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);

        // Apply the adapter to the spinner
        mActivitySpinner.setAdapter(activitySpinnerAdapter);

        // Set the integer mSelected to the constant values
        mActivitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);

                if (!TextUtils.isEmpty(selection)) {
                    if (selection.equals(getString(R.string.type_activity_others))){
                        mActivity = HabitEntry.HABIT_OTHERS;
                    } else if (selection.equals(getString(R.string.type_activity_sportive))) {
                        mActivity = HabitEntry.HABIT_SPORTIVE;
                    } else if (selection.equals(getString(R.string.type_activity_intellectual))) {
                        mActivity = HabitEntry.HABIT_INTELLECTUAL;
                    } else {
                        mActivity = HabitEntry.HABIT_SOCIAL;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                mActivity = HabitEntry.HABIT_OTHERS;
            }
        });
    }

    /**
     * Get user input from editor and save new habit into database.
     */
    private void insertHabit() {
        // Read from input fields
        // Use trim to eliminate leading or trailing white space
        String nameString = mNameEditText.getText().toString().trim();
        String timeString = mTimeEditText.getText().toString().trim();
        int time = Integer.parseInt(timeString);

        // Create database helper
        HabitDbHelper mDbHelper = new HabitDbHelper(this);

        // Gets the database in write mode
        SQLiteDatabase db = mDbHelper.getWritableDatabase();

        // Create a ContentValues object where column names are the keys,
        // and habit attributes from the editor are the values.
        ContentValues values = new ContentValues();

        values.put(HabitEntry.COLUMN_HABIT_NAME, nameString);
        values.put(HabitEntry.COLUMN_HABIT_TIME, time);
        values.put(HabitEntry.COLUMN_HABIT_KIND, mKind);
        values.put(HabitEntry.COLUMN_HABIT_ACTIVITY, mActivity);

        // Insert a new row for habit in the database, returning the ID of that new row.
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
        // Inflate the menu options from the res/menu/menu_editor.xml file.
        // This adds menu items to the app bar.
        getMenuInflater().inflate(R.menu.menu_editor, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // User clicked on a menu option in the app bar overflow menu
        switch (item.getItemId()){
            case R.id.action_save:
                // Save habit to database
                insertHabit();
                // Exit activity
                finish();
                return true;
            case R.id.action_delete:
                // Do nothing for now
                return true;
            // Respond to a click on the "Up" arrow button in the app bar
            case R.id.home:
                // Navigate back to parent activity (CatalogActivity)
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }
          return super.onOptionsItemSelected(item);
    }
}

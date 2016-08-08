package com.example.android.sqllitecrud;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;


public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FeedReaderContract.FeedReaderDbHelper mDbHelper = new FeedReaderContract.FeedReaderDbHelper(this);
        SQLiteDatabase db = mDbHelper.getWritableDatabase();
        //mDbHelper.deleteDatabase(db);

        // Create
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Walk");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, "10 minutes");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FREQUENCY, 1);
        long itemId = createRow(values, db);

        values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Internet");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, "20 minutes");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FREQUENCY, 1);
        long delRowId = createRow(values, db);

        values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Program");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, "60 minutes");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FREQUENCY, 1);
        createRow(values, db);

        // Read
        String[] projection = {
                FeedReaderContract.FeedEntry._ID,
                FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE,
                FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT,
                FeedReaderContract.FeedEntry.COLUMN_NAME_FREQUENCY
        };
        Cursor c = readRows(projection, db);

        // Update
        int updatedRows;
        values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TITLE, "Walk");
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_CONTENT, "35 minutes");
        updatedRows = updateRow(1, values, db);
        Log.v(TAG,"Update String column, " + updatedRows + " rows of data");

        values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_FREQUENCY, 2);
        updatedRows = updateRow(3, values, db);
        Log.v(TAG,"Update Int column, " + updatedRows + " rows of data");

        // Delete
        deleteRow(delRowId, db);

        // Read again
        readRows(projection, db);
    }

    private long createRow(ContentValues values, SQLiteDatabase db) {

        Log.v(TAG, "Create row with values: " + values.toString());

        return db.insert(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                FeedReaderContract.FeedEntry.COLUMN_NAME_NULLABLE,
                values);
    }

    private Cursor readRows(String[] projection, SQLiteDatabase db) {
        String sortOrder =
                FeedReaderContract.FeedEntry._ID + " ASC";

        Cursor c = db.query(
                FeedReaderContract.FeedEntry.TABLE_NAME,  // The table to query
                projection,                               // The columns to return
                null,                                     // The columns for the WHERE clause
                null,                                     // The values for the WHERE clause
                null,                                     // don't group the rows
                null,                                     // don't filter by row groups
                sortOrder                                 // The sort order
        );

        c.moveToFirst();
        long itemId = c.getLong(
                c.getColumnIndexOrThrow(FeedReaderContract.FeedEntry._ID)
        );

        //iterate over rows
        for (int i = 0; i < c.getCount(); i++) {
            Log.v(TAG, "Selected row with value: " +
                    c.getString(c.getColumnIndexOrThrow("_id")) + " " +
                    c.getString(c.getColumnIndexOrThrow("title")) + " " +
                    c.getString(c.getColumnIndexOrThrow("content")) + " " +
                    c.getString(c.getColumnIndexOrThrow("frequency")));
                    c.moveToNext();
        }

        c.close();
        return c;
    }

    private int updateRow(long itemId, ContentValues values, SQLiteDatabase db){
        // Which row to update, based on the ID
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";
        String[] selectionArgs = { String.valueOf(itemId) };

        Log.v(TAG, "updating row in database with id: " + itemId);
        return db.update(
                FeedReaderContract.FeedEntry.TABLE_NAME,
                values,
                selection,
                selectionArgs);
    }

    private long deleteRow(long itemId, SQLiteDatabase db) {
        String selection = FeedReaderContract.FeedEntry._ID + " LIKE ?";  // WHERE col_name LIKE ?
        String[] selectionArgs = new String[]{ String.valueOf(itemId) };

        Log.v(TAG, "Deleting row with id: " + itemId);
        return db.delete(FeedReaderContract.FeedEntry.TABLE_NAME, selection, selectionArgs);
    }
}


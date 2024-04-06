package com.example.agevoyage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DatabaseHelper extends SQLiteOpenHelper {

    // Database name
    public static final String DATABASE_NAME = "AgeVoyage.db";
    // Table names
    public static final String TABLE_USERS = "users";
    public static final String TABLE_PLACE_DETAILS = "places";

    // Constructor
    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    // Creating the database tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create users table
        String createUserTableQuery = "CREATE TABLE " + TABLE_USERS +
                " (email TEXT PRIMARY KEY, password TEXT)";
        db.execSQL(createUserTableQuery);

        // Create places table with state column
        String createPlacesTableQuery = "CREATE TABLE " + TABLE_PLACE_DETAILS +
                " (_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name TEXT, best_time TEXT, age_category TEXT, state TEXT, image_uri TEXT, budgetamount INTEGER,description TEXT)";
        db.execSQL(createPlacesTableQuery);

        // Insert default user data
        insertDefaultUser(db);
    }

    // Insert default user data
    private void insertDefaultUser(SQLiteDatabase db) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", "abe");
        contentValues.put("password", "abe");
        db.insert(TABLE_USERS, null, contentValues);
    }

    // Upgrading database (if needed)
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLACE_DETAILS);
        onCreate(db);
    }

    // Method to insert user data
    public boolean insertUserData(String email, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        try {
            long result = db.insert(TABLE_USERS, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting user data: " + e.getMessage());
            return false;
        } finally {
            db.close(); // Close the database connection
        }
    }

    // Method to insert place data
    public boolean insertPlaceData(String name, String bestTime, String ageCategory, String state, String imageUri, int budgetAmount, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("best_time", bestTime);
        contentValues.put("age_category", ageCategory);
        contentValues.put("state", state);
        contentValues.put("image_uri", imageUri);
        contentValues.put("budgetamount", budgetAmount);
        contentValues.put("description", description); // Add description to ContentValues
        try {
            long result = db.insert(TABLE_PLACE_DETAILS, null, contentValues);
            return result != -1;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error inserting place data: " + e.getMessage());
            return false;
        } finally {
            db.close(); // Close the database connection
        }
    }

    public boolean updatePlaceData(int id, String name, String bestTime, String ageCategory, String state, String imageUri, int budgetAmount,String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("best_time", bestTime);
        contentValues.put("age_category", ageCategory);
        contentValues.put("state", state);
        contentValues.put("image_uri", imageUri);
        contentValues.put("budgetamount", budgetAmount);
        contentValues.put("description", description);


        try {
            int rowsAffected = db.update(TABLE_PLACE_DETAILS, contentValues, "_id = ?", new String[]{String.valueOf(id)});
            return rowsAffected > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error updating place data: " + e.getMessage());
            return false;
        } finally {
            db.close(); // Close the database connection
        }
    }


    // Method to fetch all place details
    public Cursor getAllPlaceDetails() {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.rawQuery("SELECT * FROM " + TABLE_PLACE_DETAILS, null);
    }

    public Cursor getPlaceById(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_PLACE_DETAILS, null,
                "_id=?", new String[]{String.valueOf(id)}, null, null, null);

        return cursor;
    }

    // Method to delete a place by name
    public boolean deletePlace(String placeName) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_PLACE_DETAILS, "name=?", new String[]{placeName});
        db.close();
        return result != 0;
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ? AND password = ?", new String[]{email, password});
        try {
            int count = cursor.getCount();
            return count > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking email and password: " + e.getMessage());
            return false;
        } finally {
            cursor.close();
            db.close();
        }
    }

    public boolean checkEmail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE email = ?", new String[]{email});
        try {
            int count = cursor.getCount();
            return count > 0;
        } catch (Exception e) {
            Log.e("DatabaseHelper", "Error checking email: " + e.getMessage());
            return false;
        } finally {
            cursor.close(); // Close cursor to avoid memory leaks
            db.close(); // Close the database connection
        }
    }

    public boolean insertData(String email, String password) {
        return insertUserData(email, password);
    }
}

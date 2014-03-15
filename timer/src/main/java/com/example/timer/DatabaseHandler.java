package com.example.timer;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.util.Log;

import java.security.Key;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

/**
 * Created by cYa on 05.03.14.
 */
public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "DB_TIMERLIST",
    TABLE_TIMERS = "TAB_TIMERS",
    KEY_ID = "c_id",
    KEY_NAME = "c_name",
    KEY_TIME = "c_time",
    KEY_INFO = "c_info";


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + TABLE_TIMERS + "(" + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + KEY_NAME + " TEXT," + KEY_TIME + " TEXT," + KEY_INFO + " TEXT)");
    
        Log.d("Timer4Everything", "CREATE DATABASE");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TIMERS);

        Log.d("Timer4Everything", "UPGRADE DATABASE");

        onCreate(db);
    }

    public void createTimer(TimerList timer)
    {
        SQLiteDatabase db = this.getWritableDatabase();





        Cursor c = null;
        c = db.rawQuery("pragma table_info (TAB_TIMERS)", null);


        if (c .moveToFirst()) {

            while (c.isAfterLast() == false) {
                String txt = "";

                for (int i = 0; i < c.getColumnCount(); i++) {

                    try {
                        txt += "  |  " + c.getString(i);
                    } catch (Exception e) {
                        txt += "  |  EXCEPTION";
                    }

                }
                Log.d("Timer4Everything", txt);

                c.moveToNext();
            }
        }





        ContentValues values = new ContentValues();

        values.put(KEY_NAME, timer.getName());
        values.put(KEY_TIME, timer.getTimer());
        values.put(KEY_INFO, "sad");  //timer.getInfo());         <- ist dies Zeile auskommentiert dann Funzt es -.-  ------ table timer has no column named info???

       long result = db.insertOrThrow(TABLE_TIMERS, null, values);
        Log.d("Timer4Everything", "InsertResult: " + result);

        db.insert(TABLE_TIMERS, null, values);
        db.close();
    }

    public TimerList getTimer(int id)
    {
        SQLiteDatabase db = getReadableDatabase();

        Cursor cursor = db.query(TABLE_TIMERS, new String[] { KEY_ID, KEY_NAME, KEY_TIME}, KEY_ID + "=?", new String[] { String.valueOf(id) }, null, null, null, null);   //nach time kommt eig KEY_INFO

        if(cursor != null)
            cursor.moveToFirst();

        TimerList timer = new TimerList(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3));
        
        cursor.close();
        db.close();
        
        return timer;
    }

    public void deleteTimer(TimerList timer)
    {
        SQLiteDatabase db = getWritableDatabase();
        
        db.delete(TABLE_TIMERS, KEY_ID + "=?", new String[] { String.valueOf(timer.getId()) });
        
        db.close();
    }

    public int getTimersCount()
    {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMERS, null);

        int count = cursor.getCount();
        
        cursor.close();
        db.close();

        return count;
    }

    public int updateContact(TimerList timer)
    {
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(KEY_NAME, timer.getName());
        values.put(KEY_TIME, timer.getTimer());
        values.put(KEY_INFO, timer.getInfo());
        
        int result = db.update(TABLE_TIMERS, values, KEY_ID + "=?", new String[] { String.valueOf(timer.getId()) });
        
        db.close();
        
        return result;
    }

    public List<TimerList> getAllTimer()
    {
        List<TimerList> timers = new ArrayList<TimerList>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_TIMERS, null);

        if(cursor.moveToFirst())
        {
            do{
                TimerList timer = new TimerList(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2), "hallo"); //cursor.getString(3));
                timers.add(timer);
            }
            while(cursor.moveToNext());
        }
        
        cursor.close();
        db.close();
        
        return timers;
    }
}

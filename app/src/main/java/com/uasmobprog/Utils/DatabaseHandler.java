package com.uasmobprog.Utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import com.uasmobprog.Model.Employee;
import com.uasmobprog.Model.Location;
import com.uasmobprog.Model.Name;
import com.uasmobprog.Model.Picture;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String NAME = "addressBook";
    private static final String TABLE = "addressBookTable";
    private static final String ID = "id";
    private static final String FIRSTNAME = "firstname";
    private static final String LASTNAME = "lastname";
    private static final String CELL = "cell";
    private static final String EMAIL = "email";
    private static final String CITY = "city";
    private static final String COUNTRY = "country";
    private static final String IMG = "profile_picture";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE + "(" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + FIRSTNAME + " TEXT, "
            +LASTNAME + " TEXT, " + CELL + " TEXT, " + EMAIL +" TEXT, "+ CITY + " TEXT, "+ COUNTRY + " TEXT, "+ IMG + " TEXT)";

    private SQLiteDatabase db;

    public DatabaseHandler(Context context) {
        super(context, NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        // Create tables again
        onCreate(db);
    }

    public void openDatabase() {
        db = this.getWritableDatabase();
    }

    public void insertEmployee(Employee emp){
        ContentValues cv = new ContentValues();
        cv.put(EMAIL, emp.getEmail());
        cv.put(FIRSTNAME, emp.getName().getFirst());
        cv.put(LASTNAME, emp.getName().getLast());
        cv.put(CELL, emp.getCell());
        cv.put(CITY, emp.getLocation().getCity());
        cv.put(COUNTRY,emp.getLocation().getCountry());
        cv.put(IMG, emp.getPicture().getLarge());

        db.insert(TABLE, null, cv);
    }
    public boolean isExists(Employee emp){
        db.beginTransaction();
            String Query = "Select * from " + TABLE + " where " + EMAIL + " = '" + emp.getEmail()+"'";
        Cursor cursor = null;
            try {
                cursor = db.rawQuery(Query, null);
                if (cursor.getCount() <= 0) {
                    cursor.close();
                    return false;
                }
            }finally {
            db.endTransaction();
                assert cursor != null;
                cursor.close();
            }
            return true;
    }
    public List<Employee> getAllEmployee(){
        List<Employee> empList = new ArrayList<>();
        Cursor cur=null;
        db.beginTransaction();
        try{
            cur = db.query(TABLE, null, null, null, null, null, null, null);
            if(cur != null){
                if(cur.moveToFirst()){
                    do{
                        Employee emp = new Employee();
                        emp.setEmployeeId(cur.getInt(0));
                        emp.setEmail(cur.getString(4));
                        Name name = new Name();
                        name.setFirst(cur.getString(1));
                        name.setLast(cur.getString(2));
                        emp.setName(name);
                        emp.setCell(cur.getString(3));
                        Location loc = new Location();
                        loc.setCity(cur.getString(5));
                        loc.setCountry(cur.getString(6));
                        emp.setLocation(loc);
                        Picture pic = new Picture();
                        pic.setLarge(cur.getString(7));
                        emp.setPicture(pic);
                        empList.add(emp);
                    }
                    while(cur.moveToNext());
                }
            }
        }
        finally {
            db.endTransaction();
            assert cur != null;
            cur.close();
        }
        return empList;
    }


}

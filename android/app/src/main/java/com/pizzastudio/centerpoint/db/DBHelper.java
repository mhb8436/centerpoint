package com.pizzastudio.centerpoint.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.pizzastudio.centerpoint.db.model.CenterPoint;
import com.pizzastudio.centerpoint.db.model.Meet;
import com.pizzastudio.centerpoint.db.model.Participant;
import com.pizzastudio.centerpoint.db.model.Place;
import com.pizzastudio.centerpoint.db.model.Restaurant;
import com.pizzastudio.centerpoint.db.model.Route;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final String TAG = DBHelper.class.getCanonicalName();
    private static DBHelper sInstance;

    public static synchronized DBHelper getInstance(Context context) {

        if (sInstance == null) {
            sInstance = new DBHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "centerpoint_db";


    public DBHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.d(TAG, "DBHelper onCreate method called ");
        db.execSQL(Meet.CREATE_TABLE);
        db.execSQL(Participant.CREATE_TABLE);
        db.execSQL(CenterPoint.CREATE_TABLE);
        db.execSQL(Restaurant.CREATE_TABLE);
        db.execSQL(Route.CREATE_TABLE);
        Log.d(TAG, "create table script : " + Place.CREATE_TABLE);
        db.execSQL(Place.CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        Log.d(TAG, "onUpgrade : "  + oldVersion + "=>" + newVersion);
        db.execSQL("DROP TABLE IF EXISTS " + Meet.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Participant.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + CenterPoint.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Restaurant.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Route.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + Place.TABLE_NAME);

        // Create tables again
        onCreate(db);
    }


    public class MeetDB {

        public long insert(String name, String date, String prefer, String timestamp){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Meet.COLUMN_NAME, name);
            values.put(Meet.COLUMN_DATE, date);
            values.put(Meet.COLUMN_PREFER, prefer);
            values.put(Meet.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(Meet.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Meet get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Meet.TABLE_NAME,
                    new String[]{Meet.COLUMN_ID, Meet.COLUMN_NAME, Meet.COLUMN_DATE, Meet.COLUMN_PREFER, Meet.COLUMN_TIMESTAMP},
                    Meet.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            Meet item = new Meet(
                    cursor.getInt(cursor.getColumnIndex(Meet.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Meet.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Meet.COLUMN_DATE)),
                    cursor.getString(cursor.getColumnIndex(Meet.COLUMN_PREFER)),
                    cursor.getString(cursor.getColumnIndex(Meet.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }

        public List<Meet> list(){
            List<Meet> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Meet.TABLE_NAME + " ORDER BY " +
                    Meet.COLUMN_TIMESTAMP + " DESC";
//            String selectQuery = "select a.*, b.cnt as participant_count, c.* from meet a, (select meet_id, count(1) as cnt from participant) p, centerpoint c where a.id = p.meet_id and a.id = c.meet_id";
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Meet one = new Meet();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Meet.COLUMN_ID)));
                    one.setName(cursor.getString(cursor.getColumnIndex(Meet.COLUMN_NAME)));
                    one.setDate(cursor.getString(cursor.getColumnIndex(Meet.COLUMN_DATE)));
                    one.setPrefer(cursor.getString(cursor.getColumnIndex(Meet.COLUMN_PREFER)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(Meet.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }

        public List<Meet> listForView(){
            List<Meet> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "select a.*, (select count(1) from participant p where p.meet_id = a.id ) as participant_count, (select name from centerpoint c where c.meet_id = a.id )as centerpoint_name from meet a where 1=1 order by a.id desc";
            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Meet one = new Meet(
                            cursor.getInt(cursor.getColumnIndex("id")),
                            cursor.getString(cursor.getColumnIndex("name")),
                            cursor.getString(cursor.getColumnIndex("date")),
                            cursor.getString(cursor.getColumnIndex("prefer")),
                            cursor.getString(cursor.getColumnIndex("timestamp")),
                            cursor.getInt(cursor.getColumnIndex("participant_count")),
                            cursor.getString(cursor.getColumnIndex("centerpoint_name"))
                    );

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }

        public int count(){
            String countQuery = "SELECT  * FROM " + Meet.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(Meet item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Meet.COLUMN_NAME, item.getName());
            values.put(Meet.COLUMN_DATE, item.getDate());
            values.put(Meet.COLUMN_PREFER, item.getPrefer());
            values.put(Meet.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(Meet.TABLE_NAME, values, Meet.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(Meet item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Meet.TABLE_NAME, Meet.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }

        public void deleteAll(){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Meet.TABLE_NAME, "1=?", new String[]{"1"});
            db.close();
        }


    } // end of MeetDB


    public class ParticipantDB {
        public long insert(String name, String type, String place, int place_id, double latitude, double longitude, String address, int meet_id){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Participant.COLUMN_NAME, name);
            values.put(Participant.COLUMN_TYPE, type);
            values.put(Participant.COLUMN_PLACE, place);
            values.put(Participant.COLUMN_PLACE_ID, place_id);
            values.put(Participant.COLUMN_LATITUDE, latitude);
            values.put(Participant.COLUMN_LONGITUDE, longitude);
            values.put(Participant.COLUMN_ADDRESS, address);
            values.put(Participant.COLUMN_MEET_ID, meet_id);

            // insert row
            long id = db.insert(Participant.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Participant get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Participant.TABLE_NAME,
                    new String[]{Participant.COLUMN_ID, Participant.COLUMN_NAME, Participant.COLUMN_TYPE, Participant.COLUMN_PLACE, Participant.COLUMN_PLACE_ID, Participant.COLUMN_LATITUDE, Participant.COLUMN_LONGITUDE, Participant.COLUMN_ADDRESS, Participant.COLUMN_MEET_ID, Participant.COLUMN_TIMESTAMP},
                    Participant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            Participant item = new Participant(
                    cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_NAME)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_PLACE)),
                    cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_PLACE_ID)),
                    cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_MEET_ID)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }

        public List<Participant> list(){
            List<Participant> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Participant.TABLE_NAME + " ORDER BY " +
                    Participant.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Participant one = new Participant();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_ID)));
                    one.setName(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_NAME)));
                    one.setType(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TYPE)));
                    one.setPlace(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_PLACE)));
                    one.setPlace_id(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_PLACE_ID)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LONGITUDE)));
                    one.setAddress(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_ADDRESS)));
                    one.setMeet_id(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_MEET_ID)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }

        public List<Participant> findByMeetID(int meet_id){
            List<Participant> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Participant.TABLE_NAME + " WHERE "+Participant.COLUMN_MEET_ID + " = " + meet_id  +  " ORDER BY " +
                    Participant.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Participant one = new Participant();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_ID)));
                    one.setName(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_NAME)));
                    one.setType(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TYPE)));
                    one.setPlace(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_PLACE)));
                    one.setPlace_id(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_PLACE_ID)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LONGITUDE)));
                    one.setAddress(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_ADDRESS)));
                    one.setMeet_id(cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_MEET_ID)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }


        public int count(){
            String countQuery = "SELECT  * FROM " + Participant.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(Participant item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Participant.COLUMN_NAME, item.getName());
            values.put(Participant.COLUMN_TYPE, item.getType());
            values.put(Participant.COLUMN_PLACE, item.getPlace());
            values.put(Participant.COLUMN_PLACE_ID, item.getPlace_id());
            values.put(Participant.COLUMN_LATITUDE, item.getLatitude());
            values.put(Participant.COLUMN_LONGITUDE, item.getLongitude());
            values.put(Participant.COLUMN_ADDRESS, item.getAddress());
            values.put(Participant.COLUMN_MEET_ID, item.getMeet_id());
            values.put(Participant.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(Participant.TABLE_NAME, values, Participant.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(Participant item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Participant.TABLE_NAME, Participant.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }

        public void deleteAll(int meetID){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Participant.TABLE_NAME, Participant.COLUMN_MEET_ID + "=?", new String[]{String.valueOf(meetID)});
            db.close();
        }


        public void updateForDev(){
            SQLiteDatabase db = getWritableDatabase();

//            db.execSQL("update participant set place_id = place");
            db.execSQL("update participant set place_id = 4");
        }

    } // end of ParticipantDB


    public class CenterPointDB {

        public long insert(String name, double latitude, double longitude, int meet_id, String timestamp){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(CenterPoint.COLUMN_NAME, name);
            values.put(CenterPoint.COLUMN_LATITUDE, latitude);
            values.put(CenterPoint.COLUMN_LONGITUDE, longitude);
            values.put(CenterPoint.COLUMN_MEET_ID, meet_id);
            values.put(CenterPoint.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(CenterPoint.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public CenterPoint get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(CenterPoint.TABLE_NAME,
                    new String[]{CenterPoint.COLUMN_ID, CenterPoint.COLUMN_NAME, CenterPoint.COLUMN_LATITUDE, CenterPoint.COLUMN_LONGITUDE, CenterPoint.COLUMN_MEET_ID, CenterPoint.COLUMN_TIMESTAMP},
                    Participant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            CenterPoint item = new CenterPoint(
                    cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(Participant.COLUMN_LONGITUDE)),
                    cursor.getInt(cursor.getColumnIndex(Participant.COLUMN_MEET_ID)),
                    cursor.getString(cursor.getColumnIndex(Participant.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }

        public List<CenterPoint> list(){
            List<CenterPoint> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + CenterPoint.TABLE_NAME + " ORDER BY " +
                    CenterPoint.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    CenterPoint one = new CenterPoint();
                    one.setId(cursor.getInt(cursor.getColumnIndex(CenterPoint.COLUMN_ID)));
                    one.setName(cursor.getString(cursor.getColumnIndex(CenterPoint.COLUMN_NAME)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(CenterPoint.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(CenterPoint.COLUMN_LONGITUDE)));
                    one.setMeet_id(cursor.getInt(cursor.getColumnIndex(CenterPoint.COLUMN_MEET_ID)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(CenterPoint.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }


        public int count(){
            String countQuery = "SELECT  * FROM " + CenterPoint.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(CenterPoint item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(CenterPoint.COLUMN_NAME, item.getName());
            values.put(CenterPoint.COLUMN_LATITUDE, item.getLatitude());
            values.put(CenterPoint.COLUMN_LONGITUDE, item.getLongitude());
            values.put(CenterPoint.COLUMN_MEET_ID, item.getMeet_id());
            values.put(CenterPoint.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(CenterPoint.TABLE_NAME, values, CenterPoint.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(CenterPoint item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(CenterPoint.TABLE_NAME, CenterPoint.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }

        public void deleteAll(){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(CenterPoint.TABLE_NAME, "1=?", new String[]{"1"});
            db.close();
        }

    } // end of CenterPointDB

    public class RestaurantDB {

        public long insert(String name, double latitude, double longitude, String type, String address, int price, int score, int meet_id, String timestamp){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Restaurant.COLUMN_NAME, name);
            values.put(Restaurant.COLUMN_LATITUDE, latitude);
            values.put(Restaurant.COLUMN_LONGITUDE, longitude);
            values.put(Restaurant.COLUMN_TYPE, type);
            values.put(Restaurant.COLUMN_ADDRESS, address);
            values.put(Restaurant.COLUMN_PRICE, price);
            values.put(Restaurant.COLUMN_SCORE, score);
            values.put(Restaurant.COLUMN_MEET_ID, meet_id);
            values.put(Restaurant.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(Restaurant.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Restaurant get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Restaurant.TABLE_NAME,
                    new String[]{Restaurant.COLUMN_ID, Restaurant.COLUMN_NAME, Restaurant.COLUMN_LATITUDE, Restaurant.COLUMN_LONGITUDE, Restaurant.COLUMN_TYPE, Restaurant.COLUMN_ADDRESS, Restaurant.COLUMN_PRICE, Restaurant.COLUMN_SCORE, Restaurant.COLUMN_MEET_ID, Restaurant.COLUMN_TIMESTAMP},
                    Restaurant.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            Restaurant item = new Restaurant(
                    cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_NAME)),
                    cursor.getDouble(cursor.getColumnIndex(Restaurant.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(Restaurant.COLUMN_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_TYPE)),
                    cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_ADDRESS)),
                    cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_PRICE)),
                    cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_SCORE)),
                    cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_MEET_ID)),
                    cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }

        public List<Restaurant> list(){
            List<Restaurant> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Restaurant.TABLE_NAME + " ORDER BY " +
                    Restaurant.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Restaurant one = new Restaurant();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_ID)));
                    one.setName(cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_NAME)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(Restaurant.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(Restaurant.COLUMN_LONGITUDE)));
                    one.setType(cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_TYPE)));
                    one.setAddress(cursor.getString(cursor.getColumnIndex(Restaurant.COLUMN_ADDRESS)));
                    one.setPrice(cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_PRICE)));
                    one.setScore(cursor.getInt(cursor.getColumnIndex(Restaurant.COLUMN_SCORE)));
                    one.setMeet_id(cursor.getInt(cursor.getColumnIndex(CenterPoint.COLUMN_MEET_ID)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(CenterPoint.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }


        public int count(){
            String countQuery = "SELECT  * FROM " + Restaurant.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(Restaurant item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Restaurant.COLUMN_NAME, item.getName());
            values.put(Restaurant.COLUMN_LATITUDE, item.getLatitude());
            values.put(Restaurant.COLUMN_LONGITUDE, item.getLongitude());
            values.put(Restaurant.COLUMN_TYPE, item.getType());
            values.put(Restaurant.COLUMN_ADDRESS, item.getAddress());
            values.put(Restaurant.COLUMN_PRICE, item.getPrice());
            values.put(Restaurant.COLUMN_SCORE, item.getScore());
            values.put(Restaurant.COLUMN_MEET_ID, item.getMeet_id());
            values.put(Restaurant.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(Restaurant.TABLE_NAME, values, Restaurant.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(Restaurant item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Restaurant.TABLE_NAME, Restaurant.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }
    } // end of RestaurantDB


    public class RouteDB {

        public long insert(int participant_id, long duration, String transit, String encoded_polyline, String timestamp){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Route.COLUMN_PARTICIPANT_ID, participant_id);
            values.put(Route.COLUMN_DURATION, duration);
            values.put(Route.COLUMN_TRANSIT, transit);
            values.put(Route.COLUMN_ENCODED_POLYLINE, encoded_polyline);
            values.put(Route.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(Route.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Route get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Route.TABLE_NAME,
                    new String[]{Route.COLUMN_ID, Route.COLUMN_PARTICIPANT_ID, Route.COLUMN_DURATION, Route.COLUMN_TRANSIT, Route.COLUMN_ENCODED_POLYLINE, Route.COLUMN_TIMESTAMP},
                    Route.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            Route item = new Route(
                    cursor.getInt(cursor.getColumnIndex(Route.COLUMN_ID)),
                    cursor.getInt(cursor.getColumnIndex(Route.COLUMN_PARTICIPANT_ID)),
                    cursor.getLong(cursor.getColumnIndex(Route.COLUMN_DURATION)),
                    cursor.getString(cursor.getColumnIndex(Route.COLUMN_TRANSIT)),
                    cursor.getString(cursor.getColumnIndex(Route.COLUMN_ENCODED_POLYLINE)),
                    cursor.getString(cursor.getColumnIndex(Route.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }

        public List<Route> list(){
            List<Route> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Route.TABLE_NAME + " ORDER BY " +
                    Route.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Route one = new Route();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Route.COLUMN_ID)));
                    one.setParticipant_id(cursor.getInt(cursor.getColumnIndex(Route.COLUMN_PARTICIPANT_ID)));
                    one.setDuration(cursor.getLong(cursor.getColumnIndex(Route.COLUMN_DURATION)));
                    one.setTransit(cursor.getString(cursor.getColumnIndex(Route.COLUMN_TRANSIT)));
                    one.setEncoded_polyline(cursor.getString(cursor.getColumnIndex(Route.COLUMN_ENCODED_POLYLINE)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(CenterPoint.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }


        public int count(){
            String countQuery = "SELECT  * FROM " + Route.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(Route item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Route.COLUMN_PARTICIPANT_ID, item.getParticipant_id());
            values.put(Route.COLUMN_DURATION, item.getDuration());
            values.put(Route.COLUMN_TRANSIT, item.getTransit());
            values.put(Route.COLUMN_ENCODED_POLYLINE, item.getEncoded_polyline());
            values.put(Route.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(Route.TABLE_NAME, values, Route.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(Route item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Route.TABLE_NAME, Route.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }
    } // end of RouteDB

    public class PlaceDB {

        public long insert(String display_name, String place_id, String search_string, Double latitude, Double longitude, String address, String timestamp){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            // `id` and `timestamp` will be inserted automatically.
            // no need to add them
            values.put(Place.COLUMN_DISPLAY_NAME, display_name);
            values.put(Place.COLUMN_PLACE_ID, place_id);
            values.put(Place.COLUMN_SEARCH_STRING, search_string);
            values.put(Place.COLUMN_LATITUDE, latitude);
            values.put(Place.COLUMN_LONGITUDE, longitude);
            values.put(Place.COLUMN_ADDRESS, address);
            values.put(Place.COLUMN_TIMESTAMP, timestamp);

            // insert row
            long id = db.insert(Place.TABLE_NAME, null, values);

            // close db connection
            db.close();

            // return newly inserted row id
            return id;
        }

        public Place get(long id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Place.TABLE_NAME,
                    new String[]{Place.COLUMN_ID, Place.COLUMN_DISPLAY_NAME, Place.COLUMN_PLACE_ID, Place.COLUMN_LATITUDE, Place.COLUMN_LONGITUDE, Place.COLUMN_ADDRESS, Place.COLUMN_SEARCH_STRING, Route.COLUMN_TIMESTAMP},
                    Place.COLUMN_ID + "=?",
                    new String[]{String.valueOf(id)}, null, null, null, null);

            if (cursor != null)
                cursor.moveToFirst();

            // prepare note object
            // int id, String display_name, String place_id, String search_string, Double latitude, Double longitude, String address, String timestamp
            Place item = new Place(
                    cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID)),
                    cursor.getString(cursor.getColumnIndex(Place.COLUMN_DISPLAY_NAME)),
                    cursor.getString(cursor.getColumnIndex(Place.COLUMN_PLACE_ID)),
                    cursor.getString(cursor.getColumnIndex(Place.COLUMN_SEARCH_STRING)),
                    cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LATITUDE)),
                    cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LONGITUDE)),
                    cursor.getString(cursor.getColumnIndex(Place.COLUMN_ADDRESS)),
                    cursor.getString(cursor.getColumnIndex(Place.COLUMN_TIMESTAMP)));

            // close the db connection
            cursor.close();

            return item;
        }



        public int existPlace(String place_id){
            SQLiteDatabase db = getReadableDatabase();

            Cursor cursor = db.query(Place.TABLE_NAME,
                    new String[]{Place.COLUMN_ID, Place.COLUMN_DISPLAY_NAME, Place.COLUMN_PLACE_ID, Place.COLUMN_LATITUDE, Place.COLUMN_LONGITUDE, Place.COLUMN_ADDRESS, Place.COLUMN_SEARCH_STRING, Route.COLUMN_TIMESTAMP},
                    Place.COLUMN_PLACE_ID + "=?",
                    new String[]{String.valueOf(place_id)}, null, null, null, null);

            int count = cursor.getCount();
            if(count > 0){
                cursor.moveToFirst();
                return cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID));
            }
            return -1;

        }

        public List<Place> list(){
            List<Place> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Place.TABLE_NAME + " ORDER BY " +
                    Place.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Place one = new Place();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID)));
                    one.setDisplay_name(cursor.getString(cursor.getColumnIndex(Place.COLUMN_DISPLAY_NAME)));
                    one.setPlace_id(cursor.getString(cursor.getColumnIndex(Place.COLUMN_PLACE_ID)));
                    one.setSearch_string(cursor.getString(cursor.getColumnIndex(Place.COLUMN_SEARCH_STRING)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LONGITUDE)));
                    one.setAddress(cursor.getString(cursor.getColumnIndex(Place.COLUMN_ADDRESS)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(Place.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }

        public List<Place> findBySearchString(String searchString){
            List<Place> items = new ArrayList<>();

            // Select All Query
            String selectQuery = "SELECT  * FROM " + Place.TABLE_NAME + " WHERE "+Place.COLUMN_SEARCH_STRING+" like '%"+searchString+"%' ORDER BY " +
                    Place.COLUMN_TIMESTAMP + " DESC";

            SQLiteDatabase db = getWritableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            // looping through all rows and adding to list
            if (cursor.moveToFirst()) {
                do {
                    Place one = new Place();
                    one.setId(cursor.getInt(cursor.getColumnIndex(Place.COLUMN_ID)));
                    one.setDisplay_name(cursor.getString(cursor.getColumnIndex(Place.COLUMN_DISPLAY_NAME)));
                    one.setPlace_id(cursor.getString(cursor.getColumnIndex(Place.COLUMN_PLACE_ID)));
                    one.setSearch_string(cursor.getString(cursor.getColumnIndex(Place.COLUMN_SEARCH_STRING)));
                    one.setLatitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LATITUDE)));
                    one.setLongitude(cursor.getDouble(cursor.getColumnIndex(Place.COLUMN_LONGITUDE)));
                    one.setAddress(cursor.getString(cursor.getColumnIndex(Place.COLUMN_ADDRESS)));
                    one.setTimestamp(cursor.getString(cursor.getColumnIndex(Place.COLUMN_TIMESTAMP)));

                    items.add(one);
                } while (cursor.moveToNext());
            }

            // close db connection
            db.close();

            // return notes list
            return items;

        }

        public int count(){
            String countQuery = "SELECT  * FROM " + Place.TABLE_NAME;
            SQLiteDatabase db = getReadableDatabase();
            Cursor cursor = db.rawQuery(countQuery, null);
            int count = cursor.getCount();
            cursor.close();

            // return count
            return count;
        }

        public int update(Place item){
            SQLiteDatabase db = getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(Place.COLUMN_DISPLAY_NAME, item.getDisplay_name());
            values.put(Place.COLUMN_PLACE_ID, item.getPlace_id());
            values.put(Place.COLUMN_SEARCH_STRING, item.getSearch_string());
            values.put(Place.COLUMN_TIMESTAMP, item.getTimestamp());

            // updating row
            return db.update(Place.TABLE_NAME, values, Place.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
        }


        public void delete(Place item){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Place.TABLE_NAME, Place.COLUMN_ID + " = ?",
                    new String[]{String.valueOf(item.getId())});
            db.close();
        }

        public void deleteAll(){
            SQLiteDatabase db = getWritableDatabase();
            db.delete(Place.TABLE_NAME, "1=?", new String[]{"1"});
            db.close();
        }

    } // end of PlaceDB


} // end of DBHelper

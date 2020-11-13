package appinventor.ai_plumtreeinbackyard.SoapCal_Lite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Janet on 10/20/2016.
 */
public class DBHelper extends SQLiteOpenHelper {
    // Database name
    private static final String DATABASE_NAME = "SoapCal.db";
    // Database version
    private static final int DATABASE_VERSION = 3;
    // Table Name
    private static final String TABLE_NAME_ONE = "soap_recipe_value";
    private static final String TABLE_NAME_TWO = "name_percentage";
    private static final String TABLE_NAME_THREE = "checkbox_selected";
    private static final String TABLE_NAME_FOUR = "name_percentage_by_percentage";
    private static final String TABLE_NAME_FIVE = "checkbox_selected_by_percentage";

    // Table one columns
    public static final String KEY_ID_ONE = "Id_one";
    public static final String KEY_NAOH_DISCOUNT = "NaOH_discount";
    public static final String KEY_NAOH_PURITY = "NaOH_purity";
    public static final String KEY_WATER_TIMES = "water_times";
    public static final String KEY_SF = "sf";
    public static final String KEY_SF_NAME = "sf_name";
    public static final String KEY_EO = "eo";
    public static final String KEY_EO_NAME = "eo_name";
    public static final String KEY_AD = "ad";
    public static final String KEY_AD_NAME = "ad_name";
    public static final String KEY_TOTAL_OIL_WEIGHT = "total_oil_weight";

    // Table two columns
    public static final String KEY_ID_TWO = "Id_two";
    public static final String KEY_OIL_NAME = "oil_name";
    public static final String KEY_OIL_WEIGHT = "oil_weight";

    // Table three columns
    public static final String KEY_ID_THREE= "Id_three";
    public static final String KEY_CHECKED = "checked";

    // Table four columns
    public static final String KEY_ID_FOUR = "Id_four";
    public static final String KEY_OIL_NAME_BY_PERCENTAGE = "oil_name";
    public static final String KEY_OIL_PERCENTAGE = "oil_percentage";

    // Table five columns
    public static final String KEY_ID_FIVE= "Id_five";
    public static final String KEY_CHECKED_BY_PERCENTAGE = "checked_by_percentage";

    // Creating table one query
    private static final String CREATE_TABLE_ONE = "CREATE TABLE " + TABLE_NAME_ONE + " ("
            + KEY_ID_ONE + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + KEY_NAOH_DISCOUNT + " TEXT , "
            + KEY_NAOH_PURITY + " TEXT , "
            + KEY_WATER_TIMES + " TEXT , "
            + KEY_SF + " TEXT , "
            + KEY_SF_NAME + " TEXT , "
            + KEY_EO + " TEXT , "
            + KEY_EO_NAME + " TEXT , "
            + KEY_AD + " TEXT , "
            + KEY_AD_NAME + " TEXT , "
            + KEY_TOTAL_OIL_WEIGHT + " TEXT)";

    // Creating table two query
    private static final String CREATE_TABLE_TWO = "CREATE TABLE " + TABLE_NAME_TWO + " ("
            + KEY_ID_TWO + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + KEY_OIL_NAME + " TEXT , "
            + KEY_OIL_WEIGHT + " TEXT)";

    // Creating table three query
    private static final String CREATE_TABLE_THREE = "CREATE TABLE " + TABLE_NAME_THREE + " ("
            + KEY_ID_THREE + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + KEY_CHECKED + " TEXT)";

    // Creating table four query
    private static final String CREATE_TABLE_FOUR = "CREATE TABLE " + TABLE_NAME_FOUR + " ("
            + KEY_ID_FOUR + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + KEY_OIL_NAME_BY_PERCENTAGE + " TEXT , "
            + KEY_OIL_PERCENTAGE + " TEXT)";

    // Creating table five query
    private static final String CREATE_TABLE_FIVE = "CREATE TABLE " + TABLE_NAME_FIVE + " ("
            + KEY_ID_FIVE + " INTEGER PRIMARY KEY AUTOINCREMENT , "
            + KEY_CHECKED_BY_PERCENTAGE + " TEXT)";

    // Table one alter query -- add KEY_AD column
    private static final String TABLE1_ALTER_1 = "ALTER TABLE "
            + TABLE_NAME_ONE + " ADD COLUMN " + KEY_AD + " TEXT";

    // Table one alter query -- add KEY_AD_NAME column
    private static final String TABLE1_ALTER_2 = "ALTER TABLE "
            + TABLE_NAME_ONE + " ADD COLUMN " + KEY_AD_NAME + " TEXT";

    // Table one alter query -- add KEY_NAOH_PURITY column
    private static final String TABLE1_ALTER_3 = "ALTER TABLE "
            + TABLE_NAME_ONE + " ADD COLUMN " + KEY_NAOH_PURITY + " TEXT";

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_ONE);
        db.execSQL(CREATE_TABLE_TWO);
        db.execSQL(CREATE_TABLE_THREE);
        db.execSQL(CREATE_TABLE_FOUR);
        db.execSQL(CREATE_TABLE_FIVE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        switch(oldVersion) {
            case 1:
                db.execSQL(TABLE1_ALTER_1);
                db.execSQL(TABLE1_ALTER_2);
            case 2:
                db.execSQL(TABLE1_ALTER_3);
        }
    }

    /*public boolean doesTableExist(String tableName) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("select DISTINCT tbl_name from sqlite_master where tbl_name = '" + tableName + "'", null);
        if (cursor.getCount() > 0) {
            cursor.close();
            db.close();
            return true;
        }
        else {
            cursor.close();
            db.close();
            return false;
        }
    }*/

    public int getTableTwoListLength() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_TWO, new String[]{KEY_ID_TWO, KEY_OIL_NAME, KEY_OIL_WEIGHT}, null,null, null, null, null);
        int length = cursor.getCount();
        cursor.close();
        db.close();
        return length;
    }

    public int getTableThreeListLength() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_THREE, new String[]{KEY_ID_THREE, KEY_CHECKED}, null,null, null, null, null);
        int length = cursor.getCount();
        cursor.close();
        db.close();
        return length;
    }

    public int getTableFourListLength() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FOUR, new String[]{KEY_ID_FOUR, KEY_OIL_NAME_BY_PERCENTAGE, KEY_OIL_PERCENTAGE}, null,null, null, null, null);
        int length = cursor.getCount();
        cursor.close();
        db.close();
        return length;
    }

    public int getTableFiveListLength() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME_FIVE, new String[]{KEY_ID_FIVE, KEY_CHECKED_BY_PERCENTAGE}, null,null, null, null, null);
        int length = cursor.getCount();
        cursor.close();
        db.close();
        return length;
    }

    public ArrayList<Integer> getTableOneIdArray() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_ONE, KEY_NAOH_DISCOUNT, KEY_NAOH_PURITY, KEY_WATER_TIMES, KEY_SF,
                KEY_SF_NAME, KEY_EO, KEY_EO_NAME, KEY_AD, KEY_AD_NAME, KEY_TOTAL_OIL_WEIGHT};
        Cursor cursor = database.query(TABLE_NAME_ONE, columns, null,null, null, null, null);
        ArrayList<Integer> ids = new ArrayList<>();
        if (cursor != null && cursor.getCount()>0) {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                ids.add(cursor.getInt(cursor.getColumnIndex(KEY_ID_ONE)));
                cursor.moveToNext();
            }
            cursor.close();
            database.close();
        }
        return ids;
    }

    public void insertTableOneById(int id, String NaOH_discount, String NaOH_purity, String water_times, String sf,
                                   String sf_name, String eo, String eo_name, String ad, String ad_name, String total_oil_weight) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID_ONE, id);
        contentValue.put(KEY_NAOH_DISCOUNT, NaOH_discount);
        contentValue.put(KEY_NAOH_PURITY, NaOH_purity);
        contentValue.put(KEY_WATER_TIMES, water_times);
        contentValue.put(KEY_SF, sf);
        contentValue.put(KEY_SF_NAME, sf_name);
        contentValue.put(KEY_EO, eo);
        contentValue.put(KEY_EO_NAME, eo_name);
        contentValue.put(KEY_AD, ad);
        contentValue.put(KEY_AD_NAME, ad_name);
        contentValue.put(KEY_TOTAL_OIL_WEIGHT, total_oil_weight);
        database.insert(TABLE_NAME_ONE, null, contentValue);
        database.close();
    }

    public void insertTableTwoById(int id, String name, String weight) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID_TWO, id);
        contentValue.put(KEY_OIL_NAME, name);
        contentValue.put(KEY_OIL_WEIGHT, weight);
        database.insert(TABLE_NAME_TWO, null, contentValue);
        database.close();
    }

    public void insertTableThreeById(int id, String ifChecked) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID_THREE, id);
        contentValue.put(KEY_CHECKED, ifChecked);
        database.insert(TABLE_NAME_THREE, null, contentValue);
        database.close();
    }

    public void insertTableFourById(int id, String name, String percentage) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID_FOUR, id);
        contentValue.put(KEY_OIL_NAME_BY_PERCENTAGE, name);
        contentValue.put(KEY_OIL_PERCENTAGE, percentage);
        database.insert(TABLE_NAME_FOUR, null, contentValue);
        database.close();
    }

    public void insertTableFiveById(int id, String ifChecked) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(KEY_ID_FIVE, id);
        contentValue.put(KEY_CHECKED_BY_PERCENTAGE, ifChecked);
        database.insert(TABLE_NAME_FIVE, null, contentValue);
        database.close();
    }

    public String getTableOneContent(int id, String singleColumn) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_ONE, KEY_NAOH_DISCOUNT, KEY_NAOH_PURITY, KEY_WATER_TIMES, KEY_SF,
                KEY_SF_NAME, KEY_EO, KEY_EO_NAME, KEY_AD, KEY_AD_NAME,KEY_TOTAL_OIL_WEIGHT};
        Cursor cursor = database.query(TABLE_NAME_ONE, columns, KEY_ID_ONE + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        cursor.moveToFirst();
        String content = cursor.getString(cursor.getColumnIndex(singleColumn));
        cursor.close();
        database.close();
        return content;
    }

    public Boolean getTableThreeContent(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_THREE, KEY_CHECKED};
        Cursor cursor = database.query(TABLE_NAME_THREE, columns, KEY_ID_THREE + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        cursor.moveToFirst();
        String content = cursor.getString(cursor.getColumnIndex(KEY_CHECKED));
        cursor.close();
        database.close();
        return Boolean.parseBoolean(content);
    }

    public Boolean getTableFiveContent(int id) {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_FIVE, KEY_CHECKED_BY_PERCENTAGE};
        Cursor cursor = database.query(TABLE_NAME_FIVE, columns, KEY_ID_FIVE + "=?", new String[] { String.valueOf(id) }, null, null, null, null);
        cursor.moveToFirst();
        String content = cursor.getString(cursor.getColumnIndex(KEY_CHECKED_BY_PERCENTAGE));
        cursor.close();
        database.close();
        return Boolean.parseBoolean(content);
    }

    public void updateTableOne(int id, String NaOH_discount, String NaOH_purity, String water_times, String sf,
                               String sf_name, String eo, String eo_name, String ad, String ad_name, String total_oil_weight) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_NAOH_DISCOUNT, NaOH_discount);
        contentValues.put(KEY_NAOH_PURITY, NaOH_purity);
        contentValues.put(KEY_WATER_TIMES, water_times);
        contentValues.put(KEY_SF, sf);
        contentValues.put(KEY_SF_NAME, sf_name);
        contentValues.put(KEY_EO, eo);
        contentValues.put(KEY_EO_NAME, eo_name);
        contentValues.put(KEY_AD, ad);
        contentValues.put(KEY_AD_NAME, ad_name);
        contentValues.put(KEY_TOTAL_OIL_WEIGHT, total_oil_weight);
        database.update(TABLE_NAME_ONE, contentValues, KEY_ID_ONE + " = " + id, null);
        database.close();
    }

    public void updateTableThree(int id, String ifChecked) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CHECKED, ifChecked);
        database.update(TABLE_NAME_THREE, contentValues, KEY_ID_THREE + " = " + id, null);
        database.close();
    }

    public void updateTableFive(int id, String ifChecked) {
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(KEY_CHECKED_BY_PERCENTAGE, ifChecked);
        database.update(TABLE_NAME_FIVE, contentValues, KEY_ID_FIVE + " = " + id, null);
        database.close();
    }

    public ArrayList<String> getAllOilName() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_TWO, KEY_OIL_NAME, KEY_OIL_WEIGHT};
        Cursor cur = database.query(TABLE_NAME_TWO, columns, null,null, null, null, null);
        ArrayList<String> sf = new ArrayList<>();
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            sf.add(cur.getString(1));
            cur.moveToNext();
        }
        cur.close();
        database.close();
        return sf;
    }

    public ArrayList<String> getAllOilNameByPercentage() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_FOUR, KEY_OIL_NAME_BY_PERCENTAGE, KEY_OIL_PERCENTAGE};
        Cursor cur = database.query(TABLE_NAME_FOUR, columns, null,null, null, null, null);
        ArrayList<String> sf = new ArrayList<>();
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            sf.add(cur.getString(1));
            cur.moveToNext();
        }
        cur.close();
        database.close();
        return sf;
    }

    public ArrayList<String> getAllOilWeight() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_TWO, KEY_OIL_NAME, KEY_OIL_WEIGHT};
        Cursor cur = database.query(TABLE_NAME_TWO, columns, null,null, null, null, null);
        ArrayList<String> sf = new ArrayList<>();
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            sf.add(cur.getString(2));
            cur.moveToNext();
        }
        cur.close();
        database.close();
        return sf;
    }

    public ArrayList<String> getAllOilPercentage() {
        SQLiteDatabase database = this.getReadableDatabase();
        String[] columns = new String[]{KEY_ID_FOUR, KEY_OIL_NAME_BY_PERCENTAGE, KEY_OIL_PERCENTAGE};
        Cursor cur = database.query(TABLE_NAME_FOUR, columns, null,null, null, null, null);
        ArrayList<String> sf = new ArrayList<>();
        cur.moveToFirst();
        while (!cur.isAfterLast()) {
            sf.add(cur.getString(2));
            cur.moveToNext();
        }
        cur.close();
        database.close();
        return sf;
    }

    public void deleteTableOneById(int id) {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME_ONE, KEY_ID_ONE + "=" + id, null);
        database.close();
    }

    public void deleteAllTableTwo() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME_TWO, null, null);
        database.close();
    }

    public void deleteAllTableThree() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME_THREE, null, null);
        database.close();
    }

    public void deleteAllTableFour() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME_FOUR, null, null);
        database.close();
    }

    public void deleteAllTableFive() {
        SQLiteDatabase database = this.getWritableDatabase();
        database.delete(TABLE_NAME_FIVE, null, null);
        database.close();
    }
}

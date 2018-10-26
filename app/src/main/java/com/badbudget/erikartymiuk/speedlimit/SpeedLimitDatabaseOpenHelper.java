package com.badbudget.erikartymiuk.speedlimit;

import android.app.Activity;
import android.app.Application;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SpeedLimitDatabaseOpenHelper extends SQLiteOpenHelper {

    private Application context;

    private static SpeedLimitDatabaseOpenHelper sInstance;

    /* Current database version number */
    private static final int DATABASE_VERSION = 1;
    /* Bad budget database name */
    private static final String DATABASE_NAME = "SpeedLimitDatabase.db";

    /* Possible states of EULA Agreement */
    public static final int EULA_NOT_AGREE = 1;
    public static final int EULA_AGREED = 2;

    /* Constants for use in various sql statements */
    private static final String CREATE_TABLE = "CREATE TABLE";
    private static final String LEFT_PAREN = "(";
    private static final String RIGHT_PAREN = ")";
    private static final String SPACE = " ";

    private static final String GLOBAL_META_DATA_CREATE_TABLE =
            CREATE_TABLE + SPACE +
                    SpeedLimitDatabaseContract.GlobalMetaData.TABLE_NAME + LEFT_PAREN +
                    SpeedLimitDatabaseContract.GlobalMetaData.COLUMN_AGREED_EULA + SPACE + SpeedLimitDatabaseContract.GlobalMetaData.AGREED_EULA_TYPE +
                    RIGHT_PAREN;

    private SpeedLimitDatabaseOpenHelper(Application context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    public static synchronized SpeedLimitDatabaseOpenHelper getInstance(Activity context)
    {
        if (sInstance == null) {
            sInstance = new SpeedLimitDatabaseOpenHelper(context.getApplication());
        }
        return sInstance;
    }

    public void onCreate(SQLiteDatabase writeableDB)
    {
        System.out.println(GLOBAL_META_DATA_CREATE_TABLE);
        writeableDB.execSQL(GLOBAL_META_DATA_CREATE_TABLE);

        ContentValues globalMetaDataValues = new ContentValues();
        globalMetaDataValues.put(SpeedLimitDatabaseContract.GlobalMetaData.COLUMN_AGREED_EULA, EULA_NOT_AGREE);

        writeableDB.insert(SpeedLimitDatabaseContract.GlobalMetaData.TABLE_NAME, null, globalMetaDataValues);
    }

    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { }

}

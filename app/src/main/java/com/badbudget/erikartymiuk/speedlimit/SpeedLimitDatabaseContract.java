package com.badbudget.erikartymiuk.speedlimit;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.List;

public class SpeedLimitDatabaseContract {

    private static final String INTEGER_TYPE = "INTEGER";

    public SpeedLimitDatabaseContract() {};

    public static int getEulaAgreeStatus(SQLiteDatabase writeableDB)
    {
        String[] projection = {
                SpeedLimitDatabaseContract.GlobalMetaData.COLUMN_AGREED_EULA
        };

        Cursor cursor = writeableDB.query(
                SpeedLimitDatabaseContract.GlobalMetaData.TABLE_NAME,
                projection,
                null,
                null,
                null,
                null,
                null
        );
        int idIndex = cursor.getColumnIndexOrThrow(SpeedLimitDatabaseContract.GlobalMetaData.COLUMN_AGREED_EULA);

        int agreedEulaInt = 0;
        while (cursor.moveToNext()) {
            agreedEulaInt = cursor.getInt(idIndex);
        }

        return agreedEulaInt;
    }

    public static void setEulaAgreeStatus(SQLiteDatabase writeableDB, int status)
    {
        ContentValues globalMetaDataValues = new ContentValues();
        globalMetaDataValues.put(SpeedLimitDatabaseContract.GlobalMetaData.COLUMN_AGREED_EULA, status);

        writeableDB.insert(SpeedLimitDatabaseContract.GlobalMetaData.TABLE_NAME, null, globalMetaDataValues);
    }

    /**
     * Schema for Global Meta Data
     */
    public static class GlobalMetaData implements BaseColumns
    {
        public static final String TABLE_NAME = "globalMetaData";

        public static final String COLUMN_AGREED_EULA = "agreedEULA";
        public static final String AGREED_EULA_TYPE = INTEGER_TYPE;
    }

}

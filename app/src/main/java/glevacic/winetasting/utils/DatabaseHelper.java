package glevacic.winetasting.utils;

import android.content.Context;

import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

public class DatabaseHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "database.sqlite";
    private static final int DATABASE_VERSION = 5;

    public static final String TABLE = "tasks";
    public static final String ID = "_id";
    public static final String HEADING = "heading";
    public static final String DESCRIPTION = "description";
    public static final String CARD_TYPE = "type";
    public static final String[] COLUMNS = { HEADING, DESCRIPTION, CARD_TYPE };

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        setForcedUpgrade();
    }
}
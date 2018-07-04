package project.sergeev.organizer.organizer;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "test.db"; // название бд
    private static final int SCHEMA = 21; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OWNER = "OWNER";
    public static final String COLUMN_NOTE = "NOTE";
    public static final String COLUMN_DATE = "DATE";

    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_OWNER
                + " TEXT, " + COLUMN_NOTE + " TEXT, " + COLUMN_DATE + " TEXT);");
//        // добавление начальных данных
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_OWNER
//                + ", " + COLUMN_NOTE  + ", " + COLUMN_DATE + ") VALUES ('Ivan', 'Notice', '24.11.2017');");
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_OWNER
//                + ", " + COLUMN_NOTE  + ", " + COLUMN_DATE + ") VALUES ('Nexus', 'Message1', '13.12.2017');");
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_OWNER
//                + ", " + COLUMN_NOTE  + ", " + COLUMN_DATE + ") VALUES ('Nexus', 'Message3', '28.11.2017');");
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_OWNER
//                + ", " + COLUMN_NOTE  + ", " + COLUMN_DATE + ") VALUES ('Nexus', 'Message4', '28.11.2017');");
//        db.execSQL("INSERT INTO "+ TABLE +" (" + COLUMN_OWNER
//                + ", " + COLUMN_NOTE  + ", " + COLUMN_DATE + ") VALUES ('Nexus', 'Message5', '28.11.2017');");
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
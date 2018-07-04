package project.sergeev.organizer.organizer;

import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;
import android.content.Context;

public class usersDataBase extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "Access.db"; // название бд
    private static final int SCHEMA = 2; // версия базы данных
    static final String TABLE = "users"; // название таблицы в бд
    // названия столбцов
    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_OWNER = "OWNER";
    public static final String COLUMN_LOGIN = "LOGIN";
    public static final String COLUMN_PASSWORD = "PASSWORD";

    public usersDataBase(Context context) {
        super(context, DATABASE_NAME, null, SCHEMA);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE users (" + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_OWNER
                + " TEXT, " + COLUMN_LOGIN + " TEXT, " + COLUMN_PASSWORD + " INTEGER);");
    }//Создание БД при помощи метода CREATE

    //Метод одновления БД при помощи метода Upgrade
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE);
        onCreate(db);
    }
}
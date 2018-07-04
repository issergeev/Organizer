package project.sergeev.organizer.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddEvent extends AppCompatActivity {
    DatePicker datePicker;
    TimePicker timePicker;
    EditText editText;
    Button addButton, deleteButton;

    DataBaseHelper sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    SharedPreferences sharedPreferences;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event);

        sharedPreferences = getSharedPreferences("LoginDate", Context.MODE_PRIVATE);

        datePicker = (DatePicker) findViewById(R.id.eventDatePicker);
        timePicker = (TimePicker) findViewById(R.id.eventTimePicker);
        editText = (EditText) findViewById(R.id.eventText);
        addButton = (Button) findViewById(R.id.addEventButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sharedPreferences.getInt("Today", 0) <= datePicker.getDayOfMonth() +
                        datePicker.getMonth() + datePicker.getYear()) {
                    if (!editText.getText().toString().equals("")) {
                        save();
                    } else{
                        Toast.makeText(AddEvent.this, "Невозможно создать событие с пустым содержанием!",
                                Toast.LENGTH_SHORT).show();
                    }
                } else{
                    Toast.makeText(AddEvent.this, "Невозможно создать " +
                                    "событие на выбранную дату!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        deleteButton = (Button) findViewById(R.id.deleteButton);
        intent = getIntent();

        sqlHelper = new DataBaseHelper(getApplicationContext());
        db = sqlHelper.getWritableDatabase();

        if (intent.getLongExtra("_id", 0) == 0){
            deleteButton.setVisibility(View.GONE);
        }else {
            deleteButton.setVisibility(View.VISIBLE);
            addButton.setText("Обновить событие");
            userCursor = db.rawQuery("SELECT note " +
                    "FROM users " +
                    "WHERE _id = " + intent.getLongExtra("_id", 0), null);
            userCursor.moveToFirst();
            editText.setText(userCursor.getString(0));
        }
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long id = intent.getLongExtra("_id", 0);
                db.delete(DataBaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(id)});
                Toast.makeText(AddEvent.this, "Событие удалено", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    //Метод сохраняет записи в БД при помощи обхекта класса ContentValues
    public void save(){
        long id = intent.getLongExtra("_id", 0);
        db.delete(DataBaseHelper.TABLE, "_id = ?", new String[]{String.valueOf(id)});
        ContentValues cv = new ContentValues();
        cv.put(DataBaseHelper.COLUMN_OWNER, sharedPreferences.getString("Login", ""));
        cv.put(DataBaseHelper.COLUMN_NOTE, editText.getText().toString());
        cv.put(DataBaseHelper.COLUMN_DATE, datePicker.getDayOfMonth() + "." +
                (datePicker.getMonth() + 1) + "." + datePicker.getYear());

        if (addButton.getText().equals("Создать событие")){
            Toast.makeText(this, "Событие создано", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "Событие обновлено", Toast.LENGTH_SHORT).show();
        }

        db.insert(DataBaseHelper.TABLE, null, cv);
        goHome();
    }

    //Метод закрывает подключние к БД и выполняет переход на предыдущую активность
    private void goHome(){
        // закрываем подключения
        db.close();
        // переход к главной activity
        Intent intent = new Intent(this, SelectActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
    }

    //Метод выполняется при уничтожении активности
    @Override
    public void onDestroy(){
        super.onDestroy();
        // Закрываем подключение и курсор
        db.close();
        //userCursor.close();
    }
}
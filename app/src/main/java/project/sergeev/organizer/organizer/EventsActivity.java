package project.sergeev.organizer.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

public class EventsActivity extends AppCompatActivity {

    ListView userList;
    TextView header;
    DataBaseHelper databaseHelper;
    SQLiteDatabase db;
    Cursor userCursor;
    SimpleCursorAdapter userAdapter;

    SharedPreferences sharedPreferences;
    DatePicker datePicker;
    TextView textView;

    Intent intent;

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu2, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bar_item_2:
                startActivity(new Intent(this, AddEvent.class));
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.events_layout);

        header = (TextView)findViewById(R.id.header);
        userList = (ListView)findViewById(R.id.list);
        userList.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long id) {
                Intent intent1 = new Intent(EventsActivity.this, AddEvent.class).
                        putExtra("_id", id);
                startActivity(intent1);
                return false;
            }
        });

        databaseHelper = new DataBaseHelper(getApplicationContext());

        sharedPreferences = getSharedPreferences(RegisterActivity.LoginDate, Context.MODE_PRIVATE);
        datePicker = (DatePicker) findViewById(R.id.datePickerInvisible);
        textView = (TextView) findViewById(R.id.noEvents);
        intent = getIntent();
    }
    @Override
    public void onResume() {
        super.onResume();
        // открываем подключение
        db = databaseHelper.getWritableDatabase();

        //получаем данные из бд в виде курсора
        userCursor =  db.rawQuery("SELECT * " +
                                  "FROM users " +
                                  "WHERE owner = '" + sharedPreferences.getString("Login", "")
                + "' AND date = '" + intent.getStringExtra("Date") + "'", null);
        // определяем, какие столбцы из курсора будут выводиться в ListView
        String[] headers = new String[] {
                DataBaseHelper.COLUMN_NOTE, DataBaseHelper.COLUMN_DATE,
                DataBaseHelper.COLUMN_OWNER
                //олучаем данные из БД в адаптер
        };
        // создаем адаптер, передаем в него курсор
        userAdapter = new SimpleCursorAdapter(this, android.R.layout.two_line_list_item,
                userCursor, headers, new int[]{
                android.R.id.text1, android.R.id.text2
        }, 0);
        header.setText("Ваши события на " + intent.getStringExtra("Date"));
        if (userCursor.getCount() == 0){
            header.setVisibility(View.GONE);
            textView.setVisibility(View.VISIBLE);
            textView.setText(sharedPreferences.getString(StartActivity.Owner, "") + ", у вас нет событий на " +
            intent.getStringExtra("Date"));
        }
        userList.setAdapter(userAdapter);
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        db.close();
        userCursor.close();
    }
}
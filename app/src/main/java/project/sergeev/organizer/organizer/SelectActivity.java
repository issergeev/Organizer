package project.sergeev.organizer.organizer;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;

public class SelectActivity extends AppCompatActivity {

    DatePicker datePicker;
    Button button;
    AlertDialog.Builder alertDialog;
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    //Создание меню в верхней части экрана (Action Bar)
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.actionbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    //Обработка нажатий на кнопки контекстного меню
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bar_item_2:
                startActivity(new Intent(this, AddEvent.class));
                return true;
            case R.id.ber_item_1:
                startActivity(new Intent(this, PersonalData.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.select_activity);
        datePicker = (DatePicker) findViewById(R.id.datePicker);
        sharedPreferences = getSharedPreferences(StartActivity.LoginDate, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.putInt("Today", Integer.valueOf(datePicker.getDayOfMonth() +
                datePicker.getMonth() + datePicker.getYear()));
        editor.apply();

        context = SelectActivity.this;
        button = (Button) findViewById(R.id.seeEventsButton);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SelectActivity.this, EventsActivity.class).putExtra("Date",
                        datePicker.getDayOfMonth() + "." + (datePicker.getMonth() + 1) + "." + datePicker.getYear());
                startActivity(intent);
            }
        });
    }

    //Метод onBackPressed() выполняется при нажатии кнопки "Назад"
    public void onBackPressed() {
        alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle("Выход");  // заголовок
        alertDialog.setMessage("Вы уверенныы, что хотите выйти?"); // сообщение
        alertDialog.setPositiveButton("Да, выйти", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                Toast.makeText(context, "Всего доброго, " +
                                sharedPreferences.getString(StartActivity.Owner, "user") + "!",
                        Toast.LENGTH_LONG).show();
                finish();
            }
        });
        alertDialog.setNegativeButton("Нет", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                ;
            }
        });
        alertDialog.setCancelable(true);
        alertDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            public void onCancel(DialogInterface dialog) {;
            ;
            }
        });
        alertDialog.show();
    }
}
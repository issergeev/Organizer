package project.sergeev.organizer.organizer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.TimePicker;

public class HelloActivity extends AppCompatActivity {

    SharedPreferences sharedPreferences;
    String LoginDate = "LoginDate",
           Name = "Owner";
    TextView helloText;
    TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hello_layout);
        setTitle("");

        sharedPreferences = getSharedPreferences(LoginDate, Context.MODE_PRIVATE);
        timePicker = (TimePicker) findViewById(R.id.timePickerInvisible);


        helloText = (TextView) findViewById(R.id.helloText);
        if(timePicker.getHour() < 12) {
            helloText.setText("Доброе утро, " + sharedPreferences.getString(Name, "пользователь") + "!");
        }else if (timePicker.getHour() >= 12 && timePicker.getHour() < 18){
            helloText.setText("Добрый день, " + sharedPreferences.getString(Name, "пользователь") +"!");
        }else if (timePicker.getHour() >= 18 && timePicker.getHour() <= 23){
            helloText.setText("Добрый вечер, " + sharedPreferences.getString(Name, "пользователь") + "!");
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                startActivity(new Intent(HelloActivity.this, SelectActivity.class));
                overridePendingTransition(R.anim.imcome_alpha, R.anim.gome_alpha);
                finish();
            }
        }, 2000);
    }
}
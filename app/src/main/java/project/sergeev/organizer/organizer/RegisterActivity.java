package project.sergeev.organizer.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    SQLiteDatabase db;
    usersDataBase usersDataBase;
    Cursor userCursor;

    static final String LoginDate = "LoginDate",
                        Name = "Name",
                        Login = "Login",
                        Password = "Password";
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    EditText login, password, new_name,new_login, new_password;
    Button createButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registration_activity);

        sharedPreferences = getSharedPreferences(LoginDate, Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        new_name = (EditText) findViewById(R.id.new_name);
        new_login = (EditText) findViewById(R.id.new_login);
        new_password = (EditText) findViewById(R.id.new_password);
        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        usersDataBase = new usersDataBase(getApplicationContext());

        createButton = (Button) findViewById(R.id.create_activity_button);

        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    db = usersDataBase.getWritableDatabase();

                    //получаем данные из бд в виде курсора
                    userCursor =  db.rawQuery("SELECT * " +
                            "FROM users " +
                            "WHERE login = '" + new_login.getText().toString() + "'", null);


                if (userCursor.getCount() == 0 && !new_name.getText().toString().equals("") &&
                        !new_login.getText().toString().equals("") &&
                        !new_password.getText().toString().equals("") &&new_password.getText().toString().length() < 6){
                    Toast.makeText(getApplicationContext(), "Пароль должен содержать не менее 6 символов!",
                            Toast.LENGTH_SHORT).show();
                }

                if(userCursor.getCount() == 0 && !new_name.getText().toString().equals("") &&
                        !new_login.getText().toString().equals("") &&
                        !new_password.getText().toString().equals("") && new_password.getText().toString().length() >= 6) {
                    editor.putString(Name, new_name.getText().toString());
                    editor.putString(Login, new_login.getText().toString());
                    editor.putString(Password, new_password.getText().toString());
                    editor.apply();

                    ContentValues cv = new ContentValues();
                    cv.put(usersDataBase.COLUMN_OWNER, new_name.getText().toString());
                    cv.put(usersDataBase.COLUMN_LOGIN, new_login.getText().toString());
                    cv.put(usersDataBase.COLUMN_PASSWORD, new_password.getText().toString());
                    db.insert(usersDataBase.TABLE, null, cv);

                    //Создание всплывающего сообщения на экране
                    Toast.makeText(getApplicationContext(), "Вы успешно зарегистрированы!", Toast.LENGTH_SHORT).show();

                    RegisterActivity.super.onBackPressed();
                    finish();
                }else if (userCursor.getCount() == 0 && new_name.getText().toString().equals("") ||
                        new_login.getText().toString().equals("") ||
                        new_password.getText().toString().equals(""))
                    Toast.makeText(getApplicationContext(), "Необходимо заполнить все обязательные поля перед продолжением!",
                            Toast.LENGTH_SHORT).show();

                if (userCursor.getCount() != 0){
                    Toast.makeText(getApplicationContext(), "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
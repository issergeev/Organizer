package project.sergeev.organizer.organizer;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class PersonalData extends AppCompatActivity {

    EditText name, login, password, new_password;
    Button applyButton;
    SharedPreferences sharedPreferences;
    usersDataBase sqlHelper;
    SQLiteDatabase db;
    Cursor userCursor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_data);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        name = (EditText) findViewById(R.id.change_name);
        login = (EditText) findViewById(R.id.change_login);
        password = (EditText) findViewById(R.id.change_password);
        new_password = (EditText) findViewById(R.id.change_new_password);
        applyButton = (Button) findViewById(R.id.apply_button);

        sqlHelper = new usersDataBase(getApplicationContext());
        db = sqlHelper.getWritableDatabase();

        sharedPreferences = getSharedPreferences(StartActivity.LoginDate, Context.MODE_PRIVATE);

        name.setText(sharedPreferences.getString(StartActivity.Owner, "user"));
        login.setText(sharedPreferences.getString(StartActivity.Login, ""));

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (password.getText().toString().equals(sharedPreferences.getString(StartActivity.Password, "0000"))){
                    if (login.getText().toString().equals(sharedPreferences.getString(StartActivity.Login, "Login"))){
                        if (new_password.getText().toString().length() >= 6 &&
                                !login.getText().toString().equals("") &&
                                !name.getText().toString().equals("")) {
                            db.delete(usersDataBase.TABLE, "login = ?",
                                    new String[]{
                                    login.getText().toString()
                            });

                            ContentValues cv = new ContentValues();
                            cv.put(usersDataBase.COLUMN_OWNER, name.getText().toString());
                            cv.put(usersDataBase.COLUMN_LOGIN, login.getText().toString());
                            cv.put(usersDataBase.COLUMN_PASSWORD, new_password.getText().toString());
                            db.insert(usersDataBase.TABLE, null, cv);

                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString(StartActivity.Owner, name.getText().toString());
                            editor.putString(StartActivity.Password, new_password.getText().toString());
                            editor.apply();

                            Toast.makeText(PersonalData.this, "Ваши личные данные успешно изменены!", Toast.LENGTH_SHORT).show();
                            finish();
                        } else if (new_password.getText().toString().length() < 6){
                            Toast.makeText(PersonalData.this, "Пароль должен содержать не менее 6 символов!", Toast.LENGTH_SHORT).show();
                        } else if (name.getText().toString().equals("") || new_password.getText().toString().equals("")){
                            Toast.makeText(PersonalData.this, "Логин или пароль не заполнены!", Toast.LENGTH_SHORT).show();
                        }
                    } else{
                        userCursor =  db.rawQuery("SELECT * " +
                                "FROM users " +
                                "WHERE login = '" + login.getText().toString() + "'", null);

                        if (userCursor.getCount() == 0) {
                            if (new_password.getText().toString().length() >= 6 &&
                                    !login.getText().toString().equals("") &&
                                    !name.getText().toString().equals("")) {
                                db.delete(usersDataBase.TABLE, "login = ?",
                                        new String[]{sharedPreferences.getString(StartActivity.Login, "")});

                                ContentValues cv = new ContentValues();
                                cv.put(usersDataBase.COLUMN_OWNER, name.getText().toString());
                                cv.put(usersDataBase.COLUMN_LOGIN, login.getText().toString());
                                cv.put(usersDataBase.COLUMN_PASSWORD, new_password.getText().toString());
                                db.insert(usersDataBase.TABLE, null, cv);

                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putString(StartActivity.Owner, name.getText().toString());
                                editor.putString(StartActivity.Password, new_password.getText().toString());
                                editor.apply();

                                Toast.makeText(PersonalData.this, "Ваши личные данные успешно изменены!", Toast.LENGTH_SHORT).show();
                                finish();
                            } else if (new_password.getText().toString().length() < 6){
                                Toast.makeText(PersonalData.this, "Пароль должен содержать не менее 6 символов!", Toast.LENGTH_SHORT).show();
                            } else if (name.getText().toString().equals("") || new_password.getText().toString().equals("")){
                                Toast.makeText(PersonalData.this, "Логин или пароль не заполнены!", Toast.LENGTH_SHORT).show();
                            }
                        } else{
                            Toast.makeText(PersonalData.this, "Пользователь с таким логином уже существует!", Toast.LENGTH_SHORT).show();
                        }
                    }
                } else{
                    Toast.makeText(PersonalData.this, "Вы ввели неверный пароль!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}
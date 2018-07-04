package project.sergeev.organizer.organizer;

//Импорты классов JAVA, необходимых активности
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

//Начало описания JAVA класса
public class StartActivity extends AppCompatActivity {

    //Создание переменных класса
    static final String LoginDate = "LoginDate",
                 Owner = "Owner",
                 Login = "Login",
                 Password = "Password",
                 isRemember = "isRemember";

    SharedPreferences sharedPreferences;

    SharedPreferences.Editor editor;
    EditText login, password;
    Button loginButton, createButton;
    CheckBox saveCheckBox;
    Cursor userCursor;
    SQLiteDatabase db;
    usersDataBase usersDataBase;

    //Метод onCreate() выполняется при старте активности. В нем выполняется инициализация переменных класса
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_activity);

        sharedPreferences = getSharedPreferences(LoginDate, Context.MODE_PRIVATE); //Создаем объект класса настроек и получаем в него
        //сами настойки нашего приложения

        login = (EditText) findViewById(R.id.login);
        password = (EditText) findViewById(R.id.password);

        createButton = (Button) findViewById(R.id.create_button);
        createButton.setOnClickListener(new View.OnClickListener() { //Устанавливаем слушателя кнопки
            @Override
            public void onClick(View view) {
                startActivity(new Intent(StartActivity.this, RegisterActivity.class));
            }
        });

        usersDataBase = new usersDataBase(getApplicationContext()); //Создаем объект класса "база данных" и передаем в него
        //созданную БД
        db = usersDataBase.getWritableDatabase();

        loginButton = (Button) findViewById(R.id.login_button);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Код запроса к БД
                userCursor =  db.rawQuery("SELECT * " +
                        "FROM users " +
                        "WHERE login = '" + login.getText().toString() +
                        "' AND password = '" + password.getText().toString() + "'", null);
                if (userCursor.getCount() == 1) {
                    userCursor.moveToFirst();
                    //Сохраняем настройки в файл настроек
                    editor = sharedPreferences.edit();
                    editor.putString(Owner, userCursor.getString(1));
                    editor.putString(Login, login.getText().toString());
                    editor.putString(Password, password.getText().toString());
                    editor.apply();
                    startActivity(new Intent(StartActivity.this, HelloActivity.class));
                    overridePendingTransition(R.anim.imcome_alpha, R.anim.gome_alpha);
                }else {
                    //Вызываем встплывающее сообщение на экране
                    Snackbar snackbar = Snackbar.make(view, "Логин или пароль неверный!", Snackbar.LENGTH_LONG)
                            .setAction("Создать аккаунт", Create);
                    snackbar.show();
                }
            }
            View.OnClickListener Create = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(StartActivity.this, RegisterActivity.class));
                }
            };
        });

        saveCheckBox = (CheckBox) findViewById(R.id.save_data);
    }

    //Метод onResume() выполняется при повторной отрисовки Activity после ее скрытия, а затем повторного возвращения на экран,
    //а так же при первом старте активности
    protected void onResume(){
        super.onResume();

        saveCheckBox.setChecked(sharedPreferences.getBoolean(isRemember, false));

        if(!sharedPreferences.getString(Login, "").equals("") &&
                !sharedPreferences.getString(Password, "").equals("") &&
                saveCheckBox.isChecked()){
            login.setText(sharedPreferences.getString(Login, ""));
            password.setText(sharedPreferences.getString(Password, ""));
        }
    }

    //Метод onPause() выполняется при скрытия активности из поля видимоти
    protected void onPause(){
        super.onPause();

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(isRemember, saveCheckBox.isChecked());
        editor.apply();
    }
}
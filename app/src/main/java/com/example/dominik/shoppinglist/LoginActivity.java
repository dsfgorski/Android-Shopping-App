package com.example.dominik.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
    public void doLogin(View v){
        EditText loginTextView = (EditText) findViewById(R.id.editLogin);
        String login = loginTextView.getText().toString();
        SharedPreferences userSP = getSharedPreferences("User_DB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putBoolean("isAuthorized",true);
        editor.putString("userName",login);
        editor.commit();

        Intent intent = new Intent (this,MainActivity.class);
        startActivity(intent);
    }
}

package com.example.dominik.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.SharedPreferences;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity {

    public static final int PERMISSION_REQUEST = 200;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openOrCreateDatabase();
        if(ContextCompat.checkSelfPermission(this,android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA}, PERMISSION_REQUEST);
        }

        SharedPreferences userSP = getSharedPreferences("User_DB", Context.MODE_PRIVATE);
        boolean isAuthorizedValue = userSP.getBoolean("isAuthorized", false);
        String userNameValue = userSP.getString("userName", "0");

        if (isAuthorizedValue) {
            Toast.makeText(this, userNameValue + " is logged in", Toast.LENGTH_SHORT).show();
            Intent listIntent = new Intent(this, ArticlesActivity.class);
            startActivity(listIntent);
            finish();
        }
        if (!isAuthorizedValue) {
            Toast.makeText(this, "Please log in", Toast.LENGTH_SHORT).show();
            Intent loginIntent = new Intent(this, LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }
    }
    public void openOrCreateDatabase (){
        ItemsSQLiteDatabaseHelper db = new ItemsSQLiteDatabaseHelper(this);
        db.getWritableDatabase();
    }
}

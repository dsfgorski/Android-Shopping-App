package com.example.dominik.shoppinglist;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TextView;

public class ArticlesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_articles);
        setUserTextView();
        tableHeadline();
        queryData();
    }
    public void setUserTextView(){
        SharedPreferences userSP = getSharedPreferences("User_DB", Context.MODE_PRIVATE);
        String userNameValue = userSP.getString("userName", "0");
        TextView loginTextView = (TextView) findViewById(R.id.loginTextView);
        loginTextView.setText(userNameValue);
    }
    public void queryData(){
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);

        ItemsSQLiteDatabaseHelper myHelper = new ItemsSQLiteDatabaseHelper(this);
        myHelper.getReadableDatabase();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ItemsSQLiteDatabaseHelper.TABLE_ARTICLES + " WHERE " + ItemsSQLiteDatabaseHelper.ARTICLE_QUANTITY + " > '0'";
        Cursor cursor = db.rawQuery(selectQuery,null);

        if(cursor.moveToFirst()){
            do{
                String text = cursor.getString(0);
                Log.i("query",text);


                    View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
                    TextView article_id  = (TextView) tableRow.findViewById(R.id.article_id);
                    TextView article_name  = (TextView) tableRow.findViewById(R.id.article_name);
                    TextView article_ean  = (TextView) tableRow.findViewById(R.id.article_ean);
                    TextView article_quantity  = (TextView) tableRow.findViewById(R.id.article_quantity);

                    article_id.setText(cursor.getString(0));
                    article_name.setText(cursor.getString(1));
                    article_ean.setText(cursor.getString(2));
                    article_quantity.setText(cursor.getString(3));
                    tableLayout.addView(tableRow);



            }while(cursor.moveToNext());
        }
        Log.i("query","Cursor empty");
        cursor.close();
        db.close();
    }
    public void tableHeadline(){
        TableLayout tableLayout = (TableLayout)findViewById(R.id.tableLayout);

        View tableRow = LayoutInflater.from(this).inflate(R.layout.table_item,null,false);
        TextView article_id  = (TextView) tableRow.findViewById(R.id.article_id);
        TextView article_name  = (TextView) tableRow.findViewById(R.id.article_name);
        TextView article_ean  = (TextView) tableRow.findViewById(R.id.article_ean);
        TextView article_quantity  = (TextView) tableRow.findViewById(R.id.article_quantity);

        article_id.setText("ID");
        article_name.setText("NAME");
        article_ean.setText("EAN CODE");
        article_quantity.setText("#");
        tableLayout.addView(tableRow);
    }
    public void addArticle(View v){
        Intent intent = new Intent(this,AddActivity.class);
        startActivity(intent);
    }
    public void deleteArticle(View v){
        Intent intent = new Intent(this,DeleteActivity.class);
        startActivity(intent);
    }
    public void logOut(View v){
        SharedPreferences userSP = getSharedPreferences("User_DB", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = userSP.edit();
        editor.putBoolean("isAuthorized",false);
        editor.putString("userName","0");
        editor.commit();

        Intent intent = new Intent (this,MainActivity.class);
        startActivity(intent);
    }
}

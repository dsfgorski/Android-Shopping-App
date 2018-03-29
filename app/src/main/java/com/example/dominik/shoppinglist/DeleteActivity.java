package com.example.dominik.shoppinglist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.barcode.Barcode;

public class DeleteActivity extends AppCompatActivity {

    TextView result,articleName;
    EditText articleNumber;
    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete);
        result = (TextView)findViewById(R.id.eanCode);
        articleName = (TextView)findViewById(R.id.nameTextView);
        articleNumber = (EditText)findViewById(R.id.numberEditText);

        scanCode();
    }
    public void scanCode(){
        Toast.makeText(this,"Scan initiated",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(getApplicationContext(),ScanActivity.class);
        startActivityForResult(intent,REQUEST_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK){
            if (data != null){
                final Barcode barcode = data.getParcelableExtra("barcode");
                result.post(new Runnable() {
                    @Override
                    public void run() {
                        result.setText(barcode.displayValue);
                        AddActivity addActivity = new AddActivity();
                        if (!addActivity.isName(barcode.displayValue)){
                            Toast.makeText(getApplicationContext(),"Article not in database",Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });

            }
        }
    }
    public void insertRow(View v){
        Log.i("insertRow","Initiated");

        ItemsSQLiteDatabaseHelper myHelper = new ItemsSQLiteDatabaseHelper(this);
        SQLiteDatabase db = myHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        result = (TextView)findViewById(R.id.eanCode);
        String eanCode = result.getText().toString();
        Log.i("insertRow","eanCode " + eanCode);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_EAN,eanCode);

        articleName = (TextView)findViewById(R.id.nameTextView);
        String artName = articleName.getText().toString();
        Log.i("insertRow","articleName " + artName);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_NAME,artName);

        articleNumber = (EditText)findViewById(R.id.numberEditText);
        Integer articleQuantity = Integer.parseInt(articleNumber.getText().toString());
        Log.i("insertRow","articleQuantity " + articleQuantity);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_QUANTITY,articleQuantity);

        String where = ItemsSQLiteDatabaseHelper.ARTICLE_EAN + "=?";
        String[] whereArgs = new String[] {String.valueOf(eanCode)};

        db.update(ItemsSQLiteDatabaseHelper.TABLE_ARTICLES, contentValues, where, whereArgs);
        db.close();
        Toast.makeText(this,"Article Added",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ArticlesActivity.class);
        startActivity(intent);
        finish();
    }
    public Boolean isName(int eanCode){
        ItemsSQLiteDatabaseHelper myHelper = new ItemsSQLiteDatabaseHelper(this);
        myHelper.getReadableDatabase();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ItemsSQLiteDatabaseHelper.TABLE_ARTICLES
                + " WHERE " + ItemsSQLiteDatabaseHelper.ARTICLE_EAN + " = '" + eanCode + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()) {
            articleNumber = (EditText)findViewById(R.id.numberEditText);
            articleNumber.setText(cursor.getString((cursor.getColumnIndex(ItemsSQLiteDatabaseHelper.ARTICLE_QUANTITY))));
            articleName = (TextView)findViewById(R.id.nameTextView);
            articleName.setText(cursor.getString((cursor.getColumnIndex(ItemsSQLiteDatabaseHelper.ARTICLE_NAME))));

            return true;
        }
        else {
            return false;
        }
    }
}
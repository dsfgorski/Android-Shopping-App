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

public class AddActivity extends AppCompatActivity {

    TextView result;
    EditText name,number;

    public static final int REQUEST_CODE = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        result = (TextView)findViewById(R.id.addeanCode);
        name = (EditText)findViewById(R.id.nameEditText);
        number = (EditText)findViewById(R.id.numberEditText);

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
                        isName(barcode.displayValue);
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

        result = (TextView)findViewById(R.id.addeanCode);
        Log.i("insertRow","ean");
        String eanCode = result.getText().toString();
        Log.i("insertRow","eanCode " + eanCode);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_EAN,eanCode);

        name = (EditText)findViewById(R.id.nameEditText);
        String articleName = name.getText().toString();
        Log.i("insertRow","articleName " + articleName);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_NAME,articleName);

        number = (EditText)findViewById(R.id.numberEditText);
        Integer articleQuantity = Integer.parseInt(number.getText().toString());
        Log.i("insertRow","articleQuantity " + articleQuantity);
        contentValues.put(ItemsSQLiteDatabaseHelper.ARTICLE_QUANTITY,articleQuantity);
        if (isName(eanCode)) {
            String where = ItemsSQLiteDatabaseHelper.ARTICLE_EAN + "=?";
            String[] whereArgs = new String[] {String.valueOf(eanCode)};
            db.update(ItemsSQLiteDatabaseHelper.TABLE_ARTICLES, contentValues, where, whereArgs);
        }
        else {
            db.insert(ItemsSQLiteDatabaseHelper.TABLE_ARTICLES, null, contentValues);
        }
        db.close();
        Toast.makeText(this,"Article Added",Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(this,ArticlesActivity.class);
        startActivity(intent);
        finish();
    }
    public Boolean isName(String eanCode){
        ItemsSQLiteDatabaseHelper myHelper = new ItemsSQLiteDatabaseHelper(this);
        myHelper.getReadableDatabase();
        SQLiteDatabase db = myHelper.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ItemsSQLiteDatabaseHelper.TABLE_ARTICLES
                + " WHERE " + ItemsSQLiteDatabaseHelper.ARTICLE_EAN + " = '" + eanCode + "'";
        Cursor cursor = db.rawQuery(selectQuery,null);
        if(cursor.moveToFirst()) {
            number = (EditText)findViewById(R.id.numberEditText);
            number.setText(cursor.getString((cursor.getColumnIndex(ItemsSQLiteDatabaseHelper.ARTICLE_QUANTITY))));
            name = (EditText)findViewById(R.id.nameEditText);
            name.setText(cursor.getString((cursor.getColumnIndex(ItemsSQLiteDatabaseHelper.ARTICLE_NAME))));

            return true;
        }
        else {
            return false;
        }
    }
}

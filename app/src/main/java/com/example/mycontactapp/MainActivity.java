package com.example.mycontactapp;

import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    DatabaseHelper myDb;
    EditText editName, editPhone, editAddress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MyContactApp", "MainActivity: setting up the layout");
        setContentView(R.layout.activity_main);
        editName = findViewById(R.id.editText_name);
        editPhone = findViewById(R.id.editText_phone);
        editAddress = findViewById(R.id.editText_address);



        myDb = new DatabaseHelper(this);
        Log.d("MyContactApp", "MainActivity: instantiated DatabaseHelper");
    }

    public void addData(View view) {
        Log.d("MyContactApp", "MainActivity: Add contact button pressed");
        boolean isInserted = myDb.insertData(editName.getText().toString(), editPhone.getText().toString(), editAddress.getText().toString()) ;
        if (isInserted) {
            Toast.makeText( MainActivity.this, "Success - contact inserted", Toast.LENGTH_SHORT).show();

        }
        else {
            Toast.makeText( MainActivity.this, "Failed - contact not inserted", Toast.LENGTH_SHORT).show();

        }
    }

    public void viewData(View view) {
        Cursor res = myDb.getAllData();
        Log.d("MyContactApp", "MainActivity: got all data");
        if (res.getCount()==0) {
            showMessage("Error", "No data found in database");
            return;
        }

        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()) {
            //Append res columns to the buffer - see StringBuffer and Cursor API's
            buffer.append("ID: " + res.getString(0) + "\n");
            buffer.append("Name: " + res.getString(1) + "\n");
            buffer.append("Phone: " + res.getString(2) + "\n");
            buffer.append("Address: " + res.getString(3) + "\n");



        }
        Log.d("MyContactApp", "MainActivity: ViewData-buffer assembled");
        showMessage("Data", buffer.toString());
    }

    public void showMessage(String title, String message) {
        Log.d("MyContactApp", "MainActivity: showing message");
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();

    }

    public void searchRecord(View view) {
        Log.d("MyContactApp", "MainActivity: beginning search");
        Cursor curs = myDb.getAllData();
        StringBuffer buffer = new StringBuffer();
        if (editName.getText().toString().isEmpty() && editPhone.getText().toString().isEmpty()
                && editAddress.getText().toString().isEmpty()) {
            showMessage("Error", "Nothing to search for!");
            return;
        }
        while (curs.moveToNext()) {
            if ((editName.getText().toString().isEmpty() || editName.getText().toString().equals(curs.getString(1))) &&
                    (editAddress.getText().toString().isEmpty() || editAddress.getText().toString().equals(curs.getString(3))) &&
                    (editPhone.getText().toString().isEmpty() || editPhone.getText().toString().equals(curs.getString(2)))) {
                buffer.append("ID: " + curs.getString(0) + "\n"+
                        "Name: " + curs.getString(1) + "\n"+
                        "Phone: " + curs.getString(2) + "\n"+
                        "Address: " + curs.getString(3) + "\n");
            }

        }

        if (buffer.toString().isEmpty()) {
            showMessage("Error", "None found");
            return;
        }
        showMessage("Search results", buffer.toString());
    }
}



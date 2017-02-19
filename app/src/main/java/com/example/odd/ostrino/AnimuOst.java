package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Scanner;


public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener {

    Button btnAdd, btnViewList, btnAddOsts;
    SQLiteDatabase dtb;
    private String TAG = "OstInfo";
    DBHandler db;
    List<Ost> ostList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animuost);

        db = new DBHandler(this);
        //Reset database
        //db.onUpgrade(dbS, 1, 2);

        btnAdd = (Button) findViewById(R.id.btnAddOst);
        btnViewList = (Button) findViewById(R.id.btnViewOstList);
        btnAddOsts = (Button) findViewById(R.id.btnBatchAdd);

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddScreen dialog = new AddScreen();
                dialog.show(getFragmentManager(), TAG);
            }
        });

        btnViewList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View Block Number List in the Text View Widget
                ListScreen ls = new ListScreen();
                launchListScreen();
            }
        });

        btnAddOsts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ostList = db.getAllOsts();
                dtb = db.getWritableDatabase();
                try {
                    InputStream is = getResources().getAssets().open("Osts.txt");
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is));
                    String line;
                    while((line = reader.readLine()) != null) {
                        Ost ost = new Ost();
                        String[] lineArray = line.split("; ");
                        ost.setTitle(lineArray[0]);
                        ost.setShow(lineArray[1]);
                        ost.setTags(lineArray[2]);
                        ost.setUrl(lineArray[3]);
                        if (ostList.contains(ost)){
                            System.out.println( ost);
                            db.addNewOst(ost);
                        }
                    }
                }catch (java.io.IOException e){
                    System.out.println("File not found");
                }

            }
        });
        }

    @Override
    public void onSaveButtonClick(DialogFragment dialog) {
        EditText entTitle = (EditText) dialog.getDialog().findViewById(R.id.edtTitle);
        String title = entTitle.getText().toString();
        EditText entShow = (EditText) dialog.getDialog().findViewById(R.id.edtShow);
        String show = entShow.getText().toString();
        EditText entTags = (EditText) dialog.getDialog().findViewById(R.id.edtTags);
        String tags = entTags.getText().toString();
        EditText entUrl = (EditText) dialog.getDialog().findViewById(R.id.edtUrl);
        String url = entUrl.getText().toString();
        Ost ost = new Ost(title, show, tags, url);

        db.addNewOst(ost);
        Toast.makeText(getApplicationContext(), ost.getTitle() + " added", Toast.LENGTH_LONG).show();
    }

    private void launchListScreen(){
        Intent intent = new Intent(this, ListScreen.class);
        startActivity(intent);
    }
}
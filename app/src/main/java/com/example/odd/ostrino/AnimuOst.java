package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener {

    Button btnAdd, btnViewList;
    SQLiteDatabase dtb;
    private String TAG = "OstInfo";
    DBHandler db;
    ListScreen ls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animuost);

        db = new DBHandler(this);
        //Reset database
        //SQLiteDatabase dbS = db.getWritableDatabase();
        //db.onUpgrade(dbS, 1, 2);

        btnAdd = (Button) findViewById(R.id.btnAddOst);
        btnViewList = (Button) findViewById(R.id.btnViewOstList);

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
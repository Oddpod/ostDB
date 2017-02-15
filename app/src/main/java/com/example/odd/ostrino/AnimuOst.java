package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener{

    Button btnAdd, btnList;
    DBHandler db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animuost);

        db = new DBHandler(this);
    }

    @Override
    public void onSaveButtonCLick(DialogFragment dialog) {

    }
}

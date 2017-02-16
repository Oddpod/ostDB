package com.example.odd.ostrino;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.LayoutInflater;

import static android.R.attr.id;

/**
 * Created by Odd on 12.02.2017.
 */

public class AddScreen extends DialogFragment{

    interface AddScreenListener {

        void onSaveButtonClick(DialogFragment dialog);
    }

    AddScreenListener addScreenListener;
    Context context;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);

        try{
            addScreenListener = (AddScreenListener) activity;
        } catch(ClassCastException e){
            throw new ClassCastException(activity.toString()
                    + " must implement AddScreenListener");
        }

    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.addscreen, null))

                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        addScreenListener.onSaveButtonClick(AddScreen.this);
                    }
        });
        return builder.create();
    }
}

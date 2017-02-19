package com.example.odd.ostrino;

import android.app.AlertDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

/**
 * Created by Odd on 12.02.2017.
 */

public class ListScreen extends AppCompatActivity implements AddScreen.AddScreenListener{

    private List<Ost> allOsts;
    private TableLayout tableLayout;
    private float headerTextSize = 18;
    private float rowTextSize = 11;
    private DBHandler db;
    private static int headerBackgroundColor = Color.BLACK;
    private String TAG = "OstInfo";

    public ListScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        setContentView(R.layout.activity_listscreen);
        tableLayout = (TableLayout) findViewById(R.id.tlOstTable);
        createList();
    }

    public void createList() {
        tableLayout.setWeightSum(3);
        TableRow tr_head = new TableRow(getApplicationContext());
        tr_head.setId(View.generateViewId());
        tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        //Creating Title column header
        TextView label_title = new TextView(getApplicationContext());
        label_title.setId(View.generateViewId());
        label_title.setText("Title");
        label_title.setTextColor(headerBackgroundColor);
        label_title.setPadding(5, 5, 5, 5);
        label_title.setTextSize(headerTextSize);
        tr_head.addView(label_title);

        //Show column header
        TextView label_show = new TextView(getApplicationContext());
        label_show.setId(View.generateViewId());
        label_show.setText("Show");
        label_show.setTextColor(headerBackgroundColor);
        label_show.setPadding(5, 5, 5, 5);
        label_show.setTextSize(headerTextSize);
        tr_head.addView(label_show);

        //Tags column header
        TextView label_tags = new TextView(getApplicationContext());
        label_tags.setId(View.generateViewId());
        label_tags.setText("Tags");
        label_tags.setTextColor(headerBackgroundColor);
        label_tags.setPadding(5, 5, 5, 5);
        label_tags.setTextSize(headerTextSize);
        tr_head.addView(label_tags);

        //Delete column header
        TextView label_delete = new TextView(getApplicationContext());
        label_delete.setId(View.generateViewId());
        label_delete.setText("Delete");
        label_delete.setTextColor(headerBackgroundColor);
        label_delete.setPadding(5, 5, 5, 5);
        label_delete.setTextSize(headerTextSize);
        tr_head.addView(label_delete);

        tableLayout.addView(tr_head, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

        allOsts = db.getAllOsts();

        for (Ost ost : allOsts) {
            addRow(ost);
        }
    }

    public void addRow(final Ost ost) {
        final int id = ost.getId();
        final String title = ost.getTitle();
        String show = ost.getShow();
        String tags = ost.getTags();
        final String url = ost.getUrl();
        final TableRow tR = new TableRow(getApplicationContext());
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView label_title = new TextView(getApplicationContext());
        label_title.setId(View.generateViewId());
        label_title.setText(title);
        label_title.setTextColor(Color.BLACK);
        label_title.setTextSize(rowTextSize);
        label_title.setPadding(5, 5, 5, 5);

        //Launches url when you click the title
        label_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(url);
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
            }

        });

        tR.addView(label_title);

        TextView label_show = new TextView(getApplicationContext());
        label_show.setId(View.generateViewId());
        label_show.setText(show);
        label_show.setTextColor(Color.BLACK);
        label_show.setTextSize(rowTextSize);
        label_show.setPadding(5, 5, 5, 5);
        tR.addView(label_show);

        TextView label_tags = new TextView(getApplicationContext());
        label_tags.setId(View.generateViewId());
        label_tags.setText(tags);
        label_tags.setTextColor(Color.BLACK);
        label_tags.setTextSize(rowTextSize);
        label_tags.setPadding(5, 5, 5, 5);
        tR.addView(label_tags);

        final Button deleteBtn = new Button(getApplicationContext());
        deleteBtn.setId(View.generateViewId());
        deleteBtn.setText("Delete");
        deleteBtn.setTextSize(rowTextSize);
        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.deleteOst(id);
                Toast.makeText(getApplicationContext(), title + " was deleted from database", Toast.LENGTH_LONG).show();
            }
        });

        tR.addView(deleteBtn);
        tR.setOnLongClickListener(new View.OnLongClickListener(){
            @Override
            public boolean onLongClick(View v) {
                System.out.println(ost.toString());
                AddScreen dialog = new AddScreen();
                dialog.show(getFragmentManager(), TAG);
                dialog.setText(ost);
                Toast.makeText(getApplicationContext(), " Editing Ost ", Toast.LENGTH_LONG).show();
                return false;
            }
                                  });
        tableLayout.addView(tR, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
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

        db.updateOst(ost);
        Toast.makeText(getApplicationContext(), ost.getTitle() + " updated ost", Toast.LENGTH_LONG).show();
    }
}

package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener{

    Button btnAdd, btnViewList;
    SQLiteDatabase dtb;
    private TextView tvOstLister;
    private TableLayout tableLayout;
    private String TAG = "OstInfo";
    DBHandler db;
    private float headerTextSize = 21;
    private static int headerBackgroundColor = Color.BLACK;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animuost);

        db = new DBHandler(this);

        btnAdd = (Button)findViewById(R.id.btnAddOst);
        btnViewList = (Button)findViewById(R.id.btnViewOstList);

        btnAdd.setOnClickListener(new View.OnClickListener(){
            @Override
                    public void onClick(View v){
                AddScreen dialog = new AddScreen();
                dialog.show(getFragmentManager(), TAG);
            }
        });

        btnViewList.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                //View Block Number List in the Text View Widget
                setContentView(R.layout.listscreen);
                tableLayout = (TableLayout) findViewById(R.id.tlOstTable);
                tableLayout.setWeightSum(3);
                TableRow tr_head = new TableRow(getApplicationContext());
                tr_head.setId(View.generateViewId());
                tr_head.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

                //Creating Title column header
                TextView label_title = new TextView(getApplicationContext());
                label_title.setId(View.generateViewId());
                label_title.setText("Title");
                label_title.setTextColor(headerBackgroundColor);
                label_title.setPadding(100, 5, 5, 5);
                label_title.setTextSize(headerTextSize);
                tr_head.addView(label_title);

                //Show column header
                TextView label_show = new TextView(getApplicationContext());
                label_show.setId(View.generateViewId());
                label_show.setText("Show");
                label_show.setTextColor(headerBackgroundColor);
                label_show.setPadding(100, 5, 5, 5);
                label_show.setTextSize(headerTextSize);
                tr_head.addView(label_show);

                //Tags column header
                TextView label_tags = new TextView(getApplicationContext());
                label_tags.setId(View.generateViewId());
                label_tags.setText("Tags");
                label_tags.setTextColor(headerBackgroundColor);
                label_tags.setPadding(100, 5, 5, 5);
                label_tags.setTextSize(headerTextSize);
                tr_head.addView(label_tags);

                //Delete column header
                TextView label_delete = new TextView(getApplicationContext());
                label_delete.setId(View.generateViewId());
                label_delete.setText("Delete");
                label_delete.setTextColor(headerBackgroundColor);
                label_delete.setPadding(100, 5, 5, 5);
                label_delete.setTextSize(headerTextSize);
                tr_head.addView(label_delete);

                tableLayout.addView(tr_head, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));

                tvOstLister = (TextView) findViewById(R.id.tvOstList);

                tvOstLister.setPadding(5, 2, 5, 2);

                List<Ost> ostList = db.getAllOsts();

                for (Ost ost : ostList){
                    addRow(ost);
                }

            }
        });
    }

    @Override
    public void onSaveButtonClick(DialogFragment dialog) {
        EditText entTitle = (EditText) dialog.getDialog().findViewById(R.id.edtTItle);
        String title = entTitle.getText().toString();
        EditText entShow = (EditText) dialog.getDialog().findViewById(R.id.edtShow);
        String show = entShow.getText().toString();
        EditText entTags = (EditText) dialog.getDialog().findViewById(R.id.edtTags);
        String tags = entTags.getText().toString();
        Ost ost = new Ost(title, show, tags);

        db.addNewOst(ost);
        Toast.makeText(getApplicationContext(), ost.getTitle() + " added", Toast.LENGTH_LONG).show();
    }

    public void addRow(Ost ost){
        final int id = ost.getId();
        final String title = ost.getTitle();
        String Show = ost.getShow();
        String tags = ost.getTags();
        TableRow tR = new TableRow(getApplicationContext());
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));

        TextView label_title = new TextView(getApplicationContext());
        label_title.setId(View.generateViewId());
        label_title.setText(title);
        label_title.setTextColor(Color.BLACK);
        label_title.setPadding(100, 5, 5, 5);
        tR.addView(label_title);

        TextView label_show = new TextView(getApplicationContext());
        label_show.setId(View.generateViewId());
        label_show.setTextColor(Color.BLACK);
        label_show.setPadding(100, 5, 5, 5);
        tR.addView(label_show);

        TextView label_tags = new TextView(getApplicationContext());
        label_tags.setId(View.generateViewId());
        label_tags.setTextColor(Color.BLACK);
        label_tags.setPadding(100, 5, 5, 5);
        tR.addView(label_tags);

        final Button deleteBtn = new Button(getApplicationContext());
        deleteBtn.setId(View.generateViewId());
        deleteBtn.setText("Delete");
        deleteBtn.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                db.deleteOst(id);
                Toast.makeText(getApplicationContext(), title + " was deleted from database", Toast.LENGTH_LONG).show();
            }
        });

        tR.addView(deleteBtn);

        tableLayout.addView(tR, new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.MATCH_PARENT));
    }
}

package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Odd on 12.02.2017.
 */

public class ListScreen extends AppCompatActivity implements AddScreen.AddScreenListener, FunnyJunk.YareYareListener, View.OnClickListener {

    private int ostReplaceId;
    private List<Ost> allOsts, currDispOstList;
    private List<CheckBox> checkBoxes;
    private TableLayout tableLayout;
    private float rowTextSize = 11;
    private EditText filter;
    private DBHandler db;
    private String TAG = "OstInfo";
    private String TAG2 = "Jojo";
    private String filterText;
    private TextWatcher textWatcher;
    private TableRow tR;
    private ScrollView scrollview;
    private FrameLayout youtubeLandscape;
    private YoutubeFragment youtubeFragment = null;
    Button btnDelHeader, btnPlayAll, btnplaySelected;
    private TextView title_header, show_header, tags_header, label_title, label_show, label_tags;

    public ListScreen() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DBHandler(this);
        setContentView(R.layout.activity_listscreen);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        filter = (EditText) findViewById(R.id.edtFilter);
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                filterText = filter.getText().toString();
                filterText = filterText.toLowerCase();
                if (filterText.equals("muda muda muda")) {
                    FunnyJunk dialog = new FunnyJunk();
                    dialog.show(getFragmentManager(), TAG2);
                }
                cleanTable(tableLayout);
                for (Ost ost : allOsts) {
                    addRow(ost);
                }

            }
        };
        filter.addTextChangedListener(textWatcher);
        btnPlayAll = (Button) findViewById(R.id.btnPlayAll);
        btnplaySelected = (Button) findViewById(R.id.btnPlaySelected);

        btnPlayAll.setOnClickListener(this);
        btnplaySelected.setOnClickListener(this);

        youtubeLandscape = (FrameLayout) findViewById(R.id.flLandscape);
        tableLayout = (TableLayout) findViewById(R.id.tlOstTable);
        currDispOstList = new ArrayList<>(); //Contains all ost in the shown list even when filtered
        createList();
    }

    public void createList() {
        checkBoxes = new ArrayList<>();
        //tableLayout.removeAllViews();
        //tableLayout.setWeightSum(3);

        //Creating Title column header
        title_header = (TextView) findViewById(R.id.titleHeader);
        show_header = (TextView) findViewById(R.id.showHeader);
        tags_header = (TextView) findViewById(R.id.tagsHeader);
        btnDelHeader = (Button) findViewById(R.id.btnDelHeader);

        title_header.setText("Title");
        show_header.setText("Show");
        tags_header.setText("Tags");
        btnDelHeader.setText("Delete");

        btnDelHeader.setOnClickListener(this);

        allOsts = db.getAllOsts();

        for (Ost ost : allOsts) {
            addRow(ost);
        }
    }

    public void addRow(final Ost ost) {
        final int id = ost.getId();
        String ostInfoString = ost.getTitle() + " " + ost.getShow() + " " + ost.getTags();
        ostInfoString = ostInfoString.toLowerCase();
        final String title = ost.getTitle();
        String show = ost.getShow();
        String tags = ost.getTags();
        tR = new TableRow(getApplicationContext());
        tR.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        final String url = ost.getUrl();

        TextView label_title = new TextView(getApplicationContext());
        label_title.setId(View.generateViewId());
        label_title.setText(title);
        label_title.setTextColor(Color.BLACK);
        label_title.setTextSize(rowTextSize);
        label_title.setPadding(5, 5, 5, 5);
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

        //Launches url when you click the title
        label_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println(url);
                //startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                initYoutubeFrag();
                youtubeFragment.setVideoId(url);
                launchYoutubeFrag(youtubeFragment);
            }

        });

        CheckBox checkBox = new CheckBox(getApplicationContext());
        checkBox.setId(View.generateViewId());
        checkBox.setPadding(5, 5, 0, 5);
        checkBox.setTextSize(rowTextSize);
        checkBox.setChecked(false);
        checkBoxes.add(checkBox);

        tR.addView(checkBox);
        //Long press to edit Ost
        tR.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                System.out.println(ost.toString());
                AddScreen dialog = new AddScreen();
                dialog.show(getFragmentManager(), TAG);
                ostReplaceId = ost.getId();
                dialog.setText(ost);

                //Toast.makeText(getApplicationContext(), " Editing Ost ", Toast.LENGTH_LONG).show();
                return false;
            }
        });
        if (filterText != null && !ostInfoString.contains(filterText)) {
            //System.out.println(filterText + ostInfoString);
            currDispOstList.remove(ost);
            tR.removeAllViews();
        }
        else{
            currDispOstList.add(ost);
        }
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
        ost.setId(ostReplaceId);

        db.updateOst(ost);
        cleanTable(tableLayout);
        allOsts = db.getAllOsts();
        for (Ost ost2 : allOsts) {
            addRow(ost2);
        }
        Toast.makeText(getApplicationContext(), "updated ost: " + ost.getTitle(), Toast.LENGTH_LONG).show();
    }

    private void cleanTable(TableLayout table) {
        checkBoxes.clear();

        int childCount = table.getChildCount();

        // Remove all rows except the first one
        if (childCount > 1) {
            table.removeViews(1, childCount - 1);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlayAll: {
                List<String> urlList = new ArrayList<>();
                for (Ost ost : currDispOstList) {
                    urlList.add(ost.getUrl());
                }
                //System.out.println("urlList: " + urlList);
                YoutubeFragment youFragment = new YoutubeFragment();
                youFragment.setVideoIds(urlList);
                youFragment.playAll(true);
                launchYoutubeFrag(youFragment);
            }
            case R.id.btnPlaySelected:{
                int i = 0;
                List<String> playList = new ArrayList<>();
                for (CheckBox box : checkBoxes){
                    //System.out.println(box.isChecked());
                    if(box.isChecked()){
                        //System.out.println("i: " + i);
                        String url = allOsts.get(i).getUrl();
                        playList.add(url);
                        box.setChecked(false);
                    }
                    i++;
                }
                if(playList.size()> 0){
                    YoutubeFragment yFragment = new YoutubeFragment();
                    yFragment.setVideoIds(playList);
                    yFragment.playAll(true);
                    launchYoutubeFrag(yFragment);
                }

            }
            case R.id.btnDelHeader: {
                int i = 0;
                for (CheckBox checkBox : checkBoxes) {
                    if (checkBox.isChecked()) {
                        int id = allOsts.get(i).getId();
                        db.deleteOst(id);
                    }

                    i++;
                }
                tR.removeAllViews();
                cleanTable(tableLayout);
                allOsts = db.getAllOsts();
                checkBoxes.clear(); //clear list of Checkboxes
                for (Ost ost : allOsts) {
                    addRow(ost);
                }
            }
        }
    }
    public void initYoutubeFrag(){
        if(youtubeFragment == null){
            youtubeFragment = new YoutubeFragment();
        }
    }

    public void launchYoutubeFrag(YoutubeFragment frag){
        int orientation = this.getResources().getConfiguration().orientation;
        int activityId;
        if(orientation == Configuration.ORIENTATION_LANDSCAPE){
            activityId = R.id.flLandscape;
        }
        else{
            activityId = R.id.activity_listscreen;
        }
        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .replace(activityId, frag)
                .addToBackStack(null)
                .commit();
    }
}

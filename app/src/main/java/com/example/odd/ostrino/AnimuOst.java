package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.StrictMode;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.List;
import java.util.Random;

public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener, View.OnClickListener{

    Button btnAdd, btnViewList, btnAddOsts, btnExportOsts, btnRandomOst, btnTestConnection, btnTestFloater;
    SQLiteDatabase dtb;
    private String TAG = "OstInfo";
    DBHandler db;
    Ost lastAddedOst;
    List<Ost> ostList;
    Random rnd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_animuost);

        //Instantiates the random variable used for random ost
        rnd = new Random();

        //Needed to access server
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        db = new DBHandler(this);
        //Reset database
        //db.onUpgrade(dbS, 1, 2);

        btnAdd = (Button) findViewById(R.id.btnAddOst);
        btnViewList = (Button) findViewById(R.id.btnViewOstList);
        btnAddOsts = (Button) findViewById(R.id.btnBatchAdd);
        btnExportOsts = (Button) findViewById(R.id.btnExport);
        btnRandomOst = (Button) findViewById(R.id.btnRandomOst);
        btnTestConnection = (Button) findViewById(R.id.btnTestConnection);
        btnTestFloater = (Button)findViewById(R.id.btnTestFloater);

        btnAdd.setOnClickListener(this);
        btnViewList.setOnClickListener(this);
        btnAddOsts.setOnClickListener(this);
        btnExportOsts.setOnClickListener(this);
        btnRandomOst.setOnClickListener(this);
        btnTestConnection.setOnClickListener(this);
        btnTestFloater.setOnClickListener(this);
        }

    //Set button behaviours
    public void onClick(View v){
        switch (v.getId()) {
            case R.id.btnViewOstList: {
                new ListScreen();
                launchListScreen();
                break;
            }
            case R.id.btnAddOst: {
                AddScreen dialog = new AddScreen();
                dialog.show(getFragmentManager(), TAG);
                break;
            }
            case R.id.btnBatchAdd: {
                chooseFile();
                break;
            }
            case R.id.btnExport: {
                chooseFileDir();
                break;
            }
            case R.id.btnRandomOst: {
                ostList = db.getAllOsts();
                if(ostList.size()> 0){
                    int rndId = rnd.nextInt(ostList.size());
                    Ost ost = db.getOst(rndId);
                    String url = ost.getUrl();
                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
                    break;
                }
                else{
                    Toast.makeText(getApplicationContext(), "Ost list is empty", Toast.LENGTH_SHORT).show();
                }
            }
            case R.id.btnTestConnection:{
                /*Toast.makeText(getApplicationContext(), "Updating database", Toast.LENGTH_SHORT).show();
                ServerDBHandler serverDBHandler = new ServerDBHandler();
                ostList = db.getAllOsts();
                try{
                    serverDBHandler.saveOsts(ostList);
                    ostList = serverDBHandler.getOsts(ostList);
                } catch (java.sql.SQLException e){
                    e.printStackTrace();
                }*/
                break;
            }
            case R.id.btnTestFloater:{
                if(Build.VERSION.SDK_INT >= 23) {
                    if (!Settings.canDrawOverlays(this)) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                Uri.parse("package:" + getPackageName()));
                        startActivityForResult(intent, 1234);
                    }
                    else{
                        Intent intent = new Intent(this, FloatingWindow.class);
                        startService(intent);

                    }
                }
                else
                {
                    Intent intent = new Intent(this, FloatingWindow.class);
                    startService(intent);
                }

            }
        }
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
        lastAddedOst = new Ost(title, show, tags, url);
        boolean alreadyAdded = checkiIfInDB(lastAddedOst);

        if(!alreadyAdded){
            db.addNewOst(lastAddedOst);
            Toast.makeText(getApplicationContext(), lastAddedOst.getTitle() + " added", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this, lastAddedOst.getTitle() + " From " + lastAddedOst.getShow() + " has already been added", Toast.LENGTH_SHORT).show();
            lastAddedOst = null;
        }
    }

    private void launchListScreen(){
        Intent intent = new Intent(this, ListScreen.class);
        startActivity(intent);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
                Uri currFileURI = data.getData();
                readFromFile(currFileURI);
        }
        if (requestCode == 2 && resultCode == RESULT_OK) {
            Uri currFileURI = data.getData();
            try{
                writeToFile(currFileURI);

            }catch(java.io.IOException e){
                System.out.println(" caught IOexception");
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void chooseFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        System.out.println("File Chooser launched");

        try {
            startActivityForResult(
                    Intent.createChooser(intent, "Select a File to Upload"),
                    1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(getApplicationContext(), "Please install a File Manager.", Toast.LENGTH_SHORT).show();
        }
    }

    private void chooseFileDir(){
        Intent intent;
        if (Build.VERSION.SDK_INT < 19) {
            intent = new Intent();
            intent.setAction(Intent.ACTION_GET_CONTENT);
            intent.setType("text/plain");
            startActivityForResult(intent, 2);
        } else {
            intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
            intent.setType("text/plain");
            startActivityForResult(intent, 2);
        }
    }

    public void readFromFile(Uri uri){
        try {
            InputStream is = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            while((line = reader.readLine()) != null) {
                Ost ost = new Ost();
                System.out.println(line);
                String[] lineArray = line.split("; ");
                ost.setTitle(lineArray[0]);
                ost.setShow(lineArray[1]);
                ost.setTags(lineArray[2]);
                ost.setUrl(lineArray[3]);
                boolean alreadyInDB = checkiIfInDB(ost);
                if (!alreadyInDB){
                    db.addNewOst(ost);
                }
            }
        }catch (java.io.IOException e){
            System.out.println("File not found");
        }
    }

    public void writeToFile(Uri uri) throws IOException{
        ostList= db.getAllOsts();
        try {
            OutputStream os = getContentResolver().openOutputStream(uri);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            String line;
            for( Ost ost : ostList){

                String title = ost.getTitle();
                String show = ost.getShow();
                String tags = ost.getTags();
                String url = ost.getUrl();
                line = title + "; " + show + "; " + tags + "; " + url + "; ";
                System.out.println(line);
                osw.write(line + "\n");
            }
            osw.close();
        }catch (java.io.IOException e){
            throw new java.io.IOException("File not found");
        }
    }

    public boolean checkiIfInDB(Ost ost) {
        ostList = db.getAllOsts();
        String ostString = ost.toString().toLowerCase();
            for (Ost ostFromDB : ostList){
                if(ostFromDB.toString().toLowerCase().equals(ostString)){
                    return true;
                }
            }
        return false;
        }
}
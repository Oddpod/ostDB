package com.example.odd.ostrino;

import android.app.DialogFragment;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.List;
import java.util.Random;

public class AnimuOst extends AppCompatActivity implements AddScreen.AddScreenListener {

    Button btnAdd, btnViewList, btnAddOsts, btnExportOsts, btnRandomOst;
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
        btnExportOsts = (Button) findViewById(R.id.btnExport);
        btnRandomOst = (Button) findViewById(R.id.btnRandomOst);

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

        btnAddOsts.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                showFileChooser();

            }
        });
        btnExportOsts.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                ostList = db.getAllOsts();
                //File file = new File("OstDBExported.txt");
                try {
                    //file.createNewFile();
                    //FileOutputStream os = new FileOutputStream(file);
                    //String path = file.getAbsolutePath();
                    //System.out.println(os.toString());
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    intent.setType("*/*");
                    startActivity(intent);
                    for( Ost ost : ostList){

                        String title = ost.getTitle();
                        String show = ost.getShow();
                        String tags = ost.getTags();
                        String url = ost.getUrl();
                        //os.write((title + "; " + show + "; " + tags + "; " + url + "; ").getBytes());
                    }
                }catch (Exception e){
                    System.out.println("File not found");
                }
            }
        });
        btnRandomOst.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                ostList = db.getAllOsts();
                Random rnd = new Random();
                int rndId = rnd.nextInt(ostList.size());
                Ost ost = db.getOst(rndId);
                String url = ost.getUrl();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(resultCode + requestCode + data.getData().toString());
        if (requestCode == 1 && resultCode == RESULT_OK) {
                Uri currFileURI = data.getData();
                readFromFile(currFileURI);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void showFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
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

    public void readFromFile(Uri uri){
        try {
            ostList = db.getAllOsts();
            dtb = db.getWritableDatabase();
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
                if (!ostList.contains(ost)){
                    System.out.println(ost);
                    db.addNewOst(ost);
                }
            }
        }catch (java.io.IOException e){
            System.out.println("File not found");
        }
    }

    public String getPath(Uri uri)
    {
        String[] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);
        if (cursor == null) return null;
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        String s=cursor.getString(column_index);
        cursor.close();
        return s;
    }
}
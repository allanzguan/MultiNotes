package com.allanguan.notepad;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.JsonWriter;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
        implements View.OnClickListener, View.OnLongClickListener, Serializable {

    private static final String TAG = "MainActivity";
    private final List<Note> noteList = new ArrayList<>();
    private RecyclerView recyclerView;
    private static final int ADD_CODE = 1;
    private static final int EDIT_CODE = 2;

    private Note n;
    private NoteAdapter nAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler);
        nAdapter = new NoteAdapter(noteList, this);
        recyclerView.setAdapter(nAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        noteList.clear();
        loadFile();
        updateTitle();
    }

    @Override
    protected void onPause(){
        saveNotes();
        super.onPause();
    }

    protected void updateTitle(){
        getSupportActionBar().setTitle("Multi Notes ("+ noteList.size() +")");
    }


    public void onClick(View v) {
        int pos = recyclerView.getChildLayoutPosition(v);
        n = noteList.get(pos);
        Intent data = new Intent(this, EditActivity.class);
        data.putExtra("NOTE_OBJ", n);

        startActivityForResult(data, EDIT_CODE);
    }


    public boolean onLongClick(final View v) {
        final int pos = recyclerView.getChildLayoutPosition(v);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                noteList.remove(pos);
                updateTitle();
                nAdapter.notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

            }
        });

        builder.setTitle("Delete Note \"" + noteList.get(pos).getTitle() + "\"?");

        AlertDialog dialog = builder.create();
        dialog.show();
        return true;
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.action_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item){

        Intent intent;
        switch(item.getItemId()){
            case R.id.editMenu:
                intent = new Intent(this, EditActivity.class);

                startActivityForResult(intent, ADD_CODE);
                return true;
            case R.id.aboutMenu:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == ADD_CODE){
                if(resultCode == RESULT_CANCELED){
                }
                else if(resultCode == RESULT_OK){
                    noteList.add(0, (Note) data.getSerializableExtra("OBJ"));
                    updateTitle();
                    nAdapter.notifyDataSetChanged();
                }
            }
            else if (requestCode == EDIT_CODE){
                if(resultCode == RESULT_CANCELED){
                }
                if(resultCode == RESULT_OK) {
                    Note nn = (Note) data.getSerializableExtra("OBJ");
                    n.setNotes(nn.getNotes());
                    n.setTitle(nn.getTitle());
                    n.updateTruncated();
                    n.updateDate();
                    noteList.remove(n);
                    noteList.add(0, n);
                    nAdapter.notifyDataSetChanged();
                }
            }
            else{
                Log.d(TAG, "onActivityResult: at else:  " + requestCode);
            }

    }


    private void saveNotes() {
        try{
            FileOutputStream fos = getApplicationContext().
                    openFileOutput(getString(R.string.file_name), Context.MODE_PRIVATE);

            JsonWriter writer = new JsonWriter(new OutputStreamWriter(fos, getString(R.string.encoding)));
            writer.setIndent("  ");
            writer.beginArray();
            for (Note i : noteList){
                writer.beginObject();
                writer.name("title").value(i.getTitle());
                writer.name("note").value(i.getNotes());
                writer.name("truncated").value(i.getTruncatedNote());
                writer.name("date").value(i.getDate());
                writer.endObject();
            }
            writer.endArray();
            writer.close();

        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    private void loadFile(){
        try{
            FileInputStream fis = getApplicationContext().openFileInput("Notes.json");
            byte[] data = new byte[(int) fis.available()];
            int loaded = fis.read(data);
            Log.d(TAG, "readJSONData: Loaded " + loaded + " bytes");
            fis.close();
            String json = new String(data);

            JSONArray noteArr = new JSONArray(json);
            for (int i = 0; i < noteArr.length(); i++) {
                JSONObject nObj = noteArr.getJSONObject(i);
                Note n = new Note(nObj.getString("title"), nObj.getString("note"));
                n.setTruncatedNote(nObj.getString("truncated"));
                n.setDate(nObj.getString("date"));
                noteList.add(n);
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }

    }

}
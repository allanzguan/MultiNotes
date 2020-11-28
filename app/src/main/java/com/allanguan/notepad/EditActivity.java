package com.allanguan.notepad;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    private Note n;
    TextView editTitle;
    TextView editNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        editTitle = findViewById(R.id.editTitle);
        editNote = findViewById(R.id.editNote);

        if(getIntent().hasExtra("NOTE_OBJ")){
            n = (Note) getIntent().getSerializableExtra("NOTE_OBJ");
            editTitle.setText(n.getTitle());
            editNote.setText(n.getNotes());
        }

        getSupportActionBar().setTitle("Multi Notes");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        getMenuInflater().inflate(R.menu.edit_save, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        String title = editTitle.getText().toString();
        String note = editNote.getText().toString();

        switch(item.getItemId()){
            case R.id.save:
//                doReturnData();
                if(title.isEmpty() && note.isEmpty()){
                    setResult(RESULT_CANCELED);
                    finish();
                }
                else if(title.isEmpty() && !note.isEmpty()){
//                    Toast.makeText(this, "EMPTY TITLE NOT ALLOWED, NOTE NOT SAVED", Toast.LENGTH_SHORT).show();
//                    setResult(RESULT_CANCELED);
//                    finish();
                    doReturnData();
                }
                else if(n != null && n.getNotes().equals(note) && n.getTitle().equals(title)){
                    setResult(RESULT_CANCELED);
                    finish();
                }
                else{
                    doReturnData();
                }
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void doReturnData(){
        String title = editTitle.getText().toString();
        String note = editNote.getText().toString();

        Intent data = new Intent();
        if(title.isEmpty() && !note.isEmpty()){
            Toast.makeText(this, "EMPTY TITLE NOT ALLOWED, NOTE NOT SAVED", Toast.LENGTH_SHORT).show();
            setResult(RESULT_CANCELED);
            finish();
        }

        if (n == null){
            n = new Note(editTitle.getText().toString(), editNote.getText().toString());
            data.putExtra("OBJ", n);
            setResult(RESULT_OK, data);
            finish();

        }
        if(n != null){
            if(editTitle.getText().toString().equals(n.getTitle()) && editNote.getText().toString().equals(n.getNotes())){
                setResult(RESULT_CANCELED);
                finish();
            }
            else{
                n.setTitle(editTitle.getText().toString());
                n.setNotes(editNote.getText().toString());
                data.putExtra("OBJ", n);
                setResult(RESULT_OK, data);
                finish();
            }

        }

    }


    @Override
    public void onBackPressed(){
        String title = editTitle.getText().toString();
        String note = editNote.getText().toString();

        if(title.isEmpty() && note.isEmpty()){
            setResult(RESULT_CANCELED);
            finish();
        }
//        else if (title.isEmpty() && !note.isEmpty()){
////            Toast.makeText(this, "EMPTY TITLE NOT ALLOWED, NOTE NOT SAVED", Toast.LENGTH_SHORT).show();
////            setResult(RESULT_CANCELED);
////            finish();
//        }
        else if( n!=null && n.getTitle().equals(title) && n.getNotes().equals(note)){
            setResult(RESULT_CANCELED);
            finish();
        }
        else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("Your note is not saved!");
            builder.setMessage("Save note \"" + editTitle.getText().toString() + "\"?");
            builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    doReturnData();
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    setResult(RESULT_CANCELED);
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }
    }

}
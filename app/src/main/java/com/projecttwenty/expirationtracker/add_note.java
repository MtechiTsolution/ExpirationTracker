package com.projecttwenty.expirationtracker;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class add_note extends AppCompatActivity {

    EditText note;
    Button addnote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

//        ActionBar actionBar = getSupportActionBar();
//        getSupportActionBar().hide();
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        note=findViewById(R.id.note);
        addnote=findViewById(R.id.btnAdd);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        String notes = preferences.getString("note", "");
        note.setText(notes);
        addnote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(add_note.this)
                        .setIcon(R.drawable.add_note)
                        .setTitle("SAVE NOTE")
                        .setMessage("Are you want to add note?")
                        .setCancelable(false)
                        .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(add_note.this);
                                SharedPreferences.Editor editor = preferences.edit();
                                editor.putString("note",note.getText().toString());
                                editor.apply();

                                add_note.super.onBackPressed();
                            }
                        })
                        .setNegativeButton("Edit", null)
                        .show();
            }
        });
    }
    @Override
    public void onBackPressed() {
        new AlertDialog.Builder(this)
                .setIcon(R.drawable.add_note)
                .setTitle("SAVE NOTE")
                .setMessage("Are you want to add note?")
                .setCancelable(false)
                .setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(add_note.this);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString("note",note.getText().toString());
                        editor.apply();

                        add_note.super.onBackPressed();
                    }
                })
                .setNegativeButton("Edit", null)
                .show();
    }
    }
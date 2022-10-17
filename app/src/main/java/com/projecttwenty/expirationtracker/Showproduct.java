package com.projecttwenty.expirationtracker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class Showproduct extends AppCompatActivity {
    String title,catogry,expirydate,expirycycle,expiryprice,location,reminder,note,img;
    TextView Title,Catogry,Expirydate,Expirycycle,Expiryprice,Location,Reminder,Note;
    ImageView Img;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_showproduct);
        Intent intent=getIntent();
        Img=findViewById(R.id.simg);
        Title=findViewById(R.id.stitle);
        Catogry=findViewById(R.id.scatogry);
        Expirydate=findViewById(R.id.sexpdate);
        Expirycycle=findViewById(R.id.sexpcycle);
        Expiryprice=findViewById(R.id.sexpprice);
        Reminder=findViewById(R.id.sremnder);
        Location=findViewById(R.id.slocation);
        Note=findViewById(R.id.snote);
        title=intent.getStringExtra("title");
        catogry=intent.getStringExtra("catogry");
        expirydate=intent.getStringExtra("expirydate");
        expirycycle=intent.getStringExtra("expirycycle");
        expiryprice=intent.getStringExtra("expiryprice");
        reminder=intent.getStringExtra("reminder");
        location=intent.getStringExtra("location");
        note=intent.getStringExtra("note");
        img=intent.getStringExtra("img");
        Title.setText(title);
        Catogry.setText(catogry);
        Expirydate.setText(expirydate);
        Expirycycle.setText(expirycycle);
        Expiryprice.setText(expiryprice);
        Reminder.setText(reminder);
        Location.setText(location);
        Note.setText(note);
        Picasso.get().load(img).placeholder(R.drawable.category).into(Img);
    }
}
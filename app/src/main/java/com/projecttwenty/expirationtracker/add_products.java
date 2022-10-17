package com.projecttwenty.expirationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;


import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;

public class add_products extends AppCompatActivity implements AdapterView.OnItemSelectedListener, AdapterView.OnItemClickListener {

    String title,catogry,expirydate,expirycycle,expiryprice,location,reminder,notekp,img;
    int id=0;
    FirebaseStorage fs;
    StorageReference sr;
    Uri filepath;
    String downloodurl;

    CardView note;
   EditText Title,ExpiryDate,Exprice,Reminder,Location;
   Spinner Category,ExpiryCycle;

   ImageView prdimg;
   Button Save;
   String notes;
   FirebaseDatabase fb;
   DatabaseReference dr;
   int counter=0,test=0;
     Calendar myCalendar = Calendar.getInstance();
    private ArrayList<CountryItem> mCountryList;
    private CountryAdapter mAdapter;
    String catogryName,expirycycles;
    String[] cycles = { "Weekly", "Monthly", "Yearly"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_products);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        note=findViewById(R.id.add_note);
//        ActionBar actionBar =getSupportActionBar();
//        getSupportActionBar().hide();
//        actionBar.hide();
        prdimg=findViewById(R.id.imgs);
        Title=findViewById(R.id.titl);
        Category=findViewById(R.id.ctgry);
        ExpiryDate=findViewById(R.id.expdte);
        ExpiryCycle=findViewById(R.id.expcycl);
   //     ExpiryCycle.setOnItemClickListener(this);
        Exprice=findViewById(R.id.expric);
        Reminder=findViewById(R.id.rmndr);
        Location=findViewById(R.id.loctn);
        Save=findViewById(R.id.sve);
        dr=FirebaseDatabase.getInstance().getReference();
        fs=FirebaseStorage.getInstance();
        sr=fs.getReference();
        initList();
        edit();

        mAdapter = new CountryAdapter(this, mCountryList);
        Category.setAdapter(mAdapter);
        Category.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                CountryItem clickedItem = (CountryItem) parent.getItemAtPosition(position);
                 catogryName = clickedItem.getCountryName();
                Toast.makeText(add_products.this, catogryName + " selected", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,cycles);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        ExpiryCycle.setAdapter(aa);


        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };

        dr.child("Products").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID))
                .addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists())
                {
                    counter= (int) dataSnapshot.getChildrenCount();

                }
                else
                {
                    counter=0;

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        prdimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choseimgs();
            }
        });

        ExpiryDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                test=1;
                new DatePickerDialog(add_products.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        Reminder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test=0;
                if(ExpiryDate.getText().toString().isEmpty())
                {
                    Toast.makeText(add_products.this,"First placed expiry date of product... ",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    new DatePickerDialog(add_products.this, date, myCalendar
                            .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                            myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                }

            }
        });

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_products.this, add_note.class);
//                    EditText editText = (EditText) findViewById(R.id.editText);
//                    String message = editText.getText().toString();
//                    intent.putExtra(EXTRA_MESSAGE, message);
                startActivity(intent);
            }
        });

        Save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(Title.getText().toString().isEmpty()||ExpiryDate.getText().toString().isEmpty()
                        ||Reminder.getText().toString().isEmpty()
                        )
                {
                    Toast.makeText(add_products.this,"Please fill require fields...",Toast.LENGTH_SHORT).show();
                }
                else
                {
                    final ProgressDialog progressDialog=new ProgressDialog(add_products.this);
                    progressDialog.setTitle("saving....");
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.show();

                    if(id==0)
                    {
                        id=counter+1;
                    }



                    ProductModel productModel=new ProductModel(Title.getText().toString(),catogryName,ExpiryDate.getText().toString(),
                            expirycycles,Exprice.getText().toString(),Reminder.getText().toString(),
                            Location.getText().toString(),notes,downloodurl,"expiring",String.valueOf(id));
                    dr.child("Products").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).child(String.valueOf(id)).setValue(productModel).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            progressDialog.dismiss();
                            onBackPressed();

                        }
                    });

                }

            }
        });




    }
    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        Date expDate = null;
        Date remDate = null;


        if(test==1)
        {
            ExpiryDate.setText(sdf.format(myCalendar.getTime()));
        }
        else
        {
            try {
                expDate = sdf.parse(ExpiryDate.getText().toString());
                remDate = sdf.parse(sdf.format(myCalendar.getTime()));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (remDate.before(expDate)) {
                Reminder.setText(sdf.format(myCalendar.getTime()));
            }
            else
            {
                Toast.makeText(add_products.this,"Reminder date must be befor from expiry date...",Toast.LENGTH_SHORT).show();
            }

        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        notes = preferences.getString("note", "");

    }
    private void initList() {
        mCountryList = new ArrayList<>();
        dr.child("Category").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                String messages = snapshot.getValue(String.class);
                mCountryList.add(new CountryItem(messages, R.drawable.category));
                mAdapter.notifyDataSetChanged();

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
    void choseimgs()
    {
        Intent intent=new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select image"),1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        if(requestCode==1&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null)
        {

            filepath=data.getData();

            if (filepath!=null)
            {

                final ProgressDialog progressDialog=new ProgressDialog(this);
                progressDialog.setTitle("uploading....");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();

                int p=counter+1;
                final StorageReference stdf=sr.child("images/"+Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)+"/"+p);
                UploadTask uploadTask = stdf.putFile(filepath);

                Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot,
                                        Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if (!task.isSuccessful()) {
                            throw task.getException();
                        }

                        // Continue with the task to get the download URL
                        return stdf.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if (task.isSuccessful()) {
                            Uri downloadUri = task.getResult();

                            downloodurl=downloadUri.toString();
                            if (task.isSuccessful())
                            {

                                Picasso.get().load(downloodurl).into(prdimg);
                                progressDialog.dismiss();
                                Toast.makeText(add_products.this, "Uploaded", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(add_products.this,"error "+task.getException(),Toast.LENGTH_SHORT).show();
                            }

                        }

                    }

                });




            }
        }
    }
    void edit()
    {
        Intent intent=getIntent();
        id=intent.getIntExtra("id",0);
        if(id!=0)
        {

            title=intent.getStringExtra("title");
            catogry=intent.getStringExtra("catogry");
            expirydate=intent.getStringExtra("expirydate");
            expirycycle=intent.getStringExtra("expirycycle");
            expiryprice=intent.getStringExtra("expiryprice");
            reminder=intent.getStringExtra("reminder");
            location=intent.getStringExtra("location");
            notekp=intent.getStringExtra("note");
            img=intent.getStringExtra("img");
            Title.setText(title);
            // Catogry.setText(catogry);
            ExpiryDate.setText(expirydate);
            //ExpiryCycle.setText(expirycycle);
            expirycycles=expirycycle;
            Exprice.setText(expiryprice);
            Reminder.setText(reminder);
            Location.setText(location);
            downloodurl=img;
           // Toast.makeText(add_products.this,img.length(),Toast.LENGTH_SHORT).show();
            Picasso.get().load(img).placeholder(R.drawable.add_pic).into(prdimg);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        expirycycles=cycles[position];
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

    }
}
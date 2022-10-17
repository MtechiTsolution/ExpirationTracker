package com.projecttwenty.expirationtracker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.allyants.notifyme.NotifyMe;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.msoftworks.easynotify.EasyNotify;
import com.trenzlr.firebasenotificationhelper.FirebaseNotiCallBack;
import com.trenzlr.firebasenotificationhelper.FirebaseNotificationHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.trenzlr.firebasenotificationhelper.Constants.KEY_TEXT;
import static com.trenzlr.firebasenotificationhelper.Constants.KEY_TITLE;

public class MainActivity extends AppCompatActivity {
    public static final String EXTRA_MESSAGE = "com.example.myfirstapp.MESSAGE";

    ProductFragment productFragment=new ProductFragment();
    CategoryFragment categoryFragment=new CategoryFragment();
    ExpiredFragment expiredFragment=new ExpiredFragment();


    TextView textView,catogrys,expired;
    BroadcastReceiver networkStateReceiver;
    private boolean doubleBackToExitPressedOnce=false;

    List<ProductModel> models;
    DatabaseReference dr;
    ProductModel messages;
    Date data3;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        textView=findViewById(R.id.product);
        catogrys=findViewById(R.id.catgry);
        expired=findViewById(R.id.exprd);
        loadFragment(productFragment);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(productFragment);
            }
        });
        catogrys.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(categoryFragment);
            }
        });
        expired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadFragment(expiredFragment);
            }
        });




        broadcastrecever();




    }



//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu, menu);
//
//        return true;
//    }
//    @Override
//    public void onBackPressed() {
//        if (doubleBackToExitPressedOnce) {
//            super.onBackPressed();
//            return;
//        }
//
//        this.doubleBackToExitPressedOnce = true;
//        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
//
//        new Handler().postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                doubleBackToExitPressedOnce=false;
//            }
//        }, 2000);
//    }

    private void loadFragment(Fragment fragment) {
        // load fragment
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//frame_container is your layout name in xml file
        transaction.replace(R.id.nav_host_fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
    void broadcastrecever()
    {

         Calendar calendar;
         SimpleDateFormat dateFormat;
         String date;
        calendar = Calendar.getInstance();
        dateFormat = new SimpleDateFormat("MM/dd/yy");
        date = dateFormat.format(calendar.getTime());
        dr= FirebaseDatabase.getInstance().getReference();
        networkStateReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {


                if (network()) {
                    models = new ArrayList<>();
                    models.clear();
                    dr.child("Products").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).addChildEventListener(new ChildEventListener() {
                        @Override
                        public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                            messages = snapshot.getValue(ProductModel.class);
                            if(!messages.getReminder().equals("")&&(messages.getStatus().equals("expiring")||messages.getStatus().equals("reminding")))
                            {
//                               Toast.makeText(MainActivity.this,"okd",Toast.LENGTH_SHORT).show();
                                Date date1=getdate(date);
                                Date date2=getdate(messages.getReminder());
                                  data3=getdate(messages.getExpiryDate());
                                if(date1.compareTo(date2)==0)
                                {
                                    Intent mServiceIntent;
                                    mServiceIntent = new Intent(getApplicationContext(), NotificationService.class);
                                    String message = "This is my awesome text for notification!";
                                    mServiceIntent.putExtra(CommonConstants.EXTRA_MESSAGE, message);
                                    mServiceIntent.setAction(CommonConstants.ACTION_NOTIFY);
                                    mServiceIntent.putExtra(CommonConstants.EXTRA_TIMER, 2000);
                                    mServiceIntent.putExtra(CommonConstants.title, messages.getTitle());
                                    mServiceIntent.putExtra(CommonConstants.content, messages.getTitle()+" is going to  expiry...");
                                    startService(mServiceIntent);
                                    dr.child("Products").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).child(messages.getId()).child("status").setValue("reminding");

                                }
                                if(date1.compareTo(data3)==0)
                                    dr.child("Products").child(Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID)).child(messages.getId()).child("status").setValue("expired");
                                models.add(messages);
                            }

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



                } else {



                }

            }
        };



        IntentFilter filter = new IntentFilter(
                ConnectivityManager.CONNECTIVITY_ACTION);
        try {
            registerReceiver(networkStateReceiver, filter);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public boolean network()
    {
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)getSystemService( Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        }
        else
            connected = false;

        return  connected;
    }
    Date getdate(String dates)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");
        Date date = null;
        try {
            date = sdf.parse(dates);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}
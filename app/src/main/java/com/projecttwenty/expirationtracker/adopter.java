package com.projecttwenty.expirationtracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.List;



public class adopter extends RecyclerView.Adapter<adopter.newviewholder> {
    static Context context;
    static List<String> data;
    static int m = 0;


    FirebaseDatabase fb;
    static String sp;
    static DatabaseReference dr;


    public adopter(Context context, List<String> data) {
        this.context = context;
        this.data = data;
        this.m = m;
    }




    @Override
    public newviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.product_design, null);
        Log.d("TAG", "reach1");

        fb= FirebaseDatabase.getInstance();
        dr=fb.getReference();



        return new newviewholder(view);
    }

    @Override
    public void onBindViewHolder(final newviewholder holder, final int position) {
        final String dm = data.get(position);
        holder.name.setText(dm);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class newviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;



        public newviewholder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.txt);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

//            final String dm = data.get(getPosition());
//            Intent intent=new Intent(context,Ordernow.class);
//            intent.putExtra("ids",Integer.parseInt(dm.getId()));
//            context.startActivity(intent);
        }


    }

}
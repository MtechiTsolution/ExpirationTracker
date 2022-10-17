package com.projecttwenty.expirationtracker;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;


import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;


public class adopterP extends RecyclerView.Adapter<adopterP.newviewholder> {
    static Context context;
    static List<ProductModel> data;



    FirebaseDatabase fb;
    static String sp;
    static DatabaseReference dr;


    public adopterP(Context context, List<ProductModel> data) {
        this.context = context;
        this.data = data;

    }




    @Override
    public newviewholder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.productrow, null);
        Log.d("TAG", "reach1");

        fb= FirebaseDatabase.getInstance();
        dr=fb.getReference();

        return new newviewholder(view);
    }

    @Override
    public void onBindViewHolder(final newviewholder holder, final int position) {
        final ProductModel dm = data.get(position);
        holder.name.setText(dm.getTitle());
        Picasso.get().load(dm.getImg()).placeholder(R.drawable.product).into(holder.imageView);
        if(dm.getStatus().equals("reminding"))
        {
            holder.remind.setVisibility(View.VISIBLE);
        }
       holder.delete.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        AlertDialog.Builder alertDialog2 = new AlertDialog.Builder(context);
        alertDialog2.setTitle("Confirm Delete");
        alertDialog2.setMessage("Are you sure you want to delete this product?");
        alertDialog2.setIcon(R.drawable.del);
        alertDialog2.setPositiveButton("YES",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        dr.child("Products").child(Settings.Secure.getString
                                (context.getContentResolver(),
                                        Settings.Secure.ANDROID_ID))
                                .child(dm.getId()).child("status").setValue("Deleted")
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        data.remove(position);
                                        notifyDataSetChanged();
                                    }
                                });

                        Toast.makeText(context,
                                "Product deleted Successfully", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
        alertDialog2.setNegativeButton("NO",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        Toast.makeText(context,
                                "Product Not deleted", Toast.LENGTH_SHORT)
                                .show();
                        dialog.cancel();
                    }
                });
        alertDialog2.show();

    }
});

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendingdata(dm,2);

            }
        });
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class newviewholder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        CircleImageView circleImageView;
        ImageView imageView,delete,edit,remind;


        public newviewholder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.pdtxt);
            imageView =itemView.findViewById(R.id.pdcimg);
            edit =itemView.findViewById(R.id.edit);
            delete =itemView.findViewById(R.id.del);
            remind =itemView.findViewById(R.id.rmnd);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            final ProductModel dm = data.get(getPosition());
            sendingdata(dm,1);
        }


    }

    void sendingdata(ProductModel dm,int check)
    {
        Intent intent;
        if(check==1)
        {
             intent=new Intent(context,Showproduct.class);
        }
        else
        {
             intent=new Intent(context,add_products.class);
        }


        intent.putExtra("title",dm.getTitle());
        intent.putExtra("catogry",dm.getCategory());
        intent.putExtra("expirydate",dm.getExpiryDate());
        intent.putExtra("expirycycle",dm.getExpiryCycle());
        intent.putExtra("expiryprice",dm.getExprice());
        intent.putExtra("reminder",dm.getReminder());
        intent.putExtra("location",dm.getLocation());
        intent.putExtra("note",dm.getNote());
        intent.putExtra("img",dm.getImg());
        intent.putExtra("id", Integer.parseInt(dm.getId()));
        context.startActivity(intent);
    }
    public void searchItemName(ArrayList<ProductModel> recipeArrayList) {

        data=recipeArrayList;
        notifyDataSetChanged ();

    }

}
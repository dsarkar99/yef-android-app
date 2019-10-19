package com.example.yef.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yef.Event_detail;
import com.example.yef.Model.modelclass;
import com.example.yef.R;
import com.google.android.material.snackbar.Snackbar;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class eventadapter extends RecyclerView.Adapter<eventadapter.MyViewHolder> {
    private Context context;
    private List<modelclass> contactList;
    View itemView;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public CardView cardView;
        public TextView eventname;

        public MyViewHolder(View view) {
            super(view);
            cardView = (CardView) view.findViewById(R.id.card_view);
            eventname = (TextView) view.findViewById(R.id.eventname);
        }
    }


    public eventadapter(Context context, List<modelclass> contactList) {
        this.context = context;
        this.contactList = contactList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_row, parent, false);


        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        final modelclass pdf = contactList.get(position);
        holder.eventname.setText(pdf.getname());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                                      Intent modify_intent = new Intent(context,
                                Event_detail.class);

                        modify_intent.putExtra("eventname", pdf.getname());
                Toast.makeText(context, pdf.getname(), Toast.LENGTH_SHORT).show();
                context.startActivity(modify_intent);
            }
        });

    }




    @Override
    public int getItemCount() {
        return contactList.size();
    }


    void open_direction(String l)
    {
        Toast.makeText(context, "Please Wait! Showing Possible directions on the map", Toast.LENGTH_LONG).show();
        try {
            String[] str_array = l.split(",");
            String latitute = str_array[0];
            String longitude = str_array[1];
        } catch (NullPointerException e) {
            System.out.println(e.toString());
        }

        Uri gmmIntentUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination="+l+"&travelmode=walking");
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }


}

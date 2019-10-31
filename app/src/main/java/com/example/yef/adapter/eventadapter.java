package com.example.yef.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
                DateFormat df = new SimpleDateFormat("HH:mm");
                //Date/time pattern of desired output date
                DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
                Date date = null;
                String output = null;
                try{
                    //Conversion of input String to date
                    date= df.parse(pdf.getTime());
                    //old date format to new date format
                    output = outputformat.format(date);
                    //System.out.println(output);
                }catch(ParseException pe){
                    pe.printStackTrace();
                }
                                      Intent modify_intent = new Intent(context, Event_detail.class);
                                      modify_intent.putExtra("eventname", pdf.getname());
                modify_intent.putExtra("venue", pdf.getVenue());
                modify_intent.putExtra("esdate", pdf.getStart_date());
                modify_intent.putExtra("eedate", pdf.getEnd_date());
                modify_intent.putExtra("etime", output);
                modify_intent.putExtra("etype", pdf.getEvent_type());
                modify_intent.putExtra("edetails", pdf.getEvent_details());
                //Toast.makeText(context, pdf.getname(), Toast.LENGTH_SHORT).show();
                context.startActivity(modify_intent);
                ((Activity)context).finish();
            }
        });
/*        holder.cardView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                DateFormat df = new SimpleDateFormat("HH:mm");
                //Date/time pattern of desired output date
                DateFormat outputformat = new SimpleDateFormat("hh:mm aa");
                Date date = null;
                String output = null;
                try{
                    //Conversion of input String to date
                    date= df.parse(pdf.getTime());
                    //old date format to new date format
                    output = outputformat.format(date);
                    //System.out.println(output);
                    Snackbar sb = Snackbar.make(v, output, Snackbar.LENGTH_LONG);
                    sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.snackbarbg));
                    sb.show();
                }catch(ParseException pe){
                    pe.printStackTrace();
                }

                return true;
            }
        });*/

    }




    @Override
    public int getItemCount() {
        return contactList.size();
    }





}


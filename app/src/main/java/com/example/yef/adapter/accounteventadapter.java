package com.example.yef.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.yef.Model.accountmodelclass;
import com.example.yef.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.List;

public class accounteventadapter extends RecyclerView.Adapter<accounteventadapter.MyViewHolder> {
private Context context;
private List<accountmodelclass> contactList;
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


    public accounteventadapter(Context context, List<accountmodelclass> contactList) {
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
        final accountmodelclass pdf = contactList.get(position);
        holder.eventname.setText(pdf.getname());
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceAsColor")
            @Override
            public void onClick(View view) {
                Snackbar sb = Snackbar.make(view, pdf.getname(), Snackbar.LENGTH_LONG);
                sb.getView().setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.snackbarbg));
                sb.show();
            }
        });

    }




    @Override
    public int getItemCount() {
        return contactList.size();
    }





}

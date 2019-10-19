package com.example.yef;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.Viewholder> {
    private List<ModelClass>modelClassList;
    public Adapter(List<ModelClass>modelClassList){
        this.modelClassList=modelClassList;
    }


    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view= LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_view,viewGroup,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder viewholder, int position) {
        String body=modelClassList.get(position).getBody();
        viewholder.setData(body);

    }

    @Override
    public int getItemCount() {
        return modelClassList.size();
    }

    class Viewholder extends RecyclerView.ViewHolder{
        private TextView body;


        public Viewholder(@NonNull View itemView) {
            super(itemView);
            body=itemView.findViewById(R.id.textview);
        }
        private void setData(String bodytext){
            body.setText(bodytext);

        }
    }
}

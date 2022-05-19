package com.app.roktoDorkar.view.RequestFrag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

public class ReceivedListAdapter extends RecyclerView.Adapter<ReceivedListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ReceviedListModel> receviedListModelArrayList;

    public ReceivedListAdapter(Context context, ArrayList<ReceviedListModel> receviedListModelArrayList) {
        this.context = context;
        this.receviedListModelArrayList = receviedListModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.receivedlist_item,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ReceviedListModel receviedListModel=receviedListModelArrayList.get(position);
        holder.textViewBloodgroup.setText(receviedListModel.getBloodGroup());
        holder.textViewDonarName.setText(receviedListModel.getUserName());
        holder.textViewLocation.setText(receviedListModel.getLocation());

    }

    @Override
    public int getItemCount() {
        return receviedListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBloodgroup,textViewDonarName,textViewLocation;
        MaterialButton materialButtonAccept,materialButtonDecline;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
        }
    }
}

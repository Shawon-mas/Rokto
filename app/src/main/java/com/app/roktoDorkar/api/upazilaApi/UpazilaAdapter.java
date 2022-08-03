package com.app.roktoDorkar.api.upazilaApi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;

import java.util.ArrayList;

public class UpazilaAdapter extends RecyclerView.Adapter<UpazilaAdapter.MyVieHolder>{
    Context context;
    private ArrayList<UpzilaModel.Upazila> upzilaModelArrayList;
    private UpItemClick upItemClick;
    public void setOnItemClckListener(UpItemClick itemClick){
        upItemClick=itemClick;
    }

    public UpazilaAdapter(Context context, ArrayList<UpzilaModel.Upazila> upzilaModelArrayList) {
        this.context = context;
        this.upzilaModelArrayList = upzilaModelArrayList;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.upzila_name,parent,false);
        return new MyVieHolder(view,upItemClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {
        UpzilaModel.Upazila upazila= upzilaModelArrayList.get(position);
        holder.textView_upzilla.setText(upazila.getUpazila());

    }

    @Override
    public int getItemCount() {
        return upzilaModelArrayList.size();
    }
    public void filterListUp(ArrayList<UpzilaModel.Upazila> filtering)
    {
        upzilaModelArrayList=filtering;
        notifyDataSetChanged();
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView textView_upzilla;
        public MyVieHolder(@NonNull View itemView, UpItemClick upItemClick) {
            super(itemView);
            textView_upzilla=itemView.findViewById(R.id.upzila_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (upItemClick!=null){
                        int pos=getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            upItemClick.onUPItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

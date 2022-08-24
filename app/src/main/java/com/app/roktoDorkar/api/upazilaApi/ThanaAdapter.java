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

public class ThanaAdapter extends RecyclerView.Adapter<ThanaAdapter.MyVieHolder>{
    Context context;
    private ArrayList<ThanaModel.UpThana> upThanaArrayList;
    private ThanaClick thanaClick;
    public void setOnItemClckListener(ThanaClick itemClckListener){
        thanaClick =itemClckListener;
    }

    public ThanaAdapter(Context context, ArrayList<ThanaModel.UpThana> upThanaArrayList) {
        this.context = context;
        this.upThanaArrayList = upThanaArrayList;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.upzila_name,parent,false);
        return new MyVieHolder(view, thanaClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {
        ThanaModel.UpThana upThana= upThanaArrayList.get(position);
        holder.textView_upzilla.setText(upThana.getName());

    }

    @Override
    public int getItemCount() {
        return upThanaArrayList.size();
    }
    public void filterListUp(ArrayList<DistrictModel.District> filtering)
    {
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView textView_upzilla;
        public MyVieHolder(@NonNull View itemView, ThanaClick thanaClick) {
            super(itemView);
            textView_upzilla=itemView.findViewById(R.id.upzila_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (thanaClick !=null){
                        int pos=getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            thanaClick.onThanaItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

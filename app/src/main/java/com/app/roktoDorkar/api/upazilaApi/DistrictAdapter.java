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

public class DistrictAdapter extends RecyclerView.Adapter<DistrictAdapter.MyVieHolder>{
    Context context;
    private ArrayList<DistrictModel.District> districtsModelArrayList;
    private DistrictClick districtClick;
    public void setOnItemClckListener(DistrictClick itemClckListener){
        districtClick =itemClckListener;
    }

    public DistrictAdapter(Context context, ArrayList<DistrictModel.District> districtsModelArrayList) {
        this.context = context;
        this.districtsModelArrayList = districtsModelArrayList;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.upzila_name,parent,false);
        return new MyVieHolder(view, districtClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {
        DistrictModel.District district= districtsModelArrayList.get(position);
        holder.textView_upzilla.setText(district.getName());

    }

    @Override
    public int getItemCount() {
        return districtsModelArrayList.size();
    }
    public void filterListUp(ArrayList<DistrictModel.District> filtering)
    {
        districtsModelArrayList=filtering;
        notifyDataSetChanged();
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView textView_upzilla;
        public MyVieHolder(@NonNull View itemView, DistrictClick districtClick) {
            super(itemView);
            textView_upzilla=itemView.findViewById(R.id.upzila_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (districtClick !=null){
                        int pos=getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            districtClick.onDistrictItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

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

public class DivisionAdapter extends RecyclerView.Adapter<DivisionAdapter.MyVieHolder>{
    Context context;
    private ArrayList<DivisionModel.Division> divisionModelArrayList;
    private DivisionClick divisionClick;
    public void setOnItemClckListener(DivisionClick itemClick){
        divisionClick =itemClick;
    }

    public DivisionAdapter(Context context, ArrayList<DivisionModel.Division> upzilaModelArrayList) {
        this.context = context;
        this.divisionModelArrayList = upzilaModelArrayList;
    }

    @NonNull
    @Override
    public MyVieHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.upzila_name,parent,false);
        return new MyVieHolder(view, divisionClick);
    }

    @Override
    public void onBindViewHolder(@NonNull MyVieHolder holder, int position) {
        DivisionModel.Division division= divisionModelArrayList.get(position);
        holder.textView_upzilla.setText(division.getName());

    }

    @Override
    public int getItemCount() {
        return divisionModelArrayList.size();
    }
    public void filterListUp(ArrayList<DivisionModel.Division> filtering)
    {
        divisionModelArrayList=filtering;
        notifyDataSetChanged();
    }

    public static class MyVieHolder extends RecyclerView.ViewHolder{

        TextView textView_upzilla;
        public MyVieHolder(@NonNull View itemView, DivisionClick divisionClick) {
            super(itemView);
            textView_upzilla=itemView.findViewById(R.id.upzila_item_textview);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (divisionClick !=null){
                        int pos=getAdapterPosition();
                        if (pos!=RecyclerView.NO_POSITION){
                            divisionClick.onDivisionItemClick(pos);
                        }
                    }
                }
            });
        }
    }
}

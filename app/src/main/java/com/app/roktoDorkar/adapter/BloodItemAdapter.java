package com.app.roktoDorkar.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.model.BloodItem;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class BloodItemAdapter extends ArrayAdapter<BloodItem> {

    public BloodItemAdapter(Context context,ArrayList<BloodItem> bloodItems){
      super(context,0,bloodItems);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View itemView=convertView;

        if (itemView==null)
        {
            itemView= LayoutInflater.from(getContext()).inflate(R.layout.gridview_item, parent, false);

        }
        BloodItem bloodItem=getItem(position);
        TextView textView=itemView.findViewById(R.id.bloodTypeItem);
        MaterialCardView materialCardView=itemView.findViewById(R.id.blood_card);

        for (int i=0;i<materialCardView.getChildCount();i++){

            materialCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (materialCardView.getCardBackgroundColor().getDefaultColor()==-1)
                    {
                        materialCardView.setBackgroundColor(Color.parseColor("#DE2C2C"));
                    }
                }
            });
        }

        textView.setText(bloodItem.getBloodName());
        return itemView;
    }
}

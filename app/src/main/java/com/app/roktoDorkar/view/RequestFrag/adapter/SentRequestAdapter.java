package com.app.roktoDorkar.view.RequestFrag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.sMyViewHolder>{
    private Context context;
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private String type;


    public SentRequestAdapter(Context context, ArrayList<ReceviedListModel> receviedListModelArrayList) {
        this.context = context;
        this.receviedListModelArrayList = receviedListModelArrayList;
    }

    @NonNull
    @Override
    public sMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.receivedlist_item,parent,false);

        return new sMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sMyViewHolder holder, int position) {
        ReceviedListModel receviedListModel=receviedListModelArrayList.get(position);
        type= receviedListModel.getRequestStatus();

        if (type.equals("accept"))
        {
            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);
            holder.materialButtonChat.setVisibility(View.VISIBLE);
            holder.materialButtonFriend.setVisibility(View.VISIBLE);

            holder.textViewBloodgroup.setText(receviedListModel.getSenderRequiredBlood());
            holder.textViewDonarName.setText("received by "+receviedListModel.getRequestReceiverName());
            holder.textViewLocation.setText(receviedListModel.getSenderRequestUpazila());


        }else if (type.equals("not_accept")){
            holder.materialButtonp.setVisibility(View.VISIBLE);
            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);
            holder.materialButtonChat.setVisibility(View.GONE);
            holder.materialButtonFriend.setVisibility(View.GONE);
            holder.textViewDonarName.setText("Not accepted");
        }


    }

    @Override
    public int getItemCount() {
        return receviedListModelArrayList.size() ;
    }

    public class sMyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBloodgroup,textViewDonarName,textViewLocation;
        MaterialButton materialButtonAccept,materialButtonDecline,materialButtonChat,materialButtonFriend,materialButtonp;
        MaterialCardView materialCardView_received;
        ImageView imageView;

        public sMyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
            materialButtonAccept=itemView.findViewById(R.id.accept);
            materialButtonDecline=itemView.findViewById(R.id.decline);
            materialButtonChat=itemView.findViewById(R.id.reqToChat);
            materialButtonp=itemView.findViewById(R.id.pending);
            materialCardView_received=itemView.findViewById(R.id.receivedCard);
            materialButtonFriend=itemView.findViewById(R.id.friends);
            imageView=itemView.findViewById(R.id.donor_info);
        }
    }
}

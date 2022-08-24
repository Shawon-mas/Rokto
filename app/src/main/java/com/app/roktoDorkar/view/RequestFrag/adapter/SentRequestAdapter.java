package com.app.roktoDorkar.view.RequestFrag.adapter;

import static com.app.roktoDorkar.utilites.Constants.KEY_REQBLOOD;
import static com.app.roktoDorkar.utilites.Constants.KEY_REQUPTHANA;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.app.roktoDorkar.view.RequestFrag.model.UserLister;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;

public class SentRequestAdapter extends RecyclerView.Adapter<SentRequestAdapter.sMyViewHolder>{
    private Context context;
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private final UserLister userLister;
    private String type;
    private PreferenceManager preferenceManager;



    public SentRequestAdapter(Context context, ArrayList<ReceviedListModel> receviedListModelArrayList,UserLister userLister) {
        this.context = context;
        this.receviedListModelArrayList = receviedListModelArrayList;
        this.userLister=userLister;
    }

    @NonNull
    @Override
    public sMyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.receivedlist_item,parent,false);

        return new sMyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull sMyViewHolder holder, int position) {
        preferenceManager=new PreferenceManager(context.getApplicationContext());
        ReceviedListModel receviedListModel=receviedListModelArrayList.get(position);
        type= receviedListModel.getRequestStatus();

        holder.textViewBloodgroup.setText(preferenceManager.getString(KEY_REQBLOOD));
        holder.textViewLocation.setText(preferenceManager.getString(KEY_REQUPTHANA));
        holder.textViewReqTime.setText(receviedListModel.getSenderRequestForDate());


        if (type.equals("accept"))
        {
            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);

            holder.materialButtonChat.setVisibility(View.VISIBLE);
            holder.materialButtonFriend.setVisibility(View.VISIBLE);
            holder.textViewDonarName.setText("Accepted by "+receviedListModel.getRequestReceiverName());



        }else if (type.equals("not_accept")){
            holder.materialButtonp.setVisibility(View.VISIBLE);

            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);
            holder.materialButtonChat.setVisibility(View.GONE);
            holder.materialButtonFriend.setVisibility(View.GONE);
            holder.textViewDonarName.setText("Not accepted");
        }
        holder.textViewGiftAmount.setVisibility(View.GONE);
        holder.textViewDesType.setVisibility(View.GONE);
        holder.materialButtonChat.setOnClickListener(v -> {
            userLister.onUserClickedListener(receviedListModel);
        });


    }

    @Override
    public int getItemCount() {
        return receviedListModelArrayList.size() ;
    }

    public class sMyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBloodgroup,textViewDonarName,textViewLocation,textViewGiftAmount,textViewDesType,textViewReqTime;
        MaterialButton materialButtonAccept,materialButtonDecline,materialButtonChat,materialButtonFriend,materialButtonp;
        MaterialCardView materialCardView_received;
        ImageView imageView;

        public sMyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
            textViewReqTime=itemView.findViewById(R.id.dateNtime);
            materialButtonAccept=itemView.findViewById(R.id.accept);
            materialButtonDecline=itemView.findViewById(R.id.decline);
            materialButtonChat=itemView.findViewById(R.id.reqToChat);
            textViewGiftAmount=itemView.findViewById(R.id.gift);
            textViewDesType=itemView.findViewById(R.id.description_type);
            materialButtonp=itemView.findViewById(R.id.pending);
            materialCardView_received=itemView.findViewById(R.id.receivedCard);
            materialButtonFriend=itemView.findViewById(R.id.friends);
            imageView=itemView.findViewById(R.id.donor_info);
        }
    }
}

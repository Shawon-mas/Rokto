package com.app.roktoDorkar.view.RequestFrag.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class ReceivedListAdapter extends RecyclerView.Adapter<ReceivedListAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
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

        String type= receviedListModel.getRequestType();
        String receiverUid= receviedListModel.getReceiverUid();


        if (type.equals("accept"))
        {
            holder.materialButtonAccept.setText("Friend");
            holder.materialButtonDecline.setVisibility(View.GONE);

        }
        holder.textViewBloodgroup.setText(receviedListModel.getBloodGroup());
        holder.textViewDonarName.setText(receviedListModel.getUserName());
        holder.textViewLocation.setText(receviedListModel.getLocation());
        holder.materialButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  holder.materialButtonAccept.setEnabled(false);
                db.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .collection("RequestPortal").document("RequestType")
                        .collection("Received_Request").document(receiverUid).update("requestType","accept")
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(context, "Request Accept", Toast.LENGTH_SHORT).show();
                            }
                        });


                  /*
                  db.collection("UserProfile").document(userEmail)
                                            .collection("RequestPortal").document("RequestType")
                                            .collection("Received_Request").document(userUid)
                   */
            }
        });


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
            materialButtonAccept=itemView.findViewById(R.id.accept);
            materialButtonDecline=itemView.findViewById(R.id.decline);

        }
    }
}

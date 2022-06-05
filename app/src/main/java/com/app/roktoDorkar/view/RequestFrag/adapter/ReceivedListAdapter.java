package com.app.roktoDorkar.view.RequestFrag.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.app.roktoDorkar.global.SharedPref.USER_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class ReceivedListAdapter extends RecyclerView.Adapter<ReceivedListAdapter.MyViewHolder> {
    private Context context;
    private String senderUid;
     String senderId;
    private String type;
    private String userEmail;
    private String receiverUid;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference ref = db.collection("BloodRequest").document();

    private String updateStatus="accept";
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

         type= receviedListModel.getRequestStatus();
         userEmail= receviedListModel.getSenderEmail();
         senderUid= receviedListModel.getSenderUid();
         receiverUid= receviedListModel.getRequestReceiverUid();
         senderId= receviedListModel.getDocumentId();


        if ( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(receiverUid) && type.equals("accept") )
        {

            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);
            holder.materialButtonChat.setVisibility(View.VISIBLE);
            holder.materialButtonFriend.setVisibility(View.VISIBLE);
        }else if (type.equals("accept"))
        {
           holder.materialCardView_received.setVisibility(View.GONE);

        }

        holder.textViewBloodgroup.setText(receviedListModel.getSenderRequiredBlood());
        holder.textViewDonarName.setText(receviedListModel.getSenderName());
        holder.textViewLocation.setText(receviedListModel.getSenderRequestUpazila());

        holder.materialButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  holder.materialButtonAccept.setEnabled(false);

                String dI=ref.getId();
                db.collection("BloodRequest").document(senderId).collection("senderUid").
                        whereEqualTo("senderUid",senderUid)
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {

                                SharedPreferences preferences = context.getSharedPreferences("MY_APP", MODE_PRIVATE);
                                String receiverName = preferences.getString(USER_NAME, null);

                                Map<String, Object> updatesMain = new HashMap<>();
                                updatesMain.put("requestStatus",updateStatus);
                                updatesMain.put("requestReceiverUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                updatesMain.put("requestReceiverName",receiverName);

                                db.collection("BloodRequest").document(senderId)
                                        .update(updatesMain)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused)
                                            {
                                               /* SharedPreferences preferences = context.getSharedPreferences("MY_APP", MODE_PRIVATE);
                                                String receiverName = preferences.getString(USER_NAME, null);
                                                Map<String, Object> updates = new HashMap<>();
                                                updates.put("requestStatus",updateStatus);
                                                updates.put("requestReceiverUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                updates.put("requestReceiverName",receiverName);*/
                                                Toasty.success(context,"Request Accept",Toasty.LENGTH_SHORT,false).show();
                                                /*db.collection("UserProfile").document(userEmail)
                                                        .collection("BloodRequestPortal").document("Request_Type")
                                                        .collection("Sent_Request").document(senderUid)
                                                                .update(updates).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                            @Override
                                                            public void onSuccess(Void unused)
                                                            {

                                                            }
                                                        });*/
                                            }
                                        }).addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                Toasty.success(context,"Error"+e.toString(),Toasty.LENGTH_SHORT,false).show();

                                                Log.d("2nd error",e.toString());
                                            }
                                        });
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toasty.success(context,"Error"+e.toString(),Toasty.LENGTH_SHORT,false).show();

                                Log.d("1st error",e.toString());
                            }
                        });

            }
        });
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toasty.info(context,senderId,Toasty.LENGTH_SHORT,false).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return receviedListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBloodgroup,textViewDonarName,textViewLocation;
        MaterialButton materialButtonAccept,materialButtonDecline,materialButtonChat,materialButtonFriend;
        MaterialCardView materialCardView_received;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
            materialButtonAccept=itemView.findViewById(R.id.accept);
            materialButtonDecline=itemView.findViewById(R.id.decline);
            materialButtonChat=itemView.findViewById(R.id.reqToChat);
            materialCardView_received=itemView.findViewById(R.id.receivedCard);
            materialButtonFriend=itemView.findViewById(R.id.friends);
            imageView=itemView.findViewById(R.id.donor_info);

        }
    }
}

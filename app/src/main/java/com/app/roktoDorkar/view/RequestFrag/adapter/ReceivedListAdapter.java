package com.app.roktoDorkar.view.RequestFrag.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.app.roktoDorkar.global.SharedPref.USER_NAME;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_IMAGE_URI;
import static com.app.roktoDorkar.utilites.Constants.KEY_NAME;

import android.content.Context;
import android.content.Intent;
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
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.ChatActivity;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.app.roktoDorkar.view.RequestFrag.model.UserLister;
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

     String updateStatus="accept";
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private final UserLister userLister;
    public ReceivedListAdapter(Context context, ArrayList<ReceviedListModel> receviedListModelArrayList,UserLister userLister) {
        this.context = context;
        this.receviedListModelArrayList = receviedListModelArrayList;
        this.userLister=userLister;
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


        if ( FirebaseAuth.getInstance().getCurrentUser().getUid().equals(receviedListModel.getRequestReceiverUid()) && type.equals("accept") )
        {

            holder.materialButtonDecline.setVisibility(View.GONE);
            holder.materialButtonAccept.setVisibility(View.GONE);

            holder.materialButtonChat.setVisibility(View.VISIBLE);
            holder.materialButtonFriend.setVisibility(View.VISIBLE);

        }
        else if (FirebaseAuth.getInstance().getCurrentUser().getUid().equals(receviedListModel.getSenderUid())){
            holder.materialCardView_received.setVisibility(View.GONE);

        }
        else if (type.equals("accept"))
        {
           holder.materialCardView_received.setVisibility(View.GONE);

        }

        holder.textViewBloodgroup.setText(receviedListModel.getSenderRequiredBlood());
        holder.textViewDonarName.setText(receviedListModel.getSenderName());
        holder.textViewLocation.setText(receviedListModel.getSenderRequestUpazila());
        holder.textViewDateNTime.setText(receviedListModel.getSenderRequestForDate());
        holder.textViewGiftAmount.setText("Gift Amount: "+receviedListModel.getSenderGiftAmount()+"tk");
        holder.textViewDesType.setText(receviedListModel.getSenderRequestDetails());

        holder.materialButtonAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  holder.materialButtonAccept.setEnabled(false);

                db.collection("BloodRequest").document(receviedListModel.getDocumentId()).collection("senderUid").
                        whereEqualTo("senderUid",receviedListModel.getSenderUid())
                        .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task)
                            {

                                PreferenceManager preferenceManager=new PreferenceManager(v.getContext());
                                Map<String, Object> updatesMain = new HashMap<>();

                                updatesMain.put("requestStatus",updateStatus);
                                updatesMain.put("requestReceiverUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                updatesMain.put("requestReceiverName",preferenceManager.getString(KEY_NAME));
                                updatesMain.put("zReceiverImage",preferenceManager.getString(KEY_IMAGE_URI));

                                db.collection("BloodRequest").document(receviedListModel.getDocumentId())
                                        .update(updatesMain)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void unused)
                                            {
                                                holder.materialButtonDecline.setVisibility(View.GONE);
                                                holder.materialButtonAccept.setVisibility(View.GONE);

                                                holder.materialButtonChat.setVisibility(View.VISIBLE);
                                                holder.materialButtonFriend.setVisibility(View.VISIBLE);

                                                Map<String, Object> updatesSent = new HashMap<>();

                                                updatesSent.put("requestStatus",updateStatus);
                                                updatesSent.put("requestReceiverUid",FirebaseAuth.getInstance().getCurrentUser().getUid());
                                                updatesSent.put("requestReceiverName",preferenceManager.getString(KEY_NAME));
                                                updatesSent.put("zReceiverImage",preferenceManager.getString(KEY_IMAGE_URI));


                                                DocumentReference ref2 = db.collection(KEY_COLLECTION_USERS).document(receviedListModel.getSenderEmail()).collection("MyBloodRequest").document(receviedListModel.getDocumentId());
                                                ref2.update(updatesSent).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {

                                                        Toasty.success(context,"Request Accept",Toasty.LENGTH_SHORT,false).show();

                                                    }
                                                });



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
        holder.materialButtonChat.setOnClickListener(v -> {
            userLister.onUserClickedListener(receviedListModel);


        });


    }

    @Override
    public int getItemCount() {
        return receviedListModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textViewBloodgroup,textViewDonarName,textViewLocation,textViewDateNTime,textViewGiftAmount,textViewDesType;
        MaterialButton materialButtonAccept,materialButtonDecline,materialButtonChat,materialButtonFriend;
        MaterialCardView materialCardView_received;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
            textViewDateNTime=itemView.findViewById(R.id.dateNtime);
            textViewGiftAmount=itemView.findViewById(R.id.gift);
            textViewDesType=itemView.findViewById(R.id.description_type);
            materialButtonAccept=itemView.findViewById(R.id.accept);
            materialButtonDecline=itemView.findViewById(R.id.decline);
            materialButtonChat=itemView.findViewById(R.id.reqToChat);
            materialCardView_received=itemView.findViewById(R.id.receivedCard);
            materialButtonFriend=itemView.findViewById(R.id.friends);


        }
    }
}

package com.app.roktoDorkar.adapter;

import static android.content.Context.MODE_PRIVATE;
import static com.app.roktoDorkar.global.SharedPref.USER_NAME;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.model.DonarListItem;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import es.dmoral.toasty.Toasty;

public class DonarListAdapter extends RecyclerView.Adapter<DonarListAdapter.MyViewHolder> {
    Context context;
    private ArrayList<DonarListItem> donarListItems;
    private ArrayList<ReceviedListModel> receviedListModels;
    public static String bloogGroup;
    public static String userName;
    public static String location;
    public static String userEmail;
    public static String userUid;
    public static String requestTpe="not_accept";
    public static String currentUserId=FirebaseAuth.getInstance().getCurrentUser().getUid();


    public static FirebaseFirestore db = FirebaseFirestore.getInstance();

    public DonarListAdapter(Context context, ArrayList<DonarListItem> donarListItems) {
        this.context = context;
        this.donarListItems = donarListItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.donar_list_item, parent, false);

        return new MyViewHolder(view);

        /*
        View view= LayoutInflater.from(context).inflate(R.layout.consumer_info_list,parent,false);
        return new MyViewHolder(view,clickItem);
         */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {


        DonarListItem item = donarListItems.get(position);



        holder.textViewDonarName.setText(item.getName());
        holder.textViewBloodgroup.setText(item.getBloodType());
        holder.imageView.setImageBitmap(getUserImage(item.getImageUri()));

       /* holder.materialButtonReq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                holder.materialButtonReq.setEnabled(false);

                    Map<String, Object> requestInfo = new HashMap<>();
                    requestInfo.put("bloodGroup", bloogGroup);
                    requestInfo.put("userName", userName);
                    requestInfo.put("location", location);
                    requestInfo.put("requestType", requestTpe);
                    requestInfo.put("senderUid", userUid);
                    db.collection("UserProfile").document(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                            .collection("RequestPortal").document("RequestType")
                            .collection("Sent_Request").document(userUid)
                            .set(requestInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused)
                                {
                                    SharedPreferences preferences = context.getSharedPreferences("MY_APP", MODE_PRIVATE);
                                    String senderName = preferences.getString(USER_NAME, null);
                                    Map<String, Object> requestInfo2 = new HashMap<>();
                                    requestInfo2.put("bloodGroup", bloogGroup);
                                    requestInfo2.put("userName", senderName);
                                    requestInfo2.put("location", location);
                                    requestInfo2.put("requestType", requestTpe);
                                    requestInfo2.put("receiverUid", FirebaseAuth.getInstance().getUid());
                                    db.collection("UserProfile").document(userEmail)
                                            .collection("RequestPortal").document("RequestType")
                                            .collection("Received_Request").document(currentUserId)
                                            .set(requestInfo2).addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused)
                                                {


                                                    holder.materialButtonReq.setText("Pending Request");
                                                    holder.materialButtonReq.setCompoundDrawablesWithIntrinsicBounds(R.drawable.pending, 0, 0, 0);

                                                    Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
                                                    

                                                  //  holder.materialButtonReq.setBackgroundColor(R.color.main);
                                               *//*
                                                buttonMyText.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ImageNameHere, 0, 0, 0);
                                                buttonMyText.setTextColor(Color.BLACK);
                                                *//*
                                                }
                                  });
                                }
                            });
                }
        });*/
     /*  holder.materialButtonChat.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Toasty.info(context,"Coming Soon...!",Toasty.LENGTH_SHORT).show();
           }
       });*/



    }
    private Bitmap getUserImage(String encodedImage){
        byte[] bytes= Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    @Override
    public int getItemCount() {
        return donarListItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewBloodgroup, textViewDonarName, textViewLastDonate, textViewLocation;
        MaterialButton materialButtonChat, materialButtonPen;
        RoundedImageView imageView;
        private FirebaseAuth mAuth;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup = itemView.findViewById(R.id.blood_group);
            textViewDonarName = itemView.findViewById(R.id.donar_name);
            imageView=itemView.findViewById(R.id.donarImage);

            

        }
    }
}
/*

                                SharedPreferences.Editor editor = context.getSharedPreferences("Button", MODE_PRIVATE).edit();
                                editor.putString(String.valueOf(position)+ "pressed", "yes");
                                editor.apply();
                                holder.materialButtonPen.setVisibility(View.VISIBLE);
                                holder.materialButtonReq.setVisibility(View.GONE);

                                assert donarListItems!=null;
                               Integer count_key=donarListItems.size();
                               String clickCount=String.valueOf(count_key);

                               Intent my_intent=new Intent("message");
                                my_intent.putExtra("click_count",clickCount);
                                LocalBroadcastManager.getInstance(context).sendBroadcast(my_intent);
                                v.setEnabled(false);
*/
/*

                                db.collection("UserProfile").document(userEmail)
                                        .collection("RequestPortal").document("RequestType")
                                        .collection("Received_Request").document(FirebaseAuth.getInstance().getUid()).set(requestInfo)
 Map<String, Object> requestInfo = new HashMap<>();
                requestInfo.put("bloodGroup", bloogGroup);
                requestInfo.put("userName", userName);
                requestInfo.put("location", location);
                requestInfo.put("requestType", requestType);
                requestInfo.put("userUid", userUid);

                 Map<String, Object> requestInfo2 = new HashMap<>();
                                requestInfo.put("bloodGroup", bloogGroup);
                                requestInfo.put("userName", name);
                                requestInfo.put("location", location);
                                requestInfo.put("requestType", requestType);
                                requestInfo.put("userUid", FirebaseAuth.getInstance().getUid());
                                 SharedPreferences preferences = context.getSharedPreferences("MY_APP", MODE_PRIVATE);
                                String name = preferences.getString(USER_NAME, null);

 */
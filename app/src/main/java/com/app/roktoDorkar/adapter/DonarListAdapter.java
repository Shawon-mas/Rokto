package com.app.roktoDorkar.adapter;

import static com.app.global.SharedPref.USER_NAME;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.model.DonarListItem;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DonarListAdapter extends RecyclerView.Adapter<DonarListAdapter.MyViewHolder> {
    Context context;
    private ArrayList<DonarListItem> donarListItems;
   public static String bloogGroup;
   public static String userName;
   public static String location;
   public static String email;

    public DonarListAdapter(Context context, ArrayList<DonarListItem> donarListItems) {
        this.context = context;
        this.donarListItems = donarListItems;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(context).inflate(R.layout.donar_list_item,parent,false);

        return new MyViewHolder(view);

        /*
        View view= LayoutInflater.from(context).inflate(R.layout.consumer_info_list,parent,false);
        return new MyViewHolder(view,clickItem);
         */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DonarListItem item=donarListItems.get(position);
         bloogGroup=item.getBloodType();
         userName=item.getUserName();
         location=item.getUpzilla();
         email=item.getUserEmail();
        holder.textViewBloodgroup.setText(item.getBloodType());
        holder.textViewDonarName.setText(item.getUserName());
        holder.textViewLastDonate.setText(item.getUserAge());
        holder.textViewLocation.setText(item.getUpzilla());

    }

    @Override
    public int getItemCount() {
        return donarListItems.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
           TextView textViewBloodgroup,textViewDonarName,textViewLastDonate,textViewLocation;
           MaterialButton materialButtonReq,materialButtonPen;
        private FirebaseAuth mAuth;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup=itemView.findViewById(R.id.blood_group);
            textViewDonarName=itemView.findViewById(R.id.donar_name);
            textViewLastDonate=itemView.findViewById(R.id.lastDonateDate);
            textViewLocation=itemView.findViewById(R.id.locationDonar);
            materialButtonReq=itemView.findViewById(R.id.requestBlood);
            materialButtonPen=itemView.findViewById(R.id.pendingrequestBlood);
            materialButtonReq.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v)
                {

                    SharedPreferences preferences = context.getSharedPreferences("MY_APP", Context.MODE_PRIVATE);
                    String name=preferences.getString(USER_NAME,null);
                    boolean requestType=false;

                    Map<String, Object> requestInfo = new HashMap<>();
                    requestInfo.put("bloodGroup",bloogGroup);
                    requestInfo.put("userName",name);
                    requestInfo.put("location",location);
                    requestInfo.put("requestType",requestType);

                    db.collection("UserProfile").document(email)
                            .collection("RequestPortal").document("RequestType")
                            .collection("Received Request").document().set(requestInfo).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused)
                                {

                                     Toast.makeText(context, "Request Sent", Toast.LENGTH_SHORT).show();
                                     materialButtonPen.setVisibility(View.VISIBLE);
                                     materialButtonReq.setSelected(true);
                                     materialButtonReq.setVisibility(View.GONE);
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e)
                                {
                                    Log.d("Error:",e.toString());
                                }
                            });
                    if (!requestType)
                    {
                        materialButtonReq.setVisibility(View.GONE);
                        materialButtonPen.setVisibility(View.VISIBLE);

                    }


                }
            });
        }
    }
}

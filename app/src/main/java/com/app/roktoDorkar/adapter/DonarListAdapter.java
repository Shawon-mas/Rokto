package com.app.roktoDorkar.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.model.DonarListItem;
import com.app.roktoDorkar.model.UserListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;

import java.util.ArrayList;

public class DonarListAdapter extends RecyclerView.Adapter<DonarListAdapter.MyViewHolder> {
    Context context;
    private ArrayList<DonarListItem> donarListItems;
    public static FirebaseFirestore db = FirebaseFirestore.getInstance();
    private final UserListener userListener;

    public DonarListAdapter(Context context, ArrayList<DonarListItem> donarListItems, UserListener userListener) {
        this.context = context;
        this.donarListItems = donarListItems;
        this.userListener = userListener;
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
        holder.appCompatImageView.setOnClickListener(v -> {
            userListener.onUserClickedListeners(item);
        });

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
        AppCompatImageView appCompatImageView;
        private FirebaseAuth mAuth;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewBloodgroup = itemView.findViewById(R.id.blood_group);
            textViewDonarName = itemView.findViewById(R.id.donar_name);
            imageView=itemView.findViewById(R.id.donarImage);
            appCompatImageView=itemView.findViewById(R.id.donarChat);

        }
    }
}

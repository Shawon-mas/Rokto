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
import com.app.roktoDorkar.model.PatnerListener;
import com.app.roktoDorkar.model.PatnerModel;
import com.app.roktoDorkar.model.UserListener;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.makeramen.roundedimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PatnerAdapter extends RecyclerView.Adapter<PatnerAdapter.MyViewHolder> {
    Context context;
    private ArrayList<PatnerModel> patnerModels;

    private final PatnerListener patnerListener;

    public PatnerAdapter(Context context, ArrayList<PatnerModel> patnerModels, PatnerListener patnerListenerN) {
        this.context = context;
        this.patnerModels = patnerModels;
        this.patnerListener = patnerListenerN;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.patner_layout, parent, false);

        return new MyViewHolder(view);

        /*
        View view= LayoutInflater.from(context).inflate(R.layout.consumer_info_list,parent,false);
        return new MyViewHolder(view,clickItem);
         */
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        PatnerModel item = patnerModels.get(position);
        holder.textViewName.setText(item.getName());
        holder.textViewNumber.setText("Number of volunteers:"+item.getQuantity());
        Picasso.get()
                .load(item.getImageUri())
                .resize(50, 50)
                .centerCrop()
                .into(holder.imageView);
        holder.itemView.setOnClickListener(v -> {
            patnerListener.onUserClickedListeners(item);
        });
       /* holder.appCompatImageView.setOnClickListener(v -> {
            userListener.onUserClickedListeners(item);
        });*/

    }
    private Bitmap getUserImage(String encodedImage){
        byte[] bytes= Base64.decode(encodedImage,Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes,0,bytes.length);
    }

    @Override
    public int getItemCount() {
        return patnerModels.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName, textViewNumber, textViewLastDonate, textViewLocation;
        MaterialButton materialButtonChat, materialButtonPen;
        RoundedImageView imageView;
        AppCompatImageView appCompatImageView;
        private FirebaseAuth mAuth;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.sponsarName);
            textViewNumber = itemView.findViewById(R.id.sponsarQuantity);
            imageView=itemView.findViewById(R.id.logoSponsar);


        }
    }
}

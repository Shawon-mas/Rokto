package com.app.roktoDorkar.view;

import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.adapter.DonarListAdapter;

import com.app.roktoDorkar.databinding.ActivityDonarsListBinding;
import com.app.roktoDorkar.model.DonarListItem;
import com.app.roktoDorkar.model.UserListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class DonarsListActivity extends AppCompatActivity implements UserListener {
    private ActivityDonarsListBinding binding;
    private ArrayList<DonarListItem> donarListItems;
    private DonarListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    public static String type;
    public static String location;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDonarsListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recylerView();
    }

    private void recylerView() {

        binding.donarListRecyclerView.setHasFixedSize(true);
        binding.donarListRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        donarListItems = new ArrayList<>();
        adapter = new DonarListAdapter(DonarsListActivity.this, donarListItems, this);
        binding.donarListRecyclerView.setAdapter(adapter);
        type=getIntent().getStringExtra("type");
        location=getIntent().getStringExtra("location");
        db.collection(KEY_COLLECTION_USERS)
                .whereEqualTo(KEY_BLOODTYPE,type)
                .whereEqualTo(KEY_UPZILA,  location)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error!=null){
                    Log.d("Firestore error", error.getMessage());
                    return;
                }
                for (DocumentChange documentChange:value.getDocumentChanges()){
                    if (documentChange.getType() == DocumentChange.Type.ADDED){
                        donarListItems.add(documentChange.getDocument().toObject(DonarListItem.class));
                    }
                    adapter.notifyDataSetChanged();
                }
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }

    @Override
    public void onUserClickedListeners(DonarListItem donarListItem) {


        Intent intent=new Intent(getApplicationContext(),DonarChatActivity.class);
        intent.putExtra("donar_uid",donarListItem.getuId());
        intent.putExtra("donar_name",donarListItem.getName());
        intent.putExtra("donar_image",donarListItem.getImageUri());
        intent.putExtra("donar_token",donarListItem.getFcmToken());
        intent.putExtra("color", R.color.chatPrimary_bg);
        startActivity(intent);

    }
}
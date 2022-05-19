package com.app.roktoDorkar.view;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class DonarsListActivity extends AppCompatActivity {
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
        adapter = new DonarListAdapter(DonarsListActivity.this, donarListItems);
        binding.donarListRecyclerView.setAdapter(adapter);
        type=getIntent().getStringExtra("type");
        location=getIntent().getStringExtra("location");
        db.collection("UserProfile")
                .whereEqualTo("bloodType",type)
                .whereEqualTo("upzilla",  location)
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
    public void onBackPressed() {

        super.onBackPressed();
        Intent intent=new Intent(getApplicationContext(),HomeActivity.class);
        startActivity(intent);
    }
}
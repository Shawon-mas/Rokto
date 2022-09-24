package com.app.roktoDorkar.view.bottomViewActivites;

import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.adapter.DonarListAdapter;
import com.app.roktoDorkar.adapter.PatnerAdapter;
import com.app.roktoDorkar.databinding.ActivityBloodReqBinding;
import com.app.roktoDorkar.databinding.ActivityHistoryBinding;
import com.app.roktoDorkar.model.DonarListItem;
import com.app.roktoDorkar.model.PatnerListener;
import com.app.roktoDorkar.model.PatnerModel;
import com.app.roktoDorkar.view.DonarsListActivity;
import com.app.roktoDorkar.view.HomeActivity;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import org.xml.sax.helpers.ParserAdapter;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class HistoryActivity extends AppCompatActivity implements PatnerListener {
private ActivityHistoryBinding binding;
    private ArrayList<PatnerModel> patnerModels;
    private PatnerAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        bottomNav();
        recyclerImplement();
    }

    private void recyclerImplement() {
        binding.patnerRecycler.setHasFixedSize(true);
        binding.patnerRecycler.setLayoutManager(new LinearLayoutManager(this));
        patnerModels = new ArrayList<>();
        adapter = new PatnerAdapter(HistoryActivity.this, patnerModels, this);
        binding.patnerRecycler.setAdapter(adapter);
        db.collection("patner").addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error!=null){
                            Log.d("Firestore error", error.getMessage());
                            return;
                        }
                        for (DocumentChange documentChange:value.getDocumentChanges()){
                            if (documentChange.getType() == DocumentChange.Type.ADDED){
                                patnerModels.add(documentChange.getDocument().toObject(PatnerModel.class));
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



    private void bottomNav() {
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(1,R.drawable.search_donar));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(2,R.drawable.request));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(3,R.drawable.add));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(4,R.drawable.group));
        binding.bottomNavHistory.add(new MeowBottomNavigation.Model(5,R.drawable.account));
        binding.bottomNavHistory.setOnShowListener(new MeowBottomNavigation.ShowListener() {
            @Override
            public void onShowItem(MeowBottomNavigation.Model item) {
                switch (item.getId())
                {
                    case 1:
                        startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 2:
                        startActivity(new Intent(getApplicationContext(), RequestActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 3:
                        startActivity(new Intent(getApplicationContext(), BloodReqActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                    case 4:

                        overridePendingTransition(0, 0);
                        return;
                    case 5:
                        startActivity(new Intent(getApplicationContext(), AccountActivity.class));
                        overridePendingTransition(0, 0);
                        return;
                }
            }
        });
        binding.bottomNavHistory.show(4,true);
        binding.bottomNavHistory.setOnClickMenuListener(new MeowBottomNavigation.ClickListener() {
            @Override
            public void onClickItem(MeowBottomNavigation.Model item)
            {
            }
        });
        binding.bottomNavHistory.setOnReselectListener(new MeowBottomNavigation.ReselectListener() {
            @Override
            public void onReselectItem(MeowBottomNavigation.Model item) {
                return;
            }
        });
    }
    @Override
    public void onBackPressed() {
        binding.bottomNavHistory.show(1,true);
        super.onBackPressed();
    }

    @Override
    public void onUserClickedListeners(PatnerModel patnerModel) {
        /*Toasty.info(getApplicationContext(),patnerModel.getName(),Toasty.LENGTH_SHORT).show();
        WebView webView = new WebView(getApplicationContext());
        webView.loadUrl(patnerModel.getLink());
        patnerModel.getLink()*/
        Intent intent = new Intent(Intent.ACTION_VIEW).setData(Uri.parse(patnerModel.getLink()));
        startActivity(intent);


    }
}
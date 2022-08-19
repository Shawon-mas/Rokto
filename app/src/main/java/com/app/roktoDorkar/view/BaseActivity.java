package com.app.roktoDorkar.view;

import static com.app.roktoDorkar.utilites.Constants.KEY_AVAILABILITY;
import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app.roktoDorkar.utilites.PreferenceManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class BaseActivity extends AppCompatActivity {
    private DocumentReference documentReference;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PreferenceManager preferenceManager=new PreferenceManager(getApplicationContext());
         FirebaseFirestore db = FirebaseFirestore.getInstance();
         documentReference=db.collection(KEY_COLLECTION_USERS)
                 .document(FirebaseAuth.getInstance().getCurrentUser().getEmail());

    }

    @Override
    protected void onPause() {
        super.onPause();
        documentReference.update(KEY_AVAILABILITY,0);
    }

    @Override
    protected void onResume() {
        super.onResume();
        documentReference.update(KEY_AVAILABILITY,1);
    }
}

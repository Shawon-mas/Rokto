package com.app.roktoDorkar.view.RequestFrag;

import static android.content.Context.MODE_PRIVATE;
import static com.app.roktoDorkar.global.SharedPref.USER_BLOODTYPE;
import static com.app.roktoDorkar.global.SharedPref.USER_NAME;
import static com.app.roktoDorkar.global.SharedPref.USER_UPAZILA;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.view.ChatActivity;
import com.app.roktoDorkar.view.RequestFrag.adapter.ReceivedListAdapter;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.app.roktoDorkar.view.RequestFrag.model.UserLister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;


public class ReceivedFrag extends Fragment implements UserLister {
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private RecyclerView recyclerView_receivedfrag;
    private ReceivedListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView textView_message;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_received, container, false);
     textView_message=root.findViewById(R.id.message);
       /* if (receviedListModelArrayList.size()==0)
        {
            textView_message.setVisibility(View.VISIBLE);
            textView_message.setText("No Received Request");
        }else {
            textView_message.setVisibility(View.GONE);
        }*/
        intiViews(root);
        return root;
    }

    private void intiViews(ViewGroup root) {
        recyclerView_receivedfrag=root.findViewById(R.id.recyclerviewReceivedList);
        recyclerView_receivedfrag.setHasFixedSize(true);
        recyclerView_receivedfrag.setLayoutManager(new LinearLayoutManager(getContext()));
        receviedListModelArrayList=new ArrayList<>();
        adapter=new ReceivedListAdapter(getContext(),receviedListModelArrayList,this);
        recyclerView_receivedfrag.setAdapter(adapter);

        SharedPreferences preferences = getContext().getSharedPreferences("MY_APP", MODE_PRIVATE);
        String bloodType = preferences.getString(USER_BLOODTYPE, null);
        String upazila = preferences.getString(USER_UPAZILA, null);
        db.collection("BloodRequest").whereEqualTo("senderRequiredBlood",bloodType)
                .whereEqualTo("senderRequestUpazila",upazila)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        if (error!=null)
                        {
                            Log.d("Firestore error", error.getMessage());

                            return;
                        }
                        for (DocumentChange documentChange:value.getDocumentChanges())
                        {

                            if (documentChange.getType()== DocumentChange.Type.ADDED)
                            {


                                receviedListModelArrayList.add(documentChange.getDocument().toObject(ReceviedListModel.class));
                            }
                            adapter.notifyDataSetChanged();
                        }
                    }
                });




    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.notifyDataSetChanged();
    }
    @Override
    public void onStop() {
        super.onStop();
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onUserClickedListener(ReceviedListModel receviedListModel) {
        Intent intent=new Intent(getContext(), ChatActivity.class);
        intent.putExtra("user_name",receviedListModel.getSenderName());
        startActivity(intent);
    }
}
/*
db.collection("UserProfile").document(email)
                            .collection("RequestPortal").document("RequestType")
                            .collection("Received Request")
 */
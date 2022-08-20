package com.app.roktoDorkar.view.RequestFrag;


import static com.app.roktoDorkar.utilites.Constants.KEY_BLOODTYPE;
import static com.app.roktoDorkar.utilites.Constants.KEY_UPZILA;

import android.content.Intent;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.app.roktoDorkar.R;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.ChatActivity;
import com.app.roktoDorkar.view.RequestFrag.adapter.ReceivedListAdapter;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.app.roktoDorkar.view.RequestFrag.model.UserLister;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import java.util.ArrayList;

import es.dmoral.toasty.Toasty;


public class ReceivedFrag extends Fragment implements UserLister {
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private RecyclerView recyclerView_receivedfrag;
    private ReceivedListAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private TextView textView_message;
    private ProgressBar progressBar_received;
    private PreferenceManager preferenceManager;
    private LottieAnimationView lottieAnimationView;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_received, container, false);
        preferenceManager=new PreferenceManager(getContext());
        intiViews(root);
        return root;
    }

    private void intiViews(ViewGroup root) {




        progressBar_received=root.findViewById(R.id.receivedProgressbar);
        textView_message=root.findViewById(R.id.dataTextReceived);
        lottieAnimationView=root.findViewById(R.id.animationView);
        recyclerView_receivedfrag=root.findViewById(R.id.recyclerviewReceivedList);
        recyclerView_receivedfrag.setHasFixedSize(true);
        recyclerView_receivedfrag.setLayoutManager(new LinearLayoutManager(getContext()));
        receviedListModelArrayList=new ArrayList<>();


        adapter=new ReceivedListAdapter(getContext(),receviedListModelArrayList,this);
        recyclerView_receivedfrag.setAdapter(adapter);


        db.collection("BloodRequest").whereEqualTo("senderRequiredBlood",preferenceManager.getString(KEY_BLOODTYPE))
                .whereEqualTo("senderRequestUpazila",preferenceManager.getString(KEY_UPZILA))
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error)
                    {
                        if (error!=null)
                        {
                            Log.d("Firestore error", error.getMessage());

                            return;
                        }
                        if (value.isEmpty())
                        {
                            lottieAnimationView.setVisibility(View.VISIBLE);
                            textView_message.setVisibility(View.VISIBLE);
                        }
                        if (value!=null)
                        {
                            int count=receviedListModelArrayList.size();
                            for (DocumentChange documentChange:value.getDocumentChanges())
                            {

                                if (documentChange.getType()== DocumentChange.Type.ADDED)
                                {

                                    receviedListModelArrayList.add(documentChange.getDocument().toObject(ReceviedListModel.class));
                                    lottieAnimationView.setVisibility(View.GONE);
                                    textView_message.setVisibility(View.GONE);

                                }
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
        intent.putExtra("document_id",receviedListModel.getDocumentId());
        intent.putExtra("receiver_id",receviedListModel.getSenderUid());
        intent.putExtra("receiver_image",receviedListModel.getzSenderImage());

        intent.putExtra("color",R.color.chatPrimary_bg);
        startActivity(intent);
    }
}
/*
db.collection("UserProfile").document(email)
                            .collection("RequestPortal").document("RequestType")
                            .collection("Received Request")
 */
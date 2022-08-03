package com.app.roktoDorkar.view.RequestFrag;

import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_USERS;
import static com.app.roktoDorkar.utilites.Constants.KEY_EMAIL;

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

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.ChatActivity;
import com.app.roktoDorkar.view.RequestFrag.adapter.SentRequestAdapter;
import com.app.roktoDorkar.view.RequestFrag.model.ReceviedListModel;
import com.app.roktoDorkar.view.RequestFrag.model.UserLister;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


public class MyRequestFrag extends Fragment implements UserLister {
    private ArrayList<ReceviedListModel> receviedListModelArrayList;
    private RecyclerView recyclerView_receivedfrag;
    private SentRequestAdapter adapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private PreferenceManager preferenceManager;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root= (ViewGroup) inflater.inflate(R.layout.fragment_my_request, container, false);
        preferenceManager=new PreferenceManager(getContext());
        intiViews(root);
        return root;
    }

    private void intiViews(ViewGroup root) {
        recyclerView_receivedfrag=root.findViewById(R.id.recyclerviewReqSentList);
        recyclerView_receivedfrag.setHasFixedSize(true);
        recyclerView_receivedfrag.setLayoutManager(new LinearLayoutManager(getContext()));
        receviedListModelArrayList=new ArrayList<>();
          adapter=new SentRequestAdapter(getContext(),receviedListModelArrayList,this);
          recyclerView_receivedfrag.setAdapter(adapter);
          db.collection(KEY_COLLECTION_USERS).document(preferenceManager.getString(KEY_EMAIL)).collection("MyBloodRequest")
                  .addSnapshotListener(new EventListener<QuerySnapshot>() {
                      @Override
                      public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
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
        intent.putExtra("user_name",receviedListModel.getRequestReceiverName());
        intent.putExtra("document_id",receviedListModel.getDocumentId());
        intent.putExtra("receiver_id",receviedListModel.getRequestReceiverUid());
        intent.putExtra("color",R.color.chatPrimary_bg);
        startActivity(intent);

    }
}
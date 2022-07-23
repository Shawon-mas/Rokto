package com.app.roktoDorkar.view;

import static com.app.roktoDorkar.utilites.Constants.KEY_COLLECTION_CHAT;
import static com.app.roktoDorkar.utilites.Constants.KEY_MESSAGE;
import static com.app.roktoDorkar.utilites.Constants.KEY_RECEIVER_ID;
import static com.app.roktoDorkar.utilites.Constants.KEY_SENDER_ID;
import static com.app.roktoDorkar.utilites.Constants.KEY_TIMESTAMP;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.app.roktoDorkar.R;
import com.app.roktoDorkar.databinding.ActivityChatBinding;
import com.app.roktoDorkar.utilites.PreferenceManager;
import com.app.roktoDorkar.view.RequestFrag.adapter.ChatAdapter;
import com.app.roktoDorkar.view.RequestFrag.model.ChatMessage;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ChatActivity extends AppCompatActivity {
    private ActivityChatBinding binding;
    private  List<ChatMessage> chatMessages;
    private ChatAdapter adapter;
    private FirebaseFirestore database;
    private PreferenceManager preferenceManager;
    private FirebaseAuth mAuth;
    int toolbarColor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityChatBinding.inflate(getLayoutInflater());
        preferenceManager=new PreferenceManager(getApplicationContext());
        setContentView(binding.getRoot());
        mAuth = FirebaseAuth.getInstance();
        setStatusBarGradiant(this);
        loadUserDetails();
        init();
        listenMessage();
        clickListeners();
    }

    private void setStatusBarGradiant(ChatActivity chatActivity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = chatActivity.getWindow();

            Drawable background = chatActivity.getResources().getDrawable(R.drawable.status_bar);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

            window.setStatusBarColor(chatActivity.getResources().getColor(android.R.color.transparent));
            window.setNavigationBarColor(chatActivity.getResources().getColor(android.R.color.transparent));
            window.setBackgroundDrawable(background);
        }
    }

    private void init(){
         FirebaseUser user = mAuth.getCurrentUser();
         chatMessages=new ArrayList<>();
        adapter=new ChatAdapter(chatMessages,user.getUid());
        binding.chatRecyclerView.setAdapter(adapter);
        database=FirebaseFirestore.getInstance();
     }
     private void sendMessage(){
         FirebaseUser user = mAuth.getCurrentUser();
         HashMap<String,Object> message=new HashMap<>();
         message.put(KEY_SENDER_ID,user.getUid());
         message.put(KEY_RECEIVER_ID,getIntent().getStringExtra("receiver_id"));
         message.put(KEY_MESSAGE,binding.inputMessage.getText().toString());
         message.put(KEY_TIMESTAMP,new Date());
         database.collection(KEY_COLLECTION_CHAT).add(message);
         binding.inputMessage.setText(null);

     }
     private void listenMessage()
     {
         FirebaseUser user = mAuth.getCurrentUser();
         database.collection(KEY_COLLECTION_CHAT)
                 .whereEqualTo(KEY_SENDER_ID,user.getUid())
                 .whereEqualTo(KEY_RECEIVER_ID,getIntent().getStringExtra("receiver_id"))
                 .addSnapshotListener(eventListener);

         database.collection(KEY_COLLECTION_CHAT)
                 .whereEqualTo(KEY_SENDER_ID,getIntent().getStringExtra("receiver_id"))
                 .whereEqualTo(KEY_RECEIVER_ID,user.getUid())
                 .addSnapshotListener(eventListener);



     }
     private final EventListener<QuerySnapshot> eventListener=(value, error) -> {
         if (error !=null)
         {
             return;
         }
         if (value!=null)
         {
             int count=chatMessages.size();
             for (DocumentChange documentChange:value.getDocumentChanges())
             {
                 if (documentChange.getType()==DocumentChange.Type.ADDED)
                 {
                     ChatMessage chatMessage=new ChatMessage();
                     chatMessage.senderId=documentChange.getDocument().getString(KEY_SENDER_ID);
                     chatMessage.receiverId=documentChange.getDocument().getString(KEY_RECEIVER_ID);
                     chatMessage.message=documentChange.getDocument().getString(KEY_MESSAGE);
                     chatMessage.dateTime=getReadableDateTime(documentChange.getDocument().getDate(KEY_TIMESTAMP));
                     chatMessage.dateObject=documentChange.getDocument().getDate(KEY_TIMESTAMP);
                     chatMessages.add(chatMessage);
                 }
             }
             Collections.sort(chatMessages,(obj1,obj2) -> obj1.dateObject.compareTo(obj2.dateObject));
             if (count==0)
             {
                 adapter.notifyDataSetChanged();
             }else {
                 adapter.notifyItemRangeInserted(chatMessages.size(),chatMessages.size());
                 binding.chatRecyclerView.smoothScrollToPosition(chatMessages.size()-1);
             }
             binding.chatRecyclerView.setVisibility(View.VISIBLE);
         }
         binding.chatProgressbar.setVisibility(View.GONE);
     };
    private void loadUserDetails() {
        String name=getIntent().getStringExtra("user_name");
        binding.textName.setText(name);
    }
    private void clickListeners()
    {
        binding.imageBack.setOnClickListener(v -> {
            onBackPressed();
        });
        binding.layoutSend.setOnClickListener(v -> {sendMessage();});
    }
    private String getReadableDateTime(Date date){
           return new SimpleDateFormat("MMMM dd, yyyy - hh:mm a", Locale.getDefault()).format(date);
    }
}
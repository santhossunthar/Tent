package com.eYe3.Tent.tab;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.eYe3.Tent.R;
import com.eYe3.Tent.adapters.MessageAdapter;
import com.eYe3.Tent.models.Message;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class MessageFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView messageRecyclerView;
    DatabaseReference  messageRef;
    String currentUserId;
    FirebaseAuth mAuth;
    List<Message> messageList;
    MessageAdapter messageAdapter ;
    private String mParam1;
    private String mParam2;

    public MessageFragment() {

    }

    public static MessageFragment newInstance(String param1, String param2) {
        MessageFragment fragment = new MessageFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState){
        View fragmentView = inflater.inflate(R.layout.fragment_message, container, false);

        messageRecyclerView = fragmentView.findViewById(R.id.message_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        messageRecyclerView .setLayoutManager(linearLayoutManager);
        messageRecyclerView.setHasFixedSize(true);
        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        messageRef = FirebaseDatabase.getInstance().getReference().child("Messages");
        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();

        messageRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                messageList = new ArrayList<>();
                for (DataSnapshot messagesnap: dataSnapshot.getChildren()) {
                    Message message = messagesnap.getValue(Message.class);
                    messageList.add(message) ;
                }

                messageAdapter = new MessageAdapter(getActivity(),messageList);
                messageRecyclerView.setAdapter(messageAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
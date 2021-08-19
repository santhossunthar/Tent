package com.eYe3.Tent.tab;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.eYe3.Tent.PostActivity;
import com.eYe3.Tent.R;
import com.eYe3.Tent.adapters.PostAdapter;
import com.eYe3.Tent.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class TentGroupFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    RecyclerView postRecyclerView;
    DatabaseReference userRef, postRef;
    String currentUserId;
    FirebaseAuth mAuth;
    List<Post> postList;
    PostAdapter postAdapter ;
    ImageView userPic;
    TextView askQuestion;
    private String mParam1;
    private String mParam2;


    public TentGroupFragment() {
    }

    public static TentGroupFragment newInstance(String param1, String param2) {
        TentGroupFragment fragment = new TentGroupFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
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
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View fragmentView = inflater.inflate(R.layout.fragment_tg, container, false);
        postRecyclerView = fragmentView.findViewById(R.id.post_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setNestedScrollingEnabled(false);

        userPic=fragmentView.findViewById(R.id.user_pic);
        askQuestion=fragmentView.findViewById(R.id.ask_question);

        askQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent postcreate=new Intent(getContext(), PostActivity.class);
                startActivity(postcreate);
            }
        });

        mAuth=FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String userImage=dataSnapshot.child("Profile Image").getValue().toString();
                    final Context context = getContext();
                    if (isValidContextForGlide(context)){
                        Glide.with(context).load(userImage).placeholder(R.drawable.profile_img_icon).into(userPic);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");

        return fragmentView;
    }

    @Override
    public void onStart() {
        super.onStart();
        getUsersPost();
    }

    private void getUsersPost() {
        postRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {

                    Post post = postsnap.getValue(Post.class);
                    postList.add(post) ;
                }

                postAdapter = new PostAdapter(getActivity(),postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

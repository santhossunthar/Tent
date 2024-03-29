package com.eYe3.Tent.tab;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.eYe3.Tent.R;
import com.eYe3.Tent.adapters.PostAdapter;
import com.eYe3.Tent.models.Post;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyTentFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;
    RecyclerView postRecyclerView;
    DatabaseReference userRef, postRef;
    String currentUserId;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    List<Post> postList;
    PostAdapter postAdapter;
    CircleImageView userImage;
    TextView userName, userEmail, userStatus;

    public MyTentFragment() {

    }

    public static TentGroupFragment newInstance(String param1, String param2) {
        TentGroupFragment fragment = new TentGroupFragment();
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
                             Bundle savedInstanceState) {
        View fragmentView = inflater.inflate(R.layout.fragment_mt, container, false);
        postRecyclerView = fragmentView.findViewById(R.id.my_posts_rv);
        userEmail = fragmentView.findViewById(R.id.user_profile_email);
        userImage = fragmentView.findViewById(R.id.user_profile_image);
        userName = fragmentView.findViewById(R.id.user_profile_name);
        userStatus = fragmentView.findViewById(R.id.user_profile_status);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setHasFixedSize(true);
        postRecyclerView.setNestedScrollingEnabled(false);

        mAuth = FirebaseAuth.getInstance();
        currentUserId = mAuth.getCurrentUser().getUid();
        currentUser = mAuth.getCurrentUser();
        postRef = FirebaseDatabase.getInstance().getReference().child("Posts");
        userRef = FirebaseDatabase.getInstance().getReference().child("Users");

        return fragmentView;
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
    public void onStart() {
        super.onStart();

        if (currentUser != null) {
            String email = currentUser.getEmail();
            userEmail.setText(email);
        }

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String username = dataSnapshot.child("User Name").getValue().toString();
                    String img = dataSnapshot.child("Profile Image").getValue().toString();
                    String status = dataSnapshot.child("User Status").getValue().toString();

                    userName.setText(username);
                    userStatus.setText(status);

                    final Context context = getContext();
                    if (isValidContextForGlide(context)) {
                        Glide.with(context).load(img).placeholder(R.drawable.profile_img_icon).into(userImage);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query myPostQuery = postRef.orderByChild("userId")
                .startAt(currentUserId).endAt(currentUserId);

        myPostQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap : dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post);
                }

                postAdapter = new PostAdapter(getActivity(), postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
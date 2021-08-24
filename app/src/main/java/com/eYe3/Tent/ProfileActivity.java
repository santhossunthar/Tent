package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.bumptech.glide.Glide;
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

public class ProfileActivity extends AppCompatActivity {
    ImageView profileImage;
    TextView profileUserName, profileUserStatus;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference postRef;
    String currentUserId, postKey, uid, userStatus, comUserId;
    Button logOutBtn;
    Toolbar mToolbar;
    RecyclerView postRecyclerView;
    List<Post> postList;
    PostAdapter postAdapter ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar=findViewById(R.id.user_profile_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");

        profileImage=findViewById(R.id.profile_user_image);
        profileUserName=findViewById(R.id.profile_user_name_view);
        profileUserStatus=findViewById(R.id.profile_user_status_view);
        mAuth=FirebaseAuth.getInstance();

        postKey=getIntent().getExtras().getString("postKey");
        uid=getIntent().getExtras().getString("userId");
        userStatus=getIntent().getExtras().getString("userStatus");

        currentUserId=mAuth.getCurrentUser().getUid();
        currentUser=mAuth.getCurrentUser();
        postRef= FirebaseDatabase.getInstance().getReference().child("Posts");

        postRecyclerView = findViewById(R.id.profile_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setHasFixedSize(true);
    }

    @Override
    public void onStart() {
        super.onStart();

        postRef.child(postKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    String userimage = dataSnapshot.child("userphoto").getValue().toString();
                    String username = dataSnapshot.child("username").getValue().toString();

                    Glide.with(ProfileActivity.this).load(userimage).into(profileImage);
                    profileUserName.setText(username);
                    profileUserStatus.setText(userStatus);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        Query myPostQuery=postRef.orderByChild("userId")
                .startAt(uid).endAt(uid);

        myPostQuery.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postList = new ArrayList<>();
                for (DataSnapshot postsnap: dataSnapshot.getChildren()) {
                    Post post = postsnap.getValue(Post.class);
                    postList.add(post) ;
                }

                postAdapter = new PostAdapter(getApplicationContext(),postList);
                postRecyclerView.setAdapter(postAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }
}

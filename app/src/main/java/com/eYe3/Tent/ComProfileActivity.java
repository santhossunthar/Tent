package com.eYe3.Tent;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
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

public class ComProfileActivity  extends AppCompatActivity {
    ImageView profileImage;
    TextView profileUserName, profileUserStatus;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference comRef, postRef;
    String currentUserId, postKey, comKey, uid, comUserId,userName,userImage, userStatus;
    Button logOutBtn;
    Toolbar mToolbar;
    RecyclerView postRecyclerView;
    List<Post> postList;
    PostAdapter postAdapter ;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
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

        postRecyclerView = findViewById(R.id.profile_Rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setReverseLayout(true);
        linearLayoutManager.setStackFromEnd(true);
        postRecyclerView.setLayoutManager(linearLayoutManager);
        postRecyclerView.setHasFixedSize(true);

        comKey=getIntent().getExtras().getString("comKey");
        uid=getIntent().getExtras().getString("uid");
        userName=getIntent().getExtras().getString("userName");
        userImage=getIntent().getExtras().getString("userPhoto");
        userStatus=getIntent().getExtras().getString("userStatus");

        profileUserName.setText(userName);
        Glide.with(this).load(userImage).into(profileImage);
        profileUserStatus.setText(userStatus);

        postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
    }

    @Override
    public void onStart() {
        super.onStart();

        Query postQuery=postRef.orderByChild("userId")
                .startAt(uid).endAt(uid);

        postQuery.addValueEventListener(new ValueEventListener() {
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
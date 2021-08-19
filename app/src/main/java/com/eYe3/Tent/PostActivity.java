package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class PostActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    String currentUserId, postDate, postTime, postKey;
    Button postAddBtn;
    ImageView postUserImage;
    TextView  postDescription, userName;
    ProgressBar postProBar;
    DatabaseReference postRef, userRef;

    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        mToolbar=findViewById(R.id.post_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Ask Your Question");

        postUserImage = findViewById(R.id.popup_user_img);
        postDescription = findViewById(R.id.popup_post_desc);
        userName=findViewById(R.id.popup_user_name);

        postAddBtn = findViewById(R.id.popup_post_btn);
        postProBar = findViewById(R.id.popup_probar);

        mAuth = FirebaseAuth.getInstance();
        currentUserId=mAuth.getCurrentUser().getUid();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("Posts").push();

        postKey=postRef.getKey();

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String profileImage=dataSnapshot.child("Profile Image").getValue().toString();
                    String userProfileName=dataSnapshot.child("User Name").getValue().toString();

                    userName.setText(userProfileName);
                    Glide.with(PostActivity.this).load(profileImage).placeholder(R.drawable.profile_img_icon).into(postUserImage);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        final Calendar date=Calendar.getInstance();
        SimpleDateFormat currentDate=new SimpleDateFormat("dd MMM yyyy");
        postDate=currentDate.format(date.getTime());

        final Calendar time=Calendar.getInstance();
        SimpleDateFormat currentTime=new SimpleDateFormat("HH:mm");
        postTime=currentTime.format(date.getTime());

        postAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String desc = postDescription.getText().toString();
                postAddBtn.setVisibility(View.INVISIBLE);

                if (desc.isEmpty()) {
                    showMessage("Post Description is Empty!");
                    postAddBtn.setVisibility(View.VISIBLE);

                } else {
                    userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists())
                            {
                                String profileImage=dataSnapshot.child("Profile Image").getValue().toString();
                                String userName=dataSnapshot.child("User Name").getValue().toString();
                                String userStatus=dataSnapshot.child("User Status").getValue().toString();

                                HashMap postMap=new HashMap();
                                postMap.put("username",userName);
                                postMap.put("userphoto",profileImage);
                                postMap.put("userId",currentUserId);
                                postMap.put("postKey",postKey);
                                postMap.put("description",desc);
                                postMap.put("userstatus",userStatus);
                                postMap.put("timestamp", ServerValue.TIMESTAMP);

                                postRef.updateChildren(postMap).addOnCompleteListener(new OnCompleteListener() {
                                    @Override
                                    public void onComplete(@NonNull Task task) {
                                        if (task.isSuccessful())
                                        {
                                            showMessage("Post Added Successfully");
                                            goToMainActivity();
                                        }else
                                        {
                                            showMessage("Can not Add Post Try Again!");
                                            postAddBtn.setVisibility(View.VISIBLE);
                                        }
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });
                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(PostActivity.this,message,Toast.LENGTH_LONG).show();
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

    private void goToMainActivity() {
        Intent mainactivity=new Intent(PostActivity.this,MainActivity.class);
        startActivity(mainactivity);
        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}

package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.eYe3.Tent.adapters.CommentAdapter;
import com.eYe3.Tent.models.Comment;
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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ClickPostActivity extends AppCompatActivity {
    ImageView imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTimeName,userName;
    EditText comEditText;
    ImageView comSend;
    String postKey, uid, postDate, postTime,comKey, userStatus, comUserId;
    String currentUserId;
    private Toolbar mToolbar;
    FirebaseAuth mAuth;
    DatabaseReference userRef,postRef,comRef;
    RecyclerView RvComment;
    CommentAdapter commentAdapter;
    List<Comment> listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_post);

        mToolbar=findViewById(R.id.click_post_toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Tent");

        imgUserPost = findViewById(R.id.post_user_img);
        imgCurrentUser = findViewById(R.id.com_current_img);
        userName=findViewById(R.id.post_user_name);
        txtPostDesc = findViewById(R.id.post_desc);
        txtPostDateName = findViewById(R.id.post_date);
        txtPostTimeName=findViewById(R.id.post_time);

        comEditText=findViewById(R.id.comment);
        comSend=findViewById(R.id.com_send);

        RvComment=findViewById(R.id.com_Rv);

        postKey=getIntent().getExtras().getString("postKey");
        uid=getIntent().getExtras().getString("userId");
        userStatus=getIntent().getExtras().getString("userStatus");

        mAuth=FirebaseAuth.getInstance();
        userRef=FirebaseDatabase.getInstance().getReference().child("Users");
        postRef=FirebaseDatabase.getInstance().getReference().child("Posts");
        comRef=FirebaseDatabase.getInstance().getReference().child("Comments").child(postKey).push();

        comKey=comRef.getKey();

        currentUserId=mAuth.getCurrentUser().getUid();

        imgUserPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Setup profile activity intent
            }
        });

        userName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Setup profile activity intent
            }
        });

        String userImage = getIntent().getExtras().getString("userPhoto") ;
        Glide.with(ClickPostActivity.this).load(userImage).into(imgUserPost);

        String userProfileName = getIntent().getExtras().getString("userName") ;
        userName.setText(userProfileName);

        String postdesc = getIntent().getExtras().getString("description") ;
        txtPostDesc.setText(postdesc);

        String posttime = timestampToString(getIntent().getExtras().getLong("postTime"));
        txtPostTimeName.setText(posttime);

        long postdate = getIntent().getExtras().getLong("postTime");
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy");
        String dateString = formatter.format(new Date(Long.parseLong(String.valueOf(postdate))));
        txtPostDateName.setText(dateString);

        comKey=comRef.getKey();

        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String profileimage=dataSnapshot.child("Profile Image").getValue().toString();
                    Glide.with(ClickPostActivity.this).load(profileimage).into(imgCurrentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        comSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String comDesc=comEditText.getText().toString();

                comSend.setVisibility(View.INVISIBLE);

                userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.exists())
                        {
                            String profileimage=dataSnapshot.child("Profile Image").getValue().toString();
                            String username=dataSnapshot.child("User Name").getValue().toString();

                            HashMap comMap=new HashMap();
                            comMap.put("content",comDesc);
                            comMap.put("uid",currentUserId);
                            comMap.put("userphoto",profileimage);
                            comMap.put("username",username);
                            comMap.put("comKey",comKey);
                            comMap.put("postKey",postKey);
                            comMap.put("userStatus",userStatus);
                            comMap.put("timestamp", ServerValue.TIMESTAMP);
                            comRef.updateChildren(comMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful())
                                    {
                                        showMessage("Comment Added Successfully");
                                    }else
                                    {
                                        showMessage("error try again");
                                        comSend.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage("error");
                        comSend.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        iniRvComment();
    }

    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }

    private void iniRvComment() {
        RvComment.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference comValueRef=FirebaseDatabase.getInstance().getReference().child("Comments").child(postKey);
        comValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {
                    Comment comment = snap.getValue(Comment.class);
                    listComment.add(comment) ;
                }

                View.OnClickListener imageListener=new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                };

                commentAdapter = new CommentAdapter(getApplicationContext(),listComment);
                RvComment.setAdapter(commentAdapter);
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

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }
}



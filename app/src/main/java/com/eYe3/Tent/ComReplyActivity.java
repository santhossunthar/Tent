package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.bumptech.glide.Glide;
import com.eYe3.Tent.adapters.ComReplyAdapter;
import com.eYe3.Tent.models.ComReply;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ComReplyActivity extends AppCompatActivity {

    ImageView imgComUser, imgCurrentUser, comReplyBtn;
    TextView txtComDesc, txtComTime,comUserName;
    EditText comReply;
    String currentUserId,content, comKey, postKey;

    FirebaseAuth mAuth;
    DatabaseReference userRef,comRef;
    RecyclerView comRv;
    ComReplyAdapter commentAdapter;
    List<ComReply> listComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_reply);

        imgCurrentUser=findViewById(R.id.com_reply_user_img);
        comReplyBtn=findViewById(R.id.com_reply_send);
        comReply=findViewById(R.id.comment_reply);
        comRv=findViewById(R.id.com_reply_rv);

        postKey=getIntent().getExtras().getString("postKey");
        comKey=getIntent().getExtras().getString("comKey");

        mAuth= FirebaseAuth.getInstance();

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        comRef=FirebaseDatabase.getInstance().getReference().child("CommentReply").child(postKey).child(comKey).push();

        currentUserId=mAuth.getCurrentUser().getUid();
        userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists())
                {
                    String profileimage=dataSnapshot.child("Profile Image").getValue().toString();
                    Glide.with(ComReplyActivity.this).load(profileimage).into(imgCurrentUser);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        comReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String comDesc=comReply.getText().toString();

                comReplyBtn.setVisibility(View.INVISIBLE);

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
                                        comReplyBtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        showMessage("error");
                        comReplyBtn.setVisibility(View.VISIBLE);
                    }
                });
            }
        });

        iniRvComment();
    }

    private void iniRvComment() {
        comRv.setLayoutManager(new LinearLayoutManager(this));

        DatabaseReference comValueRef=FirebaseDatabase.getInstance().getReference().child("CommentReply").child(postKey).child(comKey);
        comValueRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                listComment = new ArrayList<>();
                for (DataSnapshot snap:dataSnapshot.getChildren()) {
                    ComReply comment = snap.getValue(ComReply.class);
                    listComment.add(comment) ;
                }

                commentAdapter = new ComReplyAdapter(getApplicationContext(),listComment);
                comRv.setAdapter(commentAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(this,message,Toast.LENGTH_LONG).show();
    }

    private String timestampToString(long time){
        Calendar calendar = Calendar.getInstance(Locale.ENGLISH);
        calendar.setTimeInMillis(time);
        String date = DateFormat.format("hh:mm",calendar).toString();
        return date;
    }
}

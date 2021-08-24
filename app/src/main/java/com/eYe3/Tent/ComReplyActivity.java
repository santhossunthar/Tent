package com.eYe3.Tent;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class ComReplyActivity extends AppCompatActivity {
    ImageView imgCurrentUser, comReplyBtn;
    EditText comReply;
    String comKey, postKey;
    RecyclerView comRv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_com_reply);

        imgCurrentUser = findViewById(R.id.com_reply_user_img);
        comReplyBtn = findViewById(R.id.com_reply_send);
        comReply = findViewById(R.id.comment_reply);
        comRv = findViewById(R.id.com_reply_rv);

        postKey = getIntent().getExtras().getString("postKey");
        comKey = getIntent().getExtras().getString("comKey");

        comReplyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // todo: Setup comment click event
            }
        });
    }
}
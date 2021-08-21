package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

public class ClickPostActivity extends AppCompatActivity {
    ImageView imgUserPost, imgCurrentUser;
    TextView txtPostDesc, txtPostDateName, txtPostTimeName,userName;
    EditText comEditText;
    ImageView comSend;
    String postKey, uid, userStatus;
    RecyclerView RvComment;
    private Toolbar mToolbar;

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



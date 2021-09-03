package com.eYe3.Tent;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.StorageReference;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String spinnerItem;
    EditText userFullName, userName;
    Button regBtn;
    FirebaseAuth mAuthentication;
    String username, userfullname;
    String email,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userFullName = findViewById(R.id.user_full_name);
        userName = findViewById(R.id.user_name);
        regBtn = findViewById(R.id.reg_btn);

        email=getIntent().getExtras().getString("email");
        password=getIntent().getExtras().getString("password");

        mAuthentication= FirebaseAuth.getInstance();

        final ProgressDialog progressDialog=new ProgressDialog(UserActivity.this);

        regBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = userName.getText().toString();
                userfullname = userFullName.getText().toString();

                if (username.isEmpty() && userfullname.isEmpty() && spinnerItem.isEmpty()) {
                    showMessage("All Fields are empty!");
                    regBtn.setVisibility(View.VISIBLE);
                }
                if (username.isEmpty()) {
                    showMessage("Email is empty");
                    regBtn.setVisibility(View.VISIBLE);
                }
                if (userfullname.isEmpty()) {
                    showMessage("Password is empty");
                    regBtn.setVisibility(View.VISIBLE);
                } else {

                    progressDialog.startProgressDialog();
                    // todo: Add create user account method

                }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        spinnerItem=parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}

package com.eYe3.Tent;

import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {
    private EditText userEmail, userPass;
    private Button reqBtn, logBtn;
    private String email,password;
    FirebaseAuth mAuth;
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        userEmail = findViewById(R.id.get_user_email);
        userPass = findViewById(R.id.get_user_passw);
        reqBtn = findViewById(R.id.reg_btn);
        logBtn = findViewById(R.id.log_Btn);

        logBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent loginactivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginactivity);
                finish();
            }
        });

        mAuth = FirebaseAuth.getInstance();
        user=mAuth.getCurrentUser();

        final ProgressDialog progressDialog=new ProgressDialog(RegisterActivity.this);

        reqBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                reqBtn.setVisibility(View.INVISIBLE);

                email = userEmail.getText().toString().trim();
                password = userPass.getText().toString();

                if (email.isEmpty() && password.isEmpty())
                {
                    showMessage("All Fields are empty!");
                    reqBtn.setVisibility(View.VISIBLE);
                }
                if (email.isEmpty())
                {
                    showMessage("Email is empty");
                    reqBtn.setVisibility(View.VISIBLE);
                }
                if (password.isEmpty())
                {
                    showMessage("Password is empty");
                    reqBtn.setVisibility(View.VISIBLE);
                }
                else
                    {
                        progressDialog.startProgressDialog();
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if (email.matches(emailPattern))
                        {
                            if (password.length()>6){
                                updateUI();

                            }else
                            {
                                showMessage("Password should be 6 characters long!");
                            }
                        }
                        else
                        {
                            Toast.makeText(getApplicationContext(),"Invalid email address", Toast.LENGTH_SHORT).show();
                            progressDialog.dismissDialog();
                        }
                    }
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
    }

    private void updateUI() {
            Intent userActivity = new Intent(getApplicationContext(), UserActivity.class);
            userActivity.putExtra("email",email);
            userActivity.putExtra("password",password);
            startActivity(userActivity);
            finish();
    }
}


package com.eYe3.Tent;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {
    private EditText userMail, userPassword;
    private Button logBtn;
    private FirebaseAuth mAuth;
    private TextView createAccount;
    private TextView forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userMail = findViewById(R.id.user_mail);
        userPassword = findViewById(R.id.user_password);
        logBtn = findViewById(R.id.log_btn);
        mAuth = FirebaseAuth.getInstance();
        forgotPass=findViewById(R.id.forgot_password);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent forgotPass=new Intent(getApplicationContext(),PasswordActivity.class);
                startActivity(forgotPass);
                finish();
            }
        });

        final ProgressDialog progressDialog=new ProgressDialog(LoginActivity.this);

        logBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Setup login button on click event
            }
        });

        createAccount=findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // todo: Setup register activity intent
            }
        });
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}






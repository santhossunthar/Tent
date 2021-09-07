package com.eYe3.Tent;

import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText userMail, userPassword;
    private Button logBtn;
    private FirebaseAuth mAuth;
    private TextView createAccount;
    private TextView forgotPass;
    private Intent mainActivity;

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
                progressDialog.startProgressDialog();

                final String mail=userMail.getText().toString();
                final String password=userPassword.getText().toString();

                if (mail.isEmpty() || password.isEmpty()){
                    showMessage("Verify all fields");
                    progressDialog.dismissDialog();
                }else{
                    signIn(mail,password);
                    progressDialog.dismissDialog();
                }
            }
        });

        mainActivity=new Intent(this,MainActivity.class);

        createAccount=findViewById(R.id.create_account);
        createAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent registeractivity = new Intent(getApplicationContext(),RegisterActivity.class);
                startActivity(registeractivity);
                finish();
            }
        });
    }

    private void signIn(String mail, String password) {

        mAuth.signInWithEmailAndPassword(mail,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    updateUI();
                }
                else {
                    showMessage(task.getException().getMessage());
                }
            }
        });
    }

    private void updateUI() {
        startActivity(mainActivity);
        finish();
    }

    private void showMessage(String text) {
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = mAuth.getCurrentUser();

        if (user!=null) {
            user.reload();
            boolean eMailVf = user.isEmailVerified();
            if (eMailVf == true)
            {
                updateUI();
            }else
            {
                // todo: Setup mail activity intent
            }
        }
    }
}






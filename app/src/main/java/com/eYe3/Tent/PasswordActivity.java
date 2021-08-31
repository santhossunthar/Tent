package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class PasswordActivity extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    private TextView backToLogin;
    private EditText forgotMail;
    private Button forgotBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        backToLogin=findViewById(R.id.back_login);
        forgotMail=findViewById(R.id.forgot_email);
        forgotBtn=findViewById(R.id.forgot_btn);
        firebaseAuth=FirebaseAuth.getInstance();

        backToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent loginActivity = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginActivity);
                finish();
            }
        });

        forgotBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String userMail=forgotMail.getText().toString().trim();

                if (userMail.isEmpty()){
                    Toast.makeText(PasswordActivity.this,"Field is Empty!",Toast.LENGTH_SHORT).show();
                }else
                    {
                    firebaseAuth.sendPasswordResetEmail(userMail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()){
                                Toast.makeText(PasswordActivity.this,"Password Reset Link sent",Toast.LENGTH_SHORT).show();
                                finish();
                                startActivity(new Intent(PasswordActivity.this,LoginActivity.class));
                            }else
                            {
                                Toast.makeText(PasswordActivity.this,"Error Sending Email",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}

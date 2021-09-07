package com.eYe3.Tent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MailActivity extends AppCompatActivity {
    TextView mailVfText;
    Button vfBtn, mailvfbtn;
    FirebaseUser user;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mail);

        mailVfText=findViewById(R.id.mail_vf_text);
        vfBtn=findViewById(R.id.mail_vf_resend_btn);
        mailvfbtn=findViewById(R.id.mail_vf_btn);

        user=FirebaseAuth.getInstance().getCurrentUser();

        mailvfbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user!=null) {
                user.reload();
                boolean eMailVf=user.isEmailVerified();
                if (eMailVf==true)
                {
                    Toast.makeText(MailActivity.this,"Your Email is verified",Toast.LENGTH_SHORT).show();
                    goToMainActivity();
                }else
                {
                    Toast.makeText(MailActivity.this,"Email is not verified, Please Verify Your Email First",Toast.LENGTH_SHORT).show();
                }
            }
            }
        });

        vfBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(MailActivity.this,"Email Verification Link sent",Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void goToMainActivity() {
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
    }
}

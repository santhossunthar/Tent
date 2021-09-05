package com.eYe3.Tent;

import androidx.annotation.NonNull;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import java.util.HashMap;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    String spinnerItem;
    static int galleryPick = 1;
    CircleImageView img;
    EditText userFullName, userName;
    Button regBtn;
    FirebaseAuth mAuth, mAuthentication;
    DatabaseReference userRef;
    StorageReference profilePicRef;
    String currentUserId, username, userfullname;
    String email, password;
    Uri pickedImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);

        userFullName = findViewById(R.id.user_full_name);
        userName = findViewById(R.id.user_name);
        regBtn = findViewById(R.id.reg_btn);

        email = getIntent().getExtras().getString("email");
        password = getIntent().getExtras().getString("password");

        mAuthentication = FirebaseAuth.getInstance();

        final ProgressDialog progressDialog = new ProgressDialog(UserActivity.this);

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
                    CreateUserAccount(email, password);
                }
            }
        });
    }

    private void CreateUserAccount(String email, String password) {
        mAuthentication.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            mAuth = FirebaseAuth.getInstance();
                            currentUserId = mAuth.getCurrentUser().getUid();
                            userRef = FirebaseDatabase.getInstance().getReference().child("Users").child(currentUserId);
                            HashMap userMap = new HashMap();
                            userMap.put("User Name", username);
                            userMap.put("User Full Name", userfullname);
                            userMap.put("gender", "none");
                            userMap.put("dob", "none");
                            userMap.put("Profile Image", "none");
                            userMap.put("User Status", spinnerItem);
                            userRef.updateChildren(userMap).addOnCompleteListener(new OnCompleteListener() {
                                @Override
                                public void onComplete(@NonNull Task task) {
                                    if (task.isSuccessful()) {
                                        uploadProfilePic();
                                    } else {
                                        showMessage("Error Occured" + task.getException().getMessage());
                                        regBtn.setVisibility(View.VISIBLE);
                                    }
                                }
                            });
                        } else {
                            showMessage("Account creation failed" + task.getException().getMessage());
                        }
                    }
                });
    }

    private void uploadProfilePic() {
        profilePicRef = FirebaseStorage.getInstance().getReference().child("Profile Images");
        final StorageReference imageFilePath = profilePicRef.child(currentUserId + ".jpg");
        imageFilePath.putFile(pickedImg).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(final UploadTask.TaskSnapshot taskSnapshot) {
                taskSnapshot.getStorage().getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        Task<Uri> urlTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!urlTask.isSuccessful()) ;
                        Uri downloadUrl = urlTask.getResult();

                        final String download_Url = String.valueOf(downloadUrl);
                        HashMap h = new HashMap();
                        h.put("Profile Image", download_Url);
                        userRef.updateChildren(h).addOnCompleteListener(new OnCompleteListener() {
                            @Override
                            public void onComplete(@NonNull Task task) {
                                if (task.isSuccessful()) {
                                    showMessage("Account created");
                                    goToMailActivity();
                                } else {
                                    showMessage("error");
                                }
                            }
                        });
                    }
                });
            }
        });
    }

    private void goToMailActivity() {
        // todo: Setup mail activity intent
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
        spinnerItem = parent.getSelectedItem().toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
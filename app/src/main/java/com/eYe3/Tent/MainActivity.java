package com.eYe3.Tent;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import com.bumptech.glide.Glide;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth mAuth;
    FirebaseUser currentUser ;
    DatabaseReference userRef;
    StorageReference mStorage;
    String currentUserId;
    private AppBarConfiguration mAppBarConfiguration;

    public static boolean isValidContextForGlide(final Context context) {
        if (context == null) {
            return false;
        }
        if (context instanceof Activity) {
            final Activity activity = (Activity) context;
            if (activity.isDestroyed() || activity.isFinishing()) {
                return false;
            }
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null) {
            currentUserId = mAuth.getCurrentUser().getUid();
        }else{
            Intent loginactivity=new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginactivity);
            finish();
        }

        currentUser=FirebaseAuth.getInstance().getCurrentUser();

        userRef= FirebaseDatabase.getInstance().getReference().child("Users");
        mStorage= FirebaseStorage.getInstance().getReference().child("Profile Images");

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_tent, R.id.nav_downloads, R.id.nav_share)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        updateNavHeader();
    }

    private void updateNavHeader() {
        NavigationView navigationView = findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        final TextView navUsername = headerView.findViewById(R.id.nav_header_pro_name);
        TextView navUserMail = headerView.findViewById(R.id.nav_header_pro_email);
        final ImageView navUserPhoto = headerView.findViewById(R.id.nav_header_pro_img);
        Button navSignOut=headerView.findViewById(R.id.nav_signOut);

        if (currentUser!=null)
        {
            String email=currentUser.getEmail();
            navUserMail.setText(email);
        }

        try{
            userRef.child(currentUserId).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists())
                    {
                        String userName=dataSnapshot.child("User Name").getValue().toString();
                        String img=dataSnapshot.child("Profile Image").getValue().toString();

                        navUsername.setText(userName);

                        final Context context = getApplicationContext();
                        if (isValidContextForGlide(context)){
                            Glide.with(context).load(img).into(navUserPhoto);
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }catch (Exception e){
            Intent loginactivity = new Intent(getApplicationContext(),LoginActivity.class);
            startActivity(loginactivity);
            finish();
        }

        navSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                showMessage("Signout Successfully!");
                Intent loginactivity=new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(loginactivity);
                finish();
            }
        });
    }

    private void showMessage(String message) {
        Toast.makeText(MainActivity.this,message, Toast.LENGTH_LONG).show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }
}


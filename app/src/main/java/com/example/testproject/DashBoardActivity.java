package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;



import org.jetbrains.annotations.NotNull;

import java.net.MalformedURLException;
import java.net.URL;

public class DashBoardActivity extends AppCompatActivity {
    ProgressBar progressBar;
    ImageButton logoutButton, historyButton, autoMatchButtom;
    String[] permissions = {Manifest.permission.CAMERA,Manifest.permission.RECORD_AUDIO};
    int cameraPermissionCode =1;


    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);

        if(!isPermissionGranted())
        { askPermissions(); }

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        progressBar =findViewById(R.id.progressBar2);
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);

        autoMatchButtom = findViewById(R.id.play);
        autoMatchButtom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                Toast.makeText(DashBoardActivity.this, "This button is for automatching", Toast.LENGTH_SHORT).show();
            }
        });
        historyButton = findViewById(R.id.history);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashBoardActivity.this, "History button pressed", Toast.LENGTH_SHORT).show();
            }
        });
        logoutButton = findViewById(R.id.logout);
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(DashBoardActivity.this, "Logout button pressed", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder confirmLogout = new AlertDialog.Builder(DashBoardActivity.this); //create alertBox
                confirmLogout.setTitle("LogOut ?");
                confirmLogout.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SharedPreferences sharedPreferences = getSharedPreferences("loginData", Context.MODE_PRIVATE);
                        sharedPreferences.edit().clear().commit();//remove user data from local storaage
                        Intent intent = new Intent(DashBoardActivity.this,loginActivity.class);
                        startActivity(intent);
                        finish(); }
                }); //if Yes Button pressed, go to loginActivity
                confirmLogout.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override public void onClick (DialogInterface dialog,int which)
                    { }
                });
                confirmLogout.create().show();  //show the alert button


            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_Navigation);
        bottomNavigationView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        bottomNavigationView.setBackgroundColor(ContextCompat.getColor(getApplicationContext(), android.R.color.transparent));
    }

    @Override protected void onStart() {
        super.onStart();
        //Acquire data from Offline Database SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("loginCounter",Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        String email = sharedPreferences.getString("email",String.valueOf(MODE_PRIVATE));

        //Acquire data from Online Database Firestore
        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override public void onSuccess(DocumentSnapshot documentSnapshot)
            {
                String userEmail = documentSnapshot.getString("email");//taking data from firestore,, not from any previous activity
                String userName = documentSnapshot.getString("name");
                String userPhone = documentSnapshot.getString("phone");
                String userPassword = documentSnapshot.getString("password");
                String userCaseStudy = documentSnapshot.getString("caseStudy");
                String userGroupDiscussion =documentSnapshot.getString("groupDiscussion");
                String userDomain = documentSnapshot.getString("domain");
                String userRating = documentSnapshot.getString("rating");

                if(counter)
                {
                    progressBar.setVisibility(View.INVISIBLE);
                    Intent intent = new Intent(DashBoardActivity.this,  profileActivity.class);
                    intent.putExtra("userName", userName);
                    intent.putExtra("userEmail",userEmail);
                    intent.putExtra("userPassword",userPassword);
                    intent.putExtra("userPhone", userPhone);
                    intent.putExtra("userDomain", userDomain);
                    intent.putExtra("userCaseStudy", userCaseStudy);
                    intent.putExtra("userGroupDiscussion", userGroupDiscussion);
                    intent.putExtra("userRating", userRating);
                }
                else
                { startActivity(new Intent(DashBoardActivity.this, loginActivity.class));finish(); }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override public void onFailure(@NonNull @NotNull Exception e) {
                startActivity(new Intent(DashBoardActivity.this, loginActivity.class));
                Toast.makeText(DashBoardActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
        Fragment fragment = new profileFragment();
        @Override
        public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
            fragment = new profileFragment();
            if(item.getItemId()==R.id.nav_home) fragment = new profileFragment();
            if(item.getItemId()==R.id.nav_history) fragment = new leaderboardFragment();
            if(item.getItemId()==R.id.nav_settings) fragment = new settingsFragment();
            if(item.getItemId()==R.id.nav_logout) fragment = new rulesAndInstructionsFragment();

            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).commit();
            return true;
        }
    };

    private void askPermissions() { ActivityCompat.requestPermissions(this,permissions,cameraPermissionCode); }
    private boolean isPermissionGranted()
    { if(ActivityCompat.checkSelfPermission(this,Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED ||ActivityCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO)!=PackageManager.PERMISSION_GRANTED) return false;
    else return true; }
}
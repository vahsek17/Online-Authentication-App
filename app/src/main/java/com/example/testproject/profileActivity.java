package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.testproject.databinding.ActivityRulesAndInstructionsBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class profileActivity extends AppCompatActivity {

    String userMail, userName, userPhone, userDomain, userRating, userGd, userCaseStudy;
    static int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);//To remove acrtion bar

        ProgressBar progressBar =findViewById(R.id.progressBar4);
        progressBar.bringToFront();
        progressBar.setVisibility(View.VISIBLE);


        Button editButton = findViewById(R.id.button3);

        EditText name = findViewById(R.id.editTextTextPersonName7);
        EditText domain = findViewById(R.id.editTextTextPersonName);
        EditText phone = findViewById(R.id.editTextTextPersonName2);
        EditText email = findViewById(R.id.editTextTextPersonName3);
        EditText rating = findViewById(R.id.editTextTextPersonName4);
        EditText gd = findViewById(R.id.editTextTextPersonName6);
        EditText caseStudy = findViewById(R.id.editTextTextPersonName5);

        name.setEnabled(false);
        email.setEnabled(false);
        phone.setEnabled(false);
        domain.setEnabled(false);
        rating.setEnabled(false);
        gd.setEnabled(false);
        caseStudy.setEnabled(false);


        SharedPreferences sharedPreferences = getSharedPreferences("loginData", MODE_PRIVATE);
        Boolean counter = sharedPreferences.getBoolean("loginCounter", Boolean.valueOf(String.valueOf(MODE_PRIVATE)));
        String Email = sharedPreferences.getString("email", String.valueOf(MODE_PRIVATE));

        DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(Email);
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userEmail = documentSnapshot.getString("email");//taking data from firestore,, not from any previous activity
                String userName = documentSnapshot.getString("name");
                String userPhone = documentSnapshot.getString("phone");
                String userPassword = documentSnapshot.getString("password");

                String userCaseStudy = documentSnapshot.getString("caseStudy");
                String userGroupDiscussion = documentSnapshot.getString("groupDiscussion");
                String userDomain = documentSnapshot.getString("domain");
                String userRating = documentSnapshot.getString("rating");

                progressBar.setVisibility(View.INVISIBLE);
                email.setText(userEmail);
                name.setText(userName);
                phone.setText(userPhone);
                domain.setText(userDomain);
                rating.setText(userRating + "");
                gd.setText(userGroupDiscussion + "");
                caseStudy.setText(userCaseStudy + "");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });


        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (i % 2 == 0) {
                    name.setEnabled(true);
                    email.setEnabled(true);
                    phone.setEnabled(true);
                    domain.setEnabled(true);
                    editButton.setText("SAVE");
                    i += 1;
                }
                else {
                    name.setEnabled(false);
                    email.setEnabled(false);
                    phone.setEnabled(false);
                    domain.setEnabled(false);

                    userName = name.getText().toString();
                    userMail = email.getText().toString();
                    userDomain = domain.getText().toString();
                    userCaseStudy = caseStudy.getText().toString();
                    userGd = gd.getText().toString();
                    userPhone = phone.getText().toString();
                    userRating = rating.getText().toString();


                    Users user = new Users(name.getText().toString(), phone.getText().toString(), email.getText().toString(), getIntent().getStringExtra("userPassword"), domain.getText().toString(), caseStudy.getText().toString(),gd.getText().toString(), rating.getText().toString());

                    firestore.collection("Users").document(userMail).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(profileActivity.this, "details Saved", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(profileActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });



                    editButton.setText("EDIT");
                    i += 1;
                }
            }
        });
    }
}
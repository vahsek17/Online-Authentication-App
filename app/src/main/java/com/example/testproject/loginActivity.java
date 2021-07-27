package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class loginActivity extends AppCompatActivity {

    FirebaseAuth Auth;
    EditText emailBox, passwordBox;
    Button loginButton, signupButton, fotgotButton, skipButton;
    Users user;
    String userName="-", userPassword="-",userEmail="-",userPhone="-", userDomain ="_",userCaseStudy="-", userGroupDiscussion="-", userRating ="-";

    void saveData(String userEmail, String userName,String userPhone,String userPassword, String userDomain, String userCaseStudy,String userGroupDiscussion, String userRating )
    {
        SharedPreferences sharedPreferences = getSharedPreferences("loginData",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        editor.putBoolean("loginCounter",true);
        editor.putString("email",userEmail);
        editor.apply();

        Intent intent = new Intent(loginActivity.this, DashBoardActivity.class);
        intent.putExtra("userName", userName);
        intent.putExtra("userEmail",userEmail);
        intent.putExtra("userPassword",userPassword);
        intent.putExtra("userPhone", userPhone);
        intent.putExtra("userDomain", userDomain);
        intent.putExtra("userCaseStudy", userCaseStudy);
        intent.putExtra("userGroupDiscussion", userGroupDiscussion);
        intent.putExtra("userRating", userRating);


        startActivity(intent);
        finish();
    }

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);      //removes TopMost Action Bar that shows battery etc.


        ProgressBar progressBar= findViewById(R.id.progressBar3);
        progressBar.setVisibility(View.GONE);
        progressBar.bringToFront();

        emailBox =findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        Auth = FirebaseAuth.getInstance();
        loginButton = findViewById(R.id.already);
        signupButton = findViewById(R.id.Create);
        fotgotButton = findViewById(R.id.forgot);
        skipButton= findViewById(R.id.skip);

//_______________________________________________________________________________________________________________
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                String userName = "guest Account", userEmail="guest@Email.com", userPhone ="guestPhone", userPassword = "guestPassword";
//                saveData(userEmail,userName, userPhone, userPassword);
                progressBar.setVisibility(View.VISIBLE);
                DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document("guest@email.com");
                documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override public void onSuccess(DocumentSnapshot documentSnapshot)
                    {
                        //taking data from firestore,, not from any previous activity
                         userEmail = documentSnapshot.getString("email");
                         userName = documentSnapshot.getString("name");
                         userPhone = documentSnapshot.getString("phone");
                         userPassword = documentSnapshot.getString("password");

                        userCaseStudy = documentSnapshot.getString("caseStudy");
                        userGroupDiscussion =documentSnapshot.getString("groupDiscussion");
                        userDomain = documentSnapshot.getString("domain");
                        userRating = documentSnapshot.getString("rating");
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


                //Sign In into account
                Auth.signInWithEmailAndPassword("guest@email.com", "111111").addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                    @Override public void onSuccess(AuthResult authResult) {
                        loginButton.setEnabled(false);
                        saveData(userEmail,userName,userPhone,userPassword, userDomain, userCaseStudy, userGroupDiscussion, userRating);
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override public void onFailure(@NonNull @NotNull Exception e) {
                        Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        progressBar.setVisibility(View.INVISIBLE);
                        loginButton.setEnabled(true);
                    }
                });

            }
        });
//_______________________________________________________________________________________________________________
        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            { startActivity(new Intent(loginActivity.this, signup_activity.class));
                finish(); }
        });
//_______________________________________________________________________________________________________________
        fotgotButton.setOnClickListener((new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    EditText resetMail = new EditText(v.getContext());
                    AlertDialog.Builder passwordResetDialog = new AlertDialog.Builder(v.getContext());
                    passwordResetDialog.setTitle("Reset Password ?");
                    passwordResetDialog.setMessage("Enter your email here to receive password link.");
                    passwordResetDialog.setView(resetMail);

                    passwordResetDialog.setPositiveButton("yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            String mail = resetMail.getText().toString();
                            if (mail.isEmpty() == false) {
                                Auth.sendPasswordResetEmail(mail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(loginActivity.this, "E-mail has been sent to " + mail, Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(loginActivity.this, "Exception Occured" + mail, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        }
                    });

                    passwordResetDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
                    passwordResetDialog.create().show();
            }

        }));
//_______________________________________________________________________________________________________________
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String email = emailBox.getText().toString();
                String password = passwordBox.getText().toString();

                if (email.isEmpty() == false && password.isEmpty() == false) {
                    progressBar.setVisibility(View.VISIBLE);

                    //Acquire data from Database
                    DocumentReference documentReference = FirebaseFirestore.getInstance().collection("Users").document(email);
                    documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override public void onSuccess(DocumentSnapshot documentSnapshot)
                        {
                            //taking data from firestore,, not from any previous activity
                            userEmail = documentSnapshot.getString("email");
                            userName = documentSnapshot.getString("name");
                            userPhone = documentSnapshot.getString("phone");
                            userPassword = documentSnapshot.getString("password");

                            userCaseStudy = documentSnapshot.getString("caseStudy");
                            userGroupDiscussion =documentSnapshot.getString("groupDiscussion");
                            userDomain = documentSnapshot.getString("domain");
                            userRating = documentSnapshot.getString("rating");
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                    //Sign In into account
                    Auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                        @Override public void onSuccess(AuthResult authResult) {
                            loginButton.setEnabled(false);
                            saveData(userEmail,userName,userPhone,userPassword, userDomain, userCaseStudy, userGroupDiscussion, userRating);
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override public void onFailure(@NonNull @NotNull Exception e) {
                            Toast.makeText(loginActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.INVISIBLE);
                            loginButton.setEnabled(true);
                        }
                    });
                } else {
                    Toast.makeText(loginActivity.this, "Above feilds are mandatory", Toast.LENGTH_SHORT).show();
                }
            }
        });
//_______________________________________________________________________________________________________________
    }
}
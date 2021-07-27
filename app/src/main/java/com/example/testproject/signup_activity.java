package com.example.testproject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

public class signup_activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    EditText emailBox, passwordBox, confPassword,name, phone;
    Button create, already;

    @Override public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) { }
    @Override public void onNothingSelected(AdapterView<?> parent) { }

    @Override protected void onCreate(Bundle savedInstanceState)  {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);

        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseFirestore firestore =FirebaseFirestore.getInstance();

        emailBox =findViewById(R.id.emailBox);
        passwordBox = findViewById(R.id.passwordBox);
        confPassword = findViewById(R.id.confPassword);
        name = findViewById(R.id.Name);
        phone = findViewById(R.id.Phone);
        create = findViewById(R.id.Create);
        already = findViewById(R.id.already);

        already.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                startActivity(new Intent(signup_activity.this, loginActivity.class));
                finish();
            }
        });
//_______________________________________________________________________________________________________________
        create.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                String email = emailBox.getText().toString();
                String phone_no = phone.getText().toString();
                String password =passwordBox.getText().toString();
                String confirmPassword = confPassword.getText().toString();
                String fullName =name.getText().toString();


                int errors =0;
                if(phone_no.isEmpty()||password.isEmpty()||confirmPassword.isEmpty()||fullName.isEmpty()||phone_no.isEmpty()||email.isEmpty()) { errors+=1;Toast.makeText(signup_activity.this, "Above feilds are mandatory", Toast.LENGTH_SHORT).show(); }
                if (fullName.contains(" ")==false && errors==0) {errors+=1;Toast.makeText(signup_activity.this, "Please Enter your full name", Toast.LENGTH_SHORT).show();}
                if(password.contentEquals(confirmPassword)==false && errors==0) { errors+=1;Toast.makeText(signup_activity.this, "Passwords do not match", Toast.LENGTH_SHORT).show(); }
                if((phone_no.length()>10 ||phone_no.length()<7) && errors==0) { errors+=1;Toast.makeText(signup_activity.this, "Phone number is not valid", Toast.LENGTH_SHORT).show(); }


                if(errors==0)
                {
//Adding credentials in firebase authenticator
//It is not the real database, it is just for authentication
//__________________________________________________________________
                    Users user = new Users(fullName, phone_no, email, password,"Default","","","");

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task)
                        {
                            if (task.isSuccessful())
                            {
                                firestore.collection("Users").document(email).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Intent intent= new Intent(signup_activity.this, DashBoardActivity.class);
                                        intent.putExtra("userName",user.getName());
                                        intent.putExtra("userPhone",user.getPhone());
                                        intent.putExtra("userPassword",user.getPassword());
                                        intent.putExtra("userEmail",user.getEmail());
                                        intent.putExtra("userCaseStudy","");
                                        intent.putExtra("userGroupDiscusstion","");
                                        intent.putExtra("userDomain","");
                                        intent.putExtra("userRating","");

                                        startActivity(intent);    finish();
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull @NotNull Exception e) { Toast.makeText(signup_activity.this, e.getMessage(), Toast.LENGTH_SHORT).show(); }});
                            }
                            else { Toast.makeText(signup_activity.this, task.getException().toString(), Toast.LENGTH_SHORT).show(); }
                        }
                    });
//Adding Data to FireStore Database
//Initially it was not working, but later I changed the firestore rule to true, and give internet connectivity in manifest file
//_____________________________________________________________________________________________________________________________


                }
            }
        });
//_______________________________________________________________________________________________________________
    }
}
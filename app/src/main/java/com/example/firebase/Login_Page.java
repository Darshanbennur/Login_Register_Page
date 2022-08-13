package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Login_Page extends AppCompatActivity {
    EditText userID,password;
    Button loginButton;
    TextView registerHere;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-2d1df-default-rtdb.firebaseio.com/");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userID = findViewById(R.id.userID);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.loginButton);
        registerHere = findViewById(R.id.register);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = userID.getText().toString();
                String pass = password.getText().toString();
                
                if (ID.isEmpty() || pass.isEmpty()){
                    Toast.makeText(Login_Page.this, "Please Enter The UserID or Password", Toast.LENGTH_SHORT).show();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            if (snapshot.hasChild(ID)){
                                String passwordChecker = snapshot.child(ID).child("Password").getValue(String.class);

                                if (passwordChecker.equals(pass)){
                                    Toast.makeText(Login_Page.this, "Logged in!!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(Login_Page.this,MainActivity.class));
                                    finish();
                                }
                                else{
                                    Toast.makeText(Login_Page.this, "Incorrect Password!!", Toast.LENGTH_SHORT).show();
                                }
                            }
                            else{
                                Toast.makeText(Login_Page.this, "User not Registered!!", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        registerHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Login_Page.this,Register_Page.class));
            }
        });

    }
}
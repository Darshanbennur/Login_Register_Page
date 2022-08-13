package com.example.firebase;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class Register_Page extends AppCompatActivity {
    EditText userID,fullName,password,confirmationPassword,EmailID,phoneNumber,userDateofBirth;
    Button registerButton;
    TextView loginHere;

    String[] genders = {"Male","Female","Others"};
    String[] bloodGroups = {"O+","O-","A+","A-","B+","B-","AB+","AB-"};

    AutoCompleteTextView autoCompleteTextView;
    AutoCompleteTextView autoCompleteTextView_02;

    ArrayAdapter<String> arrayAdapter;
    ArrayAdapter<String> arrayAdapter_02;

    DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://fir-2d1df-default-rtdb.firebaseio.com/");

    String Gen = "";
    String BloodGrp = "";
    String dater = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_page);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        userID = findViewById(R.id.userID);
        fullName = findViewById(R.id.userName);
        password = findViewById(R.id.password);
        confirmationPassword = findViewById(R.id.confirmPassword);
        registerButton = findViewById(R.id.registerButton);
        loginHere = findViewById(R.id.loginHere);
        EmailID = findViewById(R.id.userEmailID);
        phoneNumber = findViewById(R.id.userPhoneNumber);
        userDateofBirth = findViewById(R.id.userDateOfBirth);

        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        userDateofBirth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        Register_Page.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        dater = day + "/" + month + "/" + year;
                        userDateofBirth.setText(dater);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        autoCompleteTextView = findViewById(R.id.gender_Selector);
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.list_item,genders);
        autoCompleteTextView.setAdapter(arrayAdapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Gen = arrayAdapter.getItem(i);
            }
        });

        autoCompleteTextView_02 = findViewById(R.id.bloodGroup);
        arrayAdapter_02 = new ArrayAdapter<String>(this,R.layout.list_item,bloodGroups);
        autoCompleteTextView_02.setAdapter(arrayAdapter_02);

        autoCompleteTextView_02.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                BloodGrp = arrayAdapter_02.getItem(i);
            }
        });

        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ID = userID.getText().toString();
                String userName = fullName.getText().toString();
                String pass = password.getText().toString();
                String confPass = confirmationPassword.getText().toString();
                String email = EmailID.getText().toString();
                String number = phoneNumber.getText().toString();

                if (ID.isEmpty() || userName.isEmpty() || pass.isEmpty() || confPass.isEmpty() || email.isEmpty() || number.isEmpty() || dater.isEmpty() || Gen.isEmpty() || BloodGrp.isEmpty()){
                    Toast.makeText(Register_Page.this, "Please Enter Every Detail", Toast.LENGTH_SHORT).show();
                }
                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    Toast.makeText(Register_Page.this, "Enter Valid Email", Toast.LENGTH_SHORT).show();
                    EmailID.setError("Enter Valid Email");
                }
                else if (number.length()!=10){
                    Toast.makeText(Register_Page.this, "Mobile Number should have 10 Digits", Toast.LENGTH_SHORT).show();
                    phoneNumber.setError("Mobile Number should have 10 Digits");
                }
                else if (pass.length()<8){
                    Toast.makeText(Register_Page.this, "Password should be of atleast 8 Characters", Toast.LENGTH_SHORT).show();
                    password.setError("Password should be of atleast 8 Characters");
                }
                else if (!pass.equals(confPass)){
                    Toast.makeText(Register_Page.this, "Password MisMatch!!", Toast.LENGTH_SHORT).show();
                    confirmationPassword.setError("Password Mismatch");
                    confirmationPassword.setText("");
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            if (snapshot.hasChild(ID)){
                                Toast.makeText(Register_Page.this, "User With this ID Already Registered!!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                databaseReference.child("users").child(ID).child("Full Name").setValue(userName);
                                databaseReference.child("users").child(ID).child("Email ID").setValue(email);
                                databaseReference.child("users").child(ID).child("Phone Number").setValue(number);
                                databaseReference.child("users").child(ID).child("Date of Birth").setValue(dater);
                                databaseReference.child("users").child(ID).child("Password").setValue(pass);
                                databaseReference.child("users").child(ID).child("Gender").setValue(Gen);
                                databaseReference.child("users").child(ID).child("BloodGroup").setValue(BloodGrp);
                                Toast.makeText(Register_Page.this, "Registration Successful!!", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            }
        });

        loginHere.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
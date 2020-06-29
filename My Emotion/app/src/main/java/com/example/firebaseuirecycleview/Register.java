package com.example.firebaseuirecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {

    EditText emailId, password, firstname, lastname, dayofbirth;
    Button signUpBtn;
    TextView signInTV;
    FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mFirebaseAuth = FirebaseAuth.getInstance();
        emailId = findViewById(R.id.emailTextField);
        password = findViewById(R.id.passwordTextField);
        firstname = findViewById(R.id.firstnameTextField);
        lastname = findViewById(R.id.lastnameTextField);
        dayofbirth = findViewById(R.id.dobTextField);

        signUpBtn = findViewById(R.id.signUpBtn);

        signInTV = findViewById(R.id.signInTextView);

        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    dayofbirth.setText(current);
                    dayofbirth.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }

            private String current = "";
            private String ddmmyyyy = "DDMMYYYY";
            private Calendar cal = Calendar.getInstance();
        };

        dayofbirth.addTextChangedListener(tw);

        signUpBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                String email = emailId.getText().toString();
                String pwd =  password.getText().toString();
                final String fname = firstname.getText().toString();
                final String lname = lastname.getText().toString();
                final String dob = dayofbirth.getText().toString();

                if(email.isEmpty() && pwd.isEmpty() && fname.isEmpty() && lname.isEmpty() && dob.isEmpty()){
                    Toast.makeText(Register.this,"Fields Are Empty!", Toast.LENGTH_SHORT).show();
                }
                else if(email.isEmpty()){
                    emailId.setError("Please enter email ID");
                    emailId.requestFocus();
                }
                else if(pwd.isEmpty()){
                    password.setError("Please enter your password");
                    password.requestFocus();
                }
                else if(pwd.length() < 8){
                    password.setError("Password need atleast 8 characters");
                    password.requestFocus();
                }
                else if(fname.isEmpty()){
                    firstname.setError("Please enter your first name");
                    firstname.requestFocus();
                }
                else if(lname.isEmpty()){
                    lastname.setError("Please enter your last name");
                    lastname.requestFocus();
                }
                else if(dob.isEmpty()){
                    dayofbirth.setError("Please enter your birthday");
                    dayofbirth.requestFocus();
                }
                else if(!(email.isEmpty() && pwd.isEmpty() && fname.isEmpty() && lname.isEmpty() && dob.isEmpty())){
                    mFirebaseAuth.createUserWithEmailAndPassword(email,pwd).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(!task.isSuccessful()){
                                Toast.makeText(Register.this,"Sign Up Unsuccessful, Please Try Again!", Toast.LENGTH_SHORT).show();
                            }
                            else{
                                Map<String, Object> userDetail = new HashMap<>();
                                String UID = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                userDetail.put("firstname",fname);
                                userDetail.put("lastname", lname);
                                userDetail.put("dob", dob);
                                FirebaseFirestore.getInstance().collection(UID).document("Info").set(userDetail);
                                startActivity(new Intent(Register.this, Navigation.class));
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(Register.this,"Error Occurred!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        signInTV.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent i = new Intent(Register.this,Login.class);
                startActivity(i);
            }
        });
    }
}


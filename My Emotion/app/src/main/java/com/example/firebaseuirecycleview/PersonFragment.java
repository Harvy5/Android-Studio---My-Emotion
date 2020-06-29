package com.example.firebaseuirecycleview;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class PersonFragment extends Fragment {

    TextView email, firstname, lastname, dob;
    Button signOutBtn;

    FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_person, container, false);

        email = v.findViewById(R.id.emailTextView);
        firstname = v.findViewById(R.id.firstNameTextView);
        lastname = v.findViewById(R.id.lastNameTextView);
        dob = v.findViewById(R.id.dobTextView);

        signOutBtn = v.findViewById(R.id.signOutButton);

        FirebaseFirestore.getInstance().collection(mFirebaseAuth.getCurrentUser().getUid()).document("Info").get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists()){
                    email.setText(mFirebaseAuth.getCurrentUser().getEmail());
                    firstname.setText(documentSnapshot.getString("firstname"));
                    lastname.setText(documentSnapshot.getString("lastname"));
                    dob.setText(documentSnapshot.getString("dob"));
                }
                else {
                    Toast.makeText(getActivity(), "Error!", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
        signOutBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i = new Intent(getActivity(), Login.class);
                startActivity(i);
            }
        });
        return v;
    }
}

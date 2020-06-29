package com.example.firebaseuirecycleview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class StoryView extends AppCompatActivity {

    TextView titleView;
    TextView descriptionView;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_view);

        titleView = findViewById(R.id.story_view_title);
        descriptionView = findViewById(R.id.story_view_description);

        ViewStory vs = (ViewStory) getIntent().getSerializableExtra("vs");
        String title = vs.getTitle();
        String description = "Description : \n" + vs.getDescription();
        titleView.setText(title);
        descriptionView.setText(description);
    }
}

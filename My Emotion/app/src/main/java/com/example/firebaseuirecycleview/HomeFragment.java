package com.example.firebaseuirecycleview;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class HomeFragment extends Fragment {

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference userRef = db.collection("homepage");

    private StoryAdapter adapter;

    Button helpBtn;
    TextView helpTextView;

    @Nullable
    @Override
    public android.view.View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        android.view.View v = inflater.inflate(R.layout.fragment_home, container, false);

        Query query = userRef.orderBy("priority", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Story> options = new FirestoreRecyclerOptions.Builder<Story>().setQuery(query, Story.class).build();

        adapter = new StoryAdapter(options);

        RecyclerView recyclerView = v.findViewById(R.id.homeRecyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new StoryAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                String id = documentSnapshot.getId();
                Intent i = new Intent(getActivity(), StoryView.class);
                String title = documentSnapshot.getString("title");
                String description = documentSnapshot.getString("description");
                ViewStory vs = new ViewStory();
                vs.setTitle(title);
                vs.setDescription(description);
                i.putExtra("vs", vs);
                startActivity(i);
                Toast.makeText(getActivity(), title, Toast.LENGTH_SHORT).show();
            }
        });

        helpBtn = v.findViewById(R.id.helpButton);
        helpTextView = v.findViewById(R.id.helpTextView);
        helpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Guidelines");
                builder.setMessage("1. The homepage shows other people's stories / advertisements / news.\n" +
                        "2. Use the plus sign at the bottom right (Story) to add a personal story.\n" +
                        "3. Click on the story to view the full text.\n" +
                        "4. Swipe left to delete the story.").setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                builder.show();
            }
        });

        return v;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}

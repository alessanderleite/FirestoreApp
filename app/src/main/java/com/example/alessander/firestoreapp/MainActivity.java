package com.example.alessander.firestoreapp;


import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import javax.annotation.Nullable;

public class MainActivity extends AppCompatActivity {
    private EditText editTextTitle;
    private EditText editTextDescription;
    private EditText editTextPriority;
    private EditText editTextTags;
    private TextView textViewData;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference notebookRef = db.collection("Notebook");



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextTitle = findViewById(R.id.edit_text_title);
        editTextDescription = findViewById(R.id.edit_text_description);
        editTextPriority = findViewById(R.id.edit_text_priority);
        editTextTags = findViewById(R.id.edit_text_tags);
        textViewData = findViewById(R.id.text_view_data);

        updateArray();
    }

    @Override
    protected void onStart() {
        super.onStart();
        notebookRef.addSnapshotListener(this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    return;
                }

                for (DocumentChange dc : queryDocumentSnapshots.getDocumentChanges()) {
                    DocumentSnapshot documentSnapshot = dc.getDocument();
                    String id = documentSnapshot.getId();
                    int oldIndex = dc.getOldIndex();
                    int newIndex = dc.getNewIndex();

                    switch (dc.getType()) {
                        case ADDED:
                            textViewData.append("\nAdded: " + id +
                            "\nOld Index: " + oldIndex + "New Index: " + newIndex);
                            break;
                        case MODIFIED:
                            textViewData.append("\nModified: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex);
                            break;
                        case REMOVED:
                            textViewData.append("\nRemoved: " + id +
                                    "\nOld Index: " + oldIndex + "New Index: " + newIndex);
                            break;
                    }
                }
            }
        });
    }

    public void addNote(View v) {
        String title = editTextTitle.getText().toString();
        String description = editTextDescription.getText().toString();

        if (editTextPriority.length() == 0) {
            editTextPriority.setText("0");
        }

        int priority = Integer.parseInt(editTextPriority.getText().toString());

        String tagInput = editTextTags.getText().toString();
        String[] tagArray = tagInput.split("\\s*,\\s*");
        List<String> tags = Arrays.asList(tagArray);

        Note note = new Note(title, description, priority, tags);

        notebookRef.add(note);
    }

    public void loadNotes(View v) {
        notebookRef.whereArrayContains("tags", "tag5").get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        String data = "";

                        for (QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Note note = documentSnapshot.toObject(Note.class);
                            note.setDocumentId(documentSnapshot.getId());

                            String documentId = note.getDocumentId();

                            data += "ID: " + documentId;

                            for (String tag : note.getTags()) {
                                data += "\n-" + tag;
                            }

                            data += "\n\n";
                        }
                        textViewData.setText(data);
                    }
                });
    }

    private void updateArray() {
        notebookRef.document("jLk0bSMX3srZ0pF59HXZ")
//                .update("tags", FieldValue.arrayUnion("new tag"));
        .update("tags", FieldValue.arrayRemove("new tag"));
    }

}
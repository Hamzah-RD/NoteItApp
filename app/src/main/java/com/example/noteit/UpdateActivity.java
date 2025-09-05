package com.example.noteit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;


    public class UpdateActivity extends AppCompatActivity {
        private ImageView back, update;
        private TextView dateTime;
        private EditText noteTitle, subtitle, noteText;
        private String id = "";
        private View viewIndicator;
        private ImageView imageNote;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            EdgeToEdge.enable(this);
            setContentView(R.layout.activity_update);
            initializeViews();

            // Get data from intent
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            String titleFromIntent = intent.getStringExtra("title");
            String subtitleFromIntent = intent.getStringExtra("subtitle");
            String noteTextFromIntent = intent.getStringExtra("noteText");
            String dateTimeFromIntent = intent.getStringExtra("dateTime");

            // Prefill fields
            noteTitle.setText(titleFromIntent);
            subtitle.setText(subtitleFromIntent);
            noteText.setText(noteTextFromIntent);
            dateTime.setText(dateTimeFromIntent);

            update.setOnClickListener(v -> updateNote());
            back.setOnClickListener(v ->
            {
                new AlertDialog.Builder(this)
                        .setTitle("Conform exist")
                .setMessage("Are you sure ,you want to exist")
                .setCancelable(false)
                        .setPositiveButton("Yes",((dialog, which) -> {
                            finish();
                        }))
                        .setNegativeButton("No",(dialog, which) -> {
                            dialog.dismiss();
                        })
                        .show();
            });
        }

        private void updateNote() {
            FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();

            String titleStr = noteTitle.getText().toString().trim();
            String subtitleStr = subtitle.getText().toString().trim();
            String noteTextStr = noteText.getText().toString().trim();
            String datetimeStr = dateTime.getText().toString().trim();

            NotesModel notesModel = new NotesModel(
                    id, titleStr, subtitleStr, noteTextStr, datetimeStr, firebaseAuth.getUid()
            );

            FirebaseFirestore firestore = FirebaseFirestore.getInstance();
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setMessage("Updating...");
            progressDialog.show();

            firestore.collection("notes")
                    .document(id)
                    .set(notesModel)
                    .addOnSuccessListener(unused -> {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateActivity.this, "Note updated successfully", Toast.LENGTH_SHORT).show();
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(UpdateActivity.this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }

        private void initializeViews() {
            back = findViewById(R.id.ig_back);
            update = findViewById(R.id.ig_save);
            dateTime = findViewById(R.id.tv_datetime);
            noteTitle = findViewById(R.id.et_title);
            subtitle = findViewById(R.id.et_subtitle);
            noteText = findViewById(R.id.et_NoteText);
            viewIndicator = findViewById(R.id.viewinductor);
            imageNote = findViewById(R.id.imageNote);
        }
    }


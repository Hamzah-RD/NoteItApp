package com.example.noteit;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.noteit.databinding.ActivityCreateNewNoteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.UUID;


public class CreateNewNote extends AppCompatActivity {
    private ImageView back, save;
    private TextView dateTime;
    private EditText noteTitle;
     EditText subtitle;

     EditText noteText;
    private View viewIndicator;
    private ImageView imageNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);
        initializeViews();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();
            }
        });


    }

    private void saveNote() {
        FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
        String notesId= UUID.randomUUID().toString();
        String titleStr = noteTitle.getText().toString();
        String subtitleStr = subtitle.getText().toString();
        String noteTextStr = noteText.getText().toString();
        String datetimeStr = dateTime.getText().toString();
        NotesModel notesModel=new NotesModel(notesId,titleStr,subtitleStr,noteTextStr,datetimeStr,firebaseAuth.getUid());

        FirebaseFirestore firestore=FirebaseFirestore.getInstance();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setMessage("Your Data");
        progressDialog.setTitle(" Saving");
        firestore.collection("notes")
                .document(notesId)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(CreateNewNote.this,"Saving",Toast.LENGTH_SHORT);
                        progressDialog.show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(CreateNewNote.this,e.getMessage(),Toast.LENGTH_SHORT);
                    progressDialog.cancel();
                    }
                });
    }

    private void initializeViews() {
        back = findViewById(R.id.ig_back);
        save = findViewById(R.id.ig_save);
        dateTime = findViewById(R.id.tv_datetime);
        noteTitle = findViewById(R.id.et_title);
        subtitle = findViewById(R.id.et_subtitle);
        noteText = findViewById(R.id.et_NoteText);
        viewIndicator = findViewById(R.id.viewinductor);
        imageNote = findViewById(R.id.imageNote);
    }
}
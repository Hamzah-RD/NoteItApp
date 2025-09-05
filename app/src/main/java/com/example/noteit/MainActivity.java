package com.example.noteit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.noteit.databinding.ActivityCreateNewNoteBinding;
import com.example.noteit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView Add;
    private NotesAdapter notesAdapter;
    RecyclerView recyclerview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Add.findViewById(R.id.ig_newNote);
        notesAdapter=new NotesAdapter(this);
        recyclerview=findViewById(R.id.recyclerview_Note);
        recyclerview.setAdapter(notesAdapter);
        recyclerview.setLayoutManager(new LinearLayoutManager(this));

        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CreateNewNote.class);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        ProgressDialog progressDialog=new ProgressDialog(this);
        progressDialog.setTitle("Checking User");
        progressDialog.setMessage("In Progress");

         FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
         if (firebaseAuth.getCurrentUser()==null)
         {
             progressDialog.show();
             firebaseAuth.signInAnonymously()
                     .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                         @Override
                         public void onSuccess(AuthResult authResult) {
                             Toast.makeText(MainActivity.this,"Checking User",Toast.LENGTH_SHORT);
                             progressDialog.cancel();

                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT);
                             progressDialog.cancel();
                         }
                     });
         }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getData();
    }

    private void getData() {

        FirebaseFirestore.getInstance()
                .collection("notes")
                .whereEqualTo("uid",FirebaseAuth.getInstance().getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                    @Override
                    public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                        notesAdapter.clear();
                       List<DocumentSnapshot> dsList= queryDocumentSnapshots.getDocuments();
                       for (int i=0;i<dsList.size();i++)
                       {
                           DocumentSnapshot documentSnapshot=dsList.get(i);
                           NotesModel notesModel=documentSnapshot.toObject(NotesModel.class);
                           notesAdapter.add(notesModel);
                       }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });
    }
}
package com.example.noteit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.example.noteit.databinding.ActivityCreateNewNoteBinding;
import com.example.noteit.databinding.ActivityMainBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Firebase;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    ImageView Add;
    private NotesAdapter notesAdapter;
    RecyclerView recyclerview;
    EditText search;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        Add=findViewById(R.id.ig_newNote);
        notesAdapter=new NotesAdapter(this);
        recyclerview=findViewById(R.id.recyclerview_Note);
        search=findViewById(R.id.et_search);

        recyclerview.setAdapter(notesAdapter);
      //  recyclerview.setLayoutManager(new GridLayoutManager(this,2));
        recyclerview.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        Add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,CreateNewNote.class);
                startActivity(intent);
            }
        });
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                notesAdapter.filterList(s.toString());

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
                             Toast.makeText(MainActivity.this,"Checking User",Toast.LENGTH_SHORT).show();
                             progressDialog.cancel();

                         }
                     })
                     .addOnFailureListener(new OnFailureListener() {
                         @Override
                         public void onFailure(@NonNull Exception e) {
                             Toast.makeText(MainActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();
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
                .orderBy("dateTime", Query.Direction.DESCENDING)
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
                        Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
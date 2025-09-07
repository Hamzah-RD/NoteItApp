package com.example.noteit;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


    public class UpdateActivity extends AppCompatActivity {
        private ImageView back, update,delete;
        private TextView dateTime;
        private EditText noteTitle, subtitle, noteText;
        private String id = "";
        private String selectedNoteColor = "#333333";
        private View viewIndicator;
        private ImageView imageNote;

        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
           // EdgeToEdge.enable(this);
            setContentView(R.layout.activity_update);
            delete=findViewById(R.id.ig_delete);
            initializeViews();
            initMiscellaneous();
            dateTime.setText(getCurrentDateTime());
            // Get data from intent
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            String titleFromIntent = intent.getStringExtra("title");
            String subtitleFromIntent = intent.getStringExtra("subtitle");
            String noteTextFromIntent = intent.getStringExtra("noteText");
            String dateTimeFromIntent = intent.getStringExtra("dateTime");

            // Prefill fields
           dateTimeFromIntent = intent.getStringExtra("dateTime");
            noteTitle.setText(titleFromIntent);
            subtitle.setText(subtitleFromIntent);
            noteText.setText(noteTextFromIntent);
            dateTime.setText(dateTimeFromIntent);

            update.setOnClickListener(v -> updateNote());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    FirebaseAuth firebaseAuth=FirebaseAuth.getInstance();
                    new AlertDialog.Builder(UpdateActivity.this)
                            .setTitle("Deleting")
                            .setMessage("Are You Sure you want to delete this Note")
                            .setCancelable(false)
                            .setPositiveButton("Yes",((dialog, which) -> {
                                FirebaseFirestore firebaseFirestore=FirebaseFirestore.getInstance();
                                firebaseFirestore.collection("notes")
                                        .document(id)
                                        .delete();
                                finish();
                            }))
                            .setNegativeButton("No",(dialog, which) -> {
                                dialog.dismiss();
                            })
                            .setNegativeButton("No", (dialog, which) -> dialog.dismiss())
                            .show();




                }
            });

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
                    id, titleStr, subtitleStr, noteTextStr, datetimeStr, firebaseAuth.getUid(),getIntent().getStringExtra("color"));

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

        private String getCurrentDateTime() {
            return new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
                    .format(new Date());
        }


        private void initMiscellaneous() {
            final LinearLayout layoutMiscellaneous = findViewById(R.id.layoutmiscellaneous);
            final BottomSheetBehavior<LinearLayout> bottomSheetBehavior =
                    BottomSheetBehavior.from(layoutMiscellaneous);

            // Toggle bottom sheet
            layoutMiscellaneous.findViewById(R.id.Textmiscellaneous).setOnClickListener(v -> {
                if (bottomSheetBehavior.getState() != BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            });

            // Color selection
            setupColorSelection(layoutMiscellaneous, bottomSheetBehavior);

//        // Image selection
//        layoutMiscellaneous.findViewById(R.id.addImagelayout).setOnClickListener(v -> {
//            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
//            handleImageSelection();
//        });
        }

        private void setupColorSelection(LinearLayout layout, BottomSheetBehavior<LinearLayout> behavior) {
            int[] colorViews = {R.id.imagecolor1, R.id.imagecolor2, R.id.imagecolor3,
                    R.id.imagecolor4, R.id.imagecolor5, R.id.imagecolor6, R.id.imagecolor7};
            String[] colors = {"#333333", "#ea7c54", "#f6ecc9", "#cdeff1",
                    "#FF5722", "#002DFF", "#00FFA5"};
            int[] saveIcons = new int[7]; // Initialize with 0

            for (int i = 0; i < colorViews.length; i++) {
                int finalI = i;
                layout.findViewById(colorViews[i]).setOnClickListener(v -> {
                    selectedNoteColor = colors[finalI];
                    resetAllSaveIcons(layout, colorViews);
                    ImageView current = layout.findViewById(colorViews[finalI]);
                    current.setImageResource(R.drawable.bg_ic_save);
                    setSubtitleIndicatorColor();
                    behavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                });
            }
        }

        private void resetAllSaveIcons(LinearLayout layout, int[] colorViews) {
            for (int viewId : colorViews) {
                ImageView view = layout.findViewById(viewId);
                view.setImageResource(0);
            }
        }

        private void setSubtitleIndicatorColor() {
            if (viewIndicator.getBackground() instanceof GradientDrawable) {
                GradientDrawable gradientDrawable = (GradientDrawable) viewIndicator.getBackground();
                gradientDrawable.setColor(Color.parseColor(selectedNoteColor));
            }


        }

    }


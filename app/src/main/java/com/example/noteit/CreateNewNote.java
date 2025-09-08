package com.example.noteit;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.example.noteit.databinding.ActivityCreateNewNoteBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.UUID;


public class CreateNewNote extends AppCompatActivity {
    private ImageView back, save;
    private TextView dateTime;
    private EditText noteTitle;
    EditText subtitle;

    EditText noteText;
    private String selectedNoteColor = "#333333";
    private View viewIndicator;
    private ImageView imageNote;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_note);
        initializeViews();
        initMiscellaneous();

        dateTime.setText(getCurrentDateTime());

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveNote();
            }
        });


    }

    private void saveNote() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        String notesId = UUID.randomUUID().toString();
        String titleStr = noteTitle.getText().toString();
        String subtitleStr = subtitle.getText().toString();
        String noteTextStr = noteText.getText().toString();
        String datetimeStr = dateTime.getText().toString();

        if (titleStr.isEmpty() && subtitleStr.isEmpty()) {
            Toast.makeText(CreateNewNote.this, "Please enter at least a title or subtitle", Toast.LENGTH_SHORT).show();
            return;
        }
        NotesModel notesModel = new NotesModel(notesId, titleStr, subtitleStr, noteTextStr, datetimeStr, firebaseAuth.getUid(), selectedNoteColor);

        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Your Data");
        progressDialog.setTitle(" Saving");
        progressDialog.show();
        firestore.collection("notes")
                .document(notesId)
                .set(notesModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateNewNote.this, "Saving", Toast.LENGTH_SHORT).show();
                        finish();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(CreateNewNote.this, e.getMessage(), Toast.LENGTH_SHORT).show();

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
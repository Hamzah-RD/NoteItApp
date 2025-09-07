package com.example.noteit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.viewHolder> {
    private Context context;
    private List<NotesModel> notesModelList;
    private List<NotesModel> fullList;


    public NotesAdapter(Context context) {
        this.context = context;
        this.notesModelList=new ArrayList<>();
        this.fullList=new ArrayList<>();
    }

    public void add(NotesModel notesModel)
    {
        notesModelList.add(notesModel);
        fullList.add(notesModel);
        notifyDataSetChanged();
    }
    public void clear()
    {
        notesModelList.clear();
        fullList.clear();
        notifyDataSetChanged();
    }
    public void filterList(String text)
    {
        if (text == null || text.isEmpty()) {
            // restore all notes
            notesModelList = new ArrayList<>(fullList);
        } else {
            List<NotesModel> filtered = new ArrayList<>();
            String query = text.toLowerCase();

            for (NotesModel note : fullList) {
                if (note.getTitle().toLowerCase().contains(query) ||
                        note.getsubtitle().toLowerCase().contains(query)) {
                    filtered.add(note);
                }
            }
            notesModelList = filtered;
        }
        notifyDataSetChanged(); // refresh RecyclerView
    }


    public List<NotesModel> getList()
    {
        return notesModelList;
    }




    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_container,parent,false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
       NotesModel notesModel= notesModelList.get(position);
        holder.title.setText(notesModel.getTitle());
        holder.subtile.setText(notesModel.getsubtitle());
        String noteColor = notesModel.getColor();
        if (noteColor != null && !noteColor.isEmpty()) {
            try {
                GradientDrawable gradientDrawable = (GradientDrawable) holder.layoutNotes.getBackground();
                gradientDrawable.setColor(Color.parseColor(noteColor));
            } catch (Exception e) {
                holder.layoutNotes.setBackgroundColor(Color.WHITE);
            }
        } else {
            holder.layoutNotes.setBackgroundColor(Color.WHITE);
        }


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (context,UpdateActivity.class);
                intent.putExtra("id",notesModel.getId());
                intent.putExtra("title",notesModel.getTitle());
                intent.putExtra("subtitle",notesModel.getsubtitle());
                intent.putExtra("noteText",notesModel.getNoteText());
                intent.putExtra("dateTime", notesModel.getDateTime());
                intent.putExtra("color", notesModel.getColor());
                context.startActivity(intent);

            }
        });

    }

    @Override
    public int getItemCount() {
        return notesModelList.size();
    }

    public  class viewHolder extends RecyclerView.ViewHolder {
        private TextView title,subtile;
        private LinearLayout layoutNotes;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tvtitle);
            subtile=itemView.findViewById(R.id.tvsubtitle);
            layoutNotes=itemView.findViewById(R.id.layoutNotes);

        }
    }
}

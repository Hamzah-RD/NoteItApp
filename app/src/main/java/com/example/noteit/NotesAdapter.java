package com.example.noteit;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class NotesAdapter  extends RecyclerView.Adapter<NotesAdapter.viewHolder> {
    private Context context;
    private List<NotesModel> notesModelList;
    public NotesAdapter(Context context) {
        this.context = context;
        notesModelList=new ArrayList<>();
    }

    public void add(NotesModel notesModel)
    {
        notesModelList.add(notesModel);
        notifyDataSetChanged();
    }
    public void clear()
    {
        notesModelList.clear();
        notifyDataSetChanged();
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
        holder.subtile.setText(notesModel.getSubtiltel());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent (context,UpdateActivity.class);
                intent.putExtra("id",notesModel.getId());
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
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            title=itemView.findViewById(R.id.tvtitle);
            subtile=itemView.findViewById(R.id.tvsubtitle);

        }
    }
}

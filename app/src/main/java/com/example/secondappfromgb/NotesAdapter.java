package com.example.secondappfromgb;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> {

    interface OnNoteClicked {
        void onNoteClicked (Note note);

    }

    private OnNoteClicked noteClicked;

    public OnNoteClicked getNoteClicked() {
        return noteClicked;
    }

    public void setNoteClicked(OnNoteClicked noteClicked) {
        this.noteClicked = noteClicked;
    }

    private List<Note> data = new ArrayList<>();

    public void setData(Collection<Note> notes) {
        data.addAll(notes);
    }


    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
        NotesViewHolder holder = new NotesViewHolder(itemView);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull NotesViewHolder holder, int position) {
        Note item_note = data.get(position);

        holder.name.setText(item_note.getName());
        holder.description.setText(item_note.getDescription());

        @SuppressLint("SimpleDateFormat")
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(item_note.getDate().getTime());
        holder.date.setText(dateStr);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class NotesViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        TextView description;
        TextView date;

        public NotesViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            description = itemView.findViewById(R.id.description);
            date = itemView.findViewById(R.id.date);

            itemView.findViewById(R.id.card_view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (noteClicked != null) {
                        int clickedPosition = getAdapterPosition();
                        noteClicked.onNoteClicked(data.get(clickedPosition));
                    }
                }
            });

        }
    }
}

package com.example.secondappfromgb;

import java.util.List;

public interface NotesRepository {
    void getAll(Callback<List<Note>> callback);

    void add(Note note, Callback<Note> callback);

    void remove (Note note, Callback<Note> callback);

    void remove (int index, Callback<Note> callback);

    void update(Note note, Note updateNote, Callback<Note> callback);
}

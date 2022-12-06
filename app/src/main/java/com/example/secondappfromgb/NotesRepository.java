package com.example.secondappfromgb;

import java.util.List;

public interface NotesRepository {
    List<Note> getAll();

    void add(Note note);

    void remove (Note note);

    void remove (int index);
}

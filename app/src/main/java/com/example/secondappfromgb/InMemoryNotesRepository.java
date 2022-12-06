package com.example.secondappfromgb;

import android.content.Context;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;

public class InMemoryNotesRepository implements NotesRepository{
    private static NotesRepository INSTANCE;
    private Context context;
    private ArrayList<Note> result = new ArrayList<>();

    public InMemoryNotesRepository(Context context) {
        this.context = context;
        result.add(new Note("Note 1", "Description 1", new GregorianCalendar(2020, 0, 1),"111111111111111111111111111111"));
        result.add(new Note("Note 2", "Description 2", new GregorianCalendar(2021, 1, 12),"22222222222222222"));
        result.add(new Note("Note 3", "Description 3", new GregorianCalendar(2022, 3, 20),"33333333333333333333333333333333333333333"));
        result.add(new Note("Note 4", "Description 4", new GregorianCalendar(1999, 11, 31),"444444444"));
    }

    public static NotesRepository getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new InMemoryNotesRepository(context);
        }
        return INSTANCE;
    }

    @Override
    public List<Note> getAll() {
        return result;
    }

    @Override
    public void add(Note note) {
        result.add(note);
    }

    @Override
    public void remove(Note note) {
        result.remove(note);
    }

    @Override
    public void remove(int index) {
        result.remove(index);
    }


}

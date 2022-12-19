package com.example.secondappfromgb;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.WindowDecorActionBar;

import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InMemoryNotesRepository implements NotesRepository{
    private static NotesRepository INSTANCE;
    private Context context;
    private ArrayList<Note> result = new ArrayList<>();

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler handler = new Handler(Looper.getMainLooper());

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
    public void getAll(Callback<List<Note>> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(result);
                    }
                });
            }
        });
    }

    @Override
    public void add(Note note, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                result.add(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(note);
                    }
                });
            }
        });
    }

    @Override
    public void remove(Note note, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                result.remove(note);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(null);
                    }
                });
            }
        });
    }

    @Override
    public void remove(int index, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                result.remove(index);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(null);
                    }
                });
            }
        });
    }

    @Override
    public void update(Note note, Note updateNote, Callback<Note> callback) {
        executor.execute(new Runnable() {
            @Override
            public void run() {
                int index;
                if (result.contains(note)) {
                    index = result.indexOf(note);
                } else {
                    return;
                }

                result.set(index, updateNote);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callback.onSuccess(updateNote);
                    }
                });
            }
        });
    }


}

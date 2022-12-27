package com.example.secondappfromgb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.WindowDecorActionBar;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class InMemoryNotesRepository implements NotesRepository{

    private SharedPreferences sharedPreferences;
    public static final String SHARED_KEY = "SHARED_KEY";

    private static NotesRepository INSTANCE;
    private Context context;
    private ArrayList<Note> result = new ArrayList<>();

    private Executor executor = Executors.newSingleThreadExecutor();

    private Handler handler = new Handler(Looper.getMainLooper());

    public InMemoryNotesRepository(Context context) {
        this.context = context;
        sharedPreferences = context.getSharedPreferences(SHARED_KEY, Context.MODE_PRIVATE);
        result = new ArrayList<>();
        String savedNotes = sharedPreferences.getString(SHARED_KEY, null);
        if (savedNotes == null || savedNotes.isEmpty()) {
            Toast.makeText(context, "Empty", Toast.LENGTH_SHORT).show();
        } else {
            try {
                Type type = new TypeToken<ArrayList<Note>>() {
                }.getType();
                 result = new GsonBuilder().create().fromJson(savedNotes, type);
            } catch (JsonSyntaxException e) {
                Toast.makeText(context, "Ошибка трансформации", Toast.LENGTH_SHORT).show();
            }
        }
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
                String jsonNotes = new GsonBuilder().create().toJson(result);
                sharedPreferences.edit().putString(SHARED_KEY, jsonNotes).apply();

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
                String jsonNotes = new GsonBuilder().create().toJson(result);
                sharedPreferences.edit().putString(SHARED_KEY, jsonNotes).apply();
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
                String jsonNotes = new GsonBuilder().create().toJson(result);
                sharedPreferences.edit().putString(SHARED_KEY, jsonNotes).apply();
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
                String jsonNotes = new GsonBuilder().create().toJson(result);
                sharedPreferences.edit().putString(SHARED_KEY, jsonNotes).apply();
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

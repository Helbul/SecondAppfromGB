package com.example.secondappfromgb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NoteListFragment extends Fragment {

    public static final String NOTE_CLICKED_KEY = "NOTE_CLICKED_KEY";
    public static final String SELECTED_NOTE = "SELECTED_NOTE";
    private Note note;
    private List<Note> notes;
    private NotesAdapter notesAdapter;
    ProgressBar progressBar;

    private Note selectedNote;
    private int indexSelectedNote;

    public NoteListFragment() {
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putParcelable(SELECTED_NOTE, note);
        super.onSaveInstanceState(outState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_notes_list, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Toolbar toolbar = view.findViewById(R.id.toolbar);

        if (requireActivity() instanceof ToolbarHolder) {
            ((ToolbarHolder) requireActivity()).setToolbar(toolbar);
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.menu_settings):
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new SettingsFragment())
                                .addToBackStack("")
                                .commit();
                        return true;

                    case (R.id.menu_about_us):
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, new AboutUsFragment())
                                .addToBackStack("")
                                .commit();
                        return true;

                    case (R.id.menu_exit):
                        Toast.makeText(requireContext(), "Exit!!!!", Toast.LENGTH_LONG).show();
                        return true;

                }

                return false;
            }
        });

        if (savedInstanceState != null) {
            note = savedInstanceState.getParcelable(SELECTED_NOTE);
        }



        RecyclerView recyclerView = view.findViewById(R.id.container);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(requireContext(),
                LinearLayoutManager.VERTICAL,
    false));
        notesAdapter = new NotesAdapter(this);
        notesAdapter.setNoteClicked(new NotesAdapter.OnNoteClicked() {
            @Override
            public void onNoteClicked(Note clickedNote) {
                note = clickedNote;
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_details_container, NoteDetailsFragment.newInstance(note))
                            .commit();

                } else {
                    Toast.makeText(requireContext(), note.getName(), Toast.LENGTH_SHORT).show();
                    getParentFragmentManager()
                            .beginTransaction()
                            .replace(R.id.fragment_container, NoteDetailsFragment.newInstance(note))
                            .addToBackStack("")
                            .commit();
                }
            }

            @Override
            public void onNoteLongClicked(Note note, int index) {
                selectedNote = note;
                indexSelectedNote = index;
            }
        });
        //notesAdapter.setData(notes);
        recyclerView.setAdapter(notesAdapter);
        //notesAdapter.notifyDataSetChanged();

        progressBar = view.findViewById(R.id.progress);
        progressBar.setVisibility(View.VISIBLE);


        InMemoryNotesRepository.getInstance(requireContext()).getAll(new Callback<List<Note>>() {
            @Override
            public void onSuccess(List<Note> data) {
                notes = data;
                notesAdapter.setData(notes);
                notesAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError(Throwable exception) {
                //TO DO
                progressBar.setVisibility(View.GONE);
            }
        });


        FloatingActionButton addBottom = view.findViewById(R.id.add);
        addBottom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AddNoteDialogFragment
                        .addInstance()
                        .show(getParentFragmentManager(), "AddNoteDialogFragment");
            }
        });

        getParentFragmentManager()
                .setFragmentResultListener(
                        AddNoteDialogFragment.KEY_RESULT_ADD_NOTE,
                        getViewLifecycleOwner(),
                        new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = result.getParcelable(AddNoteDialogFragment.ARG_NOTE);

                        int index = notesAdapter.addNote(note);

                        notesAdapter.notifyItemInserted(index);

                        recyclerView.smoothScrollToPosition(index);
                    }
                });

        getParentFragmentManager()
                .setFragmentResultListener(
                        AddNoteDialogFragment.KEY_RESULT_UPDATE_NOTE,
                        getViewLifecycleOwner(),
                        new FragmentResultListener() {
                            @Override
                            public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                                Note note = result.getParcelable(AddNoteDialogFragment.ARG_NOTE);

                                notesAdapter.replaceNote(note, indexSelectedNote);

                                notesAdapter.notifyItemChanged(indexSelectedNote);
                            }
                        }
                );
    }

    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = requireActivity().getMenuInflater();
        menuInflater.inflate(R.menu.menu_note_context, menu);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_delete:

                progressBar.setVisibility(View.VISIBLE);

                InMemoryNotesRepository.getInstance(requireContext()).remove(selectedNote, new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        progressBar.setVisibility(View.GONE);
                        notesAdapter.removeNote(selectedNote);
                        notesAdapter.notifyItemRemoved(indexSelectedNote);
                    }

                    @Override
                    public void onError(Throwable exception) {
                        progressBar.setVisibility(View.GONE);
                    }
                });
                return true;
            case R.id.action_edit:
                AddNoteDialogFragment.editInstance(selectedNote)
                        .show(getParentFragmentManager(), "AddNoteDialogFragment");
                return true;
        }
        return super.onContextItemSelected(item);
    }
}

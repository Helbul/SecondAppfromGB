package com.example.secondappfromgb;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

public class NoteListFragment extends Fragment {

    public static final String NOTE_CLICKED_KEY = "NOTE_CLICKED_KEY";
    public static final String SELECTED_NOTE = "SELECTED_NOTE";
    private Note note;

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

        List<Note> notes = InMemoryNotesRepository.getInstance(requireContext()).getAll();
        LinearLayout container = view.findViewById(R.id.container);

        for (Note item_note: notes) {
            View itemView = getLayoutInflater().inflate(R.layout.item_note, container, false);

            itemView.findViewById(R.id.root).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    note = item_note;
                    if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(SELECTED_NOTE, note);
                        getParentFragmentManager()
                                .setFragmentResult(NOTE_CLICKED_KEY, bundle);
                    } else {
                        Toast.makeText(requireContext(), note.getName(), Toast.LENGTH_SHORT).show();
                        getParentFragmentManager()
                                .beginTransaction()
                                .replace(R.id.fragment_container, NoteDetailsFragment.newInstance(note))
                                .addToBackStack("")
                                .commit();
                    }
                }
            });

            TextView name = itemView.findViewById(R.id.name);
            TextView description = itemView.findViewById(R.id.description);
            TextView date = itemView.findViewById(R.id.date);

            name.setText(item_note.getName());
            description.setText(item_note.getDescription());

            @SuppressLint("SimpleDateFormat")
            DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            String dateStr = dateFormat.format(item_note.getDate().getTime());
            date.setText(dateStr);

            container.addView(itemView);
        }
    }
}

package com.example.secondappfromgb;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

public class NoteDetailsFragment extends Fragment {

    private static final String ARG_NOTE = "ARG_NOTE";
    private Note note;

    TextView name;
    TextView date;
    TextView description;
    TextView content;

    public NoteDetailsFragment() {
        super(R.layout.fragment_note_details);
    }

    public static NoteDetailsFragment newInstance(Note note) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);

        NoteDetailsFragment fragment = new NoteDetailsFragment();
        fragment.setArguments(args);
        return fragment;
    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle arguments = getArguments();

        name = view.findViewById(R.id.name);
        date = view.findViewById(R.id.date);
        description = view.findViewById(R.id.description);
        content = view.findViewById(R.id.content);

        getParentFragmentManager()
                .setFragmentResultListener(NoteListFragment.NOTE_CLICKED_KEY, getViewLifecycleOwner(), new FragmentResultListener() {
                    @Override
                    public void onFragmentResult(@NonNull String requestKey, @NonNull Bundle result) {
                        Note note = result.getParcelable(NoteListFragment.SELECTED_NOTE);
                        showNote(note);
                    }
                });


        if (arguments != null) {
            note = arguments.getParcelable(ARG_NOTE);
            showNote(note);
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar_note_details);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case (R.id.menu_delete):
                        Toast.makeText(requireContext(), R.string.menu_delete, Toast.LENGTH_LONG).show();
                        return true;

                    case (R.id.menu_share):
                        Toast.makeText(requireContext(), R.string.menu_share, Toast.LENGTH_LONG).show();
                        return true;
                }

                return false;
            }
        });

    }


    private void showNote(Note note) {
        name.setText(note.getName());
        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dateStr = dateFormat.format(note.getDate().getTime());
        date.setText(dateStr);
        description.setText(note.getDescription());
        content.setText(note.getContent());
    }
}

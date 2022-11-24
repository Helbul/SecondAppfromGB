package com.example.secondappfromgb;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentResultListener;

import java.util.Calendar;

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


//        добавть проверку на наличии аргументов
//        Note note = getArguments().getParcelable(ARG_NOTE);
//        showNote(note);
        if (arguments != null) {
            note = arguments.getParcelable(ARG_NOTE);
            showNote(note);
        }

    }


    private void showNote(Note note) {
        name.setText(note.getName());
        String dateStr = String.format("%s/%s/%s", note.getDate().get(Calendar.DAY_OF_MONTH), note.getDate().get(Calendar.MONTH) + 1, note.getDate().get(Calendar.YEAR));
        date.setText(dateStr);
        description.setText(note.getDescription());
        content.setText(note.getContent());
    }
}

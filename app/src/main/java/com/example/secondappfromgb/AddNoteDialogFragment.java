package com.example.secondappfromgb;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddNoteDialogFragment extends BottomSheetDialogFragment {
    public static final String KEY_RESULT_ADD_NOTE = "KEY_RESULT_ADD_NOTE";
    public static final String KEY_RESULT_UPDATE_NOTE = "KEY_RESULT_UPDATE_NOTE";
    public static final String ARG_NOTE= "ARG_NOTE";

    public static AddNoteDialogFragment addInstance() {
        return new AddNoteDialogFragment();
    }

    public static AddNoteDialogFragment editInstance(Note note) {

        Bundle args = new Bundle();
        args.putParcelable(ARG_NOTE, note);

        AddNoteDialogFragment fragment = new AddNoteDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_note_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Note noteToEdit = null;

        if (getArguments() != null && getArguments().containsKey(ARG_NOTE)) {
            noteToEdit = getArguments().getParcelable(ARG_NOTE);
        }

        EditText name = view.findViewById(R.id.input_name);
        EditText description = view.findViewById(R.id.input_description);
        EditText content = view.findViewById(R.id.input_content);
        TextView date = view.findViewById(R.id.input_date);

        DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        GregorianCalendar currentDate = new GregorianCalendar();
        date.setText(dateFormat.format(currentDate.getTime()));

        if (noteToEdit != null) {
            name.setText(noteToEdit.getName());
            description.setText(noteToEdit.getDescription());
            content.setText(noteToEdit.getContent());
            date.setText(dateFormat.format(noteToEdit.getDate().getTime()));
        }

        Button saveButton = view.findViewById(R.id.add_note);

        MaterialDatePicker<Long> datePicker =
                MaterialDatePicker.Builder.datePicker()
                        .build();

        datePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener<Long>() {
            @Override
            public void onPositiveButtonClick(Long selection) {
                Date selectedDate = new Date(selection);
                date.setText(dateFormat.format(selectedDate));
            }
        });



        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePicker.show(getParentFragmentManager(), "MaterialDatePicker");
            }
        });

        Note finalNoteToEdit = noteToEdit;
        view.findViewById(R.id.add_note).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveButton.setEnabled(false);

                Note note = new Note(name.getText().toString(), description.getText().toString(), currentDate, content.getText().toString());

                if (finalNoteToEdit != null) {
                    editNote(note);
                } else {
                    addNote(note);
                }

            }

            private void editNote(Note note) {
                InMemoryNotesRepository.getInstance(requireContext()).update(finalNoteToEdit, note, new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ARG_NOTE, data);

                        getParentFragmentManager().setFragmentResult(KEY_RESULT_UPDATE_NOTE, bundle);

                        saveButton.setEnabled(true);
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable exception) {
                        saveButton.setEnabled(true);
                    }
                });
            }

            private void addNote(Note note) {
                InMemoryNotesRepository.getInstance(requireContext()).add(note, new Callback<Note>() {
                    @Override
                    public void onSuccess(Note data) {
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(ARG_NOTE, data);

                        getParentFragmentManager().setFragmentResult(KEY_RESULT_ADD_NOTE, bundle);

                        saveButton.setEnabled(true);
                        dismiss();
                    }

                    @Override
                    public void onError(Throwable exception) {
                        saveButton.setEnabled(true);
                    }
                });
            }
        });
    }
}
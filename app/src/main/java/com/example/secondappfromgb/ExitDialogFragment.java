package com.example.secondappfromgb;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentResultListener;

public class ExitDialogFragment extends DialogFragment {
    public static final String TAG = "ExitDialogTag";
    OnExitDialogListener onExitDialogListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        onExitDialogListener = (OnExitDialogListener) context;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return new AlertDialog.Builder(requireContext())
                .setTitle("Выход")
                .setMessage("Вы уверены, что хотите выйти?")
                .setPositiveButton("Да", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(onExitDialogListener != null) {
                            dismiss();
                            onExitDialogListener.onExitDialogOk();
                        }
                    }
                })
                .setNegativeButton("Нет", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dismiss();
                    }
                })
                .create();
    }

//    public void setOnClickListener (OnExitDialogListener onExitDialogListener) {
//        this.onExitDialogListener = onExitDialogListener;
//    }
}

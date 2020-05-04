package com.yaroslavgorbach.counter;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class DeleteCounterDialog extends AppCompatDialogFragment {

  private DeleteDialogListener mListener;


    public interface DeleteDialogListener {

        void onDialogDeleteClick();

    }

    // Override the Fragment.onAttach() method to instantiate the DeleteDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        // Verify that the host activity implements the callback interface
        try {

            mListener = (DeleteDialogListener) context;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(getActivity().toString()
                    + " must implement NoticeDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle("Delete counter?")
                .setMessage("This action can't be undone")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        mListener.onDialogDeleteClick();

                    }
                })
                .setNegativeButton("Cancel", null);

        return builder.create();
    }
}

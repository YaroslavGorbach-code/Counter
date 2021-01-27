package com.yaroslavgorbachh.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.yaroslavgorbachh.counter.R;


public class DeleteCounterDialog extends AppCompatDialogFragment {

  private DeleteDialogListener mListener;


    public interface DeleteDialogListener {

        void onDialogDeleteClick();

    }

    // Override the Fragment.onAttach() method to instantiate the DeleteDialogListener
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {

            mListener = (DeleteDialogListener) context;

        } catch (ClassCastException e) {

            throw new ClassCastException(getActivity().toString()
                    + " must implement DeleteDialogListener");
        }
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(R.string.DeleteCounterDialogTitle)
                .setMessage(R.string.DeleteCounterDialogText)
                .setPositiveButton(R.string.DeleteCounterDialogPositiveButton, (dialog, which) -> mListener.onDialogDeleteClick())
                .setNegativeButton(R.string.DeleteCounterDialogNegativeButton, null);

        return builder.create();
    }
}

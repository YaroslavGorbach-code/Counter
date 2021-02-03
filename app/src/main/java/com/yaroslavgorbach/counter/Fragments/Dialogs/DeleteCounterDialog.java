package com.yaroslavgorbach.counter.Fragments.Dialogs;

import android.app.Dialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.yaroslavgorbach.counter.Database.Models.Counter;
import com.yaroslavgorbach.counter.R;

import java.util.List;


public class DeleteCounterDialog extends AppCompatDialogFragment {

  private DeleteDialogListener mListener;
  private int mCount;


    public interface DeleteDialogListener {

        void onDialogDeleteClick();

    }

   public DeleteCounterDialog(DeleteDialogListener listener, int count){
        mListener = listener;
        mCount = count;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        String title;

        if (mCount > 1){
            title = "Удалить счетчики?";
        }else {
            title = "Удалить счетчик?";
        }

        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext())
                .setTitle(title)
                // R.string.DeleteCounterDialogTitle
                .setMessage(R.string.DeleteCounterDialogText)
                .setPositiveButton(R.string.DeleteCounterDialogPositiveButton, (dialog, which) -> {
                    mListener.onDialogDeleteClick();
                })
                .setNegativeButton(R.string.DeleteCounterDialogNegativeButton, null);

        return builder.create();
    }
}

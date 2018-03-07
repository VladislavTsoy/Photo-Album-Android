package com.example.home.android89;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;

/**
 * Album Dialog Fragment Class
 *
 * @author Jane Chang
 * @author Vladislav Tsoy
 */

public class AlbumDialogFragment extends DialogFragment {
    public static final String MESSAGE_KEY = "message_key";

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(bundle.getString(MESSAGE_KEY)).setPositiveButton("OK", (dialog,id) -> {});

        return builder.create();
    }
}

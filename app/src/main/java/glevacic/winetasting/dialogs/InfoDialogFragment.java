package glevacic.winetasting.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bluejamesbond.text.DocumentView;

import glevacic.winetasting.R;

public class InfoDialogFragment extends DialogFragment {

    private InfoDialogFragment() {
        // empty constructor required for DialogFragment
    }

    public static InfoDialogFragment newInstance(String title, String message) {
        InfoDialogFragment fragment = new InfoDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("message", message);
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(final Bundle savedInstanceState) {

        Bundle bundle;
        if (savedInstanceState == null)
            bundle = getArguments();
        else
            bundle = savedInstanceState;

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_info, null);

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getActivity());
        dialogBuilder.setView(dialogView);
        dialogBuilder.setTitle(bundle.getString("title"));

        DocumentView dv = (DocumentView) dialogView.findViewById(R.id.dialog_info_dv);
        dv.setText(bundle.getString("message"));

        dialogBuilder.setCancelable(true);
        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            }
        });

        return dialogBuilder.create();
    }
}

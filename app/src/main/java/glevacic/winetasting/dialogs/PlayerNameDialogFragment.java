package glevacic.winetasting.dialogs;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import glevacic.winetasting.R;
import glevacic.winetasting.activities.MainActivity;

public class PlayerNameDialogFragment extends DialogFragment {

    public static final int ADD_NEW_PLAYER = -1;
    private Integer index;

    private PlayerNameDialogFragment() {
        // empty constructor required for DialogFragment
    }

    public static PlayerNameDialogFragment newInstance(String title, int index) {
        PlayerNameDialogFragment fragment = new PlayerNameDialogFragment();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putInt("index", index);
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

        String title = bundle.getString("title");
        index = bundle.getInt("index");

        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.dialog_new_player, null);

        final Builder dialogBuilder = new Builder(getActivity())
                                                .setView(dialogView)
                                                .setTitle(title);

        final EditText editText = (EditText) dialogView.findViewById(R.id.dialog_new_player_edt);
        final TextView textView = (TextView) dialogView.findViewById(R.id.dialog_new_player_tv_warning);

        dialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                if (index == ADD_NEW_PLAYER)
                    ((MainActivity) getActivity()).addNewPlayer(editText);
                else
                    ((MainActivity) getActivity()).renamePlayer(index, editText);
            }
        });

        dialogBuilder.setNegativeButton("Odustani", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        final android.app.AlertDialog dialog = dialogBuilder.create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                ((AlertDialog)dialog).getButton(AlertDialog.BUTTON_POSITIVE).setEnabled(false);
            }
        });

        editText.addTextChangedListener(createTextWatcher(editText, textView, dialog));
        return dialog;
    }

    private TextWatcher createTextWatcher(final EditText editText,
                                          final TextView textView,
                                          final AlertDialog dialog) {
        return new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                ((MainActivity) getActivity()).checkPlayerName(editText, textView, dialog);
            }
        };
    }
}
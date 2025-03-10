package com.app.wegatyou;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.DialogFragment;

public class TermsandConditionsFragment extends DialogFragment {
    // Flag to track if terms and conditions are accepted
    private boolean isTermsAccepted = false;
    private boolean isSecurityAccepted = false;

    private TermsAndConditionsDialogListener dialogListener;

    public TermsandConditionsFragment(TermsAndConditionsDialogListener listener) {
        dialogListener = listener;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Terms and Conditions")
                .setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Pellentesque aliquet urna nec urna tempor, non posuere dui luctus. " +
                        "Fusce tincidunt, neque eu elementum finibus, eros tortor lobortis dui, " +
                        "ac molestie libero arcu vel justo.")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform actions when the user accepts the terms and conditions
                        isTermsAccepted = true;
                        checkIfBothDialogsAccepted();
                        dialogListener.onTermsAndConditionsAccepted(true);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform actions when the user cancels or declines the terms and conditions
                        dialogListener.onTermsAndConditionsAccepted(false);
                    }
                });

        return builder.create();
    }

    private void checkIfBothDialogsAccepted() {
        if (isTermsAccepted && isSecurityAccepted) {
            // Enable the next ImageButton or perform any desired action
            Dialog dialog = getDialog();
            if (dialog != null) {
                ImageButton nextButton = dialog.findViewById(R.id.next);
                if (nextButton != null) {
                    nextButton.setEnabled(true);
                }
            }
        }
    }

    public interface TermsAndConditionsDialogListener {
        void onTermsAndConditionsAccepted(boolean isAccepted);
    }
}

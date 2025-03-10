package com.app.wegatyou;
import androidx.fragment.app.DialogFragment;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import androidx.appcompat.app.AlertDialog;

public class SecurityFragment extends DialogFragment {
    private SecurityDialogListener listener;

    // Flag to track if security terms are accepted
    private boolean isSecurityAccepted = false;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Security")
                .setMessage("Lorem ipsum dolor sit amet, consectetur adipiscing elit. " +
                        "Pellentesque aliquet urna nec urna tempor, non posuere dui luctus. " +
                        "Fusce tincidunt, neque eu elementum finibus, eros tortor lobortis dui, " +
                        "ac molestie libero arcu vel justo.")
                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform actions when the user accepts the security terms
                        isSecurityAccepted = true;
                        listener.onSecurityTermsAccepted(isSecurityAccepted);
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Perform actions when the user cancels or declines the security terms
                        listener.onSecurityTermsAccepted(isSecurityAccepted);
                    }
                });

        return builder.create();
    }

    public interface SecurityDialogListener {
        void onSecurityTermsAccepted(boolean isAccepted);
    }

    public void setSecurityDialogListener(SecurityDialogListener listener) {
        this.listener = listener;
    }
}

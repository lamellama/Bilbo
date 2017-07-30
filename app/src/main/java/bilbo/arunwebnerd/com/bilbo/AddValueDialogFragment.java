package bilbo.arunwebnerd.com.bilbo;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.app.Dialog;
import android.view.*;
import android.content.DialogInterface;
import android.widget.EditText;

public class AddValueDialogFragment extends DialogFragment {
	public interface AddClickListener{
		// Add value
		public void addValueClicked(float value);
	}

	private static AddClickListener listener;

	public static AddValueDialogFragment newInstance(String dataToShow , AddClickListener lis) {
		AddValueDialogFragment frag = new AddValueDialogFragment();
		//listener = (AddValueDialogFragment.AddClickListener) this;
		return frag;

	}


	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		AlertDialog.Builder builder;
		// Use the Builder class for convenient dialog construction
		builder = new AlertDialog.Builder(getActivity());

		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		builder.setView(inflater.inflate(R.layout.addvalue_popup, null));

		builder.setMessage(R.string.action_add_value);
		builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Add value
					EditText t = ((EditText) ((AlertDialog) dialog).findViewById(R.id.popAddInput));

					listener.addValueClicked(Float.parseFloat(t.getText().toString()));

				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
		//getContext().
		// Create the AlertDialog object and return it
		return builder.create();
	}
}

package bilbo.arunwebnerd.com.bilbo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.*;
import android.app.*;
import java.util.function.*;
import android.widget.AdapterView.*;

public class FirstFragment extends Fragment implements OnItemSelectedListener{

	OnInputUpdateListener mCallback;
	private int tip_percent;
	private float bill_total;
	private int num_people;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
		
		tip_percent = getResources().getInteger(R.integer.tip_default);
		bill_total = Float.parseFloat( getContext().getResources().getString(R.string.total_default));
		num_people = getContext().getResources().getInteger(R.integer.tip_default);

        EditText tvTotal = (EditText) v.findViewById(R.id.tvTotal);
        tvTotal.setText(getString(R.string.total_prompt));

        Spinner tvNumberPeople = (Spinner) v.findViewById(R.id.tvNumPeople);
		tvNumberPeople.setOnItemSelectedListener(this);
        //tvNumberPeople.setText(getString(R.string.people_prompt));

        Spinner spinTipPercent = (Spinner) v.findViewById(R.id.spinnerTipPercent);
		spinTipPercent.setOnItemSelectedListener(this);

        return v;
    }

	public void onItemSelected(AdapterView<?> parent, View view,
							   int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        // parent.getItemAtPosition(pos)
		// Send the event to the host activity
        mCallback.onInputUpdate(pos);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
	
	
    // Container Activity must implement this interface
    public interface OnInputUpdateListener {
        public void onInputUpdate(int pos);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        // This makes sure that the container activity has implemented
        // the callback interface. If not, it throws an exception
        try {
            mCallback = (OnInputUpdateListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
										 + " must implement OnInputUpdateListener");
        }
    }
	
    public static FirstFragment newInstance(String text) {

        FirstFragment f = new FirstFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);
		return f;
		}
	}	

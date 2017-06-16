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
import android.util.*;

public class FirstFragment extends Fragment implements OnItemSelectedListener{

	private static final String TAG = "FirstFragment";
	OnInputUpdateListener mCallback;
	private int tipPercent;
	private float billTotal;
	private int numPeople;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
		
		tipPercent = getResources().getInteger(R.integer.tip_default);
		billTotal = Float.parseFloat( getContext().getResources().getString(R.string.total_default));
		numPeople = getContext().getResources().getInteger(R.integer.numpeople_default);
		Log.d(TAG, "numPeople: " + numPeople);

        EditText tvTotal = (EditText) v.findViewById(R.id.tvTotal);
        tvTotal.setText(getContext().getResources().getString(R.string.total_default));
		
		//tvTotal.setText(getString(R.string.total_prompt));

        Spinner tvNumberPeople = (Spinner) v.findViewById(R.id.tvNumPeople);
		tvNumberPeople.setOnItemSelectedListener(this);
		tvNumberPeople.setSelection(getIndex(tvNumberPeople, numPeople));
		//tvNumberPeople.setSelection(4);
		
        //tvNumberPeople.setText(getString(R.string.people_prompt));

        Spinner spinTipPercent = (Spinner) v.findViewById(R.id.spinnerTipPercent);
		spinTipPercent.setOnItemSelectedListener(this);

        return v;
    }
	
	private int getIndex(Spinner spinner, int value){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
			Log.d(TAG, "spinner.getItemAtPosition(i): " + spinner.getItemAtPosition(i) + " + value: " + value);
            if (spinner.getItemAtPosition(i).equals(Integer.toString(value))){
                index = i;
				Log.d(TAG, "values equal");
            }
        }
        return index;
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

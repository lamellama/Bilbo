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

public class FirstFragment extends Fragment implements OnItemSelectedListener, OnClickListener
{
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

        final EditText tvTotal = (EditText) v.findViewById(R.id.tvTotal);
        tvTotal.setText(getContext().getResources().getString(R.string.total_default));
		tvTotal.setOnFocusChangeListener(new OnFocusChangeListener() {          
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						
						billTotal = Float.valueOf(tvTotal.getText().toString());
						Log.d(TAG, "EditText lost focus, tvTotal = " + billTotal);
						mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
					}
				}
			});

        Spinner tvNumberPeople = (Spinner) v.findViewById(R.id.tvNumPeople);
		tvNumberPeople.setSelection(getIndex(tvNumberPeople, numPeople));
		tvNumberPeople.setOnItemSelectedListener(this);
		/*tvNumberPeople.setOnItemSelectedListener(new OnItemSelectedListener() {
				@Override
				public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
					
					mCallback.onInputUpdate(position);
				}

				@Override
				public void onNothingSelected(AdapterView<?> parentView) {
					// your code here
				}

			});*/

        Spinner spinTipPercent = (Spinner) v.findViewById(R.id.spinnerTipPercent);
		spinTipPercent.setOnItemSelectedListener(this);
		
		//initialise variables in main activity
		mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
		
        return v;
    }
	
	private int getIndex(Spinner spinner, int value){

        int index = 0;

        for (int i=0;i<spinner.getCount();i++){
		//	Log.d(TAG, "spinner.getItemAtPosition(i): " + spinner.getItemAtPosition(i) + " + value: " + value);
            if (spinner.getItemAtPosition(i).equals(Integer.toString(value))){
                index = i;
				//Log.d(TAG, "values equal");
            }
        }
        return index;
	}
	
	@Override
	public void onClick(View p1)
	{
		switch (p1.getId()) {
			case R.id.tvNumPeople:
				
				break;
        }
		mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
	}
	
	public void onItemSelected(AdapterView<?> parent, View view,
							   int pos, long id) {
        // An item was selected. You can retrieve the selected item using
        //Spinner inputSpinner = (Spinner)parent.getItemAtPosition(pos);
		switch (parent.getId()) {
			case R.id.tvNumPeople:
				Log.d(TAG, "Spinner numPeople selected: " + parent.getItemAtPosition(pos));
				numPeople = Integer.parseInt(parent.getItemAtPosition(pos).toString());
				break;
			case R.id.spinnerTipPercent:
				Log.d(TAG, "Spinner Tip selected: " + parent.getItemAtPosition(pos));
				tipPercent = Integer.parseInt(parent.getItemAtPosition(pos).toString());
				break;
        }
		// Send the event to the host activity
        mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
    }

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
	
	public Bundle getInput(){
		Bundle inputBun = new Bundle();
		inputBun.putInt("tip", tipPercent);
		inputBun.putInt("people", numPeople);
		inputBun.putFloat("total", billTotal);
		return inputBun;
	}
	
    // Container Activity must implement this interface
    public interface OnInputUpdateListener {
        public void onInputUpdate(int numPeeps, int tip, float total);
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

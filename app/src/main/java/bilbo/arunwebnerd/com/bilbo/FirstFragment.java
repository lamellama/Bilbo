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
import android.text.method.*;
import android.view.*;
import android.text.*;

public class FirstFragment extends Fragment implements View.OnClickListener, SeekBar.OnSeekBarChangeListener
{
	private static final String TAG = "FirstFragment";
	private OnInputUpdateListener mCallback;
	private int tipPercent;
	private float billTotal;
	private int numPeople;
	
	private TextView tvNumPeopleSeekDisplay;
	private Button btnNumPeopleMinus;
	private Button btnNumPeoplePlus;
	private TextView tipSeekDisplay;
	private Button btnTipMinus;
	private Button btnTipPlus;
	private EditText tvTotal;
	
	private TextView tvTip;
	private TextView tvBill;
	private TextView tvBillTotal;
	private SeekBar sbNumberPeople;
	private SeekBar spinTipPercent;
	
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.first_frag, container, false);
		
		
		
		tipPercent = getResources().getInteger(R.integer.tip_default);
		billTotal = Float.parseFloat( getContext().getResources().getString(R.string.total_default));
		numPeople = getContext().getResources().getInteger(R.integer.numpeople_default);
		
		tvTip = (TextView) v.findViewById(R.id.tvPPTipDisplay);
		tvBill = (TextView) v.findViewById(R.id.tvPPBillDisplay);
		tvBillTotal = (TextView) v.findViewById(R.id.tvPPTotalDisplay);

        tvTotal = (EditText) v.findViewById(R.id.tvTotal);
        tvTotal.setText(getContext().getResources().getString(R.string.total_default));
		tvTotal.setOnFocusChangeListener(new View.OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (!hasFocus) {
						
						billTotal = Float.valueOf(tvTotal.getText().toString());
						Log.d(TAG, "EditText lost focus, tvTotal = " + billTotal);
						mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
						updateTotals();
					}
				}
			});
	
		tvTotal.addTextChangedListener(new TextWatcher() {
				public void afterTextChanged(Editable s){

				}
				public void  beforeTextChanged(CharSequence s, int start, int count, int after){
					// you can check for enter key here
				}
				public void  onTextChanged (CharSequence s, int start, int before,int count) {
					if(tvTotal.getText().length() > 0){
						billTotal = Float.valueOf(tvTotal.getText().toString());
					
						mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
						updateTotals();
					}
				} 
			});
					
					
		
	//	tvTotal.setFocusable(true);
		//tvTotal.setFocusableInTouchMode(true);

        sbNumberPeople = (SeekBar) v.findViewById(R.id.sbNumPeople);
		sbNumberPeople.setProgress(numPeople);
		tvNumPeopleSeekDisplay = (TextView) v.findViewById(R.id.numPeopleSeekbarDisplay);
		btnNumPeopleMinus = (Button) v.findViewById(R.id.btnNumPeepMinus);
		btnNumPeoplePlus = (Button) v.findViewById(R.id.btnNumPeepPlus);
		btnNumPeoplePlus.setOnClickListener(this);
		btnNumPeopleMinus.setOnClickListener(this);
		sbNumberPeople.setOnSeekBarChangeListener(this);
		
		
		
        spinTipPercent = (SeekBar) v.findViewById(R.id.sbTipPercent);
		tipSeekDisplay = (TextView) v.findViewById(R.id.tipSeekbarDisplay);
		btnTipMinus = (Button) v.findViewById(R.id.btnTipMinus);
		btnTipPlus = (Button) v.findViewById(R.id.btnTipPlus);
		btnTipPlus.setOnClickListener(this);
		btnTipMinus.setOnClickListener(this);
		spinTipPercent.setOnSeekBarChangeListener(this);
		
		//initialise variables in main activity
		mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
		
        return v;
    }
	
	
	@Override
	public void onClick(View p1)
	{
		switch (p1.getId()) {
			case R.id.btnNumPeepPlus:
				sbNumberPeople.setProgress(sbNumberPeople.getProgress() + 1);
				break;
				
			case R.id.btnNumPeepMinus:
				sbNumberPeople.setProgress(sbNumberPeople.getProgress() - 1);
				break;
			case R.id.btnTipPlus:
				spinTipPercent.setProgress(spinTipPercent.getProgress() + 1);
				break;

			case R.id.btnTipMinus:
				spinTipPercent.setProgress(spinTipPercent.getProgress() - 1);
				break;
        }
		//mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
	}

    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }
	
	private void updateTotals(){
		float total = (float) 0.0;
		if(tvTotal.getText().length() > 0)
			total = Float.parseFloat(tvTotal.getText().toString());
		if((total > 0) && (numPeople > 0))
			total /= numPeople;
		tvBill.setText(Float.toString(total) + "pp");
		tvTip.setText(Integer.toString(tipPercent) + "%");
		tvBillTotal.setText(Float.toString(total + ((total * tipPercent)/100)));
	}
	
	@Override
	public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
		// An item was selected. You can retrieve the selected item using
		//Spinner inputSpinner = (Spinner)parent.getItemAtPosition(pos);
		switch (seekBar.getId()) {
			case R.id.sbNumPeople:
				Log.d(TAG, "Seekbar numPeople selected: " + seekBar.getProgress());
				numPeople = seekBar.getProgress()+1;
				if(tvNumPeopleSeekDisplay != null)
					tvNumPeopleSeekDisplay.setText(Integer.toString(numPeople));
				break;
			case R.id.sbTipPercent:
				Log.d(TAG, "Spinner Tip selected: " + seekBar.getProgress());
				tipPercent = seekBar.getProgress();
				if(tipSeekDisplay != null)
					tipSeekDisplay.setText(Integer.toString(tipPercent));
				break;
				
		}
		updateTotals();
		// Send the event to the host activity
		mCallback.onInputUpdate(numPeople, tipPercent, billTotal);
	}

	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {

	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {

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

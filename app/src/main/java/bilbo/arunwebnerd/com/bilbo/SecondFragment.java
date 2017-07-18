package bilbo.arunwebnerd.com.bilbo;

import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.util.Log;
import android.support.v7.app.*;
import java.util.*;


public class SecondFragment extends Fragment implements CustomAdapter.ViewHolder.ClickListener
{
	
    private static final String TAG = "RecyclerViewFragment";

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
	protected ItemCalculator calculator;
	
	private int numPeople;
	private int tipPercent;
	private float billTotal;
	
	public void updateArgs(Bundle args){
		Log.d(TAG, "updateArgs");
		numPeople = args.getInt("people", 0);
		tipPercent = args.getInt("tip", 0);
		billTotal = args.getFloat("total", 0);
		
		init();
	}
	
	private void init(){
		initDataset();
		
		if(mAdapter!=null)
			mAdapter.updateDataset(calculator.getPPValueList());
		
	}
	private void updateAdapterData(){
		//pass new dataset to adapter
		if(mAdapter!=null)
			mAdapter.updateDataset(calculator.getPPValueList());
	}
	public void groupItems(){
		Log.d(TAG, "makeGroup()");
		if(calculator!=null)
			calculator.makeGroup(mAdapter.getSelectedItems());
		
		updateAdapterData();
	}

	public void unGroupItems(){
		if(calculator!=null)
			for(int i = 0; i < mAdapter.getSelectedGroupId().size(); i++)
				calculator.breakGroup(mAdapter.getSelectedGroupId().get(i));
				
		updateAdapterData();
	}
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
		numPeople = getArguments().getInt("people", 0);
		tipPercent = getArguments().getInt("tip", 0);
		billTotal = getArguments().getFloat("total", 0);
		//updateArgs(getNumPeople(), getTipPercent(), getBillTotal());
        // Initialize dataset
        initDataset();
    }
	
	//onClick passed from the adapter viewholder
	@Override
	public void onItemClicked(int position)
	{
		Log.d(TAG, "OnClick");
		mAdapter.toggleSelection(position);
	}
	
	
	private int getNumPeople() {
        if (numPeople > 0)
			return numPeople;
		else
			return 0;
    }
	
	private float getBillTotal() {
        if (billTotal > 0)
			return billTotal;
		else
			return 0;
    }
	
	private int getTipPercent() {
        if(tipPercent > 0)
			return tipPercent;
		else
			return 0;
    }

    public static SecondFragment newInstance(String text) {

        SecondFragment f = new SecondFragment();
        Bundle b = new Bundle();
        b.putString("msg", text);

        f.setArguments(b);
        return f;
    }
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.second_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvPeopleGroups);

        // LinearLayoutManager is used here, this will layout the elements in a similar fashion
        // to the way ListView would layout elements. The RecyclerView.LayoutManager defines how
        // elements are laid out.
        mLayoutManager = new LinearLayoutManager(getActivity());
		
        mRecyclerView.setLayoutManager(mLayoutManager);

        mAdapter = new CustomAdapter(calculator.getPPValueList(), this);
        // Set CustomAdapter as the adapter for RecyclerView.
        mRecyclerView.setAdapter(mAdapter);
        // END_INCLUDE(initializeRecyclerView)
        Log.d(TAG, "Testing Logging");
        return rootView;
    }


    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save currently selected layout manager.
      //  savedInstanceState.putSerializable(KEY_LAYOUT_MANAGER, mCurrentLayoutManagerType);
        super.onSaveInstanceState(savedInstanceState);
    }

    //Pass input to calculator and prepare results for adapter
    private void initDataset() {
		Log.d(TAG, "initDataset(): getNumPeople(): " + getNumPeople() + " getBillTotal(): " + getBillTotal());
		//Give the calculator the input
		calculator = new ItemCalculator(getNumPeople(), getBillTotal(), getTipPercent());
		
    }
}	

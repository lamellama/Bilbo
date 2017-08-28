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
import android.support.v7.view.ActionMode;
import android.view.*;
import android.support.v7.widget.*;
import android.widget.*;
import android.content.*;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.app.Activity;
import android.os.Handler;



public class SecondFragment extends Fragment implements CustomAdapter.ViewHolder.ClickListener
{

	

	
	
	
    private static final String TAG = "RecyclerViewFragment";

    protected RadioButton mLinearLayoutRadioButton;
    protected RadioButton mGridLayoutRadioButton;

    protected RecyclerView mRecyclerView;
    protected CustomAdapter mAdapter;
    protected RecyclerView.LayoutManager mLayoutManager;
	protected ItemCalculator calculator;
	
	protected static ActionMode mActionMode;
	
	private int numPeople;
	private int tipPercent;
	private float billTotal;
	
	boolean multiMenuStarted = false;
	boolean singleMenuStarted = false;
	boolean groupSelected = false;
	
	Menu addValuePopup;
	
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
		if(mAdapter!=null){
			mAdapter.updateDataset(calculator.getPPValueList());
			mAdapter.refreshAdapter();
			}
	}
	public void groupItems(){
		Log.d(TAG, "makeGroup()");
		if(calculator!=null)
			calculator.makeGroup(mAdapter.getSelectedRealIndex());
		
		updateAdapterData();
	}

	public void unGroupItems(){
		if(calculator!=null)
			for(int i = 0; i < mAdapter.getSelectedGroupId().size(); i++)
				calculator.breakGroup(mAdapter.getSelectedGroupId().get(i));
				
		updateAdapterData();
	}
	
	public void addValueToItems(float value){
		if(calculator!=null)
			for(int i = 0; i < mAdapter.getSelectedRealIndex().size(); i++)
				calculator.addExtraValue(mAdapter.getSelectedRealIndex().get(i), value);

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
		if(savedInstanceState == null){
        	initDataset();
			}
		else{
			ArrayList<PerPersonValue> list = savedInstanceState.getParcelableArrayList("peeps");
			calculator = new ItemCalculator(getNumPeople(), getBillTotal(), getTipPercent(), list);
			calculator.restoreState(savedInstanceState);
		}
			
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
	
	private View rootView;
	
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.second_frag, container, false);
        rootView.setTag(TAG);

        // BEGIN_INCLUDE(initializeRecyclerView)
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.rvPeopleGroups);

     
        mLayoutManager = new LinearLayoutManager(getActivity());
		
        mRecyclerView.setLayoutManager(mLayoutManager);
		
		//mRecyclerView.setChoiceMode
		
        mAdapter = new CustomAdapter(calculator.getPPValueList(), this, mActionMode);
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
	  	calculator.saveInstance(savedInstanceState);
        super.onSaveInstanceState(savedInstanceState);
    }

    //Pass input to calculator and prepare results for adapter
    private void initDataset() {
		Log.d(TAG, "initDataset(): getNumPeople(): " + getNumPeople() + " getBillTotal(): " + getBillTotal());
		//Give the calculator the input
		calculator = new ItemCalculator(getNumPeople(), getBillTotal(), getTipPercent());
		
    }
	
	//Add value popup box input
	public  void messageDialog(Activity a, String title, String message){
		AlertDialog.Builder dialog = new AlertDialog.Builder(a);
		dialog.setTitle(title);
		dialog.setMessage(message);
		LayoutInflater inflater = getActivity().getLayoutInflater();

		// Inflate and set the layout for the dialog
		// Pass null as the parent view because its going in the dialog layout
		dialog.setView(inflater.inflate(R.layout.addvalue_popup, null));
		dialog.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// Add value
					EditText t = ((EditText) ((AlertDialog) dialog).findViewById(R.id.popAddInput));
					addValueToItems(Float.parseFloat(t.getText().toString()));

				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int id) {
					// User cancelled the dialog
				}
			});
		dialog.create().show();     

	}
	
	
	
	//actionbar
	private ActionMode.Callback mActionModeCallback = new ActionMode.Callback() {

		// Called when the action mode is created; startActionMode() was called
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			// Inflate a menu resource providing context menu items
			MenuInflater inflater = mode.getMenuInflater();
			inflater.inflate(R.menu.action_menu, menu);
			MenuItem ungroupBtn = menu.findItem(R.id.action_ungroup);
			MenuItem groupBtn = menu.findItem(R.id.action_group);
			MenuItem addBtn = menu.findItem(R.id.action_add_value);
			//if(mAdapter.getSelectedItemCount() > 1){
				if((singleMenuStarted)&&(!groupSelected)){
					//start singleMenu
					//inflater.inflate(R.menu.action_single_select, menu);
					ungroupBtn.setVisible(false);
					groupBtn.setVisible(false);
					addBtn.setVisible(true);
				}
				else if((multiMenuStarted) && (groupSelected)){
					//start multiMenu
					//inflater.inflate(R.menu.action_multi_select, menu);
					ungroupBtn.setVisible(true);
					groupBtn.setVisible(true);
					addBtn.setVisible(false);
				}
				else if((singleMenuStarted)&&(groupSelected)){
					//start singleMenu
					//inflater.inflate(R.menu.action_single_select, menu);
					ungroupBtn.setVisible(true);
					groupBtn.setVisible(false);
					addBtn.setVisible(false);
					
				}
				else if((multiMenuStarted) && (!groupSelected)){
					//start multiMenu
					//inflater.inflate(R.menu.action_multi_select, menu);
					ungroupBtn.setVisible(true);
					groupBtn.setVisible(true);
					addBtn.setVisible(false);
				}
			
			
			return true;
		}

		// Called each time the action mode is shown. Always called after onCreateActionMode, but
		// may be called multiple times if the mode is invalidated.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
			switch (item.getItemId()) {
				case R.id.action_group:
					groupItems();
					mode.finish();
					return true;
				case R.id.action_ungroup:
					unGroupItems();
					mode.finish(); // Action picked, so close the CAB
					return true;
				case R.id.action_add_value:
					//AddValueDialogFragment.AddClickListener listener = null;
					//AddValueDialogFragment addValueDialog = AddValueDialogFragment.newInstance("Add Value", listener);
					messageDialog(getActivity(), "Add Value", "bl");
				/*	addValueDialog.builder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// Add value
								EditText t = ((EditText) ((AlertDialog) dialog).findViewById(R.id.popAddInput));
								addValueToItems(Float.parseFloat(t.getText().toString()));

							}
						})
						.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// User cancelled the dialog
							}
						});*/
					//addValueDialog.show(getFragmentManager(), "yesNoAlert");
					//TODO
			
					return true;
				default:
					return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			mActionMode = null;
		}
	};
	
	private int itemSelected =-1;
	
	//onClick passed from the adapter viewholder
	@Override
	public void onItemClicked(int position)
	{
		Log.d(TAG, "OnClick");
		mAdapter.toggleSelection(position);
		AppCompatActivity activity=(AppCompatActivity)getActivity();
		groupSelected = false;
		for(int i = 0; i < mAdapter.getSelectedItemCount(); i++){
			
			List<Integer> selectedItems = mAdapter.getSelectedRealIndex();
			if(calculator.isThisAGroup(selectedItems.get(i))){
				//Item is a group
				groupSelected = true;
			}
		}
		
		if(multiMenuStarted){
			if(mAdapter.getSelectedItemCount() < 2){
				//closeMulti
				//Start single menu
				multiMenuStarted = false;
				singleMenuStarted =true;
				if(mActionMode != null)
					mActionMode.finish();
				mActionMode = activity.startSupportActionMode(mActionModeCallback);
				
				
			}
		}
		else if(singleMenuStarted){
			if(mAdapter.getSelectedItemCount() > 1){
				//close single
				//Start multiMenu
				multiMenuStarted = true;
				singleMenuStarted =false;
				itemSelected = mAdapter.getSelectedGroupId().get(0);
				if(mActionMode != null)
					mActionMode.finish();
				mActionMode = activity.startSupportActionMode(mActionModeCallback);
				
			}
			if(mAdapter.getSelectedItemCount() < 1){
				//close singleMenu
				singleMenuStarted =false;
				if(mActionMode != null)
					mActionMode.finish();
				
			}
		}else if(mAdapter.getSelectedItemCount() > 0){
			singleMenuStarted =true;
			
			mActionMode = activity.startSupportActionMode(mActionModeCallback);
		}
		
        
        
	}
	
	Handler adapterHandler = new Handler();
	
	
	@Override
	public void onTextNameChanged(int position, String text)
	{
		calculator.setItemText(mAdapter.getItemRealIndex(position), text);
		
		//TODO innefficient
		if(mAdapter!=null){
			mAdapter.updateDataset(calculator.getPPValueList());
			
			mAdapter.postAndNotifyAdapter(adapterHandler, mRecyclerView, mAdapter);
			}
		
		
	}
	
	
}	



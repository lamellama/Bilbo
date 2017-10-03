/*
* Copyright (C) 2014 The Android Open Source Project
*
* Licensed under the Apache License, Version 2.0 (the "License");
* you may not use this file except in compliance with the License.
* You may obtain a copy of the License at
*
*      http://www.apache.org/licenses/LICENSE-2.0
*
* Unless required by applicable law or agreed to in writing, software
* distributed under the License is distributed on an "AS IS" BASIS,
* WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
* See the License for the specific language governing permissions and
* limitations under the License.
*/

package bilbo.arunwebnerd.com.bilbo;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.util.Log;
import java.util.*;
import android.widget.*;
import android.support.v7.view.ActionMode;
import android.view.MenuInflater;
//import android.view.*;
import android.text.TextWatcher;
import android.text.Editable;
import android.os.Handler;
import android.view.View.*;
import android.graphics.Color;

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends SelectableAdapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<PerPersonValue> mDataSet;
	private ViewHolder.ClickListener mClickListener;
	RecyclerView mRecyclerView;
	private ActionMode mActionMode;
	Handler mAdapterHandler;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, TextWatcher
	{	

		
		public interface ClickListener {
            public void onItemClicked(int position);
         //   public boolean onItemLongClicked(int position);
		 	public void onTextNameChanged(int position, String text);
        }

        public final TextView tvTotal;
		
		//public Button addButton;
		public TextView tvTip;
		public TextView tvAddedValue;
		public TextView tvPPTotal;
		public TextView tvItemNumber;
		public EditText etName;
		
		public boolean selected = true;
		
		// an array of selected items (Integer indices) 
		private ClickListener listener;
		//MyCustomEditTextListener textListener;
		
		View selectedOverlay;
		boolean etNameChanged = false;
		String etNameInput;
		//public boolean hasFocus = false;

        public ViewHolder(View v, ClickListener listener) {
            super(v);
			
			selectedOverlay = itemView.findViewById(R.id.selected_overlay);
			this.listener = listener;
			itemView.setOnClickListener(this);

            tvTotal = (TextView) v.findViewById(R.id.tvTotalB);
			tvPPTotal = (TextView) v.findViewById(R.id.tvPP);
			tvTip = (TextView) v.findViewById(R.id.tvTip);
			tvAddedValue = (TextView) v.findViewById(R.id.tvAV);
			tvItemNumber = (TextView) v.findViewById(R.id.tvItemNumber);
			etName = (EditText) v.findViewById(R.id.etName);
			
			//this.textListener = customTextListener;
			etName.addTextChangedListener(this);
			etName.setOnFocusChangeListener(new OnFocusChangeListener() {

					@Override
					public void onFocusChange(View v, boolean hasFocus) {
						if (!hasFocus) {
							validateInput(v);
						}
					}
				});
			//addButton = (Button) v.findViewById(R.id.addButton);
			
        }
		
		public void validateInput(View v){
			//View has lost focus, check text and send to interface
			if((etNameInput != null)&&(etNameChanged)){
				listener.onTextNameChanged(getPosition(),etNameInput);
				etNameChanged = false;
				}
		}
		
		@Override
		public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
			// no op
		}

		@Override
		public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
			etNameInput = charSequence.toString();
			etNameChanged = true;
			
			//   mDataSet.get(position).name = charSequence.toString();

		}

		@Override
		public void afterTextChanged(Editable editable) {
			
			// no op
		}
		
		
		
		@Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
			//	mActionMode = MainActivity.getInstance().startActionMode(mActionModeCallback);
			//	view.setSelected(true);
			//	return true;
            }
        }
		
		public TextView getPPTotaltv(){return tvPPTotal;}
		public TextView getTotaltv(){return tvTotal;}
		public EditText getEtName(){return etName;}
	//	public Button getAddButton(){return addButton;}
	
	
		
        
    }
	
	
	
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<PerPersonValue> dataSet, RecyclerView recyclerView, ViewHolder.ClickListener clickListener, ActionMode actionMode) {
        mDataSet = dataSet;
		this.mClickListener = clickListener;
		mActionMode = actionMode;
		mRecyclerView = recyclerView;
		mAdapterHandler = new Handler();
		
    }
	
	public void queueDatasetUpdate(List<PerPersonValue> data){
		mDataSet = data;
		clearSelection(); //TODO would be better to update selection
		postAndNotifyAdapter(mAdapterHandler, mRecyclerView, this);
	}
	
	public void updateDataset(List<PerPersonValue> data){
		mDataSet = data;
		clearSelection();
		notifyDataSetChanged();
	}
	
	boolean notifyIsRunning = false;
	
	
	//Recycler view does not like being updated while it is doing something so this is a work around
	//BUT RecyclerView also does not like being called from another thread in some instances "BUG" so use sparringly
	public void postAndNotifyAdapter(final Handler handler, final RecyclerView recyclerView, final RecyclerView.Adapter adapter) {
		if(!notifyIsRunning){
			notifyIsRunning = true;
        handler.post(new Runnable() {
				@Override
				public void run() {
					notifyIsRunning = false;
					if (!recyclerView.isComputingLayout()) {
						adapter.notifyDataSetChanged();
					} else {
						postAndNotifyAdapter(handler, recyclerView, adapter);
					}
				}
			});
		}
    }
	
	public List<Integer> getSelectedRealIndex (){
		List<Integer> selectedItems = getSelectedItems();
		List<Integer> selectedItemIndex = new ArrayList<Integer>();
		for(int i = 0; i < selectedItems.size(); i++){
			selectedItemIndex.add(getItemRealIndex(selectedItems.get(i)));
			}
		return selectedItemIndex;
	}
	
	public int getItemRealIndex (int position){
		int itemIndex;
		
		itemIndex = mDataSet.get(position).realIndex;
		//if(itemIndex == -1)
		//	itemIndex = position;
		
		return itemIndex;
	}
	
	public List<Integer> getSelectedGroupId(){
		List<Integer> groups = new ArrayList<Integer>();
		List<Integer> selectedItems = getSelectedItems();
		
		for(int i = 0; i < selectedItems.size(); i++){
			//for(int j = 0; j < groups.size(); j++){
			groups.add(mDataSet.get(selectedItems.get(i)).group);
			//}
		}
		return groups;
	}

    // BEGIN_INCLUDE(recyclerViewOnCreateViewHolder)
    // Create new views (invoked by the layout manager)
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
    //    Log.d(TAG, "Testing recyclerView item");
        // Create a new view.
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.text_row_item, viewGroup, false);
		
        return new ViewHolder(v, mClickListener);
    }
    	// END_INCLUDE(recyclerViewOnCreateViewHolder)
	boolean etNameEnabled = true;
    // BEGIN_INCLUDE(recyclerViewOnBindViewHolder)
    // Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        Log.d(TAG, "Element selected " + viewHolder.selected);
	//	viewHolder.itemView.setSelected(viewHolder.selected);
	//	viewHolder.itemView.setTag(position);
		// each time an item comes into view, its position is checked
		// against "selected" indices
		
		
		// Highlight the item if it's selected
        viewHolder.selectedOverlay.setVisibility(isSelected(position) ? View.VISIBLE : View.INVISIBLE);
		viewHolder.tvItemNumber.setText(Integer.toString(position));
        // Get element from your dataset at this position and replace the contents of the view
        // with that element,
		Log.d(TAG, "Dataset group: " + mDataSet.get(position).group );
		if(mDataSet.get(position).group < 0){
			
			//its a group
			Log.d(TAG, "Print group");
			
			viewHolder.etName.setText("Group " + mDataSet.get(position).name);
			viewHolder.tvTip.setText(Float.toString(mDataSet.get(position).tipPercent) + "%");
			//viewHolder.etName.setText(mDataSet.get(position).name);
			viewHolder.etName.setFocusable(false);
			viewHolder.etName.setEnabled(false);
			viewHolder.etName.setCursorVisible(false);
			//viewHolder.etName.setKeyListener(null);
			etNameEnabled = false;
			viewHolder.etName.setBackgroundColor(Color.TRANSPARENT);
		}
		else {
			// its an individual
			viewHolder.tvTip.setText( Float.toString(mDataSet.get(position).tipPercent) + "%");
			viewHolder.tvAddedValue.setText(Float.toString(mDataSet.get(position).addedExtra));
			etNameEnabled = true;
			viewHolder.tvPPTotal.setText(Float.toString(mDataSet.get(position).bill));
			viewHolder.tvTotal.setText(Float.toString(mDataSet.get(position).getTotal() ));
		   	viewHolder.etName.removeTextChangedListener(viewHolder);
			viewHolder.etName.setText(mDataSet.get(position).name);
			viewHolder.etName.addTextChangedListener(viewHolder);
			viewHolder.etName.setFocusable(true);
			viewHolder.etName.setEnabled(true);
			viewHolder.etName.setCursorVisible(true);
			//viewHolder.etName.setKeyListener(null);//TODO
		   }
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
	
	@Override
	public int getItemViewType(int position) {
		return position;
	}    
	
	
}

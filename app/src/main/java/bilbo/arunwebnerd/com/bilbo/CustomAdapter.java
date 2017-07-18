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

/**
 * Provide views to RecyclerView with data from mDataSet.
 */
public class CustomAdapter extends SelectableAdapter<CustomAdapter.ViewHolder> {
    private static final String TAG = "CustomAdapter";

    private List<PerPersonValue> mDataSet;
	private ViewHolder.ClickListener clickListener;

    // BEGIN_INCLUDE(recyclerViewSampleViewHolder)
    /**
     * Provide a reference to the type of views that you are using (custom ViewHolder)
     */
    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener
	{

		
		public interface ClickListener {
            public void onItemClicked(int position);
         //   public boolean onItemLongClicked(int position);
        }

        private final TextView textView;
		public boolean selected = true;
		
		// an array of selected items (Integer indices) 
		private ClickListener listener;
		
		RecyclerView mRecyclerView; 
		View selectedOverlay;

        public ViewHolder(View v, ClickListener listener) {
            super(v);
			selectedOverlay = itemView.findViewById(R.id.selected_overlay);
			this.listener = listener;
			itemView.setOnClickListener(this);

            textView = (TextView) v.findViewById(R.id.textView);
        }
		
		@Override
        public void onClick(View v) {
            if (listener != null) {
                listener.onItemClicked(getPosition());
            }
        }

        public TextView getTextView() {
            return textView;
        }
    }
    // END_INCLUDE(recyclerViewSampleViewHolder)

    /**
     * Initialize the dataset of the Adapter.
     *
     * @param dataSet String[] containing the data to populate views to be used by RecyclerView.
     */
    public CustomAdapter(List<PerPersonValue> dataSet, ViewHolder.ClickListener clickListener) {
        mDataSet = dataSet;
		this.clickListener = clickListener;
    }
	
	public void updateDataset(List<PerPersonValue> data){
		mDataSet = data;
		clearSelection();
		notifyDataSetChanged();
		
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
		
        return new ViewHolder(v, clickListener);
    }
    // END_INCLUDE(recyclerViewOnCreateViewHolder)

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
        // Get element from your dataset at this position and replace the contents of the view
        // with that element,
		Log.d(TAG, "Dataset group: " + mDataSet.get(position).group );
		if(mDataSet.get(position).group > 0){
			//its a group
			Log.d(TAG, "Print group");
			viewHolder.getTextView().setText("Group");
		}
		else
     	   viewHolder.getTextView().setText("Blah position: " + position);
    }
    // END_INCLUDE(recyclerViewOnBindViewHolder)

    // Return the size of your dataset (invoked by the layout manager)
    @Override
    public int getItemCount() {
        return mDataSet.size();
    }
}

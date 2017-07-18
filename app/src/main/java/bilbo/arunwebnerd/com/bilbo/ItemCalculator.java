package bilbo.arunwebnerd.com.bilbo;
import java.util.*;
import org.apache.http.conn.ssl.*;
import android.util.*;

public class ItemCalculator
{
	private final static String TAG = "ItemCalculator";
	private PerPersonValue[] ppValues;
	private Map<Integer, List<Integer>> groupIndexMap;
	//private List<Integer> groups;
	private float totalExtraValue =0;
	private int numPeople;
	private float billTotal;
	private int tipPercent;
	//private int itemsInGroup = 0;
	private int uniqueIndex = 1;
	
	public ItemCalculator(int numPeeps, float bill, int tip){
		numPeople = numPeeps;
		billTotal = bill;
		tipPercent = tip;
		ppValues = new PerPersonValue[numPeople];
		for(int i = 0; i < numPeople; i++)
			ppValues[i] = new PerPersonValue();
		calculatePerPersonValues();
		groupIndexMap = new HashMap <Integer, List<Integer>>();
		//groups = new ArrayList<Integer>();
		//initialiseGroups();
	}
	
	private void addToGroup(List<Integer> items, int group){
		//Integer[] newGroup = new Integer[items.size()];
		//Log.d(TAG, "Add to group ");
		List<Integer> groupList = groupIndexMap.get(group);
		for(int i = 0; i < groupList.size(); i++){
			Log.d(TAG, "groupList before: " + groupList.get(i));
		}
		
		for(Integer i = 0; i < items.size(); i++){
			
			if(!groupList.contains(items.get(i))){
				groupList.add(items.get(i));
				ppValues[items.get(i)].group = group;
				Log.d(TAG, "addToGroup()" + group + ": " + items.get(i));
				}
			
		}
		for(int i = 0; i < groupList.size(); i++){
			Log.d(TAG, "groupList after: " + groupIndexMap.get(group).get(i));
		}
			
	}
	private List<Integer> getRealIndex(List<Integer> items){
		List<Integer> newList = new ArrayList<Integer>();
		for(int j = 0; j < items.size(); j++){
			
			int index = items.get(j) - groupIndexMap.size();
			for(int i = 0; i<ppValues.length && i < (items.get(j) + groupIndexMap.size()); i++){
				if(ppValues[i].group > 0)
					index++;
			}
			newList.add(index);
		}
		return newList;
	}
	
	private int getRealIndex(int displayIndex){
		int index = displayIndex + groupIndexMap.size();
		for(int i = 0; i<ppValues.length && i < (displayIndex + groupIndexMap.size()); i++){
			if(ppValues[i].group > 0)
				index--;
		}
		return index;
	}
	
	public void breakGroup(int groupIndex){
		Log.d(TAG, "remove group: " + groupIndex);
		if(groupIndexMap.containsKey(groupIndex)){
			Log.d(TAG, "destroy group ");
			for(int i = 0; i < groupIndexMap.get(groupIndex).size(); i ++){
			//	if((groupIndexMap.get(groupIndex).get(i) < ppValues.length)&&(groupIndexMap.get(groupIndex).get(i) >=0))
				ppValues[groupIndexMap.get(groupIndex).get(i)].group = 0;
				}
				
			groupIndexMap.remove(groupIndex);
		
		}
		//if(groups.contains(groupIndex)){groups.remove(Integer.valueOf(groupIndex));}
		
	}
	
	public void makeGroup(List<Integer> items){
		List<Integer> itemsCopy = getRealIndex(items);
		for(int x =0; x<itemsCopy.size(); x++)
			Log.d(TAG, "Makegroup(): " + itemsCopy.get(x));
		for( int i =0; i< itemsCopy.size(); i++){
			if(ppValues[itemsCopy.get(i)].group > 0){
				//this item is already in a group
				addToGroup(itemsCopy, ppValues[itemsCopy.get(i)].group);
				return;
			}
		}
		//items not already in groups, so male a new one
		//numGroups++;
		int group = uniqueIndex;
		uniqueIndex++;
		//groups.add(group);
		//Add list to 
		groupIndexMap.put(group, itemsCopy);
		//Set thier group value
		Log.d(TAG, "make new group: " + group);
		for(int i = 0; i < itemsCopy.size(); i ++)
			if((items.get(i) < ppValues.length)&&(itemsCopy.get(i) >=0)){
				ppValues[itemsCopy.get(i)].group = group;
				Log.d(TAG, "item " + itemsCopy.get(i) + " group set to: " + group);
				}
			
	}
	
	private void calculatePerPersonValues(){
		
		float perPerson = (billTotal - totalExtraValue) / numPeople;
		for(int i =0; i<ppValues.length; i++){
			ppValues[i].bill = perPerson;
		}
		
	}
	
	public void addExtraValue(int index, float val){
		if((index >= 0)&&(index < ppValues.length)){
			ppValues[index].addedExtra+=val;
			totalExtraValue+=val;
			calculatePerPersonValues();
		}
		
	}
	
	public List<PerPersonValue> getPPValueList(){
	
		int i;
		List<PerPersonValue> dataSet = new ArrayList<PerPersonValue>();
		
		//Combine groups imto single PP
		for (Map.Entry<Integer, List <Integer>> entry : groupIndexMap.entrySet()) {
			Integer groupKey = entry.getKey();
			List<Integer> groupList = entry.getValue();
			
			PerPersonValue grouped = new PerPersonValue();
			grouped.group = groupKey;
			for(int k =0; k< groupList.size(); k++){
				grouped.addedExtra += ppValues[groupList.get(k)].addedExtra;
				grouped.bill += ppValues[groupList.get(k)].bill;
				//TODO grouped. += ppValues[tempList.get(k)].addedExtra;

			}
			Log.d(TAG, "Add group to dataset");
			dataSet.add(grouped);

		}
		//int currentGroup = 0;
		/*for(i = 0; i<groups.size(); i++){
			//for(Iterator j = new Set() : groupIndexMap)
			if(groupIndexMap.containsKey(groups.get(i))){
				List<Integer> tempList;// = new ArrayList<Integer>();
				tempList = groupIndexMap.get(groups.get(i));
				//Combine each group and add it to the list
				PerPersonValue grouped = new PerPersonValue();
				grouped.group = ppValues[tempList.get(0)].group;
				for(int k =0; k< tempList.size(); k++){
					grouped.addedExtra += ppValues[tempList.get(k)].addedExtra;
					grouped.bill += ppValues[tempList.get(k)].bill;
					//TODO grouped. += ppValues[tempList.get(k)].addedExtra;
					
				}
				Log.d(TAG, "Add group to dataset");
				dataSet.add(grouped);
			}
		}*/
		//Add individuals
		for(i = 0; i < ppValues.length; i++){
			if(ppValues[i].group == 0)
				dataSet.add(ppValues[i]);
		}
		return dataSet;
	}
}

package bilbo.arunwebnerd.com.bilbo;
import java.util.*;
import org.apache.http.conn.ssl.*;

import android.os.Bundle;
import android.util.*;

public class ItemCalculator
{
	private final static String TAG = "ItemCalculator";
	List<Integer> groupKeys = new ArrayList<Integer>();
	//private PerPersonValue[] ppValues;
	private ArrayList<PerPersonValue> ppValues;
	private HashMap<Integer, List<Integer>> groupIndexMap;
	//private List<Integer> groups;
	private float totalExtraValue =0;
	private int numPeople;
	private float billTotal;
	private int tipPercent;
	//private int itemsInGroup = 0;
	private int uniqueIndex = 1;

	public void saveInstance(Bundle storageBundle){

		storageBundle.putParcelableArrayList("peeps", ppValues);
		storageBundle.putSerializable("map", groupIndexMap);
	}
	
	public void restoreState(Bundle storageBundle){
		groupIndexMap = (HashMap<Integer, List<Integer>>)storageBundle.getSerializable("map");
	}
	
	public boolean isThisAGroup(int g){
		if(g < groupIndexMap.size())
			return true;
		return false;
	}
	
	public ItemCalculator(int numPeeps, float bill, int tip){
		numPeople = numPeeps;
		billTotal = bill;
		tipPercent = tip;
		//ppValues = new PerPersonValue[numPeople];
		ppValues = new ArrayList<PerPersonValue>();
		//for(int i = 0; i < numPeople; i++)
		//	ppValues[i] = new PerPersonValue();
		initPPValueList();
		calculatePerPersonValues();
		groupIndexMap = new HashMap <Integer, List<Integer>>();
		//groups = new ArrayList<Integer>();
		//initialiseGroups();
	}
	
	public ItemCalculator(int numPeeps, float bill, int tip, ArrayList<PerPersonValue> ppList){
		numPeople = numPeeps;
		billTotal = bill;
		tipPercent = tip;
		//ppValues = new PerPersonValue[numPeople];
		//ppValues = new ArrayList<PerPersonValue>();
		ppValues = ppList;
		//for(int i = 0; i < numPeople; i++)
		//	ppValues[i] = new PerPersonValue();
		//initPPValueList();
		//calculatePerPersonValues();
		groupIndexMap = new HashMap <Integer, List<Integer>>();
		//groups = new ArrayList<Integer>();
		//initialiseGroups();
	}

	private void initPPValueList(){
		for(int i = 0; i < numPeople; i++)
			ppValues.add(new PerPersonValue(0, 0, 0));
	}
	
	private void addToGroup(List<Integer> items, int group){
		List<Integer> groupList = groupIndexMap.get(group);
		
		for(Integer i = 0; i < items.size(); i++){
			
			if(!groupList.contains(items.get(i))){
				groupList.add(items.get(i));
				ppValues.get(items.get(i)).group = group;
				Log.d(TAG, "addToGroup()" + group + ": " + items.get(i));
				}
			
		}
		for(int i = 0; i < groupList.size(); i++){
			Log.d(TAG, "groupList after: " + groupIndexMap.get(group).get(i));
		}
			
	}
	
	//Find real dataset position from adapter position
	private List<Integer> getRealIndex(List<Integer> items){
		
		List<Integer> newList = new ArrayList<Integer>();
		for(int j = 0; j < items.size(); j++){
			
			int index = items.get(j) - groupIndexMap.size();
			for(int i = 0; i<ppValues.size() && i < (items.get(j) - groupIndexMap.size()); i++){
				if(ppValues.get(i).group > 0)
					index++;
			}
			newList.add(index);
		}
		
		for(int i = 0; i < items.size(); i++){
			Log.d(TAG, "display Index: " + items.get(i) + " realIndex: " + newList.get(i));
		}
		return newList;
	}
	
	private int getRealIndex(int displayIndex){
		int index = displayIndex + groupIndexMap.size();
		for(int i = 0; i<ppValues.size() && i < (displayIndex + groupIndexMap.size()); i++){
			if(ppValues.get(i).group > 0)
				index--;
		}
		Log.d(TAG, "display Index: " + displayIndex + " realIndex: " + index);
		return index;
	}
	
	//Takes a list of group index
	//Merges multiple groups into one and 
	//returns new group index
	public int mergeGroups(List<Integer> groups){
		if(groups.size() > 1)
			if(groupIndexMap.containsKey(groups.get(0)))
			for(int i = 1; i < groups.size(); i ++){
				//Merge two lists
				groupIndexMap.get(groups.get(0)).addAll(groupIndexMap.get(groups.get(i)));
				
				//Switch their group value
				for(int j = 0; j < groupIndexMap.get(groups.get(i)).size(); j++)
					ppValues.get(groupIndexMap.get(groups.get(i)).get(j)).group = groups.get(0);
				
				//delete other group lists
				groupIndexMap.remove(groups.get(i));
				groups.remove(i);
			}
		return groups.get(0);
	}
	
	public void breakGroup(int groupIndex){
		Log.d(TAG, "remove group: " + groupIndex);
		if(groupIndexMap.containsKey(groupIndex)){
			Log.d(TAG, "destroy group ");
			for(int i = 0; i < groupIndexMap.get(groupIndex).size(); i ++){
			//	if((groupIndexMap.get(groupIndex).get(i) < ppValues.length)&&(groupIndexMap.get(groupIndex).get(i) >=0))
				ppValues.get(groupIndexMap.get(groupIndex).get(i)).group = 0;
				}
				
			groupIndexMap.remove(groupIndex);
		
		}
	}
	
	//This is where the meat is
	public void makeGroup(List<Integer> items){
		List<Integer> groups = new ArrayList<Integer>();
		
		//Seperate groups and individuals lists
		for(int z = 0; z < items.size();){
			if(items.get(z) < groupIndexMap.size()){
				//item is a group
				groups.add(groupKeys.get(items.get(z)));
				items.remove(z);
			}else{z++;}
		}
		
		if(groups.size() > 1){ //Multiple groups
			//Merge groups
			mergeGroups(groups);
			
		}
		
		//Get individual dataset indexes
		List<Integer> itemsCopy = getRealIndex(items);
		
		//Check there is anything else to merge
		if(itemsCopy.size() < 1)
			return;

		//One group in list, add all individuals to this group
		if((groups.size() == 1)&&(itemsCopy.size() > 0)){
			addToGroup(itemsCopy, groups.get(0));
			return;
		}
			
		//items not already in groups, so make a new one
		int group = Integer.valueOf(uniqueIndex);
		uniqueIndex++;
		//Add list to map
		groupIndexMap.put(group, itemsCopy);
		//Set persons group value
		Log.d(TAG, "make new group: " + group);
		for(int i = 0; i < itemsCopy.size(); i ++)
			if((items.get(i) < ppValues.size())&&(itemsCopy.get(i) >=0)){
				ppValues.get(itemsCopy.get(i)).group = group;
				Log.d(TAG, "item " + itemsCopy.get(i) + " group set to: " + group);
				}
			
	}
	
	private void calculatePerPersonValues(){
		float totalLeft = 0;
		float perPerson = 0;
		if(billTotal > totalExtraValue)
			totalLeft = billTotal - totalExtraValue;
		if((totalLeft > 0)&&(ppValues.size() > 0))
			perPerson =  totalLeft / ppValues.size();
		for(int i =0; i<ppValues.size(); i++){
			ppValues.get(i).bill = ppValues.get(i).addedExtra + perPerson;
		}
		
	}
	
	public void addExtraValue(int index, float val){
		if((index >= 0)&&(index < ppValues.size())){
			ppValues.get(index).addedExtra+=val;
			totalExtraValue+=val;
			calculatePerPersonValues();
		}
		
	}
	
	
	
	public List<PerPersonValue> getPPValueList(){
	
		int i;
		List<PerPersonValue> dataSet = new ArrayList<PerPersonValue>();
		groupKeys.clear();
		
		//Combine groups imto single PP
		for (Map.Entry<Integer, List <Integer>> entry : groupIndexMap.entrySet()) {
			Integer groupKey = entry.getKey();
			List<Integer> groupList = entry.getValue();
			
			PerPersonValue grouped = new PerPersonValue(0, 0, 0);
			grouped.group = groupKey;
			
			for(int k =0; k< groupList.size(); k++){
				grouped.addedExtra += ppValues.get(groupList.get(k)).addedExtra;
				grouped.bill += ppValues.get(groupList.get(k)).bill;
				//TODO grouped. += ppValues[tempList.get(k)].addedExtra;

			}
			Log.d(TAG, "Add group to dataset");
			groupKeys.add(groupKey);
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
		for(i = 0; i < ppValues.size(); i++){
			if(ppValues.get(i).group == 0)
				dataSet.add(ppValues.get(i));
		}
		return dataSet;
	}
}

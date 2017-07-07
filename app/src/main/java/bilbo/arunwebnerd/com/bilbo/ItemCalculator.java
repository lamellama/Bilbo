package bilbo.arunwebnerd.com.bilbo;
import java.util.*;
import org.apache.http.conn.ssl.*;
import android.util.*;

public class ItemCalculator
{
	private final static String TAG = "ItemCalculator";
	private PerPersonValue[] ppValues;
	private Map<Integer, List<Integer>> groupIndexMap;
	private List<Integer> groups;
	private float totalExtraValue =0;
	private int numPeople;
	private float billTotal;
	private int tipPercent;
	private int itemsInGroup = 0;
	private int numGroups = 0;
	
	public ItemCalculator(int numPeeps, float bill, int tip){
		numPeople = numPeeps;
		billTotal = bill;
		tipPercent = tip;
		ppValues = new PerPersonValue[numPeople];
		for(int i = 0; i < numPeople; i++)
			ppValues[i] = new PerPersonValue();
		calculatePerPersonValues();
		groupIndexMap = new HashMap <Integer, List<Integer>>();
		groups = new ArrayList<Integer>();
		//initialiseGroups();
	}
	
	private void addToGroup(List<Integer> items, int group){
		//Integer[] newGroup = new Integer[items.size()];
		List<Integer> groupList = groupIndexMap.get(group);
		for(Integer i = 0; i < items.size(); i++){
			if(!groupList.contains(items.get(i)))
				groupList.add(items.get(i));
			
			
		}
		
	}
	
	public void breakGroup(int groupIndex){
		if(groupIndexMap.containsKey(groupIndex)){
			Log.d(TAG, "destroy group ");
			for(int i = 0; i < groupIndexMap.get(groupIndex).size(); i ++)
			//	if((groupIndexMap.get(groupIndex).get(i) < ppValues.length)&&(groupIndexMap.get(groupIndex).get(i) >=0))
				ppValues[groupIndexMap.get(groupIndex).get(i)].group = 0;
				
			groupIndexMap.remove(groupIndex);
		}
	}
	
	public void makeGroup(List<Integer> items){
		for( int i =0; i< items.size(); i++){
			if(ppValues[items.get(i)].group > 0){
				//this item is already in a group
				addToGroup(items, ppValues[items.get(i)].group);
				return;
			}
		}
		//items not already in groups, so male a new one
		numGroups++;
		groups.add(numGroups);
		//Add list to 
		groupIndexMap.put(numGroups, items);
		//Set thier group value
		Log.d(TAG, "make new group ");
		for(int i = 0; i < items.size(); i ++)
			if((items.get(i) < ppValues.length)&&(items.get(i) >=0))
				ppValues[items.get(i)].group = numGroups;
			
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
		for(i = 0; i < ppValues.length; i++){
			if(ppValues[i].group == 0)
				dataSet.add(ppValues[i]);
		}
		//int currentGroup = 0;
		for(i = 0; i<groups.size(); i++){
			//for(Iterator j = new Set() : groupIndexMap)
			if(groupIndexMap.containsKey(groups.get(i))){
				List<Integer> tempList;// = new ArrayList<Integer>();
				tempList = groupIndexMap.get(groups.get(i));
				//Combine each group and add it to the list
				PerPersonValue grouped = new PerPersonValue();
				for(int k =0; k< tempList.size(); k++){
					grouped.addedExtra += ppValues[tempList.get(k)].addedExtra;
					grouped.bill += ppValues[tempList.get(k)].bill;
					//TODO grouped. += ppValues[tempList.get(k)].addedExtra;
					
				}
				dataSet.add(grouped);
			}
		}
		return dataSet;
	}
}

package bilbo.arunwebnerd.com.bilbo;
import java.util.*;
import org.apache.http.conn.ssl.*;
import android.util.*;

public class ItemCalculator
{
	private final static String TAG = "ItemCalculator";
	private PerPersonValue[] ppValues;
	private Map<Integer, List<Integer>> groupIndexMap;
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
		calculatePerPersonValues();
		groupIndexMap = new HashMap <Integer, List<Integer>>();
		//initialiseGroups();
	}
	
	private void addToGroup(Integer[] items, int group){
		Integer[] newGroup = new Integer[items.length];
		
		groupIndexList.add
	}
	
	public void makeGroup(Integer[] items){
		for( int i =0; i< items.length; i++){
			if(ppValues[items[i]].group > 0){
				//this item is already in a group
				addToGroup(items, ppValues[items[i]].group);
				return;
			}
		}
		//items not already in groups, so male a new one
		numGroups++;
		//Add list to 
		groupIndexList.add(items);
			
		Log.d(TAG, "make new group ");
		for(int i = 0; i < items.length; i ++)
			if((items[i] < ppValues.length)&&(items[i] >=0))
				ppValues[items[i]].group = numGroups;
			
			
		
		
		
	/*	groupIndexList.remove(groupList.get(0));
		groupIndexList.add(groupList.get(0), groupList);
		//new group inserted, remove duplicates
		for(int i =0;i < groupList.size(); i++){
			for(int j =0; j < groupIndexList.size(); j++){
				if( j!=groupList.get(0)){
					for(int it: groupIndexList.get(j)){
						if(it == groupList.get(i)){

							//found a group with a duplicate value
							Log.d(TAG, "found a duplicate, deleting");
							groupIndexList.get(j).remove(it);//??TODO does thhis work
						}
					}
				}
			}
		}*/
	}
	
	private void calculatePerPersonValues(){
		
		float perPerson = (billTotal - totalExtraValue) / numPeople;
		for(int i =0; i<numPeople; i++){
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
	
	public PerPersonValue[] getPPValueList(){
		int itemCount = 0;
		int i = 0;
		for(i = 0; i < ppValues.length; i++){
			if(ppValues[i].group == 0){
				itemCount++;
			}
		}
		itemCount+=itemsInGroup;
		PerPersonValue[] dataSet = new PerPersonValue[itemCount];
		int j = 0;//this is used in the next loop also
		for(i = 0; i < ppValues.length; i++){
			if(ppValues[i].group == 0){
				dataSet[j] = ppValues[i];
				j++;
				
			}
		}
		//add the groups as one item
		for(i = 0; i < groupIndexList.size(); i ++){
			dataSet[j] = ppValues[groupIndexList.get(i)[i]];//TODO
		} 
		return dataSet;
	}
}

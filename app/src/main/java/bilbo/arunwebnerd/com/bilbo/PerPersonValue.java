package bilbo.arunwebnerd.com.bilbo;

import android.os.Parcel;
import android.os.Parcelable;
import android.content.Context;

public class PerPersonValue implements Parcelable
{
	private float addedExtra = 0;
	private float bill = 0;
	public int tipPercent = 0;
	//public String label;
	public int group = 0;
	public String name;
	public int realIndex = -1; //No need to parcel

	public PerPersonValue( float add, float bil, int gro){
		addedExtra = add;
		bill = bil;
		group = gro;
		//name = "Name";
	}

	public void setBill(float bill)
	{
		this.bill = bill;
	}
	
	public float getBillPlusExtras(){
		return bill + addedExtra;
	}

	public float getBill()
	{
		return bill;
	}
	
	/*public void setAddedExtra(float extra){
		if(extra < (-bill)){
			addedExtra = -bill;
			return;
		}
		addedExtra = extra;
	}
	*/
	//Returns amount addedExtra changed by because it may not be the same as the amount input
	public float addExtra(float extra){
		if((addedExtra + extra) < (-bill)){
			addedExtra = -bill;
			return -bill;
		}
		addedExtra += extra;
		return extra;
	}
	
	public float getAddedExtra(){return addedExtra;}
	
	public float getTotal(){
	/*	float basic = bill + addedExtra;
		if(basic!=0)
			basic += (basic/100) * tipPercent;
		return basic;*/
		float total = getBillPlusExtras();
		if(total == 0)
			return total;
		return total + (total/100)*tipPercent;
	}

	protected PerPersonValue(Parcel in) {
		addedExtra = in.readFloat();
		bill = in.readFloat();
		group = in.readInt();
		name = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeFloat(addedExtra);
		dest.writeFloat(bill);
		dest.writeInt(group);
		dest.writeString(name);
	}

	@SuppressWarnings("unused")
	public static final Parcelable.Creator<PerPersonValue> CREATOR = new Parcelable.Creator<PerPersonValue>() {
		@Override
		public PerPersonValue createFromParcel(Parcel in) {
			return new PerPersonValue(in);
		}

		@Override
		public PerPersonValue[] newArray(int size) {
			return new PerPersonValue[size];
		}
	};
}

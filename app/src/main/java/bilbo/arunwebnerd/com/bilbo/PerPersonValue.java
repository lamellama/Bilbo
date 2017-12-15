package bilbo.arunwebnerd.com.bilbo;

import android.os.Parcel;
import android.os.Parcelable;

import android.content.Context;
import java.math.BigDecimal;
//import android.util.Log;
import java.math.RoundingMode;


public class PerPersonValue implements Parcelable
{
	private static int decimalPlaces = 20;
	public static int formattedDecimalPlaces = 2;
	private BigDecimal addedExtra = new BigDecimal(0);
	private BigDecimal bill = new BigDecimal(0);
	public int tipPercent = 0;
	public int group = 0;
	public String name;
	public int realIndex = -1; //No need to parcel

	public PerPersonValue( BigDecimal add, BigDecimal bil, int gro){
		addedExtra = add;
		bill = bil;
		group = gro;
	}
	
	public PerPersonValue( ){}

	public BigDecimal getTipTotal(){
		if(!getBillPlusExtras().equals(0))
			return getBillPlusExtras().divide(new BigDecimal(100), decimalPlaces, RoundingMode.HALF_UP).multiply(new BigDecimal(tipPercent));
		return new BigDecimal(0.0);
	}

	public void setBill(BigDecimal bill)
	{
		this.bill = bill;
	}
	
	public BigDecimal getBillPlusExtras(){
		return bill.add( addedExtra);
	}

	public BigDecimal getBill()
	{
		return bill;
	}
	
	//Returns amount addedExtra changed by because it may not be the same as the amount input
	public BigDecimal addExtra(BigDecimal extra){
		
		if((extra.add(addedExtra)).compareTo(bill.negate()) == -1){
			addedExtra = bill.negate();
			return bill.negate();
		}
		addedExtra = extra.add(addedExtra);
		return extra;
	}
	
	public BigDecimal getAddedExtra(){return addedExtra;}
	
	public BigDecimal getTotal(){
		BigDecimal total = getBillPlusExtras();
		if(total.equals( 0))
			return total;
		//Log.d("PerPersonValue", "totes " + total.add (total.divide(new BigDecimal(100))).multiply(new BigDecimal(tipPercent)));
		return total.add (total.divide(new BigDecimal(100), decimalPlaces, RoundingMode.HALF_UP).multiply(new BigDecimal(tipPercent)));
	}

	protected PerPersonValue(Parcel in) {
		addedExtra = new BigDecimal(in.readString());
		bill = new BigDecimal(in.readString());
		group = in.readInt();
		name = in.readString();
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(addedExtra.toString());
		dest.writeString(bill.toString());
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

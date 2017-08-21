package bilbo.arunwebnerd.com.bilbo;

import android.os.Parcel;
import android.os.Parcelable;

public class PerPersonValue implements Parcelable
{
	public float addedExtra = 0;
	public float bill = 0;
	//public String label;
	public int group = 0;
	public String name = "group name";

	public PerPersonValue(float add, float bil, int gro){
		addedExtra = add;
		bill = bil;
		group = gro;
	}
	
	public float getTotal(){
		return bill+addedExtra;
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

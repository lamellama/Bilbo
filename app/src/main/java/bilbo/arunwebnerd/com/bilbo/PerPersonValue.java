package bilbo.arunwebnerd.com.bilbo;

public class PerPersonValue
{
	public float addedExtra = 0;
	public float bill = 0;
	//public String label;
	public int group = 0;
	
	/*public PerPersonValue(){
		label = "group ";
	}*/
	
	public float getTotal(){
		return bill+addedExtra;
	}
}

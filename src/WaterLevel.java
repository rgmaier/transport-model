import java.sql.Timestamp;
import java.util.HashMap;


public class WaterLevel {

	int riverkm;
	String location;
	HashMap<Timestamp, Integer> map;
	
	public WaterLevel(String location,int riverkm){
		this.riverkm = riverkm;
		this.location = location;
		this.map = new HashMap<Timestamp, Integer>();
		
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public int getRiverkm()
	{
		return this.riverkm;
	}
	
	public void add(Timestamp time, int i)
	{
		map.put(time, new Integer(i));
	}
	
	public HashMap<Timestamp, Integer> getHashmap()
	{
		return this.map;
	}
}

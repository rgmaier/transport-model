import java.util.HashMap;


public class WaterLevel {

	int riverkm;
	String location;
	HashMap<Long, Integer> map;
	
	public WaterLevel(String location,int riverkm){
		this.riverkm = riverkm;
		this.location = location;
		this.map = new HashMap<Long, Integer>();
		
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public int getRiverkm()
	{
		return this.riverkm;
	}
	
	public void add(Long time, int i)
	{
		map.put(time, new Integer(i));
	}
	
	public HashMap<Long, Integer> getHashmap()
	{
		return this.map;
	}
}

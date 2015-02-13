import java.sql.Timestamp;


public class WaterLevel {
	Timestamp time;
	int riverkm;
	String location;
	int level; //in cm
	
	public WaterLevel(Timestamp time, int riverkm, String location, int level){
		
		this.time = time;
		this.riverkm = riverkm;
		this.location = location;
		this.level = level;
	}
	
	@Override
	public String toString()
	{
		return this.location+"-"+this.riverkm+"-"+this.time+"-"+this.level;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public int getRiverkm()
	{
		return this.riverkm;
	}
	
	public Timestamp time()
	{
		return this.time;
	}
	
	public int level()
	{
		return this.level;
	}
}

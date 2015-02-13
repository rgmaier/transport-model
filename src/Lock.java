import java.sql.Timestamp;


public class Lock {

	private Timestamp startMaintenance;
	private Timestamp endMaintenance;
	
	private int riverkm;
	private String location;
	
	public Lock(Timestamp startMaintenance, Timestamp endMaintenance, int riverkm, String location){
		
		this.startMaintenance = startMaintenance;
		this.endMaintenance = endMaintenance;
		this.riverkm = riverkm;
		this.location = location;
	}
	
	@Override
	public String toString()
	{
		return this.location+"-"+this.riverkm+"-"+this.startMaintenance+"-"+this.endMaintenance;
	}
	
	public String getLocation()
	{
		return this.location;
	}
	
	public int getRiverkm()
	{
		return this.riverkm;
	}
	
	public Timestamp startMaintenance()
	{
		return this.startMaintenance;
	}
	
	public Timestamp endMaintenance()
	{
		return this.endMaintenance;
	}
}

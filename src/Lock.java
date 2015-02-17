public class Lock {

	//3 dimensional array
	private long startMaintenance;
	private long endMaintenance;
	
	private int riverkm;
	private String location;
	
	private String side;
	
	public Lock(long startMaintenance, long endMaintenance, int riverkm, String location, String side){
		
		this.startMaintenance = startMaintenance;
		this.endMaintenance = endMaintenance;
		this.riverkm = riverkm;
		this.location = location;
		this.side = side;
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
	
	public long startMaintenance()
	{
		
		return this.startMaintenance;
	}
	
	public long endMaintenance()
	{
		return this.endMaintenance;
	}
	
	public String getSide()
	{
		return this.side;
	}
}

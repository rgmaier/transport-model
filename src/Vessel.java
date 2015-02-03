import java.sql.Timestamp;


public class Vessel {
	private int id;
	private Timestamp time;
	private int mmsi;
	private double longitude;
	private double latitude;
	private int riverkm;
	private String upRiver;
	private String shipName;
	private int vesselType;
	private int hazardCargo;
	private int draught;
	private int length;
	private int beam;
	
	private double sogAvg;
	private Timestamp arrivalTime;
	private long travelTime;
	private int distance;
	
	
	public Vessel(int id, Timestamp time, int mmsi, double longitude, double latitude, int riverkm, String upRiver, 
			String shipName, int vesselType, int hazardCargo, int draught,int length, int beam)
			{
		this.id = id;
		this.time = time;
		this.mmsi = mmsi;
		this.longitude = longitude;
		this.latitude = latitude;
		this.riverkm = riverkm;
		this.upRiver = upRiver;
		this.shipName = shipName;
		this.vesselType = vesselType;
		this.hazardCargo = hazardCargo;
		this.draught = draught;
		this.length = length;
		this.beam = beam;
			}
	
	@Override
	public String toString()
	{
		return ""+this.id+this.time+this.mmsi+this.longitude+this.latitude+this.riverkm+this.upRiver+this.shipName+this.vesselType+this.hazardCargo+this.draught+this.length+this.beam;
	}
	
	public void writeToFile()
	{
		//write Data to .csv File
	}
	
	public boolean inHarbor(Harbor[] harbor)
	{
		for(int i=0;i<harbor.length;i++)
		{
			if(this.longitude>harbor[i].getMinLong()&&this.longitude<harbor[i].getMaxLong())
			{
				if(this.latitude>harbor[i].getMinLat()&&this.latitude<harbor[i].getMaxLat())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inCountry(double longitude, double latitude)
	{
		/*
		 * 48.590910, 13.500866 - Achleiten/Passau
		 * 48.590910, 17.072103 - somewhere in Slovakia
		 * 48.019426, 17.072103 - Bratislava/Petrzalka
		 * 48.019426, 13.500866 - somehwere in Upper Austria
		 * Draws a rectangle that contains all of Austria's part of the Danube
		*/
		if(latitude > 48.019426 && latitude < 48.590910 && longitude > 13.500866 && longitude < 17.072103)
		{
			return true;
		}
		else{
			return false;
		}
	}
	public double getLongitude()
	{
		return this.longitude;
	}
	
	public double getLatitude()
	{
		return this.latitude;
	}
}

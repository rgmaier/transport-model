import java.sql.Timestamp;


public class Vessel {
	private int id;
	private Timestamp time;
	private int mmsi;
	private double longitude;
	private double latitude;
	private int riverkm;
	private int sog;
	private char upRiver;
	private String shipName;
	private int vesselType;
	private int hazardCargo;
	private int draught;
	private int length;
	private int beam;
	
	public Vessel(int id, Timestamp time, int mmsi, double longitude, double latitude, int riverkm,int sog, char upRiver, 
			String shipName, int vesselType, int hazardCargo, int draught,int length, int beam)
			{
		this.id = id;
		this.time = time;
		this.mmsi = mmsi;
		this.longitude = longitude;
		this.latitude = latitude;
		this.riverkm = riverkm;
		this.sog = sog;
		this.upRiver = upRiver;
		this.shipName = shipName;
		this.vesselType = vesselType;
		this.hazardCargo = hazardCargo;
		this.draught = draught;
		this.length = length;
		this.beam = beam;
			}
	
	
}

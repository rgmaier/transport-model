import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
	private double arrLong;
	private double arrLat;
	private int arrId;
	
	public boolean wasInCountry;
	public boolean wasInHarbor;
	
	
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
	
	public void writeToCSV(String path)
	{
		String data ="";
		data+= this.id+",";
		data+= this.time+",";
		data+= this.mmsi+",";
		data+= this.longitude+",";
		data+= this.latitude+",";
		data+= this.riverkm+",";
		data+= this.upRiver+",";
		data+= this.shipName+",";
		data+= this.vesselType+",";
		data+= this.hazardCargo+",";
		data+= this.draught+",";
		data+= this.length+",";
		data+= this.beam+",";
		data+= this.arrId+",";
		data+= this.arrivalTime+",";
		data+= this.travelTime+",";
		data+= this.distance+",";
		data+= this.sogAvg+",";
		data+= this.arrLong+",";
		data+= this.arrLat;
		
		/*if(this.riverkm>0){
			if(this.travelTime<1000||this.distance<1000){
			}		
			else{*/
				try{
					File file = new File(path);
					
					if(!file.exists())
					{
						file.createNewFile();
					}
					
					FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(data);
					bw.newLine();
					bw.close();
					System.out.println("Done");
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			//}
		//}
		
	}
	
	public boolean inHarbor(Harbor[] harbor)
	{
		for(int i=0;i<harbor.length;i++)
		{
			if(this.longitude>=harbor[i].getMinLong()&&this.longitude<=harbor[i].getMaxLong())
			{
				if(this.latitude>=harbor[i].getMinLat()&&this.latitude<=harbor[i].getMaxLat())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inHarbor(Harbor[] harbor, double longitude, double latitude)
	{
		for(int i=0;i<harbor.length;i++)
		{
			if(longitude>harbor[i].getMinLong()&&longitude<harbor[i].getMaxLong())
			{
				if(latitude>harbor[i].getMinLat()&&latitude<harbor[i].getMaxLat())
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public boolean inCountry()
	{
		/*
		 * 48.590910, 13.500866 - Achleiten/Passau
		 * 48.590910, 17.072103 - somewhere in Slovakia
		 * 48.019426, 17.072103 - Bratislava/Petrzalka
		 * 48.019426, 13.500866 - somehwere in Upper Austria
		 * Draws a rectangle that contains all of Austria's part of the Danube
		*/
		if(this.latitude > 48.019426 && this.latitude < 48.590910 && this.longitude > 13.500866 && this.longitude < 17.072103)
		{
			return true;
		}
		else{
			return false;
		}
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
	
	private void travelTime()
	{
		this.travelTime = (this.arrivalTime.getTime()-this.time.getTime())/1000;
	}
	
	private void distance(int arrivalRiverKm)
	{
		this.distance = Math.abs((arrivalRiverKm-riverkm))*1000;
		
		if((arrivalRiverKm-riverkm)>0)
		{
			this.upRiver = "b";
		}
		else{
			this.upRiver = "t";
		}
	}
	
	private void arrivalTime(Timestamp arrival)
	{
		this.arrivalTime = arrival;
	}
	
	private void sogAvg()
	{
		this.travelTime();
		
		this.sogAvg = (double) this.distance/this.travelTime;
		
	}
	
	public void cleanUp(int arrivalRiverKm, Timestamp arrival, Double arrLong, Double arrLat, int arrId)
	{
		this.arrLat = arrLat;
		this.arrLong = arrLong;
		this.distance(arrivalRiverKm);
		this.arrivalTime(arrival);
		this.sogAvg();
		this.arrId = arrId;
	}
	
	public Timestamp getTime()
	{
		return this.time;
	}
}

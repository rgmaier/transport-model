import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;


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
	
	private String category;
	
	private int[][] lockStatus; //1st dimension are all 9 locks, 2nd dimension are status chamber at start of voyage, 1hr before, when in range and 1hr after
	private int[][] waterLevel; //1st dim are all 9 water level points, 2nd dimension are water level at start of voyage, 1hr before, when in range and 1hr after
	//something weathery comebacklater TODO
	
	private String voyage;
	
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
		this.lockStatus = new int[18][4];
		this.waterLevel = new int[18][4];
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
		data+= this.arrLat+",";
		data+= this.voyage+",";
		
		for(int i = 0; i<this.waterLevel.length;i++)
		{
			for(int j = 0; j<this.waterLevel[i].length;j++)
			{
				data += this.waterLevel[i][j]+",";
			}
		}
		
		if(this.riverkm>0){
			if(this.travelTime<1000||this.distance<1000){
			}		
			else{
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
			}
		}
		
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
		 * 48.590910, 13.5029 - Achleiten/Passau
		 * 48.590910, 17.072103 - somewhere in Slovakia
		 * 48.019426, 17.072103 - Bratislava/Petrzalka
		 * 48.019426, 13.5029 - somehwere in Upper Austria
		 * Draws a rectangle that contains all of Austria's part of the Danube
		*/
		if(this.latitude > 48.019426 && this.latitude < 48.590910 && this.longitude > 13.5029 && this.longitude < 17.072103)
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
		 * 48.590910, 13.5029 - Achleiten/Passau
		 * 48.590910, 17.072103 - somewhere in Slovakia
		 * 48.019426, 17.072103 - Bratislava/Petrzalka
		 * 48.019426, 13.500866 - somehwere in Upper Austria
		 * Draws a rectangle that contains all of Austria's part of the Danube
		*/
		if(latitude > 48.019426 && latitude < 48.590910 && longitude > 13.5029 && longitude < 17.072103)
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
	
	public void cleanUp(int arrivalRiverKm, Timestamp arrival, Double arrLong, Double arrLat, int arrId, IWD operation)
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
	
	public void setLockStatus(Lock[] data)
	{
		//TODO
		
		for(int i = 0; i<data.length;i++)
		{
			
		}
		
		
	}
	
	public void setWaterLevel(WaterLevel[] data, IWD operation)
	{
		ArrayList<Integer> locations = new ArrayList<Integer>();
		
		for(int i = 0; i<data.length;i++)
		{
			locations.add(data[i].getRiverkm());
		}
		int i = 0;
		while(locations.size()>0)
		{
			ResultSet a = operation.query("SELECT timeStampLocal FROM viadonau.shipdatadump WHERE userId ="+this.mmsi+" AND riverkm = "+locations.get(locations.size()-1)+
					" AND (id BETWEEN "+this.id+" AND "+this.arrId+") LIMIT 0,1;");
			
			try{
				int count = 0;
				if (a.last()) {
					  count = a.getRow();
					  a.first();
				}
				
				if(count > 0)
				{
					a.getTimestamp(1).getTime();
					long[] temp = new long[]{this.time.getTime(),a.getTimestamp(1).getTime()-3600000,a.getTimestamp(1).getTime(),a.getTimestamp(1).getTime()+3600000};
					
					for(int j = 0;j<this.waterLevel[i].length;j++)
					{
						this.waterLevel[i][j] = data[locations.size()-1].getHashmap().get(temp[j]);
					}
				}
				else{
					//Fill array with -1;
					this.waterLevel[i][0] = data[locations.size()-1].getHashmap().get(this.time.getTime());
					for(int j = 1;j<this.waterLevel[i].length;j++)
					{
						this.waterLevel[i][j] = -1;
					}
				}
				i++;
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
		
	}
	
	public void setVoyage(HashMap<Integer,String> data)
	{		
		String temp[] = new String[2];
		
		if(this.upRiver.equals("t"))
		{
			temp[1] = data.get(this.riverkm);
			temp[0] = data.get(this.riverkm-this.distance/1000);
		}
		else{
			temp[0] = data.get(this.riverkm);
			temp[1] = data.get(this.riverkm+this.distance/1000);
		}
		
		this.voyage = temp[0]+temp[1];
	}
	
	private long roundToHour(long time)
	{
		time += 1800000;
		time = time/(3600000);
		return time*3600000;
	}
}

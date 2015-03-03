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
		
	private int[][] lockStatus; //1st dimension are all 9 locks, 2nd dimension are status chamber at start of voyage, 1hr before, when in range and 1hr after
	private int[][] waterLevel; //1st dim are all 9 water level points, 2nd dimension are water level at start of voyage, 1hr before, when in range and 1hr after

	private double[][] airPressure;
	private int[][] clouds;
	private double[][] temperature;
	private int[][] humidity;
	private String[][] windDirection;
	private int[][] windForce;
	private double[][] precipitation;
	private int[][] snow;
	
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
		this.lockStatus = new int[16][4];
		this.waterLevel = new int[9][4];
		this.airPressure = new double[3][4];
		this.clouds = new int[3][4];
		this.temperature = new double[3][4];
		this.humidity = new int[3][4];
		this.windDirection = new String[3][4];
		this.windForce = new int[3][4];
		this.precipitation = new double[3][4];
		this.snow = new int[3][4];
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
		
		for(int i = 0; i<this.waterLevel.length;i++)
		{
			for(int j = 0; j<this.waterLevel[i].length;j++)
			{
				data += this.waterLevel[i][j]+",";
			}
		}
		for(int i = 0; i<this.lockStatus.length;i++)
		{
			for(int j = 0; j<this.lockStatus[i].length;j++)
			{
				data += this.lockStatus[i][j]+",";
			}
		}
		for(int i = 0; i<this.airPressure.length;i++)
		{
			for(int j = 0; j<this.airPressure[i].length;j++)
			{
				data += this.airPressure[i][j]+",";
			}
		}
		for(int i = 0; i<this.clouds.length;i++)
		{
			for(int j = 0; j<this.clouds[i].length;j++)
			{
				data += this.clouds[i][j]+",";
			}
		}
		for(int i = 0; i<this.temperature.length;i++)
		{
			for(int j = 0; j<this.temperature[i].length;j++)
			{
				data += this.temperature[i][j]+",";
			}
		}
		for(int i = 0; i<this.humidity.length;i++)
		{
			for(int j = 0; j<this.humidity[i].length;j++)
			{
				data += this.humidity[i][j]+",";
			}
		}
		for(int i = 0; i<this.windDirection.length;i++)
		{
			for(int j = 0; j<this.windDirection[i].length;j++)
			{
				data += this.windDirection[i][j]+",";
			}
		}
		for(int i = 0; i<this.windForce.length;i++)
		{
			for(int j = 0; j<this.windForce[i].length;j++)
			{
				data += this.windForce[i][j]+",";
			}
		}
		for(int i = 0; i<this.precipitation.length;i++)
		{
			for(int j = 0; j<this.precipitation[i].length;j++)
			{
				data += this.precipitation[i][j]+",";
			}
		}
		for(int i = 0; i<this.snow.length;i++)
		{
			for(int j = 0; j<this.snow[i].length;j++)
			{
				data += this.snow[i][j]+",";
			}
		}

		data+= this.voyage;

		try {
			File file = new File(path);

			if (!file.exists()) {
				file.createNewFile();
			}

			FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(data);
			bw.newLine();
			bw.close();
			System.out.println("Done");
		} catch (IOException e) {
			e.printStackTrace();
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
	
	public String getVoyage()
	{
		return this.voyage;
	}
	
	public long getTravelTime()
	{
		return this.travelTime;
	}
	
	public int getRiverKm()
	{
		return this.riverkm;
	}
	
	public int getDistance()
	{
		return this.distance;
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
	
	public void setLockStatus(HashMap<Integer,ArrayList<Lock>> data, IWD operation)
	{
		ArrayList<Integer> locations = new ArrayList<Integer>();
		
		for(int key : data.keySet())
		{
			locations.add(key);
			locations.add(key);
		}
		
		for(int i = 0;locations.size()>0;i++,locations.remove(locations.size()-1))
		{
			ResultSet a = operation.query("SELECT timeStampLocal FROM viadonau.shipdatadump WHERE userId ="+this.mmsi+" AND riverkm = "+locations.get(locations.size()-1)+
					" AND (id BETWEEN "+this.id+" AND "+this.arrId+") LIMIT 0,1;");
			
			try{
				int count = 0;
				if(a.last()){
					count = a.getRow();
					a.first();
				}
				
				if(count > 0)
				{
					long[] temp = this.timestamps(a);
					
					for(int j = 0; j<this.lockStatus[i].length;j++)
					{
						if(i%2 == 0)
						{
							for(Lock l : data.get(locations.get(locations.size()-1)))
							{
								long start = l.startMaintenance();
								long end = l.endMaintenance();
								
								if(temp[j] > start && temp[j]<end)
								{
									if(l.getSide().equals("LK"))
									{
										lockStatus[i][j] = 1;
									}
								}
								else{
									if(l.getSide().equals("LK"))
									{
										lockStatus[i][j] = 0;
									}
								}
							}
						}
						else{
							for(Lock l : data.get(locations.get(locations.size()-1)))
							{
								if(temp[j] > l.startMaintenance() && temp[j]<l.endMaintenance())
								{
									if(l.getSide().equals("RK"))
									{
										lockStatus[i][j] = 1;
									}
								}
								else{
									if(l.getSide().equals("RK"))
									{
										lockStatus[i][j] = 0;
									}
								}
							}
						}
					}
				}
				else{
					if(i%2 == 0)
					{
						for(Lock l : data.get(locations.get(locations.size()-1)))
						{
							if(l.getSide().equals("LK"))
							{
								if(this.time.getTime()>l.startMaintenance() && this.time.getTime()<l.endMaintenance())
								{
									lockStatus[i][0] = 1;
												
								}
								else{
									lockStatus[i][0] = 0;
								}
							}
							for(int j = 1; j<lockStatus[i].length;j++)
							{
								lockStatus[i][j] = -1;
							}		
						}
					}
					else{
						for(Lock l : data.get(locations.get(locations.size()-1)))
						{
							if(l.getSide().equals("RK"))
							{
								if(this.time.getTime()>l.startMaintenance() && this.time.getTime()<l.endMaintenance())
								{
									lockStatus[i][0] = 1;
									for(int j = 1; j<lockStatus[i].length;j++)
									{
										lockStatus[i][j] = -1;
									}					
								}
								else{
									lockStatus[i][0] = 0;
									for(int j = 1; j<lockStatus[i].length;j++)
									{
										lockStatus[i][j] = -1;										
									}
								}
							}
						}
					}
				}
				
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("MMSI: "+this.mmsi);
				System.out.println("id: "+this.id);
				System.out.println("arrID: "+this.arrId);
				System.out.println("Time of ship: "+this.roundToHour(this.time.getTime()));
			}
		}
		
		
	}
	
	public void setWaterLevel(WaterLevel[] data, IWD operation)
	{
		ArrayList<Integer> locations = new ArrayList<Integer>();
		int count = 0;
		for(int i = 0; i<data.length;i++)
		{
			locations.add(data[i].getRiverkm());
		}
		for(int i = 0; locations.size()>0;i++,locations.remove(locations.size()-1))
		{
			ResultSet a = operation.query("SELECT timeStampLocal FROM viadonau.shipdatadump WHERE userId ="+this.mmsi+" AND riverkm = "+locations.get(locations.size()-1)+
					" AND (id BETWEEN "+this.id+" AND "+this.arrId+") LIMIT 0,1;");
			int exc = 0;
			long exception = 0L;
			try {
				count = 0;
				if (a.last()) {
					count = a.getRow();
					a.first();
				}

				if (count > 0) {
					long[] temp = this.timestamps(a);
					
					for (int j = 0; j < temp.length; j++) {
						exc = j;
						exception = temp[j];
						this.waterLevel[i][j] = data[locations.size() - 1]
								.getHashmap().get(this.roundToHour(temp[j]));

					}
				} else if (count == 0) {
					// Fill array with -1;
					this.waterLevel[i][0] = data[locations.size() - 1]
							.getHashmap().get(
									this.roundToHour(this.time.getTime()));
					for (int j = 1; j < this.waterLevel[i].length; j++) {
						this.waterLevel[i][j] = -1;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Time temp: "+exception);
				System.out.println("Time rnd: "+this.roundToNearestWeatherTS(exception));
				System.out.println("Time day: "+this.roundToDay(exception));
				System.out.println("it: "+exc);
				System.out.println("MMSI: "+this.mmsi);
				System.out.println("id: "+this.id);
				System.out.println("arrID: "+this.arrId);
				System.out.println("Time of ship: "+this.time);
				System.out.println("Time of ship: "+this.time.getTime());
				System.out.println("Time of ship: "+this.roundToNearestWeatherTS(this.time.getTime()));
			}
		}
	}
	
	public void setVoyage(HashMap<Integer,String> data)
	{		
		String temp[] = new String[2];
		temp[0] = data.get(this.riverkm);

		if (this.upRiver.equals("t")) {
			temp[1] = data.get(this.riverkm - this.distance / 1000);
		} else {
			temp[1] = data.get(this.riverkm + this.distance / 1000);
		}
		if (temp[0] == null) {
			this.voyage = temp[0] + temp[1];
		} 
		else if (temp[1] == null){
			this.voyage = temp[1] + temp[0];
		}else {
			if (temp[0].charAt(0) > temp[1].charAt(0)) {
				this.voyage = temp[1] + temp[0];
			} else {
				this.voyage = temp[0] + temp[1];
			}
		}
	}
	
	public void setWeather(HashMap<Integer,Weather> data, IWD operation)
	{
		ArrayList<Integer> locations = new ArrayList<Integer>();
		
		for(int key : data.keySet())
		{
			locations.add(key);
		}
		
		for(int i = 0;locations.size()>0;i++,locations.remove(locations.size()-1))
		{
			ResultSet a = operation.query("SELECT timeStampLocal FROM viadonau.shipdatadump WHERE userId ="+this.mmsi+" AND riverkm = "+locations.get(locations.size()-1)+
					" AND (id BETWEEN "+this.id+" AND "+this.arrId+") LIMIT 0,1;");
			long exception = 0L;
			int exc = 0;
			try{
				int count = 0;
				if (a.last()) {
					  count = a.getRow();
					  a.first();
				}
				if(count > 0){
					long[] temp = this.timestamps(a);
					for(int j=0; j<temp.length;j++)
					{
						exception = temp[j];
						exc = j;
						airPressure[i][j] = data.get(locations.get(locations.size()-1)).airPressure.get(this.roundToNearestWeatherTS(temp[j]));
						clouds[i][j] = data.get(locations.get(locations.size()-1)).clouds.get(this.roundToNearestWeatherTS(temp[j]));
						temperature[i][j] = data.get(locations.get(locations.size()-1)).temperature.get(this.roundToNearestWeatherTS(temp[j]));
						humidity[i][j] = data.get(locations.get(locations.size()-1)).humidity.get(this.roundToNearestWeatherTS(temp[j]));
						windDirection[i][j] = data.get(locations.get(locations.size()-1)).windDirection.get(this.roundToNearestWeatherTS(temp[j]));
						windForce[i][j] = data.get(locations.get(locations.size()-1)).windForce.get(this.roundToNearestWeatherTS(temp[j]));
						precipitation[i][j] = data.get(locations.get(locations.size()-1)).precipitation.get(this.roundToNearestWeatherTS(temp[j]));
						snow[i][j] = data.get(locations.get(locations.size()-1)).snow.get(this.roundToNearestWeatherTS(temp[j]));
					}
				}
				else{
					airPressure[i][0] = data.get(locations.get(locations.size()-1)).airPressure.get(this.roundToNearestWeatherTS(this.time.getTime()));
					clouds[i][0] = data.get(locations.get(locations.size()-1)).clouds.get(this.roundToNearestWeatherTS(this.time.getTime()));
					temperature[i][0] = data.get(locations.get(locations.size()-1)).temperature.get(this.roundToNearestWeatherTS(this.time.getTime()));
					humidity[i][0] = data.get(locations.get(locations.size()-1)).humidity.get(this.roundToNearestWeatherTS(this.time.getTime()));
					windDirection[i][0] = data.get(locations.get(locations.size()-1)).windDirection.get(this.roundToNearestWeatherTS(this.time.getTime()));
					windForce[i][0] = data.get(locations.get(locations.size()-1)).windForce.get(this.roundToNearestWeatherTS(this.time.getTime()));
					precipitation[i][0] = data.get(locations.get(locations.size()-1)).precipitation.get(this.roundToNearestWeatherTS(this.time.getTime()));
					snow[i][0] = data.get(locations.get(locations.size()-1)).snow.get(this.roundToNearestWeatherTS(this.time.getTime()));
					
					for(int j = 1; j<this.airPressure[i].length;j++)
					{
						airPressure[i][j] = -1;
						clouds[i][j] = -1;
						temperature[i][j] = -1; 
						humidity[i][j] = -1;
						windDirection[i][j] = "-1";
						windForce[i][j] = -1;
						precipitation[i][j] = -1;
						snow[i][j] = -1;
					}
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
				System.out.println("Time temp: "+exception);
				System.out.println("Time rnd: "+this.roundToNearestWeatherTS(exception));
				System.out.println("Time day: "+this.roundToDay(exception));
				System.out.println("it: "+exc);
				System.out.println("MMSI: "+this.mmsi);
				System.out.println("id: "+this.id);
				System.out.println("arrID: "+this.arrId);
				System.out.println("Time of ship: "+this.time);
				System.out.println("Time of ship: "+this.roundToNearestWeatherTS(this.time.getTime()));
				
				///SOMMMERWINTERZEIT ARGH
				
			}
		}
	}
	
	private long roundToHour(long time)
	{
		time += 1800000;
		time = time/(3600000);
		if(time*3600000 == 1382832000000L)
		{
			return time*3600000+3600000;
		}
		else{
		return time*3600000;}
	}
	
	private long roundToDay(long time)
	{
		time += 43200000;
		time = time/86400000;
		return time*86400000-7200000;
	}
	
	private long roundToNearestWeatherTS(long time)
	{          //1382824800000
		long d = 1382824800000L;
		if (d > roundToDay(time)) {
			if (time >= roundToDay(time)
					&& time <= (roundToDay(time) + 3600000 * 10)) {
				return roundToDay(time) + 3600000 * 7;
			} else if (time >= (roundToDay(time) + 3600000 * 11)
					&& time <= (roundToDay(time) + 3600000 * 16)) {
				return roundToDay(time) + 3600000 * 14;
			} else {
				return roundToDay(time) + 3600000 * 19;
			}
		} else {
			if (time >= roundToDay(time)
					&& time <= (roundToDay(time) + 3600000 * 11)) {
				return roundToDay(time) + 3600000 * 8;
			} else if (time >= (roundToDay(time) + 3600000 * 12)
					&& time <= (roundToDay(time) + 3600000 * 17)) {
				return roundToDay(time) + 3600000 * 15;
			} else {
				return roundToDay(time) + 3600000 * 20;
			}
		}
	}
	
	private long[] timestamps(ResultSet a) {
		try {
			if (this.roundToHour(a.getTimestamp(1).getTime()) == 1382828400000L) {
				return new long[] { roundToHour(this.time.getTime()),
						roundToHour(a.getTimestamp(1).getTime() - 3600000),
						roundToHour(a.getTimestamp(1).getTime()),
						roundToHour(a.getTimestamp(1).getTime() + 7200000) };
			} else if (this.roundToHour(a.getTimestamp(1).getTime()) == 1382835600000L) {
				return new long[] { roundToHour(this.time.getTime()),
						roundToHour(a.getTimestamp(1).getTime() - 7200000),
						roundToHour(a.getTimestamp(1).getTime()),
						roundToHour(a.getTimestamp(1).getTime() + 3600000) };
			} else if (this.roundToHour(a.getTimestamp(1).getTime()) == 1375308000000L) {
				return new long[] { roundToHour(this.time.getTime()),
						roundToHour(a.getTimestamp(1).getTime()),
						roundToHour(a.getTimestamp(1).getTime()),
						roundToHour(a.getTimestamp(1).getTime() + 3600000) };
			} else {
				return new long[] { roundToHour(this.time.getTime()),
						roundToHour(a.getTimestamp(1).getTime() - 3600000),
						roundToHour(a.getTimestamp(1).getTime()),
						roundToHour(a.getTimestamp(1).getTime() + 3600000) };
			}

		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
}

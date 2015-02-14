//import java.util.Scanner;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Thesis {

	//Daten einlesen einmal und dann nur übergeben, cleanup funktion wieder aufspalten.
	
	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		String user = input.next();
		String pw = input.next();
		IWD thesis = new IWD(user, pw);
		IWD results = new IWD(user, pw);
		input.close();
		
		Harbor[] data = thesis.readHarborData("Harbor.csv");
		System.out.println(data.length);
		
		Lock[] lData = thesis.readLockData("Lock.csv");
				
		WaterLevel[] wData = thesis.readWaterData("IWD/");
		
		System.out.println(wData[0].getHashmap().size());
		
		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
		try{
			ArrayList<Integer> mmsi = new ArrayList<Integer>();
			//mmsi=thesis.getMMSI();
			mmsi.add(1);
			Vessel ship = null;

			int count = 0;
			
			while(mmsi.size()>0)
			{
				System.out.println(mmsi.size());
				System.out.println(dateFormat.format(new Date()));
				//ResultSet r = thesis.query("SELECT id, timeStampLocal, userId, longitude, latitude, riverkm, upriver, shipName, vesselType, hazardCargo, draught, length, beam FROM viadonau.shipdatadump WHERE userId ="+mmsi.get(mmsi.size()-1)+";");
				ResultSet r = thesis.query("SELECT id, timeStampLocal, userId, longitude, latitude, riverkm, upriver, shipName, vesselType, hazardCargo, draught, length, beam FROM shipdatadump WHERE userId =244660182;");
				
				if (r.last()) {
					  count = r.getRow();
					  r.beforeFirst();
					}
				
				if(count > 1)
				{
					while(r.next())
					{
					
						if(ship == null){
							ship = new Vessel(r.getInt(1), r.getTimestamp(2),r.getInt(3), r.getDouble(4), r.getDouble(5), r.getInt(6), r.getString(7), r.getString(8),
									r.getInt(9), r.getInt(10), r.getInt(11), r.getInt(12), r.getInt(13));
							if(ship.inCountry()){
								ship.wasInCountry = true;
								
								if(ship.inHarbor(data)){
									ship.wasInHarbor = true;
								}
								else{
									ship.wasInHarbor = false;
								}
							}
							else{
								ship = null;
							}
							
							
							if(r.getInt(6)==0)
							{
								ship = null;
							}
						}
						else if(r.isLast()){
				
							ship.cleanUp(r.getInt(6),r.getTimestamp(2),r.getDouble(4),r.getDouble(5),r.getInt(1), results);
							ship.writeToCSV("Data.csv");
						}
						else{
							if(ship.inCountry(r.getDouble(4), r.getDouble(5))){
								if(ship.inHarbor(data, r.getDouble(4), r.getDouble(5))){
									if(ship.wasInHarbor){
										if(r.getTimestamp(2).getTime()-ship.getTime().getTime()>180000){
											if(r.getInt(6)>1000)
											{
												ship.cleanUp(r.getInt(6),r.getTimestamp(2),r.getDouble(4),r.getDouble(5),r.getInt(1), results);
												ship.writeToCSV("Data.csv");
												ship = null;
											}
											else{
												ship.writeToCSV("Data.csv");
												ship = null;
											}
											
										}
										else{
											ship = null;
										}
									}
									else{
										ship.cleanUp(r.getInt(6),r.getTimestamp(2),r.getDouble(4),r.getDouble(5),r.getInt(1), results);
										ship.writeToCSV("Data.csv");
										ship = null;
									}
								}
								else{
									//clean up here again to avoid 0000000000
									ship.cleanUp(r.getInt(6),r.getTimestamp(2),r.getDouble(4),r.getDouble(5),r.getInt(1), results);
								}
							}
							else{
								if(ship.wasInCountry){
									ship.cleanUp(r.getInt(6),r.getTimestamp(2),r.getDouble(4),r.getDouble(5),r.getInt(1), results);
									ship.writeToCSV("Data.csv");
									ship = null;
								}
								else{
									ship = null;
								}
							}
									
						}
					}
					
				
				}
				count = 0;
				mmsi.remove(mmsi.size()-1);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		
		thesis.close();

	}

}

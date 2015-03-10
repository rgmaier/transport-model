//import java.util.Scanner;
import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

public class Thesis {

	public static void main(String[] args) {

		Scanner input = new Scanner(System.in);
		String user = input.next();
		String pw = input.next();
		
		IWD results = new IWD(user, pw);
		input.close();

		Harbor[] data = results.readHarborData("IWD/Harbor.csv");
		HashMap<Integer, ArrayList<Lock>> lData = results.readLockData("IWD/");
		WaterLevel[] wData = results.readWaterData("IWD/");
		HashMap<Integer, String> pData = results.readLocations("IWD/POI.csv");
		HashMap<Integer, Weather> mData = results.readWeatherData("IWD/");

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

		try {
			
			ArrayList<Integer> mmsi = new ArrayList<Integer>();
			mmsi=results.getMMSI();
						
			Vessel ship = null;

			int count = 0;

			while (mmsi.size() > 0) {
				IWD thesis = new IWD(user, pw);
				System.out.println(mmsi.size());
				System.out.println(dateFormat.format(new Date()));
				System.out.println(mmsi.get(mmsi.size()-1));
				ResultSet r =thesis.query("SELECT id, timeStampLocal, userId, longitude, latitude, riverkm, upriver, shipName, vesselType, hazardCargo, draught, length, beam FROM viadonau.shipdatadump WHERE userId ="+mmsi.get(mmsi.size()-1)+" AND timeStampLocal > '2013-08-01 00:00:00' AND timeStampLocal < '2013-10-29 00:00:00';");
				//ResultSet r = thesis
				//		.query("SELECT id, timeStampLocal, userId, longitude, latitude, riverkm, upriver, shipName, vesselType, hazardCargo, draught, length, beam FROM shipdatadump WHERE userId =244660182;");

				if (r.last()) {
					count = r.getRow();
					r.beforeFirst();
				}

				if (count > 1) {
					while (r.next()) {

						if (ship == null) {
							ship = new Vessel(r.getInt(1), r.getTimestamp(2),
									r.getInt(3), r.getDouble(4),
									r.getDouble(5), r.getInt(6),
									r.getString(7), r.getString(8),
									r.getInt(9), r.getInt(10), r.getInt(11),
									r.getInt(12), r.getInt(13));
							if (ship.inCountry()) {
								ship.wasInCountry = true;

								if (ship.inHarbor(data)) {
									ship.wasInHarbor = true;
								} else {
									ship.wasInHarbor = false;
								}
							} else {
								ship = null;
							}

							if (r.getInt(6) == 0) {
								ship = null;
							}
						} else if (r.isLast()) {

							ship.cleanUp(r.getInt(6), r.getTimestamp(2),
									r.getDouble(4), r.getDouble(5),
									r.getInt(1), results);
							ship.setVoyage(pData);
							ship.setWaterLevel(wData, results);
							ship.setLockStatus(lData, results);
							ship.setWeather(mData, results);
							if (ship.getRiverKm() > 0) {
								if (ship.getTravelTime() > 1000
										&& ship.getDistance() > 1000) {
										ship.writeToCSV("Data.csv");
								}
							}
						} else {
							if (ship.inCountry(r.getDouble(4), r.getDouble(5))) {
								if (ship.inHarbor(data, r.getDouble(4),
										r.getDouble(5))) {
									if (ship.wasInHarbor) {
										if (r.getTimestamp(2).getTime()
												- ship.getTime().getTime() > 180000) {
											if (r.getInt(6) > 1000) {
												ship.cleanUp(r.getInt(6),
														r.getTimestamp(2),
														r.getDouble(4),
														r.getDouble(5),
														r.getInt(1), results);
												ship.setVoyage(pData);
												ship.setWaterLevel(wData,
														results);
												ship.setLockStatus(lData,
														results);
												ship.setWeather(mData, results);
												if (ship.getRiverKm() > 0) {
													if (ship.getTravelTime() > 1000
															&& ship.getDistance() > 1000) {
															ship.writeToCSV("Data.csv");
													}
												}
												ship = null;
											} else {
												ship = null;
											}

										} else {
											ship = null;
										}
									} else {
										ship.cleanUp(r.getInt(6),
												r.getTimestamp(2),
												r.getDouble(4), r.getDouble(5),
												r.getInt(1), results);
										ship.setVoyage(pData);
										ship.setWaterLevel(wData, results);
										ship.setLockStatus(lData, results);
										ship.setWeather(mData, results);
										if (ship.getRiverKm() > 0) {
											if (ship.getTravelTime() > 1000
													&& ship.getDistance() > 1000) {
												ship.writeToCSV("Data.csv");
											}
										}
										ship = null;
									}
								} else {
									// clean up here again to avoid 0000000000
									/*
									 * ship.cleanUp(r.getInt(6),r.getTimestamp(2)
									 * ,
									 * r.getDouble(4),r.getDouble(5),r.getInt(1)
									 * , results); ship.setVoyage(pData);
									 * ship.setWaterLevel(wData,results);
									 * System.out.println("Set water level");
									 */
								}
							} else {
								if (ship.wasInCountry) {
									ship.cleanUp(r.getInt(6),
											r.getTimestamp(2), r.getDouble(4),
											r.getDouble(5), r.getInt(1),
											results);
									ship.setVoyage(pData);
									ship.setWaterLevel(wData, results);
									ship.setLockStatus(lData, results);
									ship.setWeather(mData, results);
									if (ship.getRiverKm() > 0) {
										if (ship.getTravelTime() > 1000
												&& ship.getDistance() > 1000) {
											ship.writeToCSV("Data.csv");
										}
									}
									
									ship = null;
								} else {
									ship = null;
								}
							}

						}
					}

				}
				count = 0;
				r.close();
				mmsi.remove(mmsi.size() - 1);
				thesis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(dateFormat.format(new Date()));
		
		
		//This is /r/theydidthemath
		double speed = 9.7662;
		
		String[][] fData = results.readData("Data.csv");
		for(int i = 0; i<fData.length;i++)
		{
			fData[i][217] = ""+((Integer.parseInt(fData[i][16])/1000)/speed)*60;
			fData[i][218] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),20));
			fData[i][219] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),40));
			fData[i][220] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),60));
			fData[i][221] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),120));
			fData[i][222] = ""+(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]));
		}
		results.writeToFile(fData);
		results.deleteFile("Data.csv");
		System.out.println("Finished");
		results.close();
	}
	
	public static double mRound(double value, double factor){
		return Math.round(value/factor)*factor;
	}

}

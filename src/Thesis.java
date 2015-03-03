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
		IWD thesis = new IWD(user, pw);
		IWD results = new IWD(user, pw);
		input.close();

		Harbor[] data = thesis.readHarborData("Harbor.csv");
		HashMap<Integer, ArrayList<Lock>> lData = thesis.readLockData("IWD/");
		WaterLevel[] wData = thesis.readWaterData("IWD/");
		HashMap<Integer, String> pData = thesis.readLocations("POI.csv");
		HashMap<Integer, Weather> mData = thesis.readWeatherData("IWD/");

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

		try {
			ArrayList<Integer> mmsi = new ArrayList<Integer>();
			//mmsi=thesis.getMMSI();
			mmsi.add(203999335);
			mmsi.add(205430290);
			mmsi.add(205377590);
			mmsi.add(211430820);
			mmsi.add(211430770);
			mmsi.add(211209890);
			mmsi.add(211142440);
			mmsi.add(207072325);
			
						
			Vessel ship = null;

			int count = 0;

			while (mmsi.size() > 0) {
				System.out.println(mmsi.size());
				System.out.println(dateFormat.format(new Date()));
				ResultSet r =thesis.query("SELECT id, timeStampLocal, userId, longitude, latitude, riverkm, upriver, shipName, vesselType, hazardCargo, draught, length, beam FROM viadonau.shipdatadump WHERE userId ="+mmsi.get(mmsi.size()-1)+" AND timeStampLocal > '2013-08-01 00:00:00' ;");
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
				mmsi.remove(mmsi.size() - 1);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println(dateFormat.format(new Date()));
		thesis.close();
		
		//This is /r/theydidthemath
		double speed = 9.7662;
		
		String[][] fData = thesis.readData("Data.csv");
		for(int i = 0; i<fData.length;i++)
		{
			fData[i][217] = ""+((Integer.parseInt(fData[i][16])/1000)/speed)*60;
			fData[i][218] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),20));
			fData[i][219] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),40));
			fData[i][220] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),60));
			fData[i][221] = ""+((int) mRound(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]),120));
			fData[i][222] = ""+(Integer.parseInt(fData[i][15])/60-Double.parseDouble(fData[i][217]));
		}
		thesis.writeToFile(fData);
		thesis.deleteFile("Data.csv");
		System.out.println("Finished");
	}
	
	public static double mRound(double value, double factor){
		return Math.round(value/factor)*factor;
	}

}

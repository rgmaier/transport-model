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

		HashMap<String, ArrayList<Long>> avgTravelTime = new HashMap<String, ArrayList<Long>>();

		DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss:SSS");

		try {
			ArrayList<Integer> mmsi = new ArrayList<Integer>();
			mmsi=thesis.getMMSI();
			/*mmsi.add(17140534);
			mmsi.add(244660182);
			mmsi.add(205377590);
			mmsi.add(205479090);
			mmsi.add(471710);
			mmsi.add(205387990);
			mmsi.add(203999222);
			mmsi.add(17140534);*/
			
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
									if (avgTravelTime.get(ship
											.getVoyage()) == null) {
										ArrayList<Long> tmp = new ArrayList<Long>();
										tmp.add(ship.getTravelTime());
										avgTravelTime.put(
												ship.getVoyage(), tmp);
									} else {
										ArrayList<Long> tmp = avgTravelTime
												.get(ship.getVoyage());
										tmp.add(ship.getTravelTime());
										avgTravelTime.put(
												ship.getVoyage(), tmp);
									}
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
														if (avgTravelTime.get(ship
																.getVoyage()) == null) {
															ArrayList<Long> tmp = new ArrayList<Long>();
															tmp.add(ship.getTravelTime());
															avgTravelTime.put(
																	ship.getVoyage(), tmp);
														} else {
															ArrayList<Long> tmp = avgTravelTime
																	.get(ship.getVoyage());
															tmp.add(ship.getTravelTime());
															avgTravelTime.put(
																	ship.getVoyage(), tmp);
														}
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
												if (avgTravelTime.get(ship
														.getVoyage()) == null) {
													ArrayList<Long> tmp = new ArrayList<Long>();
													tmp.add(ship.getTravelTime());
													avgTravelTime.put(
															ship.getVoyage(), tmp);
												} else {
													ArrayList<Long> tmp = avgTravelTime
															.get(ship.getVoyage());
													tmp.add(ship.getTravelTime());
													avgTravelTime.put(
															ship.getVoyage(), tmp);
												}
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
											if (avgTravelTime.get(ship
													.getVoyage()) == null) {
												ArrayList<Long> tmp = new ArrayList<Long>();
												tmp.add(ship.getTravelTime());
												avgTravelTime.put(
														ship.getVoyage(), tmp);
											} else {
												ArrayList<Long> tmp = avgTravelTime
														.get(ship.getVoyage());
												tmp.add(ship.getTravelTime());
												avgTravelTime.put(
														ship.getVoyage(), tmp);
											}
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
		
		System.out.println(avgTravelTime.entrySet()); 
		HashMap<String,Double> avgTime = new HashMap<String,Double>();
		for(String key : avgTravelTime.keySet())
		{
			double temp = 0;
			for(long e : avgTravelTime.get(key))
			{
				temp+=e;
			}
			temp = temp/(avgTravelTime.get(key).size());
			avgTime.put(key, temp);
		}
		System.out.println(avgTime.entrySet());
		ArrayList<HashMap<Integer,String>> rList = thesis.readDelayData("IWD/");
		String[][] fData = thesis.readData("Data.csv");
		for(int i = 0; i<fData.length;i++)
		{
			fData[i][217] = ""+avgTime.get(fData[i][216]);
			fData[i][218] = rList.get(0).get((int) mRound(avgTime.get(fData[i][216])-Double.parseDouble(fData[i][217]),5));
			fData[i][219] = rList.get(1).get((int) mRound(avgTime.get(fData[i][216])-Double.parseDouble(fData[i][217]),10));
			fData[i][220] = rList.get(2).get((int) mRound(avgTime.get(fData[i][216])-Double.parseDouble(fData[i][217]),15));
			fData[i][221] = rList.get(3).get((int) mRound(avgTime.get(fData[i][216])-Double.parseDouble(fData[i][217]),30));
		}
		//Only write if voyage does not contain null to filter out damaged data.
		thesis.writeToFile(fData);
		System.out.println("Finished");
	}
	
	public static double mRound(double value, double factor){
		return Math.round(value/factor)*factor;
	}

}

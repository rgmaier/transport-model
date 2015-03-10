import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.io.*;
import java.util.*;
import java.util.Date;

public class IWD {
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost:3306/viadonau";

	private String user;
	private String pass;

	private Connection conn;
	private Statement stmt;

	private String csvSplitBy = ",";

	public IWD(String usr, String pw) {
		try {
			this.user = usr;
			this.pass = pw;
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL, this.user,
					this.pass);
			System.out.println("DB Connection open");
			this.stmt = conn.createStatement();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void close() {
		System.out.println("Closing DB Connection");
		try {
			if (conn != null) {
				conn.close();
			}
			if (stmt != null) {
				stmt.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public ResultSet query(String sql) {
		try {
			ResultSet rs = this.stmt.executeQuery(sql);
			return rs;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public Harbor[] readHarborData(String path) {
		BufferedReader br = null;
		String line = "";
		Harbor[] data = null;

		try {
			data = new Harbor[this.countLines(path) + 1];

			br = new BufferedReader(new FileReader(path));
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] lineData = line.split(this.csvSplitBy);
				data[i] = new Harbor(lineData[0],
						Integer.parseInt(lineData[1]),
						Double.parseDouble(lineData[2]),
						Double.parseDouble(lineData[3]),
						Double.parseDouble(lineData[4]),
						Double.parseDouble(lineData[5]));
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public HashMap<Integer, Weather> readWeatherData(String path) {
		BufferedReader br = null;
		String line = "";
		HashMap<Integer, Weather> data = new HashMap<Integer, Weather>();

		String[] files = { "MLinz.dat", "MPoelten.dat", "MVienna.dat" };

		String[][][] sData = new String[files.length][23][125];

		for (int i = 0; i < files.length; i++) {
			try {
				br = new BufferedReader(new FileReader(path + files[i]));
				int j = 0;
				while ((line = br.readLine()) != null) {
					String[] lineData = line.split(";");
					for (int k = 0; k < lineData.length; k++) {
						sData[i][j][k] = lineData[k];
					}

					j++;
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		for (int i = 0; i < sData.length; i++) {
			Weather temp = new Weather(sData[i][0][0],
					Integer.parseInt(sData[i][0][1]));
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.airPressure.put(
						Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), Double.parseDouble(sData[i][1][j]));
				temp.airPressure.put(
						Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), Double.parseDouble(sData[i][2][j]));
				temp.airPressure.put(
						Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), Double.parseDouble(sData[i][3][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.clouds.put(Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
						.getTime(), Integer.parseInt(sData[i][4][j]));
				temp.clouds.put(Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
						.getTime(), Integer.parseInt(sData[i][5][j]));
				temp.clouds.put(Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
						.getTime(), Integer.parseInt(sData[i][6][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.temperature.put(
						Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), Double.parseDouble(sData[i][7][j]));
				temp.temperature.put(
						Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), Double.parseDouble(sData[i][8][j]));
				temp.temperature.put(
						Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), Double.parseDouble(sData[i][9][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.humidity.put(
						Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), Integer.parseInt(sData[i][10][j]));
				temp.humidity.put(
						Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), Integer.parseInt(sData[i][11][j]));
				temp.humidity.put(
						Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), Integer.parseInt(sData[i][12][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.windDirection.put(
						Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), sData[i][13][j]);
				temp.windDirection.put(
						Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), sData[i][15][j]);
				temp.windDirection.put(
						Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), sData[i][17][j]);
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				temp.windForce.put(
						Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), Integer.parseInt(sData[i][14][j]));
				temp.windForce.put(
						Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), Integer.parseInt(sData[i][16][j]));
				temp.windForce.put(
						Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), Integer.parseInt(sData[i][18][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				if (sData[i][19][j].equals("Spuren")) {
					sData[i][19][j] = "0";
				}
				temp.precipitation
						.put(Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
								.getTime(), Double.parseDouble(sData[i][19][j]));
				temp.precipitation
						.put(Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
								.getTime(), Double.parseDouble(sData[i][19][j]));
				temp.precipitation
						.put(Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
								.getTime(), Double.parseDouble(sData[i][19][j]));
			}
			for (int j = 30; j < sData[i][0].length; j++) {
				if (sData[i][20][j].equals("Spuren")) {
					sData[i][20][j] = "0";
				}
				temp.snow.put(Timestamp.valueOf(sData[i][0][j] + " 07:00:00")
						.getTime(), Integer.parseInt(sData[i][22][j]));
				temp.snow.put(Timestamp.valueOf(sData[i][0][j] + " 14:00:00")
						.getTime(), Integer.parseInt(sData[i][22][j]));
				temp.snow.put(Timestamp.valueOf(sData[i][0][j] + " 19:00:00")
						.getTime(), Integer.parseInt(sData[i][22][j]));
			}
			data.put(temp.getRiverKm(), temp);

		}
		return data;
	}

	public HashMap<Integer, ArrayList<Lock>> readLockData(String path) {
		BufferedReader br = null;
		String line = "";

		ArrayList<Lock> classes = null;
		HashMap<Integer, ArrayList<Lock>> data = new HashMap<Integer, ArrayList<Lock>>();

		int tempRiverKm = 0;

		String[] files = { "SAbwinden.dat", "SAschach.dat", "SFreudenau.dat",
				"SGreifenstein.dat", "SMelk.dat", "SOttensheim.dat",
				"SPersenbeug.dat", "SWallsee.dat" };

		for (int i = 0; i < files.length; i++) {
			classes = new ArrayList<Lock>();
			try {
				br = new BufferedReader(new FileReader(path + files[i]));

				while ((line = br.readLine()) != null) {
					String[] lineData = line.split(this.csvSplitBy);
					tempRiverKm = Integer.parseInt(lineData[2]);
					classes.add(new Lock(Timestamp.valueOf(lineData[3])
							.getTime(), Timestamp.valueOf(lineData[4])
							.getTime(), Integer.parseInt(lineData[2]),
							lineData[0], lineData[1]));
				}
				data.put(tempRiverKm, classes);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}

		}

		return data;
	}

	public WaterLevel[] readWaterData(String path) {
		BufferedReader br = null;
		String line = "";

		String[] files = { "WAchleiten.dat", "WDuernstein.dat", "WGrein.dat",
				"WKienstock.dat", "WKorneuburg.dat", "WMauthausen.dat",
				"WThebnerstrassl.dat", "WWildungsmauer.dat", "WWilhering.dat" };

		WaterLevel[] data = new WaterLevel[files.length];

		for (int i = 0; i < files.length; i++) {
			try {
				br = new BufferedReader(new FileReader(path + files[i]));

				line = br.readLine();
				String[] lineData = line.split(this.csvSplitBy);
				data[i] = new WaterLevel(lineData[2],
						Integer.parseInt(lineData[3]));
				data[i].add(Timestamp.valueOf(lineData[0]).getTime(),
						Integer.parseInt(lineData[1]));

				while ((line = br.readLine()) != null) {
					lineData = line.split(this.csvSplitBy);
					data[i].add(Timestamp.valueOf(lineData[0]).getTime(),
							Integer.parseInt(lineData[1]));
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}

		return data;
	}

	public HashMap<Integer, String> readLocations(String path) {
		HashMap<Integer, String> data = new HashMap<Integer, String>();

		BufferedReader br = null;
		String line = "";

		try {
			br = new BufferedReader(new FileReader(path));
			while ((line = br.readLine()) != null) {
				String[] lineData = line.split(this.csvSplitBy);
				data.put(Integer.parseInt(lineData[0]), lineData[1]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

		return data;
	}

	public String[][] readData(String path) {
		BufferedReader br = null;
		String line = "";
		String[][] data = null;

		try {
			data = new String[this.countLines(path)][223];
			br = new BufferedReader(new FileReader(path));
			int i = 0;
			while ((line = br.readLine()) != null) {
				String[] lineData = line.split(this.csvSplitBy);
				for (int k = 0; k < lineData.length; k++) {
					data[i][k] = lineData[k];
				}
				i++;
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (br != null) {
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}

	public ArrayList<HashMap<Integer, String>> readDelayData(String path) {
		BufferedReader br = null;
		String line = "";
		ArrayList<HashMap<Integer, String>> rList = new ArrayList<HashMap<Integer, String>>();

		String[] files = { "R5.dat", "R10.dat", "R15.dat", "R30.dat" };

		for (int i = 0; i < files.length; i++) {
			try {
				br = new BufferedReader(new FileReader(path + files[i]));

				HashMap<Integer, String> temp = new HashMap<Integer, String>();

				while ((line = br.readLine()) != null) {
					String[] lineData = line.split(this.csvSplitBy);
					temp.put(Integer.parseInt(lineData[0]), lineData[1]);
				}
				rList.add(temp);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				if (br != null) {
					try {
						br.close();
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			}
		}
		return rList;
	}

	private int countLines(String path) throws IOException {
		InputStream is = new BufferedInputStream(new FileInputStream(path));
		try {
			byte[] c = new byte[1024];
			int count = 0;
			int readChars = 0;
			boolean empty = true;
			while ((readChars = is.read(c)) != -1) {
				empty = false;
				for (int i = 0; i < readChars; ++i) {
					if (c[i] == '\n') {
						++count;
					}
				}
			}
			return (count == 0 && !empty) ? 1 : count;
		} catch (Exception e) {
			e.printStackTrace();
			return 0;
		} finally {
			is.close();
		}
	}

	public ArrayList<Integer> getMMSI() {
		ArrayList<Integer> data = new ArrayList<Integer>();

		ResultSet result = this
				.query("SELECT userId FROM viadonau.shipdatadump WHERE (vesselType BETWEEN 8010 AND 8390) GROUP BY userId;");

		try {
			while (result.next()) {
				data.add(result.getInt(1));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return data;
	}

	public HashMap<String, WaterLevel[]> prepareWaterLevel() {
		return null;
	}

	public void writeToFile(String[][] content) {
		DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-d");
		String path = dateFormat.format(new Date())+".csv";
		for (int i = 0; i < content.length; i++) {
			String tmp = "";
			for (int j = 0; j < content[i].length - 1; j++) {
				tmp += content[i][j] + ",";
			}
			tmp += content[i][content[i].length - 1];
			if(!content[i][216].contains("null"))
			{
				try {
					File file = new File(path);

					if (!file.exists()) {
						file.createNewFile();
					}

					FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
					BufferedWriter bw = new BufferedWriter(fw);
					bw.write(tmp);
					bw.newLine();
					bw.close();
					System.out.println("Done");
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}

	}
	
	public void deleteFile(String path) {
		try {

			File file = new File(path);

			if (file.delete()) {
				System.out.println(file.getName() + " is deleted!");
			} else {
				System.out.println("Delete operation is failed.");
			}

		} catch (Exception e) {

			e.printStackTrace();

		}
	}
}

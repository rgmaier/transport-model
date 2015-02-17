import java.sql.*;
import java.io.*;
import java.util.*;

public class IWD {
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost:3306/viadonau";
	
	
	private String user;
	private String pass;
	
	private Connection conn;
	private Statement stmt;
	private String filePath = null;
	
	
	public IWD(String usr, String pw)
	{
		try{
			this.user = usr;
			this.pass = pw;
			Class.forName(this.JDBC_DRIVER);
			this.conn = DriverManager.getConnection(this.DB_URL,this.user,this.pass);
			System.out.println("DB Connection open");
			this.stmt = conn.createStatement();
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public void close()
	{
		System.out.println("Closing DB Connection");
		try{
			if(conn!=null)
			{
				conn.close();
			}
			if(stmt!=null)
			{
				stmt.close();
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}
	
	public ResultSet query(String sql)
	{
		try{
			ResultSet rs = this.stmt.executeQuery(sql);
			return rs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
	
	public void setFilePath(String path)
	{
		this.filePath = path;
	}
	
	public void writeToFile(String content)
	{
		try{
			File file = null;
			if(this.filePath == null)
			{
				file = new File("default.txt");
			}
			else{
				file = new File(this.filePath);
			}
			if(!file.exists())
			{
				file.createNewFile();
			}
			
			FileWriter fw = new FileWriter(file.getAbsoluteFile(),true);
			BufferedWriter bw = new BufferedWriter(fw);
			bw.write(content);
			bw.newLine();
			bw.close();
			System.out.println("Done");
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public Harbor[] readHarborData(String path)
	{
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		Harbor[] data = null;
	 
		try{
			data = new Harbor[this.countLines(path)+1];
			
			br = new BufferedReader(new FileReader(path));
			int i = 0;
			while((line=br.readLine()) != null){
				String[] lineData = line.split(csvSplitBy);
				data[i] = new Harbor(lineData[0],Integer.parseInt(lineData[1]),Double.parseDouble(lineData[2]),Double.parseDouble(lineData[3]),
						Double.parseDouble(lineData[4]),Double.parseDouble(lineData[5]));
				i++;
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if(br!=null)
			{
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		return data;
	}
	
	public HashMap<Integer,ArrayList<Lock>> readLockData(String path)
	{
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		
		ArrayList<Lock> classes = null;
		HashMap<Integer,ArrayList<Lock>> data = new HashMap<Integer,ArrayList<Lock>>();
		
		int tempRiverKm = 0;
		
		String[] files = {"SAbwinden.dat","SAschach.dat","SFreudenau.dat","SGreifenstein.dat","SMelk.dat","SOttensheim.dat","SPersenbeug.dat","SWallsee.dat"};
		
		for(int i = 0; i<files.length;i++)
		{
			classes  = new ArrayList<Lock>();
			try{
				br = new BufferedReader(new FileReader(path+files[i]));

				while((line=br.readLine()) != null){
					String[]lineData = line.split(csvSplitBy);
					tempRiverKm = Integer.parseInt(lineData[2]);
					classes.add(new Lock(Timestamp.valueOf(lineData[3]).getTime(),Timestamp.valueOf(lineData[4]).getTime(),Integer.parseInt(lineData[2]),lineData[0],lineData[1]));
				}
				data.put(tempRiverKm,classes);
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally{
				if(br!=null)
				{
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
	
	public WaterLevel[] readWaterData(String path)
	{
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		
		String[] files = {"WAchleiten.dat","WDuernstein.dat","WGrein.dat","WKienstock.dat","WKorneuburg.dat","WMauthausen.dat","WThebnerstrassl.dat","WWildungsmauer.dat","WWilhering.dat"};
		
		WaterLevel[] data = new WaterLevel[files.length];
		
		for(int i = 0; i<files.length;i++)
		{
			try{
				br = new BufferedReader(new FileReader(path+files[i]));
				
				line=br.readLine();
				String[] lineData = line.split(csvSplitBy);
				data[i] = new WaterLevel(lineData[2], Integer.parseInt(lineData[3]));
				data[i].add(Timestamp.valueOf(lineData[0]).getTime(),Integer.parseInt(lineData[1]));
				
				while((line=br.readLine())!=null){
					lineData = line.split(csvSplitBy);
					data[i].add(Timestamp.valueOf(lineData[0]).getTime(),Integer.parseInt(lineData[1]));
				}
			}
			catch(Exception e)
			{
				e.printStackTrace();
			}
			finally{
				if(br!=null)
				{
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
	
	public HashMap<Integer,String> readLocations(String path)
	{
		HashMap<Integer,String> data = new HashMap<Integer,String>();
		
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		
		try{
			br = new BufferedReader(new FileReader(path));
			while((line=br.readLine())!=null){
				String[] lineData = line.split(csvSplitBy);
				data.put(Integer.parseInt(lineData[0]),lineData[1]);
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		finally{
			if(br!=null)
			{
				try {
					br.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		
		return data;
	}
	
	private int countLines(String path) throws IOException
	{ 
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
	    } 
		catch(Exception e)
		{
			e.printStackTrace();
			return 0;
		}
		finally{
			is.close();
	    }
	}
	
	public ArrayList<Integer> getMMSI()
	{
		ArrayList<Integer> data = new ArrayList<Integer>();
		
		ResultSet result = this.query("SELECT userId FROM viadonau.shipdatadump GROUP BY userId;");
		
		try{
			while(result.next())
			{
				data.add(result.getInt(1));
				System.out.println("Added");
			}
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
				
		
		return data;
	}
	
	public HashMap<String,WaterLevel[]> prepareWaterLevel()
	{
		return null;
	}
}

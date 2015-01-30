import java.sql.*;
import java.io.*;

public class IWD {
	private final String JDBC_DRIVER = "com.mysql.jdbc.Driver";
	private final String DB_URL = "jdbc:mysql://localhost/viadonau";
	
	private String user;
	private String pass;
	
	private Connection conn;
	private Statement stmt;
	private String filePath = null;
	private String defaultPath = "C:/default.txt";
	
	
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
	
	public void printResult(String sql)
	{
		try{
			ResultSet rs = this.query(sql);
			while(rs.next())
			{
				for(int i = 1; i<=23;i++)
				{
					System.out.print(rs.getObject(i)+" - ");
				}
				System.out.println();
				
			}
			rs.close();
		}
		catch(Exception e)
		{
			e.printStackTrace();
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
				file = new File(this.defaultPath);
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
	
	public void setDefaultPath(String path)
	{
		this.defaultPath = path;
	}
	
	public String getDefaultPath(String path)
	{
		return this.defaultPath;
	}
	
	public Harbor[] readHarborData(String path)
	{
		BufferedReader br = null;
		String line = "";
		String csvSplitBy = ",";
		Harbor[] data = null;
		try{
			data = new Harbor[this.countLines(path)+1];
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		 
		try{
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
					// TODO Auto-generated catch block
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

}

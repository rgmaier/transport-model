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
	
	private ResultSet query(String sql)
	{
		try{
			ResultSet rs = this.stmt.executeQuery(sql);
			return rs;
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
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
				
			}
			System.out.print("\n");
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
				file = new File("C:/default.txt");
			}
			else{
				file = new File("this.filePath");
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
}

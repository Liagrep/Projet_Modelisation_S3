package sqlite;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


public class SelectGts
{

	public ResultSet rs ;
	Connection connection ;
	Statement statement;
	String s;
	String w;


	public SelectGts(String s){
		// load the sqlite-JDBC driver using the current class loader
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.s=s;
		this.w=null;
		connection = null;
		open();
	}

	public SelectGts(String s, String w){
		// load the sqlite-JDBC driver using the current class loader
		try {
			Class.forName("org.sqlite.JDBC");
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		this.s=s;
		this.w=w;
		connection = null;
		open();
	}

	public void open(){
		try
		{
			// create a database connection
			connection = DriverManager.getConnection("jdbc:sqlite:test.sqlite");
			statement = connection.createStatement();
			statement.setQueryTimeout(30);  // set timeout to 30 sec.
			if(w!=null)
				rs = statement.executeQuery("select "+this.s+" from FichiersGts where path ='"+w+"'");
			else
				rs = statement.executeQuery("select "+this.s+" from FichiersGts");
		
		}
		catch(SQLException e)
		{
			// if the error message is "out of memory", 
			// it probably means no database file is found
			System.err.println(e.getMessage());
		}			
	}




	public void close(){
		try
		{
			if(connection != null){
				statement.close();
				connection.close();
				
			}
			
		}
		catch(SQLException e)
		{
			// connection close failed.
			System.err.println(e);
		}
	}


	public String[] getList(){
		ArrayList<String> list = new ArrayList<String>();
		try {
			while(rs.next())
				list.add(rs.getString("path"));
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String tab[] = new String [list.size()];
		for(int i = 0; i < tab.length; i++)
			tab[i]=list.get(i);
		return tab;
	}

}


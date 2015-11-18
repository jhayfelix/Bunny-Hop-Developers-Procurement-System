/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author Doms
 */
package procurementsys.model.database;
import java.sql.*;
import com.mysql.jdbc.PreparedStatement;
import java.util.*;
import java.io.*;
import java.awt.*;

public class DBConnection {
        private static DBConnection instance = null;
        
	private String	driverName	= "com.mysql.jdbc.Driver";
	private String	url		= "jdbc:mysql://localhost:3306/";
	private String	database	= "procurementdb";
	private String	username	= "root";
	private String	password	= "password";
        
        public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }

        return instance;
    }

	public static Connection getConnection(){
            
                if (instance == null) {
                    instance = new DBConnection();
                }
                
		try{
                        Class.forName("com.mysql.jdbc.Driver");
			return DriverManager.getConnection(instance.getUrl()
                                                        + instance.getDatabase(),
                                                         instance.getUsername(),
                                                            instance.getPassword());
		}catch( Exception e){
			System.out.println("Couldnt connect");
			e.printStackTrace();
		}
		
		return null;
		
	}
	
	public String getUrl() {
        return url;
    }

    /**
     * returns database name
     *
     * @return database name
     */
    public String getDatabase() {
        return database;
    }

    public String getUsername() {
        return username;
    }

    private String getPassword() {
        return password;
    }

   
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}


	

	
}
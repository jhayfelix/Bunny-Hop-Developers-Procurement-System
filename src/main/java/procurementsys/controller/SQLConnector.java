/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package guidb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Samsung
 */
public class SQLConnector {
    private String username = ""; //Username in SQL
    private String password = ""; //Password in SQL
    private String url = ""; // Link to your SQL
    private String driver = "com.mysql.jdbc.Driver";
    private Connection con;
    
    public  SQLConnector() throws SQLException{
        try {
        Class.forName (driver);
        
        } catch (ClassNotFoundException e) {
        e.printStackTrace();
    }
 
    con = DriverManager.getConnection
    (url, username, password);

    }
    
    public Connection getCon(){
    return con;
    }
    
    
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package student.app.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author zappyzero
 */
public class Database {
    private String url="jdbc:mysql://localhost:3306/studentapp";
    private String user="root";
    private String password="root";
    private Connection conn;
    private static Database db;
    private Database(){};
    public Connection connect() throws SQLException{
        conn=DriverManager.getConnection(url, user, password);
        return conn;
    }
    public static Database getInstance(){
        if(db==null){
            db=new Database();
            return db;
        }
        return db;
    }
}

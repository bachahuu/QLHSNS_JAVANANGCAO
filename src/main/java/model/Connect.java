/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Windows
 */
public class Connect {
      private String host ;
      private String user ;
      private String pass ;
      private String url ;
      private Connection conn;
    public Connect(){
         host = "localhost";
         user = "root";
         pass ="";
         url ="jdbc:mysql://localhost:3306/qlhsnsjavanangcao";
    }
    public Connection getConnection() throws SQLException{
          try {
           if(conn != null){
             return conn; 
           } 
            conn = DriverManager.getConnection(url, user, pass);
          } catch (Exception e) {
              e.printStackTrace();
          }
          return conn;
         
      }
}

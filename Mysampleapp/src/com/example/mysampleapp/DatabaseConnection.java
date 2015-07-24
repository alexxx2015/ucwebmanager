package com.example.mysampleapp;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
public class DatabaseConnection
 {
  Connection connection = null;
   public  Connection Createconnection(String dbconnectionstring)
   {
   
	   try
   {
Class.forName("org.sqlite.JDBC").newInstance();
connection = DriverManager.getConnection(dbconnectionstring);
return connection;
}
   catch(SQLException | ClassNotFoundException | InstantiationException | IllegalAccessException e)
   {
  
   System.err.println(e.getMessage());
   return null;
   }
   



}
}
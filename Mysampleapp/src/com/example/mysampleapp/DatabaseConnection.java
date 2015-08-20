package com.example.mysampleapp;
import java.io.File;
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
		   File file = new File (dbconnectionstring.replace("jdbc:sqlite:",""));

		   if(!file.exists()) //here's how to check
		      {
		          System.out.print("DB doesnt exist..create one");
		          
		      }
		      else{

		            
		   Class.forName("org.sqlite.JDBC").newInstance();
connection = DriverManager.getConnection(dbconnectionstring);
		      }
return connection;
}
   catch(SQLException |ClassNotFoundException | InstantiationException | IllegalAccessException e)
   {
  
   System.err.println(e.getMessage());
   return null;
   }
   



}
}
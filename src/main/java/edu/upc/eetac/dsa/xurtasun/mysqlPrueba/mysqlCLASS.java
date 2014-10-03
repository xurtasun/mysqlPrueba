package edu.upc.eetac.dsa.xurtasun.mysqlPrueba;

import java.io.*;
import java.sql.*;

public class mysqlCLASS {
	   Connection connect = null;
	   Statement statement = null;
	   PreparedStatement preparedStatement = null;
	   ResultSet resultSet = null;
	  
	  
	  public void readDataBase() throws Exception {
		    try {
		      Class.forName("com.mysql.jdbc.Driver");
		      // setup the connection with the DB.
		      connect = DriverManager.getConnection("jdbc:mysql://localhost/urtasun?"+"user=root&password=xavi");

		      // statements allow to issue SQL queries to the database
		      statement = connect.createStatement();
		      // resultSet gets the result of the SQL query
		      resultSet = statement.executeQuery("select * from urtasun.COMMENTS");
		      writeResultSet(resultSet);

		      // preparedStatements can use variables and are more efficient
		      preparedStatement = connect.prepareStatement("insert into  urtasun.COMMENTS values (default, ?, ?, ?, ? , ?, ?)");
		      // "myuser, webpage, datum, summary, COMMENTS from FEEDBACK.COMMENTS");
		      // parameters start with 1
		      preparedStatement.setString(1, "Test");
		      preparedStatement.setString(2, "TestEmail");
		      preparedStatement.setString(3, "TestWebpage");
		      preparedStatement.setDate(4, new java.sql.Date(2014,9,01));
		      preparedStatement.setString(5, "TestSummary");
		      preparedStatement.setString(6, "TestComment");
		      preparedStatement.executeUpdate();

		      preparedStatement = connect.prepareStatement("SELECT myuser, webpage, datum, summary, COMMENTS from urtasun.COMMENTS");
		      resultSet = preparedStatement.executeQuery();
		      writeResultSet(resultSet);

		      // remove again the insert comment
		      preparedStatement = connect.prepareStatement("delete from urtasun.COMMENTS where myuser= ? ; ");
		      preparedStatement.setString(1, "Test");
		      preparedStatement.executeUpdate();
		      
		      resultSet = statement.executeQuery("select * from urtasun.COMMENTS");
		      writeMetaData(resultSet);
		      
		    } catch (Exception e) {
		      throw e;
		    } finally {
		     // close();
		    }

		  }

		  private void writeMetaData(ResultSet resultSet) throws SQLException {
		    // now get some metadata from the database
		    System.out.println("The columns in the table are: ");
		    System.out.println("Table: " + resultSet.getMetaData().getTableName(1));
		    for  (int i = 1; i<= resultSet.getMetaData().getColumnCount(); i++){
		      System.out.println("Column " +i  + " "+ resultSet.getMetaData().getColumnName(i));
		    }
		  }

		  private void writeResultSet(ResultSet resultSet) throws SQLException {
		    // resultSet is initialised before the first data set
		    while (resultSet.next()) {
		      String user = resultSet.getString("myuser");
		      String website = resultSet.getString("webpage");
		      String summary = resultSet.getString("summary");
		      Date date = resultSet.getDate("datum");
		      String comment = resultSet.getString("comments");
		      System.out.println("User: " + user);
		      System.out.println("Website: " + website);
		      System.out.println("Summary: " + summary);
		      System.out.println("Date: " + date);
		      System.out.println("Comment: " + comment);
		    }
		  }

		  // you need to close all three to make sure
		  private void close() {
		    close(resultSet);
		    close(statement);
		    close(connect);
		  }
		  private void close(AutoCloseable c) {
		    try {
		      if (c != null) {
		        c.close();
		      }
		    } catch (Exception e) {
		    // don't throw now as it might leave following closables in undefined state
		    }
		  }
}

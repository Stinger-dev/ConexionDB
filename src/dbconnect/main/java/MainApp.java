package dbconnect.main.java;

import java.lang.System.Logger;
import java.sql.SQLException;

import dbconnect.main.java.api.Connector;
import dbconnect.main.java.api.DBUtilities;

public class MainApp {


		public static void main(String[] args) {
	
				try {
					DBUtilities ob1 = new DBUtilities();
					System.out.println(ob1.getAllClients());
					System.out.println(ob1.getAllOrders());

					
				} catch (SQLException e) {
					e.printStackTrace();
				}
		
				
		
		}
}

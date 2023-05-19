package dbconnect.main.java.api;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import dbconnect.main.java.model.Cliente;

public class Connector {
	
	//TODO esto solo esta aqui de modo legacy, para ver ejemplos
	
	private static String USER;
	private final static String DB_USER_KEY="db.user";

	private static String PASS;
	private final static String DB_PASS_KEY="db.pass";
	
	private static String JDBC_URL;
	private final static String DB_URL_KEY="db.url";
	
	private final static String PROPERTIES_URI = "./resources/db.properties";
	
	public Connector() {
		super();
		loadProperties();
	}
	
	public void loadProperties() {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(PROPERTIES_URI));
			USER = properties.getProperty(DB_USER_KEY);
			PASS = properties.getProperty(DB_PASS_KEY);
			JDBC_URL = properties.getProperty(DB_URL_KEY);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public void connect() throws SQLException, ClassNotFoundException {
		
		

		Connection connection = DriverManager.getConnection(JDBC_URL, USER, PASS); 
		Statement statement=connection.createStatement();  
		
		
		//Inserción de datos parametrizada
		PreparedStatement ps = connection.prepareStatement("INSERT INTO Cliente (nombre, apellido, email, fechaNacimiento, genero) "
																+ " VALUES (?, ?, ?, ?, ?)");
		ps.setString(1, "Manuel");
		ps.setString(2, "Leunam");
		ps.setString(3, "mm@gmail.com");
		ps.setString(4, "1985-11-01");
		ps.setString(5, "F");
		ps.executeUpdate();
		//ps.close();
		
		// Ejecuta query de eliminación, actualización o insercioń (DELETE, UPDATE, INSERT)
		statement.executeUpdate("INSERT INTO Cliente (nombre, apellido, email, fechaNacimiento, genero) "
								+ "VALUES ('Rigoberto', 'Ricciardiello', 'rr0@yelp.com', '1983-04-15', 'M');\n");
		
		
		
		
		/*
		--
		ResultSet rs=statement.executeQuery("select * from Cliente");  
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			Cliente alumno = new Cliente(Integer.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3), 
										rs.getString(5), rs.getString(4));
			System.out.println(alumno);
			System.out.println(rs.getString(1)+"  "+rs.getString(2)+"  "+rs.getString(3));  
		}*/
		

		ResultSet tmp=statement.executeQuery("select * from Pedido");  
		
		while(tmp.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal

			System.out.println(tmp.getString(1)+"  "+tmp.getString(2)+"  "+tmp.getString(3)+"  "+tmp.getString(4));  
		}
		
		connection.close();  
		
		

	}
	

}

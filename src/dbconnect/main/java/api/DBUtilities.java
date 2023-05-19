package dbconnect.main.java.api;

import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import dbconnect.main.java.model.Cliente;
import dbconnect.main.java.model.Pedido;

public class DBUtilities {
	
	private static String USER;
	private static final String DB_USER_KEY="db.user";

	private static String PASS;
	private static final String DB_PASS_KEY="db.pass";
	
	private static String JDBC_URL;
	private static final String DB_URL_KEY="db.url";
	
	private static final String PROPERTIES_URI = "./resources/db.properties";
	
	private static Connection connection; 
	private static Statement statement;
	
	public DBUtilities() throws SQLException {
		super();
		loadProperties();
	}
	
	
	
	public void loadProperties() throws SQLException {
		try {
			Properties properties = new Properties();
			properties.load(new FileReader(PROPERTIES_URI));
			USER = properties.getProperty(DB_USER_KEY);
			PASS = properties.getProperty(DB_PASS_KEY);
			JDBC_URL = properties.getProperty(DB_URL_KEY);
			connection = DriverManager.getConnection(JDBC_URL, USER, PASS);
			statement = connection.createStatement();  
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<Cliente> getAllClients() throws NumberFormatException, SQLException {
		List<Cliente> output = new ArrayList<>();
		
		ResultSet rs=statement.executeQuery("select * from Cliente");  
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			Cliente tmpCliente = new Cliente(Integer.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3), 
										rs.getString(5), rs.getString(4));
			output.add(tmpCliente);
		}
	
		return output;
	
	}
	
	
	
	public List<Pedido> getAllOrders() throws NumberFormatException, SQLException {
		List<Pedido> output = new ArrayList<>();
	
		ResultSet rs=statement.executeQuery("select * from Pedido");  
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			Pedido tmpPedido = new Pedido(Integer.valueOf(rs.getString(1)), rs.getString(2), rs.getString(3), Integer.valueOf(rs.getString(4)));
			output.add(tmpPedido);
		}
	
		return output;
	
	}
	
	
	
	public void deleteCustomer(int idClienteABorrar) throws SQLException {
					
		PreparedStatement st = connection.prepareStatement("DELETE FROM Cliente WHERE id = ?");
		st.setInt(1, idClienteABorrar);
		st.executeUpdate(); 
		
		
		
	}
	
}

package dbconnect.main.java.api;

import java.io.FileReader;
import java.io.IOException;
import java.nio.file.spi.FileSystemProvider;
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
import dbconnect.main.java.model.Linea;
import dbconnect.main.java.model.Pedido;

public class DBUtilities {
	
	private static String USER;
	private static final String DB_USER_KEY="db.user";

	private static String PASS;
	private static final String DB_PASS_KEY="db.pass";
	
	private static String JDBC_URL;
	private static final String DB_URL_KEY="db.url";
	
	private static final String PROPERTIES_URI = "./resources/db.properties";
	
	
	public DBUtilities() throws SQLException { 
		super();
		loadProperties();
	}
	
	
	//Para crear un cliente tambien tenemos que añadir sus tablas hijas primero, asi que el orden deberia ser algo asi
		//TODO: crear codigo para añadir lineas
		//TODO: crear codigo para añadir pedidos
		//TODO: crear codigo para añadir cliente
	
	
	
	public void loadProperties() throws SQLException {
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
	
	private Connection iniciarConexion() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, USER, PASS);
	}
	
	private void terminarConexion(Connection conexion) {
		try {
			conexion.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	
	public List<Cliente> getAllClients() throws SQLException  {
		List<Cliente> output = new ArrayList<>();
	    Connection conexion = iniciarConexion();
	    Statement statement = conexion.createStatement();
		
		ResultSet rs=statement.executeQuery("select * from Cliente");  
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			Cliente tmpCliente = new Cliente(Integer.valueOf(rs.getString("id")), rs.getString("nombre"), rs.getString("apellido"), 
										rs.getString("email"), rs.getString("fechaNacimiento"), rs.getString("genero"));
			output.add(tmpCliente);
		}
		terminarConexion(conexion);
		return output;
	}
	
	
	
	public List<Pedido> getAllOrders() throws NumberFormatException, SQLException {
		List<Pedido> output = new ArrayList<>();
	    Connection conexion = iniciarConexion();
	    Statement statement = conexion.createStatement();
	    
		ResultSet rs=statement.executeQuery("""
											SELECT P.*
											FROM Linea L, Pedido P
											WHERE L.idPedido LIKE P.id 
											GROUP BY L.idPedido, P.id 
											ORDER BY SUM(L.precio*L.cantidad) DESC;
											""");

		
		while(rs.next()) {
			int idCliente = Integer.valueOf(rs.getString(4));
			int idPedido = Integer.valueOf(rs.getString(1));

			Pedido tmpPedido = new Pedido(idPedido, rs.getString("codigo"), rs.getString("status"), idCliente, getDuegnoPedido(idCliente), getLineasPedido(idPedido));
			output.add(tmpPedido);
		}
		terminarConexion(conexion);

		return output;
	
	}
	public List<Linea> getLineasPedido(int idPedido) throws SQLException {
		List<Linea> output = new ArrayList<>();
		Connection conexion = iniciarConexion();
		Statement statement = conexion.createStatement();
		ResultSet rs=statement.executeQuery(String.format("""
															SELECT L.* 
															FROM Linea L
															WHERE L.idPedido LIKE %s;
															""", idPedido));
		while(rs.next()) {
			Linea tmpLinea = new Linea(Integer.valueOf(rs.getString("id")), rs.getString("codigo"), rs.getString("nombreProducto"), idPedido
					,Integer.valueOf(rs.getString("cantidad")),Integer.valueOf(rs.getString("precio")));
			output.add(tmpLinea);
		}
		terminarConexion(conexion);

		return output;
	}
	
	public List<Pedido> getPedidosCliente(int idCliente) throws SQLException{
		List<Pedido> output = new ArrayList<>();
	    Connection conexion = iniciarConexion();
	    Statement statement = conexion.createStatement();
		ResultSet rs = statement.executeQuery(String.format("""	
															SELECT P.*
															FROM Pedido P
															WHERE P.idCliente LIKE %s;
															""", idCliente));
		
		while(rs.next()) {
			int idPedido = Integer.parseInt(rs.getString(1));
			
			Pedido tmpPedido = new Pedido(idPedido, rs.getString("codigo"), rs.getString("status"), idCliente, getDuegnoPedido(idCliente), getLineasPedido(idPedido));

			output.add(tmpPedido);
		}
		
		terminarConexion(conexion);
		return output;
	}
	
	
	public String getDuegnoPedido(int idPedido) throws SQLException {
		String output = "";
		Connection conexion = iniciarConexion();
		Statement statement = conexion.createStatement();
		ResultSet rs=statement.executeQuery(String.format("""
															SELECT CONCAT(C.nombre, ' ', C.apellido) 
															FROM Cliente C, Pedido P
															WHERE C.id LIKE P.idCliente 
															AND P.id LIKE %s;
															""", idPedido));
		if(rs.next()) { //Aunque haya solo un dato, hay que poner esto igualmente
			output = rs.getString(1);
		}
		terminarConexion(conexion);

		return output;
	}
	
	public Cliente getClienteCompleto(int idCliente) throws SQLException {
		Cliente output = null;
	    Connection conexion = iniciarConexion();
	    Statement statement = conexion.createStatement();
		
		ResultSet rs=statement.executeQuery(String.format("""
															SELECT C.*
															FROM Cliente C
															WHERE C.id LIKE %s;
															""", idCliente)); 
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			Cliente tmpCliente = new Cliente(Integer.valueOf(rs.getString("id")), rs.getString("nombre"), rs.getString("apellido"), 
										rs.getString("email"), rs.getString("fechaNacimiento"), rs.getString("genero"), getPedidosCliente(Integer.valueOf(rs.getString("id"))));
			output = tmpCliente;
		}
		terminarConexion(conexion);
		return output;
		
	}
	
	public void addOrder(Pedido pedido) throws SQLException {
	    Connection conexion = iniciarConexion();
	    PreparedStatement orden = conexion.prepareStatement("INSERT INTO Pedido(id, status, idCliente) VALUES(?,?,?)");
	    orden.setInt(1, pedido.getId());
	    orden.setString(2, pedido.getStatus());
	    orden.setInt(3, pedido.getIdCliente());
	    orden.executeUpdate();
	    
	    
		terminarConexion(conexion);

	}
	

	public void deleteCustomer(int idClienteABorrar) throws SQLException {
					
		
		//TODO: solucionar excepcion que salta cundo se hace un delete a un cliente que tiene clases dependientes
		//Hay una violacion de clave foranea al borrar el cliente cuando tiene pedidos (usad como ejemplo el cliente 4)
		//Podrias probar a hacer una consulta para comprobar si el cliente tiene hijos y si su hijo tiene hijos y una vez hecho eso, borrar en orden inverso
		// Otra opcion es deshabilitar la fk justo antes de borrar y despues añadirla de nuevo
		//Otra es cambiar la configuracion de la base de datos para añadir un "delete on cascade"(alter table)
		Connection conexion = iniciarConexion();
		
		PreparedStatement st = conexion.prepareStatement("DELETE  FROM Cliente WHERE id = ?");
		st.setInt(1, idClienteABorrar);
		st.executeUpdate(); 
		terminarConexion(conexion);

		//hay una forma mas corta de hacer esto mismo (sin tener en cuenta la excepcion)
		//statement.execute(String.format("DELETE  FROM Cliente WHERE id = %s", idClienteABorrar));
		//Asi no hay que añadir ningun objeto nuevo y es minimamente mas entendible 
	}
	
	public void addClient(Cliente clienteAnadir) throws SQLException {
		
		Connection conexion = iniciarConexion();
		PreparedStatement st = conexion.prepareStatement("INSERT INTO Cliente VALUES(?, ?, ?, ?, ?, ?);");
		
		st.setInt(1, clienteAnadir.getId());
		st.setString(2, clienteAnadir.getNombre());
		st.setString(3, clienteAnadir.getApellidos());
		st.setString(4, clienteAnadir.getEmail());
		st.setString(5, clienteAnadir.getFechaNacimiento());
		st.setString(6, clienteAnadir.getGenero());
		st.executeUpdate();
		
		terminarConexion(conexion);
	}
	
	public void addOrders(Pedido pedido) throws SQLException {
		Connection conexion = iniciarConexion();
		PreparedStatement statement = conexion.prepareStatement("INSERT INTO Pedido VALUES (? ,? ,?, ?)");
		
		statement.setInt(1, pedido.getId());
		statement.setString(2, pedido.getCodigo());
		statement.setString(3, pedido.getStatus());
		statement.setInt(4, pedido.getIdCliente());
		
		statement.executeUpdate();
		terminarConexion(conexion);
	}
	
	
	public void addLineaAPedido(Linea linea ) throws SQLException {
		
		if(getStatus(linea.getIdPedido()).equalsIgnoreCase("PROCESANDO")) {
			Connection conexion = iniciarConexion();
			PreparedStatement statement = conexion.prepareStatement("INSERT INTO Linea VALUES (? ,? ,?, ?,?,?);");
			
			statement.setInt(1, linea.getId());
			statement.setString(2, linea.getCodigo());
			statement.setString(3, linea.getNombreProducto());
			statement.setInt(4, linea.getIdPedido());
			statement.setInt(5, linea.getCantidad());
			statement.setDouble(6, linea.getPrecio());
			
			statement.executeUpdate();
			terminarConexion(conexion);
		}else {
			throw new SQLException("No se ha podido añadir el pedido");
		}
		
		
		
	}
	
	private String getStatus(int idPedido) throws SQLException {
		String output = "";
	    Connection conexion = iniciarConexion();
		
	    
		PreparedStatement statement = conexion.prepareStatement("SELECT P.status FROM Pedido P, Linea L WHERE L.idPedido = P.id AND L.idPedido = ?;");
		
 		
		ResultSet rs=statement.executeQuery(String.format("SELECT P.status FROM Pedido P, Linea L WHERE L.idPedido = P.id AND L.idPedido = %s;", idPedido)); 
		
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe tal
			output = rs.getString("status");
		}
		terminarConexion(conexion);
		return output;
		
	}
	
	
	public void updateCliente(Cliente nuevosDatos) throws SQLException {
		
		Connection conexion = iniciarConexion();
		//UPDATE Cliente SET nombre  = '', apellido = '', email = '', fechaNacimiento = '', genero = '' WHERE id  = ?;
		/*
		PreparedStatement st = conexion.prepareStatement("INSERT INTO Cliente VALUES(?, ?, ?, ?, ?, ?);");
		
		st.setInt(1, nuevosDatos.getId());
		st.setString(2, nuevosDatos.getNombre());
		st.setString(3, nuevosDatos.getApellidos());
		st.setString(4, nuevosDatos.getEmail());
		st.setString(5, nuevosDatos.getFechaNacimiento());
		st.setString(6, nuevosDatos.getGenero());
		st.executeUpdate();
		*/
		terminarConexion(conexion);
	}
	
	
	public void deleteClient(Cliente cliente) throws SQLException {
		
		Connection conexion = iniciarConexion();
		
		PreparedStatement statement = conexion.prepareStatement("DELETE FROM Linea WHERE Linea.idPedido IN (SELECT Pedido.id FROM Pedido, Cliente WHERE Pedido.idCliente = Cliente.id AND Cliente.id = ?);");
					statement.setInt(1, cliente.getId());	
					statement.executeUpdate();
					
					PreparedStatement statement2 = conexion.prepareStatement("DELETE FROM Pedido WHERE Pedido.idCliente = ?");
					statement2.setInt(1, cliente.getId());	
					statement2.executeUpdate();
					
					PreparedStatement statement3 = conexion.prepareStatement("DELETE FROM Cliente WHERE Cliente.id = ?");
					statement3.setInt(1, cliente.getId());	
					statement3.executeUpdate();
	
		terminarConexion(conexion);
	}
	

	
}

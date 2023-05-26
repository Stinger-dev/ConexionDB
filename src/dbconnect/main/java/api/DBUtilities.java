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
	
	
	
	public static void loadProperties() throws SQLException {
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
	
	//Creamos un metodo privado que nos permita hacer la conexion mas corta
	private Connection iniciarConexion() throws SQLException {
		return DriverManager.getConnection(JDBC_URL, USER, PASS);
	}
	
	
	private void terminarConexion(Connection conexion) {
		//Tenemos una para abrir y otra para terminar, es obligatorio hacerlo tras cada consulta para evitar colisiones
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
		
		ResultSet rs=statement.executeQuery("SELECT * FROM Cliente");  
		
		while(rs.next()) {//Avanza de posición en el listado de registros y devuelve true si existe
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
			int idCliente = Integer.parseInt(rs.getString(4));
			int idPedido = Integer.parseInt(rs.getString(1));

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
		
		while(rs.next()) {
			Cliente tmpCliente = new Cliente(Integer.valueOf(rs.getString("id")), rs.getString("nombre"), rs.getString("apellido"), 
										rs.getString("email"), rs.getString("fechaNacimiento"), rs.getString("genero"), getPedidosCliente(Integer.valueOf(rs.getString("id"))));
			output = tmpCliente;
		}
		terminarConexion(conexion);
		return output;
		
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
		//Como tenemos que comprobar si el pedido esta en proceso hemos creado un metodo privado que nos devuelve el estado
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
		//Este metodo es auxiliar a añadir lineas, ya que tenemos que averiguar el estado, pero al hacerlo que devuelva el String le damos flexibilidad por si queremos reusarlo mas adelante
		String output = "";
	    Connection conexion = iniciarConexion();
		
	    
		PreparedStatement statement = conexion.prepareStatement("SELECT P.status FROM Pedido P, Linea L WHERE L.idPedido = P.id AND L.idPedido = ?;");
		statement.setInt(1,idPedido);
 
 		
		ResultSet rs=statement.executeQuery(); 
		
		
		while(rs.next()) {//Aunque solo sea un dato, debemos hacer el rs next
			output = rs.getString("status");
		}
		terminarConexion(conexion);
		return output;
		
	}
	
	
	public void updateCliente(Cliente nuevosDatos) throws SQLException {
		//Para poder actualizar el clietne se pasa por paremetro el cliente con los datos modificados, para poder pasar el string fecha a date usamos una funcion de MYSQL
		Connection conexion = iniciarConexion();

		PreparedStatement st = conexion.prepareStatement("UPDATE Cliente SET nombre  = ?, apellido = ?, email = ?, fechaNacimiento = STR_TO_DATE(?,'%d-%m-%Y'), genero = ? WHERE id  = ?;");
		

		
		st.setString(1, nuevosDatos.getNombre());
		st.setString(2, nuevosDatos.getApellidos());
		st.setString(3, nuevosDatos.getEmail());
		st.setString(4, nuevosDatos.getFechaNacimiento());
		st.setString(5, nuevosDatos.getGenero());
		st.setInt(6, nuevosDatos.getId());
		st.executeUpdate();
		
		terminarConexion(conexion);
	}
	
	
	public void deleteClient(Cliente cliente) throws SQLException {
		//Para evitar que las claves foraneas no de error debemos borrar las tablas desde los hijos a los padres
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

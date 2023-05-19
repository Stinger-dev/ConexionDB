package dbconnect.main.java;

import java.lang.System.Logger;
import java.sql.SQLException;
import java.util.List;

import dbconnect.main.java.api.Connector;
import dbconnect.main.java.api.DBUtilities;
import dbconnect.main.java.model.Cliente;
import dbconnect.main.java.model.Pedido;

public class MainApp {
	public static DBUtilities ob1 ;

	public static void main(String[] args) {

			try {	
				ob1 = new DBUtilities();
				mostrarTodosLosPedidosOdenadosPorPrecioTotal();
				mostrarTodosLosClientes();

			} catch (SQLException e) {
				e.printStackTrace();
			}
	

	
	}
	
	public static void mostrarTodosLosClientes() {
		try {
			List<Cliente> tmp =  ob1.getAllClients();
			for (Cliente cli : tmp) {
				System.out.println(cli.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public static void mostrarTodosLosPedidosOdenadosPorPrecioTotal() {
		try {
			List<Pedido> tmp =  ob1.getAllOrders();
			for (Pedido ped : tmp) {
				System.out.println(ped.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
}

package dbconnect.main.java;

import java.lang.System.Logger;
import java.sql.SQLException;
import java.util.List;

import dbconnect.main.java.api.DBUtilities;
import dbconnect.main.java.model.Cliente;
import dbconnect.main.java.model.Linea;
import dbconnect.main.java.model.Pedido;

public class MainApp {
	public static DBUtilities ob1 ;

	public static void main(String[] args) {

			try {	
				ob1 = new DBUtilities();
				ob1.addLineaAPedido(new Linea(513452, "jokhdb", "jamon", 1, 2, 20));
				
			} catch (SQLException e) {
				e.printStackTrace();
			}
	

	
	}
	
	public static void mostrarTodosLosClientes() {
		try {
			List<Cliente> tmp =  ob1.getAllClients();
			System.out.println("| Nombre | Apellido | Email | Edad | Sexo |");

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
			System.out.println("| Codigo | Status | Nombre Completo Cliente | Nº Productos | Importe |");
			for (Pedido ped : tmp) {
				System.out.println(ped.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	public static void mostrarTodosLasLineasDePedido(int idPedido) {
		try {
			List<Linea> tmp =  ob1.getLineasPedido(idPedido);
			System.out.println("| Codigo | Status | Nombre Completo Cliente | Nº Productos | Importe |");
			for (Linea ped : tmp) {
				System.out.println(ped.toString());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void agnadirPedido(Pedido pedido) {
		try {
			ob1.addOrder(pedido);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}

package dbconnect.main.java;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import dbconnect.main.java.api.DBUtilities;
import dbconnect.main.java.model.Cliente;
import dbconnect.main.java.model.Linea;
import dbconnect.main.java.model.Pedido;

public class MainApp {
	
	public static DBUtilities ob1 ;

	
	public static void main(String[] args) {
		


		
		Scanner sc = new Scanner(System.in);
		int opcionMenu = 0;
		
		
		try {	
			ob1 = new DBUtilities();
		
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		
		//Bucle que reproduce el menú
		do {
			
			System.out.println(MENU);
			opcionMenu = Integer.valueOf(sc.nextLine());
			
			while ((opcionMenu != 1) && (opcionMenu != 2) && (opcionMenu != 3) && (opcionMenu != 4) &&
					(opcionMenu != 5) && (opcionMenu != 6) && (opcionMenu != 7) && (opcionMenu != 8)) {
				
				System.out.println("Te has equivocado. Vuelve a introducir la opción de nuevo: ");
				opcionMenu = Integer.valueOf(sc.nextLine());
			}
			
			
			
			//1. Mostrar todos los clientes
			if (opcionMenu == 1) {
				
				mostrarTodosLosClientes();
				
				
				
			//2. Mostrar todos los pedidos
			}else if (opcionMenu == 2) {
				
				mostrarTodosLosPedidosOrdenadosPorPrecioTotal();
				
				
				
			//3. Anadir cliente
			}else if (opcionMenu == 3) {
				
				/*Constructor Cliente: 
				 * int id, String nombre, String apellidos, 
				 * String fechaNacimiento, 
				 * String email, String genero*/
				
				System.out.println("Dime el ID del nuevo cliente: ");
				int id = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el nombre del nuevo cliente: ");
				String nombre = sc.nextLine();
				System.out.println("Dime los apellidos del nuevo cliente: ");
				String apellidos = sc.nextLine();
				System.out.println("Dime el email del nuevo cliente: ");
				String email = sc.nextLine();
				System.out.println("Dime la fecha de nacimiento del nuevo cliente (formato yyyy-mm-dd): ");
				String fechaNacimiento = sc.nextLine();
				System.out.println("Dime el género del nuevo cliente: ");
				String genero = sc.nextLine();
				
				try {
					
					/*int id, String nombre, String apellidos, String fechaNacimiento, String email,String genero*/
					ob1.addClient(new Cliente(id, nombre, apellidos, fechaNacimiento, email, genero));
					System.out.println("Cliente añadido a la base de datos.");
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
				
			//4. Actualizar un cliente existente
			}else if (opcionMenu == 4) {
				
				System.out.println("Dime el ID del cliente que quieres actualizar: ");
				int id = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el nuevo nombre del cliente a actualizar: ");
				String nombre = sc.nextLine();
				System.out.println("Dime los nuevos apellidos del cliente a actualizar: ");
				String apellidos = sc.nextLine();
				System.out.println("Dime la nueva fecha de nacimiento del cliente a actualizar (formato dd-mm-yyyy): ");
				String fechaNacimiento = sc.nextLine();
				System.out.println("Dime el nuevo email del cliente a actualizar: ");
				String email = sc.nextLine();
				System.out.println("Dime el nuevo género del cliente a actualizar: ");
				String genero = sc.nextLine();
				
				try {
					
					ob1.updateCliente(new Cliente(id, nombre, apellidos, email, fechaNacimiento, genero));
					System.out.println("Cliente con ID " + id + " actualizado en la base de datos.");
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
				
				
			//5. Eliminar cliente
			}else if (opcionMenu == 5) {
				
				System.out.println("Dime el ID del cliente que quieres borrar: ");
				int id = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el nombre del cliente a borrar: ");
				String nombre = sc.nextLine();
				System.out.println("Dime los apellidos del cliente a borrar: ");
				String apellidos = sc.nextLine();
				System.out.println("Dime la fecha de nacimiento del cliente a borrar (formato yyyy-mm-dd): ");
				String fechaNacimiento = sc.nextLine();
				System.out.println("Dime el email del cliente a borrar: ");
				String email = sc.nextLine();
				System.out.println("Dime el género del cliente a borrar: ");
				String genero = sc.nextLine();
				
				try {
					
					ob1.deleteClient(new Cliente(id, nombre, apellidos, fechaNacimiento, email, genero));
					System.out.println("Cliente con ID " + id + " ha sido borrado de la base de datos.");
				
				} catch (SQLException e) {
					e.printStackTrace();
				}
				
			
				
			//6. Anadir pedido
			}else if (opcionMenu == 6) {
				
				/*Constructor Pedido: 
				 * int id, String codigo, String status, 
				 * int idCliente, String nombreDugno*/
				
				System.out.println("Dime el ID del nuevo pedido: ");
				int id = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el código del nuevo pedido: ");
				String codigo = sc.nextLine();
				System.out.println("Dime el estado del nuevo pedido: ");
				String status = sc.nextLine();
				System.out.println("Dime el ID del cliente que ha encargado el pedido: ");
				int idCliente = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el nombre del dueño del pedido: ");
				String nombreDueno = sc.nextLine();
				
				
				agnadirPedido(new Pedido(id, codigo, status, idCliente, nombreDueno));
				System.out.println("Pedido con ID añadido " + id 
								+ " al cliente con ID " + idCliente
								+ " a la base de datos.");
				
				
				
			//7. Incluir lineas en un pedido existente
			}else if (opcionMenu == 7) {
				
				/*Constructor Linea: 
				 * int id, String codigo, String nombreProducto, 
				 * int idPedido, int cantidad, double precio*/
				
				System.out.println("Dime el ID de la nueva línea: ");
				int id = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el código del producto de la nueva línea: ");
				String codigo = sc.nextLine();
				System.out.println("Dime el nombre del producto de la nueva línea: ");
				String nombreProducto = sc.nextLine();
				System.out.println("Dime el ID del pedido al que quieres añadir la nueva línea: ");
				int idPedido = Integer.valueOf(sc.nextLine());
				System.out.println("Dime la cantidad de productos: ");
				int cantidad = Integer.valueOf(sc.nextLine());
				System.out.println("Dime el precio del producto: ");
				double precio = Double.valueOf(sc.nextLine());
				
				try {
					
					ob1.addLineaAPedido(new Linea(id, codigo, nombreProducto, idPedido, cantidad, precio));
					System.out.println("Línea con ID " + id
										+ " añadida al pedido con ID " + idPedido);
					
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

			
			System.out.println("\n");
			
			
		}while (opcionMenu != 8);
		
		
		//Texto que sale cuando la opción para salir se pulsa
		System.out.println("\n" + SEPARADOR + "\n" + "Has salido de la aplicación.");
		sc.close();
		
			
	
	}
	
	
	
	/* ======== CONSTANTES ======== */
	private static final String SEPARADOR = "---------------------";

	private static final String MENU = SEPARADOR + SEPARADOR + "\n"
									 + "         MENÚ DE BASE DE DATOS\n"
									 + SEPARADOR + SEPARADOR + "\n"
									 + "1. Mostrar información sobre todos los clientes\n"
									 + "2. Mostrar información sobre todos los pedidos ordenados por precio total\n"
									 + "3. Añadir un nuevo cliente\n"
									 + "4. Actualizar un cliente existente\n"
									 + "5. Eliminar un cliente\n"
									 + "6. Añadir un nuevo pedido\n"
									 + "7. Incluir líneas a un pedido existente en estado 'Procesando'\n"
									 + "8. Salir\n"
									 + SEPARADOR + "\n"
									 + "Elige una opción: ";
	
	
	
	/* ----------- Método mostrarTodosLosClientes() ----------- */
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
	
	
	
	/* - Método mostrarTodosLosPedidosOrdenadosPorPrecioTotal() - */
	public static void mostrarTodosLosPedidosOrdenadosPorPrecioTotal() {
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
	
	
	
	/* -------- Método mostrarTodosLasLineasDePedido() --------- */
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
	
	
	
	/* ---------------- Método agnadirPedido() ----------------- */
	public static void agnadirPedido(Pedido pedido) {
		try {
			ob1.addOrders(pedido);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	
}

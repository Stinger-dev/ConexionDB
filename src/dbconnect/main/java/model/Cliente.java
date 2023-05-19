package dbconnect.main.java.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class Cliente {
	
	private int id;
	private String nombre;
	private String apellidos;
	private String dni;
	private String email;
	private String fechaNacimiento;
	private List<Pedido> pedidos;
	
	
	public Cliente(int id, String nombre, String apellidos, String fechaNacimiento, String email, List <Pedido> pedidos) {
		super();
		this.id = id;
		this.nombre = nombre;
		this.apellidos = apellidos;
		this.fechaNacimiento = fechaNacimiento;
		this.email = email;
		this.pedidos = pedidos;
	}
	
	public Cliente(int id, String nombre, String apellidos, String fechaNacimiento, String email) {
		this(id,nombre,apellidos,fechaNacimiento,email, new ArrayList<>());
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getNombre() {
		return nombre;
	}


	public void setNombre(String nombre) {
		this.nombre = nombre;
	}


	public String getApellidos() {
		return apellidos;
	}


	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}


	public String getDni() {
		return dni;
	}


	public void setDni(String dni) {
		this.dni = dni;
	}


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getFechaNacimiento() {
		return fechaNacimiento;
	}


	public void setFechaNacimiento(String fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}
	
	public int getEdad() {
		return LocalDate.now().minusYears(LocalDate.parse(this.fechaNacimiento, DateTimeFormatter.ofPattern("yyyy-mm-D")).getYear()).getYear();
	}



	@Override
	public String toString() {
		return String.format("|%s | %s | %s | %s | %s |", this.nombre, this.apellidos, this.email, this.fechaNacimiento,this.getEdad());
	}
	
	public List<Pedido> getPedidos() {
		return pedidos;
	}
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	

}

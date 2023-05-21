package dbconnect.main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
	
	private int id;
	private String codigo;
	private String status;
	private int idCliente;
	private List<Linea> lineas;
	private String nombreDugno;
	
	//TODO: añadir un campo importe que sea la suma del valor de las lineas
	//Que al cargar los datos todos los campos de lineas se completen
	
	public Pedido(int id, String codigo, String status, int idCliente, String nombreDugno, List<Linea> lineas) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.status = status;
		this.idCliente = idCliente;
		this.lineas = lineas;
		this.nombreDugno = nombreDugno;
		
	}
	
	
	public Pedido(int id, String codigo, String status, int idCliente, String nombreDugno ) {
		this(id, codigo, status, idCliente, nombreDugno, new ArrayList<>());
	}
	
	
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getCodigo() {
		return codigo;
	}
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public int getIdCliente() {
		return idCliente;
	}
	public void setIdCliente(int idCliente) {
		this.idCliente = idCliente;
	}
	public List<Linea> getLineas() {
		return lineas;
	}
	public void setLineas(List<Linea> lineas) {
		this.lineas = lineas;
	}
	
	public double getImporte() {
		double output = 0;
		for(Linea li : this.lineas) {
			output += li.getValorTotalLinea(); 
		}
		return output;
	}
	@Override
	public String toString() {
		return String.format("| %-11s| %-12s| %-20s| %-4s| %-7s|", this.codigo, this.status, this.nombreDugno, this.lineas.size(),this.getImporte()); 
	}
	
	
	
	
	
	
	

}

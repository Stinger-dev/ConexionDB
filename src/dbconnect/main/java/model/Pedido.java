package dbconnect.main.java.model;

import java.util.ArrayList;
import java.util.List;

public class Pedido {
	
	private int id;
	private String codigo;
	private String status;
	private int idCliente;
	private List<Linea> lineas;
	
	//TODO: añadir un campo importe que sea la suma del valor de las lineas
	//Que al cargar los datos todos los campos de lineas se completen
	
	public Pedido(int id, String codigo, String status, int idCliente, List<Linea> lineas) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.status = status;
		this.idCliente = idCliente;
		this.lineas = lineas;
	}
	
	
	public Pedido(int id, String codigo, String status, int idCliente) {
		this(id, codigo, status, idCliente, new ArrayList<>());
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
	@Override
	public String toString() {
		return "Pedido [id=" + id + ", codigo=" + codigo + ", status=" + status + ", idCliente=" + idCliente
				+ ", lineas=" + lineas + "]";
		//return String.format("|%s | %s | %s | %s | %s |", this.codigo, this.status, this.email, this.lineas.size(),this.importe); 
	}
	
	
	
	
	
	
	

}

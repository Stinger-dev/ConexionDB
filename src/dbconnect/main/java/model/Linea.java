package dbconnect.main.java.model;

public class Linea {
	
	private int id;
	private String codigo;
	private String nombreProducto;
	private int idPedido;
	private int cantidad;
	private double precio;
	
	
	public Linea(int id, String codigo, String nombreProducto, int idPedido, int cantidad, double precio) {
		super();
		this.id = id;
		this.codigo = codigo;
		this.nombreProducto = nombreProducto;
		this.idPedido = idPedido;
		this.cantidad = cantidad;
		this.precio = precio;
	}


	public String getCodigo() {
		return codigo;
	}


	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public String getNombreProducto() {
		return nombreProducto;
	}


	public void setNombreProducto(String nombreProducto) {
		this.nombreProducto = nombreProducto;
	}


	public int getIdPedido() {
		return idPedido;
	}


	public void setIdPedido(int idPedido) {
		this.idPedido = idPedido;
	}


	public int getCantidad() {
		return cantidad;
	}


	public void setCantidad(int cantidad) {
		this.cantidad = cantidad;
	}


	public double getPrecio() {
		return precio;
	}


	public void setPrecio(double precio) {
		this.precio = precio;
	}

	public double getValorTotalLinea() {
		return this.cantidad*this.precio;
	}

	@Override
	public String toString() {	
		return String.format("| %-5s| %-15s| %-35s| %-3s| %-4s | %-4s|", this.id, this.codigo, this.nombreProducto, this.idPedido, this.cantidad, this.cantidad);
	}
	
	public int getId() {
		return id;
	}
	
	
	

}

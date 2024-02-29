package main;

public class Linea {

	private String fecha;
	private int hora;
	double consumidoKW;
	double vertidoKW;
	
	
	public Linea (String fecha, int hora, double consumidoKW, double vertidoKW ) {
		
		this.fecha=fecha;
		this.hora=hora;
		this.consumidoKW=consumidoKW;
		this.vertidoKW=vertidoKW;
		
		
		
		
	}


	public String getFecha() {
		return fecha;
	}


	public void setFecha(String fecha) {
		this.fecha = fecha;
	}


	public int getHora() {
		return hora;
	}


	public void setHora(int hora) {
		this.hora = hora;
	}


	public double getConsumidoKW() {
		return consumidoKW;
	}


	public void setConsumidoKW(double consumidoKW) {
		this.consumidoKW = consumidoKW;
	}


	public double getVertidoKW() {
		return vertidoKW;
	}


	public void setVertidoKW(double vertidoKW) {
		this.vertidoKW = vertidoKW;
	}
	
	public String toString() {
		 
		return "Lectura [Fecha= "+fecha+"Hora= "+hora+"KW_Consumido= "+consumidoKW+"KW_Vertido"+vertidoKW+"]";
	}
	
}

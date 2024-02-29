package main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Properties;


import javax.swing.JOptionPane;

public class Main {
	
	static String usuario;
	static Properties properties = new Properties();
	 
	public static void main(String[] args) {
		
		JOptionPane.showMessageDialog(null, "Copia los archivos Historico_cosumo.csv y\n  tarifa.properties en tu escritorio.");
		usuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario de Windows");
		File desktop = new File("C:\\Users\\"+usuario+"\\Desktop\\");
		/*Se crea un objeto de la clase file llamado desktop para comprobar
		 * si se ha introducido el nombre de usuario bien
		 */
		
		/*Condicional para comprobar si localiza la ruta al escritorio
		 * prueba de que se ha escrito el nombre de uusario bien
		 */
		if (!(desktop.exists() && desktop.isDirectory())) {
		    JOptionPane.showMessageDialog(null, "Nombre de usuario incorrecto");
		    System.exit(0);
		}
		
		//Creamo ya el archivo de destino
		
		try {
			FileWriter archivo = new FileWriter("C:\\Users\\"+usuario+"\\Desktop\\Historico_comsumo_calculo.csv");
			;
			archivo.close();
		}catch (IOException e) {
			// TODO: handle exception
			System.out.println("Error al crear el archivo 'Historico_comsumo_calculo.csv'");
			e.printStackTrace();
		}
		
		File myfile_consumo = new File("C:\\Users\\"+usuario+"\\Desktop\\Historico_comsumo.csv");
		File myfile_tarifa = new File("C:\\Users\\"+usuario+"\\Desktop\\tarifa.properties");
		File consumoCalculado = new File("C:\\Users\\"+usuario+"\\Desktop\\Historico_comsumo_calculo.csv");
		
		
		comprobarExiste(myfile_tarifa);
		comprobarExiste(myfile_consumo);	
		lecturaTarifa(myfile_tarifa);
		//properties.list(System.out);
		gestionaHistorico(myfile_consumo,consumoCalculado,properties);
		
		
	}	
	public static void gestionaHistorico(File file, File consumoCalculado, Properties tarifa) {
		
		double consumidoAcumulado=0;//variables de tipo contador para ir acumulando el consumo Total
    	double vertidoAcumulado=0;//variables de tipo contador para ir acumulando el vertido Total

	    try (BufferedReader br = new BufferedReader(new FileReader(file));
	    	 BufferedWriter bw = new BufferedWriter(new FileWriter(consumoCalculado))) {
	        // Ignoramos la primera línea si contiene encabezados
	        br.readLine(); // Leer y descartar la primera línea

	        String linea;
	        while ((linea = br.readLine()) != null) {
	        	
	        	double consumidoKW; 
	            double vertidoKW;
	        	
	        	
	        	
	        	//verificar si la linea esta vacia
	        	if (linea.isEmpty()) {
	        		continue;
	        	}
	        	
	            String[] tokens = linea.split(";", -1);// Usar String.split(";", -1) para incluir partes vacías
	            
	         // Verificar si hay suficientes tokens en la línea
	            if(tokens.length < 5) {
	            	// La línea no tiene todos los valores esperados, saltarla
	            	System.err.println ("Linea incorrecta"+linea);
	            	//continue;
	            }
	            
	            // Obtener los valores de los tokens
	            String fecha = tokens[1];
	            
	            /*Se utiliza la clase LocalDate de la API de fecha y hora de Java para crear un objeto 
	             * LocalDate a partir de una cadena de texto que representa una fecha en 
	             * formato específico
	            
	            LocalDate fechaCalendario = LocalDate.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
	            
	            if (fechaCalendario.getDayOfWeek() == DayOfWeek.SATURDAY || fechaCalendario.getDayOfWeek() == DayOfWeek.SUNDAY) {
                    System.out.println("La fecha " + fecha + " es fin de semana.");
                } */
	            int hora = Integer.parseInt(tokens[2]);
	            
	            
	         // Verificar si hay un valor para consumidoKW
	            if (!tokens[3].isEmpty()) {
	                consumidoKW = Double.parseDouble(tokens[3].replace(',', '.'));
	            } else {
	                consumidoKW = 0.0; // Valor predeterminado si no hay valor especificado
	            }

	            // Verificar si hay un valor para vertidoKW
	            if (!tokens[4].isEmpty()) {
	                vertidoKW = Double.parseDouble(tokens[4].replace(',', '.'));
	            } else {
	                vertidoKW = 0.0; // Valor predeterminado si no hay valor especificado
	            }
         
	            // Hacer algo con los valores, como imprimirlos
	            //System.out.println("Fecha: " + fecha + ", Hora: " + hora + ", ConsumidoKW: " + consumidoKW + ", VertidoKW: " + vertidoKW);
	              
	            // Calcular costo de la energía consumida y vertida según la tarifa correspondiente
	            
	            double costoConsumido = calcularCosto(consumidoKW, fecha, hora, properties);
	            double costoVertido = calcularCosto(vertidoKW,fecha,  hora, properties);
	            
	         // Formatear los valores con tres decimales
	            String formato = "%.6f"; // especifica tres decimales
	            String costoConsumidoFormateado = String.format(formato, costoConsumido);
	            String costoVertidoFormateado = String.format(formato, costoVertido);
	            
	            // Escribir los datos en el archivo de salida
	            String lineaCalculada = linea + ";" + costoConsumidoFormateado + ";" + costoVertidoFormateado;
	            bw.write(lineaCalculada);
	            bw.newLine();
	            
	            //Contador acumulativo de consumo y vertido
	            consumidoAcumulado= consumidoAcumulado + consumidoKW;
	            vertidoAcumulado= vertidoAcumulado+ vertidoKW;
          
	        }
	     // Obtener los valores de num_dias y potencia_punta de tarifa y convertirlos a números
	        int numDias = Integer.parseInt(tarifa.getProperty("num_dias"));
	        double potenciaPunta = Double.parseDouble(tarifa.getProperty("potencia_punta"));
	        double potenciaValle = Double.parseDouble(tarifa.getProperty("potencia_valle"));
	        // Calcular la potencia punta multiplicando el número de días por la potencia punta
	        double potenciaPuntaTotal = numDias * potenciaPunta;
	        double potenciaValleTotal = numDias * potenciaValle;
	        	
	        JOptionPane.showMessageDialog(null,"Potencia Punta="+ potenciaPuntaTotal+"\nPotencia Valle= "+potenciaValleTotal+"\nTotal Consumido= "+consumidoAcumulado + "\nTotal Vertido= "+vertidoAcumulado*(-1)+"\nTotal= "+(consumidoAcumulado-vertidoAcumulado));
	    } catch (IOException e) {
	        e.printStackTrace();	        	     
	    }	     
	}    
	


	
	public static double calcularCosto(double cantidad, String fecha, int hora, Properties tarifas) { 
		double aplicaTarifa;
		
		LocalDate fechaCalendario = LocalDate.parse(fecha, java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        
        if (fechaCalendario.getDayOfWeek() == DayOfWeek.SATURDAY || fechaCalendario.getDayOfWeek() == DayOfWeek.SUNDAY) {
        	aplicaTarifa = Double.parseDouble(tarifas.getProperty("energia_valle"));
        }else {        
        	if(hora<8 ) {
    			aplicaTarifa = Double.parseDouble(tarifas.getProperty("energia_valle"));
    		}else if((hora==8)||(hora==9)||(hora==14)||(hora==15)||(hora==16)||(hora==17)||(hora==22)||(hora==23)) {
    			aplicaTarifa = Double.parseDouble(tarifas.getProperty("energia_llano"));
    		}else{
    			aplicaTarifa= Double.parseDouble(tarifas.getProperty("energia_punta"));
    		}
        }
				
		return cantidad*aplicaTarifa;
	}
	
	//se lee el archivo properties 
	public static void lecturaTarifa(File file) {
		
		try {
			properties.load(new FileInputStream(file));
			int num_dias = Integer.parseInt(properties.getProperty("num_dias")) ;
			double potencia_punta = Double.parseDouble(properties.getProperty("potencia_punta").replace(',', '.'));
			double potencia_valle = Double.parseDouble(properties.getProperty("potencia_valle").replace(',', '.'));
			double energia_punta = Double.parseDouble(properties.getProperty("energia_punta").replace(',', '.'));
			double energia_llano = Double.parseDouble(properties.getProperty("energia_llano").replace(',', '.'));
			double energia_valle = Double.parseDouble(properties.getProperty("energia_valle").replace(',', '.'));
			double energia_excedente = Double.parseDouble(properties.getProperty("energia_excedente").replace(',', '.'));		
			
		} catch (FileNotFoundException e) {
			
			e.printStackTrace();
		} catch (IOException e) {
			
			e.printStackTrace();
		}
		 
	}
	//Comprueba si el archivo existe, si no informa y se deteiene
	public static void comprobarExiste(File file) {
						
		if(!file.exists()) {	
			JOptionPane.showMessageDialog(null, "Archivo"+file+" \nno encontrado.");
			System.exit(0);
		}		
	}
	
}

import java.util.Scanner;

public class principal {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		int num1, num2, num3, suma;
		
		Scanner entrada = new Scanner(System.in);
		
		
		System.out.print("Ingrersa el numero 1: ");
		num1 = entrada.nextInt();
		
		System.out.print("Ingrersa el numero 2: ");
		num2 = entrada.nextInt();
		
		System.out.print("Ingrersa el numero 3: ");
		num3 = entrada.nextInt();
		
		suma = num1 +num2+num3;
		
		System.out.println("La suma es: "+suma);
		
		
		
		
		
	}

}

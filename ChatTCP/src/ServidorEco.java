import java.net.*;
import java.io.*;

class ServidorEco {
	public static void main(String args[]) {
		ServerSocket socketSer = null;
		Socket socket = null;
		boolean seguir = true;
		
		/*
		 * Conectamos el servidor al puerto establecido por la practica y tratamos los errores
		 * cualquier fallo lo controlamos con las excepciones
		 */
		try {
			socketSer = new ServerSocket(12345);
			System.out.println("conectado a: "
					+ socketSer.getLocalSocketAddress());
		} catch (IOException e) {
			System.out.println("Puerto ya ocupado");
			try {
				if (socketSer != null) {
					socketSer.close();
				}
			} catch (IOException e2) {
				System.out.println("ya se cerro antes");
			}
		}

		/*
		 * el servidor recibe hasta que la cadena sea FIN ECHO
		 * se puede preparar para hacer una entrada de system.in, pero no he tenido tiempo
		 * y tampoco tengo los conocimientos necesarios.
		 */
		while (seguir == true) {
			try {
				socket = socketSer.accept();
				BufferedReader leido = new BufferedReader(
						new InputStreamReader(socket.getInputStream()));
				String leer = leido.readLine();
				System.out.print("El servidor ha leido: " + leer + "\n");

				if (leer.contains("FIN ECHO")) {
					System.out
							.print("Gracias por usar el servidor, ¡Adios!\n");
					seguir = false;
				} else {
					System.out.print("para terminar introducir FIN ECHO");
				}
				socket.close();
			} catch (IOException e) {
				System.out.println("error : " + e);
				try {
					socket.close();
					socketSer.close();
					seguir = false;

				} catch (IOException e1) {
					System.out.println("ya cerrado antes");
					System.out.println(e1);
				}
			}
		}
		
		/*
		 * por ultimo se asegura de cerrar el server y controlar los fallos
		 */
		try {
			if (socket != null) {
				socket.close();
				socketSer.close();
				System.out.println("puerto cerrado correctamente");
			}
		} catch (IOException e2) {
			System.out.println("puerto ya cerrado");
		}

	}
}

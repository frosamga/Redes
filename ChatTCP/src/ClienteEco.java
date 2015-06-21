import java.net.*;
import java.io.*;

/*
 * Recordar que CLienteEco tiene que tener pasados como parametros primero el host y luego el texto, terminado en FIN ECHO
 * 
 * Si da fallo de puerto ocupado, es porque los try catch no funcionan tal como debieran, para eliminar un proceso atascado en un puerto en Windows usamos este comando
 * netstat -oa | findstr 12345   // que busca el PID del proceso atascado en el puerto 12345	
 * taskkill /F /PID x    		// siendo x el numero del PID y /F el comando forzado
 */

//le pasaremos como parametro "0.0.0.0" "Probando" "Cliente" "/Servidor" "sin" "entrada.in" "FIN ECHO"

/*
 * MUY IMPORTANTE: si no se le pasan paremtros dara fallo y un puerto quedara ocupado por un proceso.
 *
 */
class ClienteEco {
	public static void main(String args[]) throws IOException {
		Socket socket = null;

		int longitud = args.length;

		/*
		 * en el caso que no haya ningun parametro pasado
		 */
		if (args.length == 0) {
			throw new RuntimeException(
					"No hay ninguna cadena pasada como argumento");
		}

		/*
		 * abre el socket para el cliente y trata sus fallos
		 */
		try {
			socket = new Socket(args[0], 12345);
		} catch (IOException e) {
			System.out.println("no puede acceder a direccion y puerto");
			System.out
					.println("Puede que no le hayas pasado la direccion como primer parametro o sea diferente de la local.");
			e.getStackTrace();
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e2) {
				System.out.println("puerto ya cerrado");
			}
		}

		/*
		 * envia datos desde el cliente al servidor, enviara lo recibido por los
		 * argumentosel servidor los recibira y nos devolvera lo que recibio
		 */
		try {

			PrintWriter envio = new PrintWriter(socket.getOutputStream());
			for (int i = 1; i < longitud; i++) {
				envio.print(args[i] + " ");
			}

			envio.close();
		} catch (Exception e) {
			System.out.println("error :" + e);
			try {
				if (socket != null) {
					socket.close();
					System.out.println("puerto cerrado correctamente");
				}
			} catch (IOException e2) {
				System.out.println("puerto ya cerrado");
			}

		}

	}
}

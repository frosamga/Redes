/**
 * Practica 7 de Redes y sistemas de concurrencia de la UMA, la practica solo permite leer por puerto FTP y
 *  obtener los errores (3 digitos y cada uno con una funcion), decodificarlos y asignar error ademas de leer 
 *  y escribir por puerto FTP, cabe destacar que falta una libreria para su correcto uso,
 *  en cualquier caso se puede bajar el.jar FTPClient
 */
package rysd.practica7;

import java.net.*;

public interface ClienteFTP {
	public void abrirSesionFTP(String servidorFTP);

	public void enviarUsuario(String usuario);

	public void enviarContraseña(String password);

	public void enviarCambioDeDirectorio(String directorio);

	public void descargarFichero(String nombre);

	public void cerrarSesionFTP();

	public void muestraFichero(String nombre);

}

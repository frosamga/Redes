package rysd.practica7;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.SocketException;

import org.apache.commons.net.ftp.FTPClient;

public class ClienteFTPImpl implements ClienteFTP {

	private InetAddress dirIP;
	private FTPClient ftp = null;
	private FileOutputStream f = null;
	private String dir;

	public void abrirSesionFTP(String servidorFTP) {
		try {
			ftp = new FTPClient();
			ftp.connect(servidorFTP);
			System.out.println("�Esta disponible?:" + ftp.isAvailable());
			System.out.println("�Esta conectado?" + ftp.isConnected());
			System.out.println("220 FTP server ready");

		} catch (SocketException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public void enviarUsuario(String usuario) {
		try {
			System.out.println(this.leerRespuesta(ftp.user(usuario)));
		} catch (IOException e) {
			e.getStackTrace();
		}

	}

	public void enviarContrase�a(String password) {
		try {
			System.out.println(this.leerRespuesta(ftp.pass(password)));
		} catch (IOException e) {
			e.getStackTrace();
		}

	}

	public void enviarCambioDeDirectorio(String directorio) {
		try {
			ftp.changeWorkingDirectory(directorio);
			System.out.println(ftp.pwd());			
			System.out.println(this.leerRespuesta(ftp.pwd()));
			dir= ftp.printWorkingDirectory();
			System.out.println("se encuentra en: " +dir);
		} catch (IOException e) {
			e.getStackTrace();
		}
	}


	public void muestraFichero(String nombre) {	
	
		try {
			System.out.println(ftp.pasv());
			ftp.retr(nombre);
			System.out.println(leerRespuesta(ftp.retr(nombre)));
			
		} catch (IOException e) {
			e.getMessage();
			System.out.println("Fallo de entrada/salida.");
		}
	}

	public void descargarFichero(String nombre) {
		
		try {
			//se debe crear la carpeta Descargas dentro del directorio donde se guarda el codigo.
	        f = new FileOutputStream(nombre);
	        ftp.retrieveFile("/"+nombre, f);
	    	System.out.println(leerRespuesta(ftp.retr("/Descargas")));
		} catch (IOException e) {
			e.getStackTrace();
			System.out.println("Fallo al descargar");
		}
	}

	public void cerrarSesionFTP() {
		try {
			ftp.disconnect();
			System.out.println("Cerrado correctamente");

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	public String leerRespuesta(int num){
		String cadena=null;
		
		if(num==200){
			cadena=" Orden correcta.";
		}else if(num==500){
			cadena=num+" Error de sintaxis, comando no reconocido.";
		}else if(num==501){
			cadena=num+" Error de sintaxis en par�metros o argumentos.";
		}else if(num==202){
			cadena=num+" Orden no implementada, no necesaria en este sistema.";
		}else if(num==502){
			cadena=num+" Orden no implementada.";
		}else if(num==503){
			cadena=num+" Secuencia de �rdenes incorrecta.";
		}else if(num==504){
			cadena=num+" Orden no implementada para ese par�metro.";
		}else if(num==211){
			cadena=num+" Estado del sistema o respuesta de ayuda del sistema.";
		}else if(num==212){
			cadena=num+" Estado del directorio.";
		}else if(num==213){
			cadena=num+" Estado del fichero.";
		}else if(num==214){
			cadena=num+" Mensaje de ayuda.";
		}else if(num==120){
			cadena=num+" El servicio estar� en funcionamiento en unos minutos.";
		}else if(num==220){
			cadena=num+" Servicio preparado para nuevo usuario.";
		}else if(num==221){
			cadena=num+" Cerrando la conexi�n de control.";
		}else if(num==421){
			cadena=num+" Servicio no disponible, cerrando la conexi�n de control.";
		}else if(num==125){
			cadena=num+" La conexi�n de datos ya est� abierta; comenzando transferencia.";
		}else if(num==225){
			cadena=num+" Conexi�n de datos abierta; no hay transferencia en proceso.";
		}else if(num==425){
			cadena=num+" No se puede abrir la conexi�n de datos.";
		}else if(num==226){
			cadena=num+" Cerrando la conexi�n de datos(accion correcta).";
		}else if(num==426){
			cadena=num+" Conexi�n cerrada; transferencia interrumpida.";
		}else if(num==227){
			cadena=num+" Iniciando modo pasivo (h1,h2,h3,h4,p1,p2).";
		}else if(num==230){
			cadena=num+" Usuario conectado, contin�e.";
		}else if(num==530){
			cadena=num+" No est� conectado.";
		}else if(num==331){
			cadena=num+" Usuario OK, necesita contrase�a.";
		}else if(num==332){
			cadena=num+" Necesita una cuenta para entrar en el sistema.";
		}else if(num==532){
			cadena=num+" Necesita una cuenta para almacenar ficheros.";
		}else if(num==150){
			cadena=num+" Estado del fichero correcto; va a abrirse la conexi�n de datos.";
		}else if(num==250){
			cadena=num+" La acci�n sobre fichero solicitado finaliz� correctamente.";
		}else if(num==350){
			cadena=num+" La acci�n requiere m�s informaci�n";
		}else if(num==450){
			cadena=num+" Acci�n no realizada/Fichero no disponible";
		}else if(num==451){
			cadena=num+" Acci�n no realizada/Fichero no disponible";
		}else if(num==551){
			cadena=num+" Acci�n interrumpida. Tipo de p�gina desconocido.";
		}else if(num==452){
			cadena=num+" Acci�n no realizada. Falta de espacio en el sistema de ficheros.";
		}else if(num==552){
			cadena=num+" Acci�n interrumpida. (para el directorio actual).";
		}else if(num==553){
			cadena=num+" Acci�n no realizada. Nombre de fichero no permitido.";
		}else{
			
			if(num/100==1){
				cadena="Respuesta preliminar positiva";
			}else if(num/100==2){
				cadena="Respuesta de finalizaci�n positiva";
			}else if(num/100==3){
				cadena="Respuesta intermedia positiva";
			}else if(num/100==4){
				cadena="Repuesta de finalizaci�n negativa transitoria";
			}else if(num/100==5){
				cadena="Respuesta de finalizaci�n negativa permanente";
			}
				
			if((num/10)%10==0){
				cadena+=" :Sintaxis";
			}else if((num/10)%10==1){
				cadena+=" :Informaci�n";
			}else if((num/10)%10==2){
				cadena+=" :Conexiones";
			}else if((num/10)%10==3){
				cadena+=" :Autenticaci�n y cuenta";
			}else if((num/10)%10==4){
				cadena+=" :Sin especificar a�n";
			}else if((num/10)%10==5){
				cadena+=" :Sistema de ficheros";
			}
		}
		
		return cadena;
	
	}
}

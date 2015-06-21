/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package rysd.practica7;

public class RySDPractica7 {

    public static void main(String[] args) {
      
    	// TODO code application logic here
        ClienteFTP cliente = new ClienteFTPImpl();
        System.out.println("****************************************************");
        cliente.abrirSesionFTP("ftp.uma.es");
        System.out.println("****************************************************");
        cliente.enviarUsuario("anonymous");
        System.out.println("****************************************************");
        cliente.enviarContraseña("");
        System.out.println("****************************************************");
        cliente.enviarCambioDeDirectorio("pub");
        System.out.println("****************************************************");
       // cliente.muestraFichero("README");
        //System.out.println("****************************************************");
        cliente.descargarFichero("README");
        System.out.println("****************************************************");
        cliente.cerrarSesionFTP();
    }
}

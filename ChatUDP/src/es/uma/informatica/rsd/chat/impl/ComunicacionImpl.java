package es.uma.informatica.rsd.chat.impl;

import es.uma.informatica.rsd.chat.ifaces.Comunicacion;
import es.uma.informatica.rsd.chat.ifaces.Controlador;
import es.uma.informatica.rsd.chat.impl.DialogoPuerto.PuertoAlias;

import java.io.IOException;
import java.net.*;

public class ComunicacionImpl implements Comunicacion {

	private DatagramSocket socket = null;
	private DatagramSocket clientSocket = null;
	private DatagramPacket paquete = null;
	private MulticastSocket multisocket = null;
	private int Puerto;
	private String nombre;
	private Controlador control;
	private byte[] receiveData = new byte[1024];
	private byte[] sendData = new byte[1024];

	public void crearSocket(PuertoAlias puer) {
		try {
			socket = new MulticastSocket(puer.puerto);
			Puerto = puer.puerto;
			nombre = puer.alias;
		} catch (IOException e) {
			e.getMessage();
		}

	}

	public void setControlador(Controlador c) {
		this.control = c;
	}

	public void runReceptor() {
		byte[] buffer;
		int contador;
		while (true) {
			buffer = new byte[100];
			paquete = new DatagramPacket(buffer, buffer.length);
			String cabecera = " ";
			contador = 0;
			try {
				multisocket.receive(paquete);
				// si es multicast
				if (((int) (short) buffer[0] & 0xff) == 1) {
					for (int i = 0; i < 4; i++) {
						cabecera += (short) ((short) buffer[i + 1] & 0xff)
								+ ".";
					}
					cabecera = cabecera.substring(0, cabecera.length() - 1)
							+ "/" + paquete.getPort() + ":";
					// lo quito los 8 bits de ip.
					contador += 4;

				}
				contador++;
				int tamAlias = (int) ((short) buffer[contador] & 0xff);
				contador++;
				byte[] alias = new byte[tamAlias];
				for (int j = 0; j < tamAlias; j++) {
					// sirve para ver el alias.
					alias[j] = buffer[j + contador];
					contador += tamAlias;
				}
				int indicador = 0;
				while (buffer[indicador + contador] != 0) {
					indicador++;
				}
				byte[] contenido = new byte[indicador];
				System.arraycopy(buffer, contador, contenido, 0,
						contenido.length);
				cabecera += new String(alias);

				if (((int) (short) buffer[0] & 0xff) == 1) {
					byte[] dir = { buffer[1], buffer[2], buffer[3], buffer[4] };
					InetAddress direccion = InetAddress.getByAddress(dir);
					paquete.setAddress(direccion);
				}
				if (!(new String(alias).equals(nombre))) {
					this.control.mostrarMensaje(paquete.getSocketAddress(),
							(cabecera + "-->" + new String(contenido)));
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void envia(InetSocketAddress sa, String mensaje) {

		int contador = 0;
		InetAddress inetdir = sa.getAddress();
		byte[] buffer = new byte[100];
		if (inetdir.isMulticastAddress()) { // multicast
			buffer[0] = new Integer(1).byteValue();
			contador++;
			System.arraycopy(inetdir.getAddress(), 0, buffer, contador, 4);
			contador += 4;
		} else { // unicast
			buffer[0] = new Integer(0).byteValue();
			contador++;
		}

		buffer[contador] = new Integer(nombre.getBytes().length).byteValue();
		contador++;
		byte[] aliasMasMens = (nombre + mensaje).getBytes();
		System
				.arraycopy(aliasMasMens, 0, buffer, contador,
						aliasMasMens.length);
		DatagramPacket paq = new DatagramPacket(buffer, buffer.length, inetdir,
				sa.getPort());
		try {
			multisocket.send(paq);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void joinGroup(InetAddress multi) {
		try {
			multisocket.joinGroup(multi);
		} catch (IOException e) {
			e.getMessage();
		}
	}

	public void leaveGroup(InetAddress multi) {
		try {
			multisocket.leaveGroup(multi);
		} catch (IOException e3) {
			e3.getMessage();
			System.out.println("ese grupo ya se ha dejado o no existe");
		}
	}

}

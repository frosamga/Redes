package es.uma.informatica.rsd.chat.impl;

import java.awt.event.ActionEvent;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import es.uma.informatica.rsd.chat.ifaces.Comunicacion;
import es.uma.informatica.rsd.chat.ifaces.Controlador;
import es.uma.informatica.rsd.chat.ifaces.Vista;
import es.uma.informatica.rsd.chat.impl.DialogoPuerto.PuertoAlias;

public class ControladorImpl implements Controlador
{
	private Vista v;
	private Comunicacion c;
	private Map<String,InetSocketAddress> conversaciones;
	
	private String alias;
	
	public ControladorImpl()
	{
		conversaciones = new HashMap<String,InetSocketAddress>();
	}
	
	public void run(String [] args)
	{
		v = new Paneles();
		c = new ComunicacionImpl();

		c.setControlador(this);
		v.setControlador(this);
		
		JFrame frame = new JFrame();
		frame.getContentPane().add((JPanel)v);
		frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		frame.setTitle(v.TITLE);
		frame.pack();
		frame.setVisible(true);
		
		PuertoAlias pa = v.getPuertoEscuchaAlias();
		while (pa == null)
		{
			v.warning("Puerto y Alias", "Debe indicar el puerto de escucha y un alias");
			pa = v.getPuertoEscuchaAlias();
		}
		
		c.crearSocket(pa);
		alias = pa.alias;
		
		Thread t = new Thread(){
			public void run ()
			{
				c.runReceptor();
			}
		};
		
		t.setDaemon(true);
		t.start();
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		new ControladorImpl().run(args);
	}

	@Override
	public void actionPerformed(ActionEvent event)
	{
		String ac = event.getActionCommand();
		if (ac.equals(NUEVO))
		{
			// Pedir la IP y el puerto al usuario mediante una ventana modal
			InetSocketAddress sa = v.getIPPuerto();
			if (sa == null)
			{
				// El usuario ha cancelado la introducci—n de datos
				return;
			}
			// else
			String nombre = sa.toString();
			
			if (!conversaciones.containsKey(nombre))
			{
				conversaciones.put(nombre, sa);
				v.crearPanel(nombre);
				if (sa.getAddress().isMulticastAddress())
				{
					c.joinGroup(sa.getAddress());
				}
			}
			else
			{
				v.warning("Conversaci—n", "La ventana de conversaci—n ya existe");
			}
		}
		else if (ac.startsWith(PREFIJO_ENVIAR))
		{
			String nombre = ac.substring(PREFIJO_ENVIAR.length());
			String mensaje = v.getMensaje(nombre);
			
			v.mostrarMensaje(nombre, alias+">"+mensaje, Vista.PROPIO);
			c.envia(conversaciones.get(nombre), mensaje);
		}
		else if (ac.startsWith(PREFIJO_CERRAR))
		{
			String nombre = ac.substring(PREFIJO_CERRAR.length());
			InetSocketAddress sa = conversaciones.get(nombre);
			
			if (sa.getAddress().isMulticastAddress())
			{
				c.leaveGroup(sa.getAddress());
			}
			
			v.cerrarPanel(nombre);
			conversaciones.remove(nombre);
		}
	}

	@Override
	public void mostrarMensaje(SocketAddress sa, String mensaje)
	{
		InetSocketAddress isa = (InetSocketAddress)sa;
		String nombre = isa.toString();
		if (!conversaciones.containsKey(nombre))
		{
			conversaciones.put(nombre, isa);
			v.crearPanel(nombre);
		}
		v.mostrarMensaje(nombre, mensaje, Vista.AJENO);
		
	}

}

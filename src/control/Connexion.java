/*______________________________*/
/**
 * 
 */
package control;

import java.net.BindException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * 			la classe connexion qui gere le soket UDP il
 *         faut avoir datagrampacket qui contient tous les data et avoir socket
 *         pour envoyer des donnees.
 * 
 */
public class Connexion
{
	private DatagramSocket socket;
	private DatagramPacket paquet;
	private InetAddress adresse = null;
	private int port;

	private static Connexion instanceServer=null;
	private static Connexion instanceClient=null;
	/**
	 * le constructeur pour client il faut avoir ip et port
	 * @param host ip ou adresse qu'il veut connecter
	 * @param port la port
	 * @throws SocketException 
	 * @throws UnknownHostException 
	 */
	private Connexion(String host, int port) throws  UnknownHostException, Exception
	{
			socket = new DatagramSocket();
			this.adresse = InetAddress.getByName(host);
			this.port = port;
	}

	/**
	 * le constructeur pour serveur
	 * @param port la porte
	 * @throws SocketException 
	 */
	private Connexion(int portServer) throws Exception
	{
			socket=new DatagramSocket(portServer);
	}
	
	
	/**
	 * obtenir une instance unique pour un serveur ou un client
	 * il faut fermer la connexion quelque part.
	 * @param isServeur true pour serveur false pour client
	 * @return une instance de cette classe
	 * @throws BindException si le port n'est pas disponible
	 * @throws Exception autre cas
	 */
	public static Connexion getInstance(boolean isServeur) throws BindException, Exception
	{
		if(instanceServer==null||instanceClient==null)
		{
			if(isServeur)
				return instanceServer=new Connexion(Config.PORT);
			else {
				return instanceClient=new  Connexion(Config.readConfClient().getProperty(Config.ADRESSE), Config.PORT);
			}
		}
		return null;
	}
	
	/**
	 * envoyer des message
	 * @param message un message
	 * @throws UnknownHostException 
	 * @throws Exception
	 */
	public void envoyer(String message) throws UnknownHostException,Exception
	{
		byte[] buffer=message.getBytes();
		paquet=new DatagramPacket(buffer, buffer.length,this.adresse,this.port);
		socket.send(paquet);
	}
	
	/**
	 * pour recvoir un paquet qui contient un message
	 * @return un message
	 * @throws Exception 
	 */
	public String recvoir() throws Exception
	{
		byte[]buffer=new byte[1024];
		paquet=new DatagramPacket(buffer, buffer.length);
		socket.receive(paquet);
		if(adresse==null)
		{
			adresse=paquet.getAddress();
			port=paquet.getPort();
		}
		return new String(buffer).trim();
	}
	
	/**
	 * la fermeture de connexion
	 */
	public void finirConnexion()
	{
		socket.close();
	}
}

/* ______________________________ */
/* ___________FIN_______________ */
/* ______________________________ */
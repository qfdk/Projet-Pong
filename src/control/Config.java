/*______________________________*/
/**
 * 
 */
package control;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * @author qfdk
 * Cree le 2014年2月9日
 * La classe Config contien tous les informations qu'on veuts utiliser.
 * On peut enregistrer tous les informations nessaisaires.
 */
public class Config
{
	/**
	 * la port fix
	 */
	public static final  int PORT=8888;
	/**
	 * l'addresse pour le fichier xml
	 */
	public static final String ADRESSE="ADRESSE";
	/**
	 * pseudo pour  le fichier xml
	 */
	public static final String PSEUDO="PSEUDO";
	/**
	 * le chemin pour le onfig client
	 */
	private static final String CONF_CLIENT="config/client.xml";
	/**
	 * le chemin pour config server
	 */
	private static final String CONF_SERVER="config/server.xml";
	
	/**
	 * le point max 
	 */
	public static int maxPoints=5;

	/**
	 * les information pour un client
	 * @param ip ip 
	 * @param pseudo pseudo 
	 * @throws IOException Si on ne peut pas enrgistrer les fichiers correctement.
	 */
	public static void saveConfClient(String ip,String pseudo) throws IOException
	{
		Properties prop = new Properties();  
		prop.setProperty(Config.ADRESSE, ip);
		prop.setProperty(Config.PSEUDO, pseudo);
		FileOutputStream fos=new FileOutputStream(CONF_CLIENT);
		prop.storeToXML(fos, "Config Client");
		fos.close();
	}
	
	/**
	 * les information pour un serveur
	 * @param pseudo
	 * @throws IOException
	 */
	public static void saveConfServer(String pseudo) throws IOException
	{
		Properties prop=new Properties();
		prop.setProperty(PSEUDO, pseudo);
		FileOutputStream fos=new FileOutputStream(CONF_SERVER);
		prop.storeToXML(fos, "Config Server");
		fos.close();
	}
	
	/**
	 * readConfClient
	 * @return la poerties
	 * @throws IOException
	 */
	public static Properties readConfClient() throws IOException
	{
		FileInputStream fis=new FileInputStream(CONF_CLIENT);
		Properties prop=new Properties();
		prop.loadFromXML(fis);
		fis.close();
		//prop.getProperty(ADRESSE);
		//prop.getProperty(PSEUDO);
		return prop;
	}
	
	/**
	 * readConfServer
	 * @return les properties du server
	 * @throws IOException
	 */
	public static Properties readConfServer() throws IOException
	{
		FileInputStream fis=new FileInputStream(CONF_SERVER);
		Properties prop=new Properties();
		prop.loadFromXML(fis);
		fis.close();
		//prop.getProperty(ADRESSE);
		//prop.getProperty(PSEUDO);
		return prop;
	}
}

/*______________________________*/
/*___________FIN_______________*/
/*______________________________*/
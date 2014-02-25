/*______________________________*/
/**
 * 
 */
package control;

import java.awt.Point;
import java.util.ArrayList;
/**
 * @author qfdk
 * Cree le 2014年2月2日
 * La classe permet de manipuler notre datagram.
 * 
 */
public class MyDatagrame
{
	private static String SEPARATEUR="#";
	private static ArrayList<Object> liste=new ArrayList<>();

	/**
	 * la methode pour enCoder notre datagram
	 * @param balle la position de la balle
	 * @param balleSpeed 
	 * @param joueur la positon du joueur
	 * @param pseudo le pseudo du joueur
	 * @return une chaine encodant.
	 */
	public static String enCoder(Point balle,Point balleSpeed,Point joueur,String pseudo)
	{
		StringBuilder sb=new StringBuilder();
		sb.append(balle.x).append(SEPARATEUR)
		.append(balle.y).append(SEPARATEUR)
		.append(balleSpeed.x).append(SEPARATEUR)
		.append(balleSpeed.y).append(SEPARATEUR)
		.append(joueur.x).append(SEPARATEUR)
		.append(joueur.y).append(SEPARATEUR)
		.append(pseudo);
		return sb.toString();
	}

	/**
	 * une methode qui permet de decoder notre datagramme.
	 * @param msg datagrame qui va decoder
	 * @return une liste contient tous les elements
	 */
	public static ArrayList<Object> deCoder(String msg)
	{
		int[]tmp=String2Int(msg.split(SEPARATEUR));
		Point points[]={
				new Point(tmp[0],tmp[1]),
				new Point(tmp[2], tmp[3]),
				new Point(tmp[4], tmp[5])
		};
		liste.clear();
		String pseudo=msg.split(SEPARATEUR)[6];
		liste.add(points);
		liste.add(pseudo);
		return liste;
	}
	
	/**
	 * Passer un tableau de String au tableau de l'entier
	 * @param data notre message
	 * @return
	 */
	private static int[] String2Int(String[]data)
	{
		int tmp[]=new int[data.length-1];
		for(int i=0;i<data.length-1;i++)
		{
			tmp[i]=Integer.parseInt(data[i]);
		}
		return tmp;
	}
}

/*______________________________*/
/*___________FIN_______________*/
/*______________________________*/
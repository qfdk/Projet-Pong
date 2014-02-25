/*______________________________*/
/**
 * 
 */
package control;

import vue.MafenetreLancer;

/**
 * @author qfdk
 * Cree le 2014年2月10日
 */
public class Lancer
{

	/**
	 * Pour lancer deux fenetres en meme temps.
	 * @param args
	 */
	public static void main(String[] args)
	{
		MafenetreLancer app=new MafenetreLancer("Lancer Pong");
		app.setVisible(true);
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				MafenetreLancer app=new MafenetreLancer("Lancer Pong");
				app.setVisible(true);
				app.setLocation(40, 50);
			}
		}).start();;
	}

}

/*______________________________*/
/*___________FIN_______________*/
/*______________________________*/
/*______________________________*/
/**
 * 
 */
package control;

import vue.MirrorPong;
import vue.Pong;

/**
 * @author qfdk
 * Cree le 2014年2月11日
 */
public class App
{

	/**
	 * Une classe pour tester rapitement.
	 * @param args
	 * @throws InterruptedException 
	 */
	public static void main(String[] args) 
	{
		new MirrorPong(true).init();
		try
		{
			Thread.sleep(1000);
		} catch (InterruptedException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		new Thread(new Runnable()
		{
			@Override
			public void run()
			{
				new Pong(false).init();
			}
		}).start();;
		
	}

}

/*______________________________*/
/*___________FIN_______________*/
/*______________________________*/
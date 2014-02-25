package vue;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.BindException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

import control.Config;
import control.Connexion;
import control.MyDatagrame;

public class AiPong extends JFrame implements Runnable, MouseListener,
		MouseMotionListener,WindowListener
{
	private static final long serialVersionUID = 7657998555042629676L;
	private Thread runner;
	private Image offscreeni;
	private Graphics offscreeng;
	private Rectangle plane;
	private Point ballPoint, joueur1, joueur2, ballSpeed;
	private boolean start, death = false;
	private int joueur1Score, joueur2Score = 0;
	private int tuffhet = 8;
	private boolean isServeur=false;
	private Connexion conn;
	private String pseudo1="Client";
	private String pseudo2="ORDINATEUR";
	private Image image;
	/*
	 * 初始化程序
	 */
	public void init()
	{
		//creerConexion();
		addMouseListener(this);// ajoute des listener pour la souris
		addMouseMotionListener(this);
		setBounds(100, 100, 640, 480);
		setTitle(pseudo2+" VS "+pseudo1);
		setVisible(true); // rend visible la frame
		try
		{
			image = ImageIO.read(new File("img/bg.jpg"));
		} catch (IOException e1)
		{
			System.out.println("Pb de lire bg");
			System.exit(-1);
		}
		offscreeni = createImage(getWidth(), getHeight());
		try
		{
			offscreeng = offscreeni.getGraphics();
			setBackground(Color.black);
			ballPoint = new Point((getWidth() / 2), (getHeight() / 2)); // balle
			
			joueur1 = new Point((getWidth() - 35), ((getHeight() / 2) - 25)); // pav�
																	// joueur1
			joueur2 = new Point(35, ((getHeight() / 2) - 25)); // pav� joueur 2

			plane = new Rectangle(15, 15, (getWidth()), (getHeight() - 30));
			ballSpeed = new Point(0, 0);
			start(); // thread demarr�
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			// 绘图方法
			repaint();
		} catch (Exception e)
		{
			System.out.println("Erreur，初始化失败");
			System.exit(-1);
		}
	}



	public void start()
	{
		if (runner == null)
		{
			runner = new Thread(this);
			runner.start(); // thread demarrer voir ->>run()
		}
	}

	// corps du jeu
	// a ajouter 
	// 1ere partie => connection des joueurs (a faire ici ou avant)
	// calcul des positions et envoi � l'autre joueur
	@Override
	public void run()
	{
		while (true)
		{
			checkJoueur2();
			checkJoueur1();
			checkWalls(); //
			moveBall(); // calcul d�placement de la balle
			testWinner();
			moveJoueur2(joueur2.y + 25);
			//envoyerDonnees();
			//lireDonnees();
			
			repaint();
			try
			{
				Thread.sleep(10); // pause pour ralentir le jeu
			} catch (InterruptedException e)
			{
			}
		}
	}

	/**
	 * 
	 */
	private void testWinner()
	{
		if(Config.maxPoints==joueur1Score)
		{
			JOptionPane.showMessageDialog(null,pseudo1+" You are Winner !","Felicidation",JOptionPane.OK_OPTION,new ImageIcon("img/ok.gif"));
			//conn.finirConnexion();
			System.exit(0);
		}
		if(Config.maxPoints==joueur2Score)
		{
			JOptionPane.showMessageDialog(null,pseudo2+" a ganer",":( Perdu",JOptionPane.OK_OPTION,new ImageIcon("img/non.gif"));
			//conn.finirConnexion();
			System.exit(0);
		}
		
	}
	/**
	 *Creation d'une connexion
	 */
	private void creerConexion()
	{
			try
			{
				conn=Connexion.getInstance(isServeur);
			} catch (BindException e)
			{
				System.out.println("pb--->creation");
				conn.finirConnexion();
				System.exit(-1);
			}
			catch (UnknownHostException e) {
				JOptionPane.showMessageDialog(this, "Host inconnu,le jeux va fermer \n changez l'adresse ,SVP");
				conn.finirConnexion();
				System.exit(-1);
			}
			catch (Exception e) {
				System.out.println("Error inconnu");
				conn.finirConnexion();
				System.exit(-1);
			}
	}
	/**
	 * 发送数据
	 */
	private void envoyerDonnees()
	{
		try
		{
			conn.envoyer(MyDatagrame.enCoder(ballPoint,ballSpeed, joueur1,pseudo1));
		} catch (Exception e)
		{  
			System.out.println("Client->Problem d'envouyer des donnees");
			conn.finirConnexion();
			System.exit(-1);
		}
	}

	/**
	 * 接收数据 并且移动位置
	 */
	private void lireDonnees()
	{
		try
		{
			moveDisdance(MyDatagrame.deCoder(conn.recvoir()));
		} catch (Exception e)
		{
			System.out.println("Client->Problem de lire des donnees");
			conn.finirConnexion();
			System.exit(-1);
		}
	}

	/**
	 * 在另一端移动
	 * @param pionts 点的集合
	 */
	private void moveDisdance(ArrayList arrary)
	{
		Point []pionts=(Point[]) arrary.get(0);
		if(start)
			ballPoint = pionts[0];
		else
			ballSpeed = pionts[1];
		joueur2 = pionts[2];
		pseudo2=(String) arrary.get(1);
//		System.out.println("cc->"+start);
	}
	// pour demarrer le jeu
	@Override
	public void mouseClicked(MouseEvent e)
	{
		if (start == false)
		{
			//给球一个初始的速度 也就是移动方向。
			ballSpeed.x = 4;
			ballSpeed.y = 2;
			start = true; // demarre le jeu
		}
	}

	// d�place le pav� du joueur 1
	@Override
	public void mouseMoved(MouseEvent e)
	{
		joueur1.y = (e.getY() - 25);
		repaint();
	}

	private void moveBall()
	{
		ballPoint.x = (ballPoint.x + ballSpeed.x);
		ballPoint.y = (ballPoint.y + ballSpeed.y);
	}

	
	/**
	 * 智能电脑AI 自动移动
	 * @param enemyPos
	 */
	private void moveJoueur2(int enemyPos)
	{
		int dist = java.lang.Math.abs(ballPoint.y - enemyPos);
		if (ballSpeed.x < 0)
		{
			if (enemyPos < (ballPoint.y - 3))
				joueur2.y = (joueur2.y + dist / tuffhet);
			else if (enemyPos > (ballPoint.y + 3))
				joueur2.y = (joueur2.y - dist / tuffhet);
		} else
		{
			if (enemyPos < (getHeight() / 2 - 3))
				joueur2.y = (joueur2.y + 2);
			else if (enemyPos > (getHeight() / 2 + 3))
				joueur2.y = (joueur2.y - 2);
		}
	}

	/**
	 * 对第一个用户的检测
	 * checkJoueur1
	 */
	private void checkJoueur1()
	{
		if (ballSpeed.x < 0)
			return;
		if ((ballPoint.x + ballSpeed.x) >= joueur1.x - 6
				& (ballPoint.x < joueur1.x))
			if ((ballPoint.y + 10 > joueur1.y & ballPoint.y < (joueur1.y + 50)))
			{
				int racketHit = (ballPoint.y - (joueur1.y + 25));
				ballSpeed.y = (ballSpeed.y + (racketHit / 7));
				ballSpeed.x = (ballSpeed.x * -1);
			}
	}

	/**
	 * 还是检测checkJoueur2
	 */
	private void checkJoueur2()
	{
		if (ballSpeed.x > 0)
			return;
		if ((ballPoint.x + ballSpeed.x) <= joueur2.x + 4
				& (ballPoint.x > joueur2.x))
			if ((ballPoint.y + 10 > joueur2.y & ballPoint.y < (joueur2.y + 50)))
			{
				int racketHit = (ballPoint.y - (joueur2.y + 25));
				ballSpeed.y = (ballSpeed.y + (racketHit / 7));
				ballSpeed.x = (ballSpeed.x * -1);
			}
	}

	// test des murs notamment droite et gauche => si balle entre dans mur
	// droite ou gauche
	// alors balle manqu�e
	private void checkWalls()
	{
		if ((ballPoint.x + ballSpeed.x) <= plane.x)
			miss();
		if ((ballPoint.x + ballSpeed.x) >= (plane.width - 20))
			miss();
		if ((ballPoint.y + ballSpeed.y) <= plane.y)
			ballSpeed.y = (ballSpeed.y * -1);
		if ((ballPoint.y + ballSpeed.y) >= (plane.height + 10))
			ballSpeed.y = (ballSpeed.y * -1);
	}

	// test balle manqu� par pav�
	private void miss()
	{
		if (ballSpeed.x < 0)
		{
			joueur1Score = (joueur1Score + 1);
			if (tuffhet > 2)
				tuffhet = (tuffhet - 1);
		} else
			joueur2Score = (joueur2Score + 1);
		ballSpeed.x = (ballSpeed.x * -1);
		ballPoint.x = (ballPoint.x + ballSpeed.x);
		for (int i = 3; i > 0; i = (i - 1))
		{
			death = true;
			repaint();
			try
			{
				Thread.sleep(300);
			} catch (InterruptedException e)
			{
			}
			;
			death = false;
			repaint();
			try
			{
				Thread.sleep(300);
			} catch (InterruptedException e)
			{
			}
			;
		}
		ballPoint = new Point((getWidth() / 2), (getHeight() / 2));
		ballSpeed.x = 0;
		ballSpeed.y = 0;
		start = false;
	}

	@Override
	public void update(Graphics g)
	{
		paint(g);
	}

	@Override
	public void paint(Graphics g)
	{
		if (offscreeng != null)
		{
			offscreeng.setColor(Color.black);
			offscreeng.fillRect(0, 0, getWidth(), getHeight());
			offscreeng.drawImage(image, 0, 0, 640, 480, null);
			if (death == false)
				offscreeng.setColor(Color.white);
			else
			{
				offscreeng.setColor(Color.red);
			}
			//charger des pseudos. 
			offscreeng.drawString(pseudo2+" : "+Integer.toString(joueur2Score), 100, 35);
			offscreeng.drawString(pseudo1+" : "+Integer.toString(joueur1Score), 215, 35);
			if (plane != null)
			{
				offscreeng.clipRect(plane.x, plane.y, plane.width - 28,
						plane.height + 1);
				offscreeng.drawRect(plane.x, plane.y, plane.width - 30,
						plane.height);
				offscreeng.fillRect(joueur1.x, joueur1.y, 6, 50);
				offscreeng.fillRect(joueur2.x, joueur2.y, 6, 50);
				
				offscreeng.fillOval(ballPoint.x, ballPoint.y, 8, 10);
			}
			g.drawImage(offscreeni, 0, 10, this);
		}
	}


	public AiPong()
	{
		this.addWindowListener(this);
		try
		{
			this.pseudo1=Config.readConfClient().getProperty(Config.PSEUDO);
		}
		catch(FileNotFoundException e)
		{
			JOptionPane.showMessageDialog(null, "AIProblem de lire le fichier config","Attention :X",JOptionPane.YES_OPTION,new ImageIcon("img/attention.gif"));
			System.out.println("AI:Problem de lire le fichier config ");
			System.exit(-1);
		}
		catch (IOException e)
		{
			System.exit(-1);
		}catch (Exception e) {
			System.exit(-1);
		}
	}
	public static void main(String[] arg)
	{
		AiPong jp = new AiPong();
		jp.init();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{

	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		
	}

	/* 
	 * pour fermer la connexion
	 * @see java.awt.event.WindowListener#windowClosed(java.awt.event.WindowEvent)
	 */
	@Override
	public void windowClosed(WindowEvent e)
	{
		//conn.finirConnexion();			
	}



	@Override
	public void windowClosing(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		JOptionPane.showMessageDialog(null, "Le jeu va fermer !","Attention :X",JOptionPane.YES_OPTION,new ImageIcon("img/attention.gif"));
	}



	@Override
	public void windowDeactivated(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		
	}



	@Override
	public void windowDeiconified(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		
	}



	@Override
	public void windowIconified(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		
	}



	@Override
	public void windowOpened(WindowEvent e)
	{
		// Pense a implemente Auto-generated method stub
		
	}
}
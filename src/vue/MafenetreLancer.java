/*______________________________*/
/**
 * 
 */
package vue;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.ImageIcon;

import control.Config;

/**
 * @author qfdk 
 * Cree le 2014年1月29日
 */
public class MafenetreLancer extends JFrame implements ActionListener
{
	/**
	 * des varialbe
	 */
	private static final long serialVersionUID = 1L;
	private JTextField tf_adresse;
	private JTextField tf_perdo;
	private JButton bp_start;
	private JButton bp_exit;
	private JRadioButton rd_client;
	private JRadioButton rd_serveur;
	private JRadioButton rd_ai;
	private JLabel lb_infos;
	/**
	 * Le constructeur
	 * @param titre
	 */
	public MafenetreLancer(String titre)
	{
		super(titre);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);
		this.setResizable(false);
		initialiserComposant();
		attachementDesEvenement();
		getContentPane().setLayout(new BorderLayout());
		this.getContentPane().add(creerPanel(), BorderLayout.CENTER);
		pack();
	}

	/**
	 * attachementDesEvenement
	 */
	private void attachementDesEvenement()
	{
		bp_start.addActionListener(this);
		bp_exit.addActionListener(this);
		rd_client.addActionListener(this);
		rd_serveur.addActionListener(this);
		rd_ai.addActionListener(this);
	}

	/**
	 * pour creer tous les composant
	 * sinon nullpointerException
	 */
	private void initialiserComposant()
	{
		tf_adresse = new JTextField(10);
		tf_adresse.setText("localhost");
		tf_adresse.setVisible(false);
		
		tf_perdo = new JTextField(10);
		bp_start = new JButton("Start");
		bp_exit = new JButton("Exit");
		rd_client = new JRadioButton("Client");
		rd_serveur = new JRadioButton("Serveur");
		rd_ai=new JRadioButton("AI");
		lb_infos=new JLabel("");
	}

	/**
	 * creer Panel principale
	 */
	private JPanel creerPanel()
	{
		JPanel jp = new JPanel(new BorderLayout());
		JLabel imageLogo = new JLabel("");
		ImageIcon image = new ImageIcon("img/filet.png");
		// image.setImage(image.getImage().getScaledInstance(560,250,Image.SCALE_DEFAULT));
		imageLogo.setIcon(image);
		jp.add(imageLogo, BorderLayout.NORTH);
		jp.add(creerPanelInfo(), BorderLayout.CENTER);
		return jp;
	}

	/**
	 * Creer le panel qui contient tous les informations
	 * 
	 */
	private JPanel creerPanelInfo()
	{

		JPanel perdo = creerFlowLayoutPour2Composant(new JLabel("Pseudo:"),tf_perdo);
		JPanel adresse = creerFlowLayoutPour2Composant(lb_infos, tf_adresse);
		JPanel jp_buttons = creerFlowLayoutPour2Composant(bp_start, bp_exit);
		JPanel jp_choisir = creerFlowLayoutPour2Composant(rd_client, rd_serveur);
		jp_choisir.add(rd_ai);
		ButtonGroup choisir = new ButtonGroup();
		choisir.add(rd_client);
		choisir.add(rd_serveur);
		choisir.add(rd_ai);
		JPanel jp_info = new JPanel(new GridLayout(2, 2));
		jp_info.add(perdo);
		jp_info.add(jp_choisir);
		jp_info.add(adresse);
		jp_info.add(jp_buttons);

		return jp_info;
	}

	/**
	 * creer une panel de type FlowLayoutPour2Composant
	 * 
	 * @param a
	 *            premier compossant
	 * @param b
	 *            deuxisme compossant
	 * @return un jpanel
	 */
	private static JPanel creerFlowLayoutPour2Composant(JComponent a, JComponent b)
	{
		JPanel jp = new JPanel(new FlowLayout());
		jp.add(a);
		jp.add(b);
		return jp;
	}

	/* (non-Javadoc)
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(ActionEvent e)
	{
		if(e.getSource()==bp_exit)
		{
			System.exit(0);
		}
		
		if(rd_client.isSelected())
		{
			lb_infos.setText("IP du serveur:");
			tf_adresse.setVisible(true);
			lb_infos.setVisible(true);
		}
		else if(rd_serveur.isSelected())
		{
			lb_infos.setText("Cliquer Start pour lancer le serveur !");
			tf_adresse.setVisible(false);
		}
		else if (rd_ai.isSelected())
		{
			lb_infos.setText("Ajouter avec un AI !");
			tf_adresse.setVisible(false);
		}
		else{
			lb_infos.setText("Il faut choisir le role! ");
		}
		
		
		if(e.getSource()==bp_start&&rd_client.isSelected())
		{
			if(tf_perdo.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Il faut choisir un pseudo","Attentions",JOptionPane.YES_OPTION,new ImageIcon("img/attention.gif"));
				tf_perdo.requestFocus();
			}else{
			if(!tf_adresse.equals(""))
				{
					try
					{
						Config.saveConfClient(tf_adresse.getText(), tf_perdo.getText());
					} catch (IOException e1)
					{
						System.out.println("pb d'enrgistrer");
					}	
				}
			new Pong(false).init();
			this.setVisible(false);
			}
		}
		if(e.getSource()==bp_start&&rd_serveur.isSelected())
		{
			if(tf_perdo.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Il faut choisir un pseudo","Attentions",JOptionPane.YES_OPTION,new ImageIcon("img/attention.gif"));
				tf_perdo.requestFocus();
			}else{
			if(!tf_perdo.getText().equals(""))
				try
				{
					Config.saveConfServer(tf_perdo.getText());
				} catch (IOException e1)
				{
					System.out.println("pb d'enrgestirer");
				}
			new MirrorPong(true).init();
			this.setVisible(false);
			}
		}
		if(e.getSource()==bp_start&&rd_ai.isSelected())
		{
			if(tf_perdo.getText().equals(""))
			{
				JOptionPane.showMessageDialog(null, "Il faut choisir un pseudo","Attentions",JOptionPane.YES_OPTION,new ImageIcon("img/attention.gif"));
				tf_perdo.requestFocus();
			}else{
			if(!tf_perdo.getText().equals(""))
				try
				{
					Config.saveConfClient("localhost", tf_perdo.getText());
				} catch (IOException e1)
				{
					System.out.println("pb d'enrgestirer");
				}
			new AiPong().init();
			this.setVisible(false);
			}
		}
	}

}

/* ______________________________ */
/* ___________FIN_______________ */
/* ______________________________ */
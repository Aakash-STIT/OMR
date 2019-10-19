package omr;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UIManager.LookAndFeelInfo;
import omr.gui.Gui;

/**
 * Main class of the application. Starts the Gui.
 * 
 * @author Tapio Auvinen
 */
public class Omr extends JFrame {

	private static final long serialVersionUID = 1L;

	public static void main(String[] args) {
		try {
			javax.swing.SwingUtilities.invokeAndWait(new Runnable() {
				public void run() {
					new Omr();
				}
			});
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	JButton blogin = new JButton("Login");
	JPanel panel = new JPanel();
	JTextField txuser = new JTextField(15);
	JPasswordField pass = new JPasswordField(15);

	Omr() {
		super("Login Autentification");

		setSize(300, 200);
		setLocation(500, 280);
		panel.setLayout(null);

		txuser.setBounds(70, 30, 150, 20);
		pass.setBounds(70, 65, 150, 20);
		blogin.setBounds(110, 100, 80, 20);

		panel.add(blogin);
		panel.add(txuser);
		panel.add(pass);

		getContentPane().add(panel);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setVisible(true);
		actionlogin();
	}

	public void actionlogin() {
		blogin.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent ae) {
				String puname = txuser.getText();
				String ppaswd = pass.getText();
				
				try {
		            Scanner in = new Scanner(new File("USERDATA.txt"));
		            while (in.hasNextLine())
		            {
		              String s = in.nextLine();  
		              String[] sArray = s.split(",");
		              
		             if (puname.equalsIgnoreCase(sArray[0]) && ppaswd.equals(sArray[1])) {		            	  
		  					createAndShowGUI();
		  					dispose();
		  					break;
		  				} else {
		  					if(in.hasNext()!=true) {
		  					JOptionPane.showMessageDialog(null, "Wrong Password / Username");
		  					txuser.setText("");
		  					pass.setText("");
		  					txuser.requestFocus();}
		  				}
		            }
		            
		            in.close();
		            
		        } catch (FileNotFoundException e) {
		            JOptionPane.showMessageDialog(null,
		                    "User Database Not Found", "Error",
		                    JOptionPane.ERROR_MESSAGE);
		        }
				
				

			}
		});
	}

	public void createAndShowGUI() {
		// Set look and feel
		try {
			for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
				if ("Nimbus".equals(info.getName())) {
					UIManager.setLookAndFeel(info.getClassName());
					break;
				}
			}
		} catch (Exception e) {
		}

		// Create gui
		new Gui();
	}
}

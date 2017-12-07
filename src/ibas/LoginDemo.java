package ibas;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Greg
 */

/*
    The main method where the GUI is called.
*/
public class LoginDemo {
	public static void main(String args[]) {
		try {
			Login frame = new Login();
			frame.setSize(900, 500);
			frame.setVisible(true);
                        frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
                        
			
		} catch (Exception e) {
			JOptionPane.showMessageDialog(null, e.getMessage());
		}
	}
}
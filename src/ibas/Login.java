package ibas;

/*
This is the main Java application. When run the GUI interface pops up and 
prompts the user with login information. Once login has been confirmed additional
options appear.
*/
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.*;


class Login extends JFrame implements ActionListener {
	
        /*
            JButton's SUBMIT & SIGNUP are the buttons in the initial GUI.
            SUBMIT is for existing users so that they can log in to their accounts
            SIGNUP is for new users to set up their account initially.
    
            JLabel's consist of usernameLabel and passwordLabel, these do as 
            their suggest and are the labels for "Username:" and "Password:"
            in the GUI,
    
            JText's usernameText & passwordText, are the fields that the user 
            enters when populating their login information
        */
    
        JButton SUBMIT;
	JButton SIGNUP;
	JPanel panel;
	JLabel usernameLabel, passwordLabel;
	final JTextField usernameText, passwordText;
        
        // DB Variables
        Connection conn;
        String connectionURL;
        String dbName = "ibas";

	Login() {
		usernameLabel = new JLabel();
		usernameLabel.setText("Username:");
		usernameText = new JTextField(15);

		passwordLabel = new JLabel();
		passwordLabel.setText("Password:");
		passwordText = new JPasswordField(15);

		SUBMIT = new JButton("SUBMIT");
		SIGNUP = new JButton("SIGN UP");

		panel = new JPanel(new GridLayout(3, 1));
		panel.add(usernameLabel);
		panel.add(usernameText);
		panel.add(passwordLabel);
		panel.add(passwordText);
		panel.add(SIGNUP);
		panel.add(SUBMIT);
		add(panel, BorderLayout.CENTER);
		SUBMIT.addActionListener(this);
		SIGNUP.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				RegistrationPage page=new RegistrationPage();
				page.setSize(900, 500);
				page.setVisible(true);
				panel.setVisible(false);
			}
		});
		setTitle("LOGIN FORM");
	}

        /*
        This is where the users credentials are checked against the database
        */
        @Override
	public void actionPerformed(ActionEvent ae) {
		String value1 = usernameText.getText();
		String value2 = passwordText.getText();
		
                
                /** DB SQL SCRIPT:
                * CREATE TABLE users (
                *  user_id INTEGER NOT NULL GENERATED ALWAYS AS IDENTITY (START WITH 1, INCREMENT BY 1) PRIMARY KEY,
                *  username VARCHAR(64) NOT NULL,
                *  password VARCHAR(64) NOT NULL
                * );
                * -- Default Admin account
                * INSERT INTO users (username, password) VALUES('abc', 'abc');
                */
                
                // Connection String for Derby DB
                connectionURL = "jdbc:derby://localhost:1527/" + dbName + 
                        ";create=true;";
                
                // DB Connection Attempt
                try {
                    conn = DriverManager.getConnection(connectionURL);
                } catch (SQLException ex) {
                    ex.getMessage();
                }
                // If no connection
                if (conn == null){
                    try {
                        throw new SQLException("Cannot connect to database.");
                    } catch (SQLException ex) {
                        ex.getMessage();
                    }
                }
                // If connection
                String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
                try {
                    // Prepared Statement
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, value1); // Input param 1
                    stmt.setString(2, value2); // Input param 2 
                    ResultSet rs = stmt.executeQuery(); // Execute SQL
                    // If valid login
                    if (rs.next()) {
			MainPage page = new MainPage();
			page.setSize(900, 500);
			page.setVisible(true);
			panel.setVisible(false);
                    // If not valid login
                    } else {
                        System.out.println("enter the valid username and password");
                        JOptionPane.showMessageDialog(this, "Incorrect login or password",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    rs.close();
                } catch (SQLException ex) {
                    ex.getMessage();
                } finally {
                    if (conn != null) {
                        try {
                            conn.close();
                        } catch (SQLException ex) {
                            ex.getMessage();
                        }
                    }
                }
	}
}
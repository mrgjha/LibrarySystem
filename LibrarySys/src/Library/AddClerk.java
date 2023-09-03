package Library;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import java.sql.*;

	public class AddClerk extends JInternalFrame implements ActionListener , FocusListener {

		private JPanel pNew = new JPanel();
		private JLabel lbUser, lbPass;
		private JTextField txtUser;
		private JPasswordField txtPass;
		private JButton btnOk, btnCancel;
		private long id=0;
		private Statement st;			//Statement for Getting the Required Table.

		//Constructor of Class.

		public AddClerk (Connection con) {

			//super(title, resizable, closable, maximizable, iconifiable)
			super ("Create New Clerk", false, true, false, true);
			setSize (280, 175);
			//Setting the Form's Labels.

			lbUser = new JLabel ("Clerk ID:");
			lbUser.setForeground (Color.black);
			lbUser.setBounds (20, 20, 100, 25);
		    lbPass = new JLabel ("Password:");
			lbPass.setForeground (Color.black);
		    lbPass.setBounds (20, 55, 100, 25);
		    
		    txtUser = new JTextField ();
			txtUser.setBounds (100, 20, 150, 25);
			txtUser.setHorizontalAlignment (JTextField.RIGHT);
			txtUser.addFocusListener (this);
			txtPass = new JPasswordField ();
			txtPass.setBounds (100, 55, 150, 25);

			//Setting the Form's Buttons.

			btnOk = new JButton ("OK");
			btnOk.setBounds (20, 100, 100, 25);
			btnOk.addActionListener (this);
			btnCancel = new JButton ("Cancel");
			btnCancel.setBounds (150, 100, 100, 25);
			btnCancel.addActionListener (this);

			txtUser.addKeyListener (new KeyAdapter () {
				public void keyTyped (KeyEvent ke) {
					char c = ke.getKeyChar ();
					if (! ((Character.isDigit (c)) || (c == KeyEvent.VK_BACK_SPACE))) {
						getToolkit().beep ();
						ke.consume ();
					}
				}
			}
			);
			//Setting Panel's Layout.

			pNew.setLayout (null);

			//Adding All the Controls in Panel.

			pNew.add (lbUser);
			pNew.add (lbPass);
			pNew.add (txtUser);
			pNew.add (txtPass);
			pNew.add (btnOk);
			pNew.add (btnCancel);

			//Adding Panel to the Form.

			getContentPane().add (pNew);

			try {
				st = con.createStatement ();	//Creating Statement Object.
			}
			catch (SQLException sqlex) {			//If Problem then Show the User a Message.
	 			JOptionPane.showMessageDialog (null, "A Problem Occured While Loading the Form.");
	 			dispose ();				//Closing the Form.
		 	}

			setVisible (true);
 
		}
		
		public void actionPerformed (ActionEvent ae) {

			Object obj = ae.getSource();

			if (obj == btnOk) {		//If OK Button Pressed.

				String password = new String (txtPass.getPassword());

				if (txtUser.getText().equals ("")) {
					txtUser.requestFocus();
					JOptionPane.showMessageDialog (this, "Username not Provided.");
				}
				else if (password.equals ("")) {
					txtPass.requestFocus();
					JOptionPane.showMessageDialog (this, "Password not Provided.");
				}
				else {
					try {	//INSERT Query to Add Book Record in Table.
						String q = "INSERT INTO Clerks " + 
							"VALUES ('" + txtUser.getText() + "', '" + password + "')";

						int result = st.executeUpdate (q);	//Running Query.
						if (result == 1) {			//If Query Successful.
							JOptionPane.showMessageDialog (this, "New Clerk has been Created.");
							txtUser.setText ("");
							txtPass.setText ("");
							txtUser.requestFocus ();
						}
						else {					//If Query Failed.
							JOptionPane.showMessageDialog (this, "Problem while Creating the User.");
							txtUser.setText ("");
							txtPass.setText ("");
							txtUser.requestFocus ();
						}
					}
					catch (SQLException sqlex) { }
				}

			}		

			if (obj == btnCancel) {		//If Cancel Button Pressed Unload the From.

				setVisible (false);
				dispose();

			}

		}
		
		public void focusGained (FocusEvent fe) { }

		public void focusLost (FocusEvent fe) {

			if (txtUser.getText().equals ("")) {	//If TextField is Empty.
			}
			else {
			
			id = Integer.parseInt (txtUser.getText ());	//Converting String to Numeric.
			long memberNo;					//Use for Comparing the Member's Id.
			boolean found = false;				//To Confirm the Member's Id Existance.

			try {	//SELECT Query to Retrieved the Record.
				String q = "SELECT * FROM Clerks WHERE id = " + id + "";
				ResultSet rs = st.executeQuery (q);	//Executing the Query.
				rs.next ();				//Moving towards the Record.
				memberNo = rs.getLong ("id");	//Storing the Record.
				if (memberNo == id) {			//If Record Found then Display Message.
					found = true;
					txtClear ();			//Clearing the TextFields.
					JOptionPane.showMessageDialog (this, id + " is already assigned.");
				}
				else {
					found = false;
				}
			}
			catch (SQLException sqlex) { }
		}

		}
		private void txtClear () {

			txtUser.setText ("");
			txtPass.setText ("");
			txtUser.requestFocus();
		}

}

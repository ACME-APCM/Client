package it.unitn.APCM.ACME.Client.Dials;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import it.unitn.APCM.ACME.Client.GuardConnection;
import it.unitn.APCM.ACME.Client.User;

/**
 * The type Login dial.
 */
//Dial used for the login
public class LoginDial extends JDialog {

    private JTextField tf_email;
    private JPasswordField pf_password;
    private JLabel lbl_email;
    private JLabel lbl_password;
    private JButton btn_login;
    private CommonDialFunction commonFunction = new CommonDialFunction();


    /**
     * Instantiates a new Login dial.
     *
     * @param parent the parent
     * @param user   the user
     */
    public LoginDial(Frame parent, User user) {
        super(parent, "Login", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbl_email = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbl_email, cs);

        tf_email = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(tf_email, cs);

        lbl_password = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbl_password, cs);

        pf_password = new JPasswordField(30);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(pf_password, cs);

        panel.setBorder(new LineBorder(Color.BLACK));

        //login button
        btn_login = new JButton("Login");

        btn_login.addActionListener(new ActionListener() {
            // Method to handle login
            public void actionPerformed(ActionEvent e) {
                GuardConnection conn = new GuardConnection();
                //Send a request to the guardConnection
                if (conn.httpRequestLogin("login", get_email(), get_password(), user)) {
                    // if login successful show a message and then close the login dial
                    commonFunction.showOptionPane(LoginDial.this, "Login", "Authenticated successfully", JOptionPane.INFORMATION_MESSAGE);      
                    dispose();
                } else {
                    //Show an error message if login is not successful and erase the text_area
                    commonFunction.showOptionPane(LoginDial.this, "Login", "Invalid username or password", JOptionPane.ERROR_MESSAGE);      
                    tf_email.setText("");
                    pf_password.setText("");
                }
            }
        });

        JPanel bp = new JPanel();
        bp.add(btn_login);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    /**
     * Gets email.
     *
     * @return the email
     */
    public String get_email() {
        return tf_email.getText().trim();
    }

    /**
     * Gets password.
     *
     * @return the password
     */
    public String get_password() {
        return new String(pf_password.getPassword());
    }
}
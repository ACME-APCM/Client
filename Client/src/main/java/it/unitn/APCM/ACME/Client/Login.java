package it.unitn.APCM.ACME.Client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.*;

public class Login extends JDialog {

    private JTextField tfEmail;
    private JPasswordField pfPassword;
    private JLabel lbEmail;
    private JLabel lbPassword;
    private JButton btnLogin;
    private boolean authenticated = false;

    public Login(Frame parent, User user) {
        super(parent, "Login", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbEmail = new JLabel("Email: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbEmail, cs);

        tfEmail = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 2;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(tfEmail, cs);

        lbPassword = new JLabel("Password: ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbPassword, cs);

        pfPassword = new JPasswordField(30);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(pfPassword, cs);

        panel.setBorder(new LineBorder(Color.GRAY));

        btnLogin = new JButton("Login");

        btnLogin.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GuardConnection conn = new GuardConnection();

                // add password hashing with argon2

                ArrayList<String> response = conn
                        .http_request("login?email=" + get_email() + "&password=" + get_password());

                if (response.get(0).equals("authenticated")) {
                    JOptionPane.showMessageDialog(Login.this,
                            "Authenticated successfully",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    authenticated = true;
                    user.setEmail(get_email());
                    user.setPassword(get_password());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(Login.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
                    tfEmail.setText("");
                    pfPassword.setText("");
                }
            }
        });

        JPanel bp = new JPanel();
        bp.add(btnLogin);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public String get_email() {
        return tfEmail.getText().trim();
    }

    public String get_password() {
        return new String(pfPassword.getPassword());
    }

    public boolean is_authenticated(){
        return this.authenticated;
    }
}
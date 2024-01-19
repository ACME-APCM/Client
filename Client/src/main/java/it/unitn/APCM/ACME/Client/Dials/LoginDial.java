package it.unitn.APCM.ACME.Client.Dials;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;

import it.unitn.APCM.ACME.Client.GuardConnection;
import it.unitn.APCM.ACME.Client.User;

public class LoginDial extends JDialog {

    private JTextField tf_email;
    private JPasswordField pf_password;
    private JLabel lbl_email;
    private JLabel lbl_password;
    private JButton btn_login;

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

        btn_login = new JButton("Login");

        btn_login.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                GuardConnection conn = new GuardConnection();

                String response = conn
                        .httpRequest("login?email=" + get_email() + "&password=" + get_password());

                if (response.equals("success")) {
                    JOptionPane.showMessageDialog(LoginDial.this,
                            "Authenticated successfully",
                            "Login",
                            JOptionPane.INFORMATION_MESSAGE);
                    user.setAuthenticated(true);
                    user.setEmail(get_email());
                    user.setPassword(get_password());
                    dispose();
                } else {
                    JOptionPane.showMessageDialog(LoginDial.this,
                            "Invalid username or password",
                            "Login",
                            JOptionPane.ERROR_MESSAGE);
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

    public String get_email() {
        return tf_email.getText().trim();
    }

    public String get_password() {
        return new String(pf_password.getPassword());
    }
}
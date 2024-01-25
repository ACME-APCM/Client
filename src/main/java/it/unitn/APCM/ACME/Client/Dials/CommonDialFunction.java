package it.unitn.APCM.ACME.Client.Dials;

import java.awt.Component;

import javax.swing.JFrame;
import javax.swing.JOptionPane;


import it.unitn.APCM.ACME.Client.User;

public class CommonDialFunction {
     //Method used in different dials to call a new login form to login or if token is expired
    protected void newLogin(User user) {
        final JFrame login_frame = new JFrame("Login");
        LoginDial login_dial = new LoginDial(login_frame, user);
        login_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        login_dial.setSize(500, 150);
        login_dial.setLocationRelativeTo(null);
        login_dial.setVisible(true);
    }

     //Method used to show an info or error panel to the user after requiring a service
    protected void showOptionPane(Component comp,String title, String msg, int msgType){
        JOptionPane.showMessageDialog(comp,
                    msg,
                    title,
                    msgType);
    }
}

package it.unitn.APCM.ACME.Client.ClientCommon;

import javax.swing.JFrame;

import it.unitn.APCM.ACME.Client.User;
import it.unitn.APCM.ACME.Client.Dials.LoginDial;

public class ClientCall {

    public void newLogin(User user) {
        final JFrame login_frame = new JFrame("Login");
        LoginDial login_dial = new LoginDial(login_frame, user);
        login_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        login_dial.setSize(500, 150);
        login_dial.setLocationRelativeTo(null);
        login_dial.setVisible(true);
    }
}

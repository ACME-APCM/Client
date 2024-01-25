package it.unitn.APCM.ACME.Client.ClientCommon;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DisplayMessage {

    //Method used to show an info or error panel to the user after requiring a service
    public void showOptionPane(Component comp,String title, String msg, int msgType){
        JOptionPane.showMessageDialog(comp,
                    msg,
                    title,
                    msgType);
    }
}

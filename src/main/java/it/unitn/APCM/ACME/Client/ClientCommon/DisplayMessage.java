package it.unitn.APCM.ACME.Client.ClientCommon;

import java.awt.Component;

import javax.swing.JOptionPane;

public class DisplayMessage {

    public void showOptionPane(Component comp,String title, String msg, int msgType){
        JOptionPane.showMessageDialog(comp,
                    msg,
                    title,
                    msgType);
    }
}

package it.unitn.APCM.ACME.Client;

import it.unitn.APCM.ACME.Client.Dials.EditorDial;

import javax.swing.*;

public class Client {

    public static void main(String args[]) {
        /*
         * NORMAL DIALOG
         */
        final JFrame Editor_frame = new JFrame("Editor");
        EditorDial editor_dial = new EditorDial(Editor_frame);
        editor_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editor_dial.setSize(1500, 700);
        editor_dial.setLocationRelativeTo(null);
        editor_dial.setVisible(true);

        System.exit(0);
    }
}
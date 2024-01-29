package it.unitn.APCM.ACME.Client;

import it.unitn.APCM.ACME.Client.Dials.EditorDial;

import javax.swing.*;

/**
 * The type Client.
 */
public class Client {

    /**
     * Main.
     *
     * @param args the args
     */
    public static void main(String args[]) {
        // Create the editor dial that will be used for all the operations
        final JFrame Editor_frame = new JFrame("Editor");
        EditorDial editor_dial = new EditorDial(Editor_frame);
        editor_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        editor_dial.setSize(1500, 700);
        editor_dial.setLocationRelativeTo(null);
        if (editor_dial.isEnabled()) {
            editor_dial.setVisible(true);
        }

        System.exit(0);
    }
}
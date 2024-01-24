package it.unitn.APCM.ACME.Client;

import it.unitn.APCM.ACME.Client.Dials.EditorDial;
import it.unitn.APCM.ACME.Client.Dials.LoginDial;

import javax.swing.*;

public class Client {

    public static void main(String args[]) {

//        /*
//         * LOGIN DIALOG
//         */
//        final JFrame login_frame = new JFrame("Login");
//        LoginDial login_dial = new LoginDial(login_frame, user);
//        login_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
//        login_dial.setSize(500, 150);
//        login_dial.setLocationRelativeTo(null);
//        login_dial.setVisible(true);

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

// Code for socket
/*
 * public class it.unitn.APCM.ACME.Client.Client {
 * 
 * public static void main(String[] args) throws UnknownHostException,
 * IOException, ClassNotFoundException, InterruptedException{
 * //get the localhost IP address, if server is running on some other IP, you
 * need to use that
 * InetAddress host = InetAddress.getLocalHost();
 * Socket socket = null;
 * ObjectOutputStream oos = null;
 * ObjectInputStream ois = null;
 * for(int i=0; i<5;i++){
 * //establish socket connection to server
 * socket = new Socket(host.getHostName(), 9876);
 * //write to socket using ObjectOutputStream
 * oos = new ObjectOutputStream(socket.getOutputStream());
 * System.out.println("Sending request to Socket Server");
 * if(i==4)oos.writeObject("exit");
 * else oos.writeObject(""+i);
 * //read the server response message
 * ois = new ObjectInputStream(socket.getInputStream());
 * String message = (String) ois.readObject();
 * System.out.println("Message: " + message);
 * //close resources
 * ois.close();
 * oos.close();
 * Thread.sleep(100);
 * }
 * }
 * }
 */
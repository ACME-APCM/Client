package it.unitn.APCM.ACME.Client;

import javax.swing.*;

public class Client {

    public static void main(String args[]) {

        User user = new User();

        /*
         * LOGIN DIALOG
         */
        final JFrame loginFrame = new JFrame("Login");
        Login loginDlg = new Login(loginFrame, user);
        loginDlg.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        loginDlg.setSize(500, 150);
        loginDlg.setLocationRelativeTo(null);
        loginDlg.setVisible(true);

        /*
         * NORMAL DIALOG
         */
        if (loginDlg.is_authenticated()) {
            final JFrame EditorFrame = new JFrame("Editor");
            EditorDial editorDial = new EditorDial(EditorFrame, user);
            editorDial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
            editorDial.setSize(1500, 700);
            editorDial.setLocationRelativeTo(null);
            editorDial.setVisible(true);
        }
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
import java.io.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * This class implements java socket client
 * @author ACMEteam
 *
 */

public class Client extends JFrame implements ActionListener {
    // label that gives feedback on the action performed
    JLabel feedbackLabel;
    // Text area
    JTextArea chatArea;
    // file
    String file;

    // constructor that initializes class variables
	public Client() {
        feedbackLabel = new JLabel("ready");
        chatArea = new JTextArea(30, 120);
    }

    //read string from file
    public String readFile(String path) {
        // if the path is not set, ask the user via an interactive window
        if (path.equals("")){
            // create an interactive file chooser window
            JFileChooser chooser = new JFileChooser();
            // open in current directory
            File workingDirectory = new File(System.getProperty("user.dir"));
            chooser.setCurrentDirectory(workingDirectory);

            // show the open dialog
            int r = chooser.showOpenDialog(null);

            // if the user selects a file
            if (r == JFileChooser.APPROVE_OPTION) {
                // save the path of the selected file
                path = (chooser.getSelectedFile().getAbsolutePath());
            } else {
                // if no selection end process with empty output
                return "";
            }
        }
        // open file and read content
        try (
            FileInputStream fis = new FileInputStream(new File(path));
        ) {
            byte data[] = new byte[fis.available()];
            fis.read(data);
            fis.close();
            String str = new String(data);
            return str;
        } catch (IOException ex) {
            // in case of reading error print error on console and return an empty output
            System.out.println(ex);
            return "";
        }
    }
    
    // set up main window
	public static void main(String args[]) {
		
		// I/O buttons
		JButton saveButton = new JButton("save");
		JButton openButton = new JButton("open");

		// make an object that handles the events
		Client ct = new Client();

		// add action listener to the button to capture user
		// response on buttons
		openButton.addActionListener(ct);
		saveButton.addActionListener(ct);
         
        JScrollPane chatScroll = new JScrollPane(ct.chatArea);
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(new JLabel("Text:", SwingConstants.LEFT), BorderLayout.PAGE_START);
        chatPanel.add(chatScroll);
       
        JPanel inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.LINE_AXIS));
        inputPanel.add(openButton);
        inputPanel.add(saveButton);

        JPanel feedbackPanel = new JPanel();
        feedbackPanel.add(ct.feedbackLabel);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.PAGE_AXIS));
        mainPanel.add(chatPanel);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(inputPanel);
        mainPanel.add(feedbackPanel);

        JFrame frame = new JFrame("ACME");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.add(mainPanel);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
	}

    // manage button clicks
	public void actionPerformed(ActionEvent evt) {
		String com = evt.getActionCommand();
        switch (com) {
            // open button
            case "open":
                // ask user to select file to read and read it
                file = readFile("");
                // give feedback
                chatArea.setText(file.toString());
                feedbackLabel.setText("File Opened");
                break;
            // save button
            case "save":
                feedbackLabel.setText(chatArea.getText());
                file = chatArea.getText(); //Text updated
                break;
            default:
                break;
        }
	}
}

//Code for socket
/*public class Client {

    public static void main(String[] args) throws UnknownHostException, IOException, ClassNotFoundException, InterruptedException{
        //get the localhost IP address, if server is running on some other IP, you need to use that
        InetAddress host = InetAddress.getLocalHost();
        Socket socket = null;
        ObjectOutputStream oos = null;
        ObjectInputStream ois = null;
        for(int i=0; i<5;i++){
            //establish socket connection to server
            socket = new Socket(host.getHostName(), 9876);
            //write to socket using ObjectOutputStream
            oos = new ObjectOutputStream(socket.getOutputStream());
            System.out.println("Sending request to Socket Server");
            if(i==4)oos.writeObject("exit");
            else oos.writeObject(""+i);
            //read the server response message
            ois = new ObjectInputStream(socket.getInputStream());
            String message = (String) ois.readObject();
            System.out.println("Message: " + message);
            //close resources
            ois.close();
            oos.close();
            Thread.sleep(100);
        }
    }
}*/
package it.unitn.APCM.ACME.Client;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;

public class EditorDial extends JDialog {

    private JButton saveButton;
    private JButton newButton;
    private JLabel messageLabel;
    private String path;
    private int buttonGridY;

    public EditorDial(Frame parent, User user) {
        super(parent, "Editor", true);

        messageLabel = new JLabel("");
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.BOTH;
        cs.weightx = 1.0;
        cs.weighty = 1.0;

        int topInset = 10;
        int leftInset = 10;
        int bottomInset = 10;
        int rightInset = 10;
        cs.insets = new Insets(topInset, leftInset, bottomInset, rightInset);

        Border border = BorderFactory.createLineBorder(Color.BLACK, 1);
        JTextArea pathArea = new JTextArea(1, 20);
        pathArea.setBorder(border);
        
        // Left panel with scrollable text
        JTextArea textArea = new JTextArea(30, 60);
        JScrollPane chatScroll = new JScrollPane(textArea);
        JPanel chatPanel = new JPanel(new BorderLayout());
        chatPanel.add(new JLabel("Text:"), BorderLayout.PAGE_START);
        chatPanel.add(chatScroll, BorderLayout.CENTER);

        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.weightx = 0.7;
        cs.weighty = 1.0;
        panel.add(chatPanel, cs);

        // Right panel with scrollable list of buttons
        JPanel buttonsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints buttonConstraints = new GridBagConstraints();
        buttonConstraints.fill = GridBagConstraints.HORIZONTAL;
        buttonConstraints.weightx = 1.0;
        buttonConstraints.insets = new Insets(5, 10, 5, 10); // Adjust spacing between buttons

        GuardConnection conn = new GuardConnection();
        ArrayList<String> response = conn.http_request("files");

        buttonGridY = 0; // Track grid y position for buttons

        for (String res : response) {
            JButton button = new JButton(res);
            button.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    path = e.getActionCommand();
                    ClientResponse response = conn.http_requestOpen("file?email=" + user.getEmail() +"&password=" + user.getPassword() +"&path=" + e.getActionCommand());
                    textArea.setText(response.get_text());
                    saveButton.setEnabled(response.get_w_mode());
                    //saveButton.setEnabled(false);
                    messageLabel.setText("File opened");
                }
            });

            buttonConstraints.gridx = 0;
            buttonConstraints.gridy = buttonGridY++;
            buttonConstraints.weightx = 1.0; // Allow buttons to expand horizontally

            buttonConstraints.ipadx = 50; // Horizontal padding between buttons (adjust as needed)
            buttonConstraints.ipady = 10; // Vertical padding between buttons (adjust as needed)

            buttonsPanel.add(button, buttonConstraints);
        }

        JScrollPane filesScrollPane = new JScrollPane(buttonsPanel);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.weightx = 0.3;
        cs.weighty = 1.0;
        panel.add(filesScrollPane, cs);

        // Bottom panel with Save and Open buttons
        JPanel inputPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add functionality for save button here
                ArrayList<String> response = conn.http_request_saveFile("file?email=" + user.getEmail() + "&password=" + user.getPassword() +"&path=" + path, textArea.getText());
                String message = "";
                for(String res: response){
                    message += res;
                }
                messageLabel.setText(message);
            }   
        });

        
        newButton = new JButton("New File");
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add functionality for create button here
                String newPath = pathArea.getText();
                ArrayList<String> response = conn.http_request_newFile("newFile?email=" + user.getEmail() + "&password=" + user.getPassword() + "&path=" + newPath, textArea.getText());
                String message = "";
                for(String res: response){
                    message += res;
                }
                messageLabel.setText(message);

                JButton button = new JButton(newPath);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        ClientResponse response = conn.http_requestOpen("file?email=" + user.getEmail() +"&password=" + user.getPassword() +"&path=" + e.getActionCommand());
                        textArea.setText(response.get_text());
                        saveButton.setEnabled(response.get_w_mode());
                    }
                });

                buttonConstraints.gridx = 0;
                buttonConstraints.gridy = buttonGridY++;
                buttonConstraints.weightx = 1.0; // Allow buttons to expand horizontally

                buttonConstraints.ipadx = 50; // Horizontal padding between buttons (adjust as needed)
                buttonConstraints.ipady = 10; // Vertical padding between buttons (adjust as needed)

                buttonsPanel.add(button, buttonConstraints);
                }
        });

        inputPanel.add(pathArea);
        inputPanel.add(newButton);
        inputPanel.add(saveButton);
        inputPanel.add(messageLabel);        

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.weighty = 0.1;
        panel.add(inputPanel, cs);

        panel.setBorder(new LineBorder(Color.GRAY));

        getContentPane().add(panel, BorderLayout.CENTER);

        pack();
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(parent);
    }
}

// // manage button clicks
// public void actionPerformed(ActionEvent evt) {
// String com = evt.getActionCommand();
// switch (com) {
// // open button
// case "open":
// // ask user to select file to read and read it
// file = readFile("");
// // give feedback
// chatArea.setText(file.toString());
// feedbackLabel.setText("File Opened");
// break;
// // save button
// case "save":
// feedbackLabel.setText(chatArea.getText());
// file = chatArea.getText(); // Text updated
// break;
// default:
// System.out.println("You pressed: " + com);
// String email = "email";
// String password = "password";

// // craft request and send to guard server

// // needed
// /*
// * user email
// * user password
// * path -> button pressed
// */

// // ArrayList<String> response = http_request(
// // guard_url + "file?email=" + email + "&pwd=" + password + "&path=" + com);

// // get response and open file if allowed
// break;
// }
// }
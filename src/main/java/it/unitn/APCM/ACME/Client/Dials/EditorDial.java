package it.unitn.APCM.ACME.Client.Dials;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.LineBorder;

import it.unitn.APCM.ACME.Client.GuardConnection;
import it.unitn.APCM.ACME.Client.User;
import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;

public class EditorDial extends JDialog {

    private JButton btn_save;
    private JButton btn_new;
    private String path;
    ArrayList<JButton> buttons = new ArrayList<JButton>();
    JLabel selected_file = new JLabel("No text selected");
    GuardConnection conn = new GuardConnection();

    public EditorDial(Frame parent, User user) {
        super(parent, "Editor", true);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();
        cs.fill = GridBagConstraints.BOTH;
        cs.weightx = 1.0;
        cs.weighty = 1.0;

        int top_inset = 10;
        int left_inset = 10;
        int bottom_inset = 10;
        int right_inset = 10;
        cs.insets = new Insets(top_inset, left_inset, bottom_inset, right_inset);

        // Left panel with scrollable text
        JTextArea text_area = new JTextArea(30, 60);
        JScrollPane chat_scroll = new JScrollPane(text_area);
        JPanel chat_panel = new JPanel(new BorderLayout());
        chat_panel.add(selected_file, BorderLayout.PAGE_START);
        chat_panel.add(chat_scroll, BorderLayout.CENTER);

        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.weightx = 0.7;
        cs.weighty = 1.0;
        panel.add(chat_panel, cs);

        JPanel buttons_panel  = new JPanel(new GridBagLayout());
        GridBagConstraints button_constraints =  new GridBagConstraints();
        createButtons(buttons, user, text_area);
        updateButtons(buttons, buttons_panel, button_constraints);
    
        JScrollPane files_ScrollPane = new JScrollPane(buttons_panel);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.weightx = 0.3;
        cs.weighty = 1.0;
        panel.add(files_ScrollPane, cs);        

        // Bottom panel with Save and Open buttons
        JPanel input_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btn_save = new JButton("Save");
        btn_save.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // Add functionality for save button here
                String response = conn.http_request_saveFile(
                        "file?email=" + user.getEmail() + "&path=" + path,
                        text_area.getText());
                
                if (response.equals("success")) {
                    JOptionPane.showMessageDialog(EditorDial.this,
                            "File saved",
                            "Save info",
                            JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JOptionPane.showMessageDialog(EditorDial.this,
                            "Error in saving file",
                            "Save info",
                            JOptionPane.ERROR_MESSAGE);
                }        
            }
        });

        btn_new = new JButton("New File");
        btn_new.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                final JFrame new_file_frame = new JFrame("New file");
                NewFileDial new_file_dial = new NewFileDial(new_file_frame, user);
                new_file_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                new_file_dial.setSize(600, 200);
                new_file_dial.setLocationRelativeTo(null);
                new_file_dial.setVisible(true);

                if (new_file_dial.isSucceeded()) {
                    JButton button = new JButton(new_file_dial.getFilePath());
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            path = e.getActionCommand();
                            ClientResponse response = conn
                                    .httpRequestOpen("file?email=" + user.getEmail() + "&path=" + path);

                            if (response != null) {
                                text_area.setText(response.get_text());
                                selected_file.setText(path);
                                btn_save.setEnabled(response.get_w_mode());
                            } else {
                                JOptionPane.showMessageDialog(EditorDial.this,
                                        "Error in opening file",
                                        "Open info",
                                        JOptionPane.ERROR_MESSAGE);
                            }
                        }
                    });
                    path = new_file_dial.getFilePath();
                    selected_file.setText(path);
                    text_area.setText("");
                    buttons.add(button);
                    updateButtons(buttons, buttons_panel, button_constraints);
                    panel.revalidate();
                }
            }
        });

        input_panel.add(btn_new);
        input_panel.add(btn_save);

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.weighty = 0.1;
        panel.add(input_panel, cs);

        panel.setBorder(new LineBorder(Color.GRAY));

        getContentPane().add(panel, BorderLayout.CENTER);

        pack();
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(parent);
    }

    private void updateButtons(ArrayList<JButton> buttons, JPanel buttons_panel, GridBagConstraints button_constraints){
        buttons_panel.removeAll();
       
        button_constraints =  new GridBagConstraints();
        button_constraints.fill = GridBagConstraints.HORIZONTAL;
        button_constraints.weightx = 1.0;
        button_constraints.insets = new Insets(5, 10, 5, 10); // Adjust spacing between buttons
       
        for(int i = 0; i < buttons.size(); i++){
            button_constraints.gridx = 0;
            button_constraints.gridy = i;
            button_constraints.weightx = 1.0; // Allow buttons to expand horizontally

            button_constraints.ipadx = 50; // Horizontal padding between buttons (adjust as needed)
            button_constraints.ipady = 10; // Vertical padding between buttons (adjust as needed)

            buttons_panel.add(buttons.get(i), button_constraints);
        }
    }

    private void createButtons(ArrayList<JButton> buttons, User user, JTextArea text_area){
        
        String packed_response = conn.httpRequest("files");
        if(packed_response != null){
            ArrayList<String> response = new ArrayList<String>(Arrays.asList(packed_response.split(",")));

            for (String res : response) {
                JButton button = new JButton(res);
                button.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent e) {
                        path = e.getActionCommand();
                        ClientResponse response = conn
                                .httpRequestOpen("file?email=" + user.getEmail() + "&path=" + path);

                        if (response != null) {
                            text_area.setText(response.get_text());
                            selected_file.setText(path);
                            btn_save.setEnabled(response.get_w_mode());
                        } else {
                            JOptionPane.showMessageDialog(EditorDial.this,
                                    "Error in opening file",
                                    "Open info",
                                    JOptionPane.ERROR_MESSAGE);
                        }      
                    }
                });

                buttons.add(button);
            }
        }
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

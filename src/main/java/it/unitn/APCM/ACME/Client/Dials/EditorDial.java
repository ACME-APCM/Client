package it.unitn.APCM.ACME.Client.Dials;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.border.LineBorder;

import it.unitn.APCM.ACME.Client.GuardConnection;
import it.unitn.APCM.ACME.Client.ClientCommon.User;
import it.unitn.APCM.ACME.Client.ClientCommon.ClientResponse;
import it.unitn.APCM.ACME.Client.ClientCommon.Response;

/**
 * The type Editor dial.
 */
public class EditorDial extends JDialog {

    private JButton btn_save;
    private JButton btn_delete;
    private JButton btn_new;
    private String path;
    private JTextArea text_area;
    private JLabel selected_file = new JLabel("No file selected"); // Show the file opened
    private ArrayList<JButton> buttons = new ArrayList<JButton>(); // list of the button representing the files

    private GuardConnection conn = new GuardConnection(); // Connection with the Guard
    private CommonDialFunction commonFunction = new CommonDialFunction(); // Common function used in the dials

    /**
     * Instantiates a new Editor dial.
     *
     * @param parent the parent
     */
    public EditorDial(Frame parent) {
        super(parent, "Editor", true);

        User user = new User();
        // Require the login
        if (!user.isAuthenticated()) {
            commonFunction.newLogin(user);
        }

        // Creates the panel
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
        text_area = new JTextArea(30, 60);
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

        // Right panel with list of files as buttons
        JPanel buttons_panel = new JPanel(new GridBagLayout());
        GridBagConstraints button_constraints = new GridBagConstraints();

        JScrollPane files_ScrollPane = new JScrollPane(buttons_panel);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.weightx = 0.3;
        cs.weighty = 1.0;
        panel.add(files_ScrollPane, cs);

        // Bottom panel with Save, new File adn delete buttons
        JPanel input_panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        btn_save = new JButton("Save");
        btn_save.setEnabled(false);
        btn_save.addActionListener(new ActionListener() {
            // Method to save the file
            public void actionPerformed(ActionEvent e) {
                String url = null;
                if (user != null && path != null) {
                    url = "file?email=" + user.getEmail() + "&path=" + path;
                    // Send a request to the guard
                    int res = (conn.httpRequestSave(url, text_area.getText(), user.getJwt())).getStatus();

                    // Analyze the response from the Guard
                    if (res == 0) {
                        // if file saved successfully, show an information message
                        commonFunction.showOptionPane(EditorDial.this, "Save info", "File saved",
                                JOptionPane.INFORMATION_MESSAGE);
                    } else if (res == 2) {
                        // if jwt token is not valid or expired, require the login
                        cleanText();
                        commonFunction.newLogin(user);
                    } else {
                        // if not, an error is occured, so set url to null and then an error message is
                        // displayed
                        url = null;
                    }
                }
                if (url == null) {
                    // Show an error message if save failed
                    cleanText();
                    commonFunction.showOptionPane(EditorDial.this, "Save Info", "Error in saving file",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btn_delete = new JButton("Delete");
        btn_delete.setEnabled(false);
        btn_delete.addActionListener(new ActionListener() {
            // Method to delete the file
            public void actionPerformed(ActionEvent e) {
                String url = null;
                if (user != null && path != null) {
                    url = "delete?email=" + user.getEmail() + "&path=" + path;
                    // Send a request to the guard
                    int res = (conn.httpRequestDelete(url, user.getJwt())).getStatus();

                    // Analyze the response from the Guard
                    if (res == 0) {
                        // if file saved successfully, show an information message
                        commonFunction.showOptionPane(EditorDial.this, "Delete info", "File deleted",
                                JOptionPane.INFORMATION_MESSAGE);

                        for (JButton button : buttons) {
                            if (button.getText().equals(path)) {
                                buttons.remove(button);
                                break;
                            }
                        }

                        cleanText();

                        // Call the method to update the buttons
                        updateButtons(buttons, buttons_panel, button_constraints);
                        panel.revalidate();
                    } else if (res == 2) {
                        // if jwt token is not valid or expired, require the login
                        cleanText();
                        commonFunction.newLogin(user);
                    } else {
                        // if not, an error is occured, so set url to null and then an error message is
                        // displayed
                        url = null;
                    }
                }
                if (url == null) {
                    // Show an error message if save failed
                    cleanText();
                    commonFunction.showOptionPane(EditorDial.this, "Delete Info", "Error in deleting file",
                            JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        btn_new = new JButton("New File");
        btn_new.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                // handle the creation of a new file, call the associated dial
                final JFrame new_file_frame = new JFrame("New file");
                NewFileDial new_file_dial = new NewFileDial(new_file_frame, user);
                new_file_dial.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                new_file_dial.setSize(600, 200);
                new_file_dial.setLocationRelativeTo(null);
                new_file_dial.setVisible(true);

                // Check the dial result
                if (new_file_dial.isSucceeded()) {
                    // If it succeeded, create a new button for the new file
                    JButton button = new JButton(new_file_dial.getFilePath());
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            // Handle the open of the file
                            openFile(user, e.getActionCommand());
                        }
                    });
                    // Open the new file in the dial
                    // Set path and text as empty
                    path = new_file_dial.getFilePath();
                    selected_file.setText(path);
                    text_area.setText("");
                    btn_save.setEnabled(true);
                    btn_delete.setEnabled(true);
                    buttons.add(button);
                    // Call the method to add the button to the dial
                    updateButtons(buttons, buttons_panel, button_constraints);
                    panel.revalidate();
                }
            }
        });

        // Add buttons to the panel and set flavours
        input_panel.add(btn_new);
        input_panel.add(btn_save);
        input_panel.add(btn_delete);

        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 2;
        cs.weighty = 0.1;
        panel.add(input_panel, cs);

        panel.setBorder(new LineBorder(Color.GRAY));
        getContentPane().add(panel, BorderLayout.CENTER);

        createButtons(buttons, user, text_area);
        updateButtons(buttons, buttons_panel, button_constraints);

        pack();
        setSize(1000, 600);
        setResizable(true);
        setLocationRelativeTo(parent);
    }

    // Method to update the list of the button
    private void updateButtons(ArrayList<JButton> buttons, JPanel buttons_panel,
            GridBagConstraints button_constraints) {

        // remove all the buttons from the right space
        buttons_panel.removeAll();
        buttons_panel.updateUI();

        button_constraints = new GridBagConstraints();
        button_constraints.fill = GridBagConstraints.HORIZONTAL;
        button_constraints.weightx = 1.0;
        button_constraints.insets = new Insets(5, 10, 5, 10); // Adjust spacing between buttons

        // Add all the buttons to the right space
        for (int i = 0; i < buttons.size(); i++) {
            button_constraints.gridx = 0;
            button_constraints.gridy = i;
            button_constraints.weightx = 1.0; // Allow buttons to expand horizontally

            button_constraints.ipadx = 50; // Horizontal padding between buttons (adjust as needed)
            button_constraints.ipady = 10; // Vertical padding between buttons (adjust as needed)

            buttons_panel.add(buttons.get(i), button_constraints);
        }
    }

    // Method used to create the list of button
    private void createButtons(ArrayList<JButton> buttons, User user, JTextArea text_area) {
        // Send a request to retrieve the list of files
        Response resp = conn.httpRequestFile("files?email=" + user.getEmail(), user);

        if (resp != null) {
            if (resp.getStatus() == 0) {
                // if response is successfull, get the list of files
                ArrayList<String> response = new ArrayList<>();

                // Check it's an ArrayList and convert the object
                if (resp.getResponse() instanceof ArrayList<?> res) {
					if (!res.isEmpty()) {
						for (Object o : res) {
							if (o instanceof String) {
								response.add((String) o);
							}
						}
                    }
                }

                // For each file, create a button and add it to the list
                for (String res : response) {
                    JButton button = new JButton(res);
                    button.addActionListener(new ActionListener() {
                        public void actionPerformed(ActionEvent e) {
                            openFile(user, e.getActionCommand());
                        }
                    });
                    buttons.add(button);
                }
            } else if (resp.getStatus() == 2) {
                // if jwt token is expired or is not valid, require a new login
                cleanText();
                commonFunction.newLogin(user);
            } else if (resp.getStatus() == 1) {
                // Open failed, show error message
                if (user.isAuthenticated()) {
                    cleanText();
                    commonFunction.showOptionPane(EditorDial.this, "Opening", "Error in opening file",
                            JOptionPane.ERROR_MESSAGE);
                } else {
                    // User not auth yet
                    // Probably the login form is closed
                    this.setEnabled(false);
                }
            }
        }
    }

    // Method to open a file
    private void openFile(User user, String requested_path) {
        // Send a request with the path of the file to open
        Response res = conn.httpRequestOpen("file?email=" + user.getEmail() + "&path=" + requested_path, user.getJwt());
        int status = res.getStatus();
        ClientResponse response = (ClientResponse) res.getResponse();

        if (status == 0) {
            path = requested_path;
            // if response is successfull, set the text
            text_area.setText(response.get_text()); // Set path of the file to make it clear
            selected_file.setText(requested_path);
            btn_save.setEnabled(response.get_w_mode()); // enable save button depending on the permission of the user
            btn_delete.setEnabled(response.get_w_mode()); // enable delete button depending on the permission of the user
        } else if (status == 2) {
            // if jwt token is invalid or expired, require a new login
            cleanText();
            commonFunction.newLogin(user);
        } else {
            // if failed, show an error message
            cleanText();
            commonFunction.showOptionPane(EditorDial.this, "Open info", "Error in opening file",
                    JOptionPane.ERROR_MESSAGE);
        }
    }

    // Method to clean text area
    private void cleanText() {
        text_area.setText("");
        selected_file.setText("No file selected");
        path = "";
        if (btn_save != null && btn_delete != null) {
            btn_save.setEnabled(false);
            btn_delete.setEnabled(false);
        }
        else {
            // btn not already created
        }
    }
}

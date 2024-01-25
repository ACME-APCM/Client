package it.unitn.APCM.ACME.Client.Dials;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.LineBorder;

import it.unitn.APCM.ACME.Client.GuardConnection;
import it.unitn.APCM.ACME.Client.User;
import it.unitn.APCM.ACME.Client.ClientCommon.ClientCall;
import it.unitn.APCM.ACME.Client.ClientCommon.DisplayMessage;

public class NewFileDial extends JDialog {

    private JLabel lbl_file_path;
    private JTextField tf_file_path;
    private JLabel lbl_r_groups;
    private JTextField tf_r_groups;
    private JLabel lbl_rw_groups;
    private JTextField tf_rw_groups;
    private JButton btn_create;
    private boolean succeeded;
    private String file_path = "";
    GuardConnection conn = new GuardConnection();

    public NewFileDial(Frame parent, User user) {
        super(parent, "NewFile", true);

        succeeded = false;
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints cs = new GridBagConstraints();

        cs.fill = GridBagConstraints.HORIZONTAL;

        lbl_file_path = new JLabel("File path: ");
        cs.gridx = 0;
        cs.gridy = 0;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbl_file_path, cs);

        tf_file_path = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 0;
        cs.gridwidth = 3;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(tf_file_path, cs);

        lbl_r_groups = new JLabel("Read groups (comma separated): ");
        cs.gridx = 0;
        cs.gridy = 1;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbl_r_groups, cs);

        tf_r_groups = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 1;
        cs.gridwidth = 3;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(tf_r_groups, cs);

        lbl_rw_groups = new JLabel("Write groups (comma separated): ");
        cs.gridx = 0;
        cs.gridy = 2;
        cs.gridwidth = 1;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(lbl_rw_groups, cs);

        tf_rw_groups = new JTextField(30);
        cs.gridx = 1;
        cs.gridy = 2;
        cs.gridwidth = 3;
        cs.ipadx = 10;
        cs.ipady = 10;
        panel.add(tf_rw_groups, cs);

        panel.setBorder(new LineBorder(Color.GRAY));

        btn_create = new JButton("Create new file");

        btn_create.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {

                file_path = tf_file_path.getText();

                String url = "newFile?email=" + user.getEmail() + "&path=" + file_path + "&r_groups="
                        + tf_r_groups.getText()
                        + "&rw_groups=" + tf_rw_groups.getText();
                
                int res = (conn.httpRequestCreate(url, user.getJwt())).getStatus();
                        
                if (res == 0) {
                    (new DisplayMessage()).showOptionPane(NewFileDial.this,"New file created","File created successfully", JOptionPane.INFORMATION_MESSAGE);
                    succeeded = true;
                    dispose();
                } else if(res == 2){
                    (new ClientCall()).newLogin(user);
                } else {
                    (new DisplayMessage()).showOptionPane(NewFileDial.this,"Creation report","Error in the cretion of file", JOptionPane.ERROR_MESSAGE);
                    tf_file_path.setText("");
                    tf_r_groups.setText("");
                    tf_rw_groups.setText("");
                }
            }
        });

        JPanel bp = new JPanel();
        bp.add(btn_create);

        getContentPane().add(panel, BorderLayout.CENTER);
        getContentPane().add(bp, BorderLayout.PAGE_END);

        pack();
        setResizable(false);
        setLocationRelativeTo(parent);
    }

    public boolean isSucceeded() {
        return succeeded;
    }

    public String getFilePath() {
        return file_path;
    }
}
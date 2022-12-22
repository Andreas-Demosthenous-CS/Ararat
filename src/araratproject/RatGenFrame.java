package araratproject;

import java.awt.Color;
import java.awt.Point;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import org.apache.commons.io.FileUtils;

public class RatGenFrame extends javax.swing.JFrame {

    private JFrame parent;

    public RatGenFrame(JFrame parent, Point location, String publicIP, int port) {
        this.parent = parent;
        parent.setEnabled(false);
        initComponents();

        setIconImage(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/exportRat.png")).getImage());
        getContentPane().setBackground(Color.white);
        setLocation(location);
        setVisible(true);

        ipField.setText(publicIP);
        portField.setText(port + "");

    }

    private void closeFrame() {
        this.dispose();
        parent.setEnabled(true);
        parent.requestFocus();
    }

    public void setProgressBar(int prc) {
        progressBar.setValue(prc);
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            //JOptionPane.showMessageDialog(this, ex.getMessage(), " Error setting the progress bar", JOptionPane.ERROR_MESSAGE);   
        }
    }

    public void setStatus(String status) {
        statusLabel.setText(status);
   
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jMenuItem1 = new javax.swing.JMenuItem();
        connectionPanel = new javax.swing.JPanel();
        ipField = new javax.swing.JTextField();
        ipLabel = new javax.swing.JLabel();
        portLabel = new javax.swing.JLabel();
        portField = new javax.swing.JTextField();
        outputPanel = new javax.swing.JPanel();
        outputFileField = new javax.swing.JTextField();
        outputFileFieldButton = new javax.swing.JButton();
        formatComboBox = new javax.swing.JComboBox<>();
        outputFileLabel = new javax.swing.JLabel();
        formatLabel = new javax.swing.JLabel();
        iconLabel = new javax.swing.JLabel();
        iconField = new javax.swing.JTextField();
        iconButton = new javax.swing.JButton();
        generateButton = new javax.swing.JButton();
        progressBar = new javax.swing.JProgressBar();
        jLabel1 = new javax.swing.JLabel();
        statusLabel = new javax.swing.JLabel();

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Generate Rat");
        setResizable(false);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        connectionPanel.setBackground(new java.awt.Color(255, 255, 255));
        connectionPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Connection"));

        ipField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ipField.setText("31.216.191.99");
        ipField.setEnabled(false);

        ipLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        ipLabel.setText("IP Address:");

        portLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        portLabel.setText("TCP Port:");

        portField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        portField.setText("7777");
        portField.setEnabled(false);

        javax.swing.GroupLayout connectionPanelLayout = new javax.swing.GroupLayout(connectionPanel);
        connectionPanel.setLayout(connectionPanelLayout);
        connectionPanelLayout.setHorizontalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(ipLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(portLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        connectionPanelLayout.setVerticalGroup(
            connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(connectionPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(portLabel)
                        .addComponent(portField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(connectionPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(ipLabel)
                        .addComponent(ipField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        outputPanel.setBackground(new java.awt.Color(255, 255, 255));
        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output"));

        outputFileField.setEditable(false);
        outputFileField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        outputFileFieldButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        outputFileFieldButton.setText("Browse");
        outputFileFieldButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        outputFileFieldButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputFileFieldButtonActionPerformed(evt);
            }
        });

        formatComboBox.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        formatComboBox.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "exe", "jar" }));
        formatComboBox.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                formatComboBoxActionPerformed(evt);
            }
        });

        outputFileLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        outputFileLabel.setText("Output File:");

        formatLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        formatLabel.setText("File Format:");

        iconLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        iconLabel.setText("Icon:");

        iconField.setEditable(false);
        iconField.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N

        iconButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        iconButton.setText("Browse");
        iconButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        iconButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                iconButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout outputPanelLayout = new javax.swing.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(outputFileLabel)
                    .addComponent(formatLabel)
                    .addComponent(iconLabel))
                .addGap(14, 14, 14)
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(iconField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(iconButton))
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(outputFileField)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(outputFileFieldButton))
                    .addGroup(outputPanelLayout.createSequentialGroup()
                        .addComponent(formatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outputPanelLayout.createSequentialGroup()
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(formatComboBox, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(formatLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(outputFileFieldButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outputFileField)
                    .addComponent(outputFileLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(outputPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(iconButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(iconField)
                    .addComponent(iconLabel))
                .addContainerGap())
        );

        generateButton.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        generateButton.setText("Generate");
        generateButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        generateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateButtonActionPerformed(evt);
            }
        });

        jLabel1.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        jLabel1.setText("Status:");

        statusLabel.setFont(new java.awt.Font("Tahoma", 0, 12)); // NOI18N
        statusLabel.setText("   ");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(progressBar, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(connectionPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(outputPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(generateButton)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(statusLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 434, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(connectionPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(outputPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(statusLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(progressBar, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(generateButton)
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void outputFileFieldButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputFileFieldButtonActionPerformed
        new BrowseFrame(this, outputFileField, " Select output file", formatComboBox.getSelectedIndex() + 2);
    }//GEN-LAST:event_outputFileFieldButtonActionPerformed

    private void formatComboBoxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_formatComboBoxActionPerformed
        int format = formatComboBox.getSelectedIndex();
        if (format == 0) {
            iconField.setEnabled(true);
            iconLabel.setEnabled(true);
            iconButton.setEnabled(true);
        } else if (format == 1) {
            iconField.setEnabled(false);
            iconLabel.setEnabled(false);
            iconButton.setEnabled(false);
        }
    }//GEN-LAST:event_formatComboBoxActionPerformed

    private void iconButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_iconButtonActionPerformed
        new BrowseFrame(this, iconField, " Select icon", 4);
    }//GEN-LAST:event_iconButtonActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        closeFrame();
    }//GEN-LAST:event_formWindowClosing

    private void generateButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateButtonActionPerformed

        new Thread(new Runnable() {
            public void run() {
                try {
                    if (outputFileField.getText().length() <= 0) {
                        throw new Exception("Specify output file");
                    }
                    RatGenFrame.this.setEnabled(false);
                    if (formatComboBox.getSelectedIndex() == 0) {
                        RatGenerator.saveRatAsExe(outputFileField.getText(), iconField.getText(), false);
                    } else if (formatComboBox.getSelectedIndex() == 1) {
                        RatGenerator.saveRatAsJar(outputFileField.getText(), false, true);
                    }
                    
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(RatGenFrame.this, ex.getMessage(), "Error generating rat file", JOptionPane.ERROR_MESSAGE);
                    return;
                }finally{
                    RatGenFrame.this.setEnabled(true);
                }
  
                JOptionPane.showMessageDialog(RatGenFrame.this, "Rat created successfully.", "Success!", JOptionPane.INFORMATION_MESSAGE);
                closeFrame();
            }
        }).start();


    }//GEN-LAST:event_generateButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel connectionPanel;
    private javax.swing.JComboBox<String> formatComboBox;
    private javax.swing.JLabel formatLabel;
    private javax.swing.JButton generateButton;
    private javax.swing.JButton iconButton;
    private javax.swing.JTextField iconField;
    private javax.swing.JLabel iconLabel;
    private javax.swing.JTextField ipField;
    private javax.swing.JLabel ipLabel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JTextField outputFileField;
    private javax.swing.JButton outputFileFieldButton;
    private javax.swing.JLabel outputFileLabel;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField portField;
    private javax.swing.JLabel portLabel;
    private javax.swing.JProgressBar progressBar;
    private javax.swing.JLabel statusLabel;
    // End of variables declaration//GEN-END:variables
}

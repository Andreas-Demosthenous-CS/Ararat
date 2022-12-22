package araratproject;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.filechooser.FileFilter;

/**
 * BrowseFrame is the frame containing the file system letting the user select a
 * file/folder.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class BrowseFrame extends javax.swing.JFrame {

    private JTextField field;
    private JFrame parent;
    private int type;
    private String title;

    private JPanel browsePanel;
    private JFileChooser fileChooser;
    private static File currentDirectory = new File(System.getProperty("user.dir"));

    /**
     *
     * @param parent the parent Frame
     * @param field the text field to transfer the file path
     * @param title the frame's title
     * @param type the frame's type
     */
    public BrowseFrame(JFrame parent, JTextField field, String title, int type) {
        this.parent = parent;
        this.field = field;
        this.title = title;
        this.type = type;

        parent.setEnabled(false);
        setEnabled(true);
        setLocation(Server.gui.getCentralLocation());

        initComponents();

        fileChooser.setCurrentDirectory(currentDirectory);

        setVisible(true);
    }

    private void initComponents() {

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), " Error setting the Look and Feel", JOptionPane.ERROR_MESSAGE);
        }

        this.setTitle(title);

        browsePanel = new javax.swing.JPanel();
        fileChooser = new javax.swing.JFileChooser();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });
        browsePanel.setBorder(javax.swing.BorderFactory.createTitledBorder(""));

        fileChooser.setDialogType(javax.swing.JFileChooser.SAVE_DIALOG);
        fileChooser.setCurrentDirectory(currentDirectory);

        if (type == 1) {
            fileChooser.setFileSelectionMode(javax.swing.JFileChooser.DIRECTORIES_ONLY);
        } else if (type == 2) {
            fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File pathname) {
                    String path = pathname.getName();
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    if (path.length() < 5) {
                        return false;
                    } else if (path.substring(path.length() - 4, path.length()).equals(".exe")) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "Windows executables (.exe)";
                }
            });

        } else if (type == 3) {
            fileChooser.setFileFilter(new FileFilter() {
                public boolean accept(File pathname) {
                    String path = pathname.getName();
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    if (path.length() < 5) {
                        return false;
                    } else if (path.substring(path.length() - 4, path.length()).equals(".jar")) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "Java Archive (.jar)";
                }

            });
        } else if (type == 4) {
            fileChooser.setFileFilter(new FileFilter() {

                public boolean accept(File pathname) {
                    String path = pathname.getName();
                    if (pathname.isDirectory()) {
                        return true;
                    }
                    if (path.length() < 5) {
                        return false;
                    } else if (path.substring(path.length() - 4, path.length()).equals(".ico")) {
                        return true;
                    }
                    return false;
                }

                public String getDescription() {
                    return "Icon File (.ico)";
                }

            });
        }

        fileChooser.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        fileChooser.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                fileChooserActionPerformed(evt);
            }
        });
        fileChooser.setAcceptAllFileFilterUsed(false);

        javax.swing.GroupLayout browsePanelLayout = new javax.swing.GroupLayout(browsePanel);
        browsePanel.setLayout(browsePanelLayout);
        browsePanelLayout.setHorizontalGroup(
                browsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browsePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 667, Short.MAX_VALUE)
                                .addContainerGap())
        );
        browsePanelLayout.setVerticalGroup(
                browsePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, browsePanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .addComponent(fileChooser, javax.swing.GroupLayout.DEFAULT_SIZE, 487, Short.MAX_VALUE)
                                .addContainerGap())
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(browsePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(browsePanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>                        

    private void fileChooserActionPerformed(java.awt.event.ActionEvent evt) {
        currentDirectory = fileChooser.getSelectedFile().getParentFile();
        switch (type) {
            case 1:
                if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    if (field != null) {
                        field.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
                parent.setEnabled(true);
                dispose();
                break;

            case 2:
                if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    if (field != null) {
                        field.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
                parent.setEnabled(true);
                dispose();
                break;
            case 3:
                if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    if (field != null) {
                        field.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
                parent.setEnabled(true);
                dispose();
                break;

            case 4:
                if (evt.getActionCommand().equals(JFileChooser.APPROVE_SELECTION)) {
                    if (field != null) {
                        field.setText(fileChooser.getSelectedFile().getAbsolutePath());
                    }
                }
                parent.setEnabled(true);
                dispose();
                break;

        }

    }

    private void formWindowClosing(java.awt.event.WindowEvent evt) {
        parent.setEnabled(true);
        dispose();
    }

}

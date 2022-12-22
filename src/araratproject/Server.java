package araratproject;

import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Server is the Object containing the main Objects necessary for the
 * application such as the sessions, gui, tab for each host, settings, ip etc.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class Server {

    /**
     * The Main control Interface showing the Networks and Hosts.
     */
    public static ServerManager gui;

    /**
     * The session DB containing the host's sessions.
     */
    public static ServerBase dataBase;

    /**
     * The control interface for controlling a Host.
     */
    public static ControlFrame controller;

    /**
     * The external IP of the server.
     */
    public static String ip;

    /**
     * The Object responsible for listening to new connections.
     */
    public static ConnectionListener listener;

    /**
     * The frame containing the server settings.
     */
    public static SettingsFrame settingsFrame;

    public static RatGenFrame genFrame;

    /**
     * The Server Constructor takes the ip, port and initializes the above
     * variables.
     *
     * @param ip the external server's IP.
     * @param tcpPort the TCP port for the incoming traffic.
     */
    public Server(String ip, int tcpPort) {

        //Initilizing the GUI
        gui = new ServerManager();

        //Initilizing the interface for controlling the hosts
        controller = new ControlFrame();

        //Initializing the sessions list
        dataBase = new ServerBase();
        //Initializing and starting the Listener
        listener = new ConnectionListener(ip, tcpPort, dataBase);
        listener.start();
        //Setting the server ip
        Server.ip = ip;

        //Initilizing the GUI
        settingsFrame = new SettingsFrame(gui, gui.getLocationOnScreen(), ip, tcpPort);
    }

}

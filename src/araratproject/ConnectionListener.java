package araratproject;

import UPnP.UPnP;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import javax.swing.JOptionPane;

/**
 * This class is responsible for handling new connections to the server,
 * creating the necessary instances and inserting the session to the data base.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class ConnectionListener extends Thread {

    /**
     * The Session counter used for the session IDs
     */
    public static int sessionCNT = 0;
    //TCP port number for text transferring
    private int tcpPort, oldPort;
    //flag for start/stop waiting for connections
    private boolean listen, running;
    //The server socket to accept the new connections
    private ServerSocket serverSocket = null;
    //the list with all sessions stored
    private final ServerBase dataBase;

    /**
     * The class's constructor.
     *
     * @param ip Server's ip
     * @param tcpPort TCP port number for text transferring
     * @param dataBase the list with all sessions stored
     */
    public ConnectionListener(String ip, int tcpPort, ServerBase dataBase) {
        this.tcpPort = tcpPort;
        this.oldPort = tcpPort;
        this.listen = true;
        this.running = false;
        this.dataBase = dataBase;

    }

    /**
     * The run() method of the Thread which will keep listening to new
     * connections to the specified tcp port.
     *
     */
    public void run() {
        //While the listen variable is true
        while (listen) {

            try {
                //Adding some delay to avoid problems
                Thread.sleep(50);

                //While the running variabl is true
                while (running) {
                    
                    //Waiting for new connection
                    Socket socket = serverSocket.accept();
                    
                    //Ensuring the listener is in 'running state'.
                    if (listen && running) {
                        //finding the public ip of the socket
                        String networkIP = socket.getInetAddress().getCanonicalHostName();

                        //creating the session if there is space in the Interface
                        if (!Server.gui.hasHostSpace(networkIP)) {
                            Server.gui.appendLog("  ! " + Toolset.getCurrentTime(new SimpleDateFormat("dd/MM/yy  HH:mm:ss")) + " >> Host from \"" + networkIP + "\" has been rejected.\n");
                            socket.close();
                            continue;
                        }

                        Session newSession = new Session(socket, "SID" + (++sessionCNT));

                        //adding it to the database
                        dataBase.add(newSession);
                    } else {
                        socket.close();
                    }
                }
            } catch (IOException | InterruptedException ex) {
                //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error listening to connections", JOptionPane.ERROR_MESSAGE);               
            }
        }
    }

    /**
     * Setting the Listener to start listening to connections.
     *
     * @throws IOException
     */
    public boolean startListener() throws IOException {

        //Closing the serverSocket before reinitialising
        if (serverSocket != null) {
            serverSocket.close();
        }
        //Initialising the server socket with the new port
        this.serverSocket = new ServerSocket(tcpPort);

        //Port forwarding using UPnP
        UPnP.openPortTCP(tcpPort);
        //If the port we just opened is available it means that it didnt open 
        //correclty, so we throw a message dialog informing the user.
        if (Toolset.isPortAvailable(Server.ip, tcpPort)) {
            JOptionPane.showMessageDialog(Server.gui, "Server failed to start. Probable causes:\n-No Internet Connectivity\n-Server IP is not correct"
                    + "\n-UPnP(or Port forwarding) is Disabled in the router settings\n-Firewall/Router/Antivirus blocks the traffic\n ",
                    "Connection Refused", JOptionPane.ERROR_MESSAGE);

            running = false;
            return false;
        }

        //Port opened succesfully
        Server.gui.appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> Port " + tcpPort + " opened\n");

        running = true;
        return true;
    }

    /**
     * Stopping the listener for listening to new connections.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void stopListener() throws IOException, InterruptedException {
        //terminating the open ports       
        UPnP.closePortTCP(tcpPort);
        UPnP.closePortTCP(oldPort);

        //adding some delay
        Thread.sleep(10);
        Server.gui.appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> Port " + oldPort + " closed\n");

        running = false;
        if (serverSocket != null) {
            serverSocket.close();
        }
    }

    /**
     * Terminates the Connection Listener.
     *
     * @throws IOException
     * @throws InterruptedException
     */
    public void terminate() throws IOException, InterruptedException {
        listen = false;
        stopListener();
        Server.gui.appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> Terminating Server\n");
    }

    /**
     * The method for changing the listening tcp port.
     *
     * @param port the new port
     * @throws IOException
     * @throws InvalidInputException
     */
    public void setPort(int port) throws IOException, InvalidInputException {

        if (tcpPort == port) {
            return;
        }

        //changing the port variable
        this.oldPort = tcpPort;
        this.tcpPort = port;

        //Appending the change on the log
        Server.gui.appendLog(Toolset.getCurrentTime(new SimpleDateFormat(" - dd/MM/yy  HH:mm:ss")) + " >> TCP Port set to: " + tcpPort + "\n");

    }

    /**
     * Getter for the current port.
     *
     * @return the current port
     */
    public int getPort() {
        return tcpPort;
    }
}

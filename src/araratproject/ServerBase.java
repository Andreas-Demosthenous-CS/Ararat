package araratproject;

import java.util.ArrayList;
import javax.swing.JOptionPane;

/**
 * The Server Base is an array list of Sessions.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class ServerBase extends ArrayList<Session> {

    /**
     * This method gets the session ID and returns the Session with the given
     * ID.
     *
     * @param ID the session ID
     * @return
     */
    public Session getByID(String ID) {

        Session s = null;
        for (int i = 0; i < size(); i++) {
            if (get(i).getSessionID().equals(ID)) {
                return get(i);
            }
        }
        return s;
    }

    public Session getByVictim(Victim victim) {

        Session s = null;
        for (int i = 0; i < size(); i++) {
            if (get(i).isActive() && get(i).getVictim().equals(victim)) {
                return get(i);
            }
        }
        return null;
    }
    
    public void removeByID(String ID){

        for (int i = 0; i < size(); i++) {
            if (get(i)!=null && get(i).getSessionID().equals(ID)) {
                if(get(i).getVictim()!=null)
                JOptionPane.showMessageDialog(Server.gui, get(i).getVictim().toString(), " session removed", JOptionPane.ERROR_MESSAGE);
                this.remove(get(i));       
                return;
            }
        }
    }

}

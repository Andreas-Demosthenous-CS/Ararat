package araratproject;

/**
 * This class is holds the information of the host that has been 
 * bound to the server and identified.
 *
 * @version 0.1
 * @author Andreas Demosthenous
 * @since 11/06/2020
 */
public class Victim{

    private String publicIP, privateIP, subnetmask, gateway, hostName, operatingSystem,name;
    private Session session;
    
    /**
     *
     * @param name
     * @param publicIP
     * @param privateIP
     * @param subnetmask
     * @param gateway
     * @param hostName
     * @param operatingSystem
     * @param session
     */
    public Victim(String name, String publicIP, String privateIP, String subnetmask, String gateway, String hostName, String operatingSystem, Session session) {
        this.name = name;
        this.publicIP = publicIP;
        this.privateIP = privateIP;
        this.subnetmask = subnetmask;
        this.gateway = gateway;
        this.hostName = hostName;
        this.operatingSystem = operatingSystem;
        this.session = session;
        
    }

    /**
     *
     */
    public Victim(){}

    /**
     *
     */
    public void addVictim() {
        Server.gui.connectVictim(this);
    }

    /**
     *
     * @return
     */
    public String getName(){
        return name;
    }

    /**
     *
     * @return
     */
    public Session getSession(){
        return session;
    }
    
    /**
     *
     * @return
     */
    public String getPublicIP() {
        return publicIP;
    }

    /**
     *
     * @return
     */
    public String getPrivateIP() {
        return privateIP;
    }
    
    /**
     *
     * @return
     */
    public String getSubnetmask(){
        return subnetmask;       
    }

    /**
     *
     * @return
     */
    public String getHostName() {
        return hostName;
    }
    
    /**
     *
     * @return
     */
    public String getGateway(){
        return gateway;
    }

    /**
     *
     * @return
     */
    public String getOperatingSystem() {
        return operatingSystem;
    }       

    /**
     *
     * @param victim
     * @return
     */
    public boolean equals(Victim victim) {
        if(victim == null)return false;
        return (publicIP.equals(victim.publicIP) && privateIP.equals(victim.privateIP) &&
                subnetmask.equals(victim.subnetmask) && gateway.equals(victim.gateway));
    }
    
    public String toString(){
        return name+".-.-.-."+publicIP+".-.-.-."+privateIP+".-.-.-."+subnetmask+".-.-.-."+gateway+".-.-.-."+hostName+".-.-.-."+operatingSystem;
    }

}

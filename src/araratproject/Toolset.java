package araratproject;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import UPnP.UPnP;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Comparator;
import java.util.Date;
import java.util.StringTokenizer;
import javax.swing.JOptionPane;

public class Toolset {

    public static boolean isInteger(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        str.trim();
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public static String locateJRE(String path) {

        File file = new File(path);
        if (!file.exists()) {
            return null;
        }
        File candidateFolders[] = file.listFiles();
        for (int i = 0; i < candidateFolders.length; i++) {
            if (candidateFolders[i].getName().toLowerCase().contains("jre")) {
                String finalPath = path + candidateFolders[i].getName();
                if (new File(finalPath).exists()) {
                    return path + candidateFolders[i].getName();
                }
            }
        }
        return null;
    }

    public static void appendFile(File file, String info) throws IOException {
        if (file == null) {
            return;
        }

        if (!file.exists()) {
            if (file.getParentFile() == null || !file.canWrite()) {
                file.setWritable(true);
                file.setReadable(true);

            }

            file.getParentFile().mkdirs();

            file.createNewFile();
        }
        new FileWriter(file, true).append(info).close();
    }

    public static void replaceInFile(File file, String from, String to) throws FileNotFoundException, IOException {
        if (!file.exists() || !file.canRead() || !file.canWrite()) {
            return;
        }
        byte[] data = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(data);
        fis.close();
        String contents = new String(data, "UTF-8");
        contents = contents.replace(from, to);
        clearFile(file);
        appendFile(file, contents);
    }

    public static void clearFile(File file) throws IOException {
        if (file == null) {
            return;
        }

        if (!file.exists()) {
            if (file.getParentFile() == null || !file.canWrite()) {
                file.setWritable(true);
                file.setReadable(true);

            }

            file.getParentFile().mkdirs();
            file.createNewFile();
        }
        new FileWriter(file).append("").close();
    }

    public static String findPublicIP() {
        String ip = UPnP.getExternalIP();
        if (ip == null) {
            return "Not Found";
        }
        return ip;
    }

    public static int findAvailablePort(int startingPort) {
        if (startingPort < 0 || startingPort > 65535) {
            return -1;
        }
        while (!isPortAvailable("localhost", startingPort)) {
            startingPort++;
            if (startingPort > 65535) {
                return -1;
            }
        }

        return startingPort;
    }

    public static boolean isPortAvailable(String externalIP, int port) {
        Socket s = null;
        try {

            Socket socket = new Socket();
            socket.connect(new InetSocketAddress(externalIP, port), 3000);

            // If the code makes it this far without an exception it means
            // something is using the port and has responded.
            return false;
        } catch (Exception ex) {
            return true;
        } finally {
            if (s != null) {
                try {
                    s.close();
                } catch (IOException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error closing test-socket", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

    public static boolean isOpen(int port) {
        ServerSocket ss = null;
        try {
            ss = new ServerSocket(port);
            ss.close();
        } catch (IOException ex) {
            if (ss != null) {
                try {
                    ss.close();
                } catch (IOException ex1) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error closing test-socket", JOptionPane.ERROR_MESSAGE);
                }
            }
            return true;
        }

        return false;
    }

    public static void execute(String[] commands, String workingDirectory) throws IOException, InterruptedException {
        ProcessBuilder pb = new ProcessBuilder(commands);

        if (workingDirectory != null && new File(workingDirectory).exists()) {

            File wd = new File(workingDirectory);
            pb.directory(wd);
        }

        final Process p = pb.start();

        /*
        for (int i = 0; i < commands.length; i++) {
            Server.gui.appendLog(commands[i] + " ");
        }
        Server.gui.appendLog("\n");
         */
        // then start a thread to read the output.
        new Thread(new Runnable() {
            public void run() {
                BufferedReader output = new BufferedReader(
                        new InputStreamReader(p.getInputStream()));

                try {
                    while ((output.readLine()) != null);
                } catch (IOException ex) {
                    //JOptionPane.showMessageDialog(Server.gui, ex.getMessage(), " Error processing command", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            }
        }).start();

        p.waitFor();

    }

    public static void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    public static String getCurrentTime(SimpleDateFormat format) {
        return format.format(new Date());
    }

    public static String extractIP(String data) {
        String ip = "";
        int ipIndex = 0;
        int octet = 0;
        for (int i = 0; i < data.length(); i++) {
            ipIndex = data.indexOf(".", ipIndex);
            if (ipIndex == -1) {
                break;
            }

            if (i > ipIndex) {
                ipIndex = i;
            }
            if (isInteger(data.substring(i, ipIndex))) {
                ip += data.substring(i, ipIndex) + ".";
                i = ipIndex++;
                octet++;
                if (octet == 3) {
                    while (ipIndex < data.length() && isInteger("" + data.charAt(ipIndex))) {
                        ip += data.charAt(ipIndex);
                        ipIndex++;
                    }
                    break;
                }
            }

        }
        return ip;
    }

    public static boolean isPublicIP(String IP) {
        return isValidIP(IP) && !isPrivateIP(IP);
    }

    public static boolean isPrivateIP(String IP) {

        if (!isValidIP(IP)) {
            return false;
        }
        String[] octets = IP.split("\\.");

        return octets[0].equals("10")
                || (octets[0].equals("172") && Integer.parseInt(octets[1]) >= 16 && Integer.parseInt(octets[1]) < 32)
                || (octets[0].equals("192") && octets[1].equals("168"))
                || (octets[0].equals("100") && Integer.parseInt(octets[1]) >= 64 && Integer.parseInt(octets[1]) < 128);

    }

    public static boolean isValidIP(String IP) {

        IP = IP.trim();
        if (IP == null) {
            return false;
        }
        StringTokenizer octets = new StringTokenizer(IP, ".");

        if (octets.countTokens() != 4) {
            return false;
        } else {
            while (octets.hasMoreTokens()) {
                String token = (String) octets.nextToken(".");
                if (!isInteger(token)) {
                    return false;
                } else if (Integer.parseInt(token) > 255) {
                    return false;
                }
            }
        }
        return true;
    }   
    
    public static void main(String args[]){
        System.out.println(extractIP("Link-local IPv6 Address . . . . . : "));
        
    }

}

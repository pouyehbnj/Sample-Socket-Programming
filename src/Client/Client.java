package Client;

/**
 *
 * @author Pouyeh
 */
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Client {

    static int port;
    DatagramSocket clientSocket;

    public Client(int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public void setTCPConnection(int port) throws IOException {
        
        ServerSocket serverSocket = new ServerSocket(port);
        ExecutorService pool;
        pool = Executors.newCachedThreadPool();
      
            System.out.println("TCP Server Socket starts listening");
            ClientThread clientrThread = new ClientThread(serverSocket);
            pool.execute(clientrThread);
         
    }

    public void chat(String ip,int port) throws IOException {
        System.out.println("ip:"+ip+" port:"+port);
        Socket socket;
        BufferedReader reader;
        BufferedWriter writer;
       
        socket = new Socket(ip, port);
        System.out.println("Client Socket for chatting is initialized");
        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        try {
            System.out.println("Start chatting... ");
            String line = consoleReader.readLine();
            writer.write(line+"\n");
            writer.flush();
            String resp = reader.readLine();
            System.out.println("Server Response: " + resp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void run() throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Please enter your query");
            System.out.println("For login enter: login $you_name");
            System.out.println("For search enter: search $searching_name");
            System.out.println("For search chat: chat I'm $you_name");
            String line = consoleReader.readLine();
            DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                    InetAddress.getByName("localhost"), 20000);
            clientSocket.send(packet);

            if (line.startsWith("exit")) {
                break;
            }

            byte[] buffer = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(receivedPacket);
            String loginResp = new String(receivedPacket.getData());
            System.out.println(loginResp);
            if (loginResp.trim().equals("user added!")) {
               // clientSocket.close();
                setTCPConnection(port);
                System.out.println("TCP connection setup done");
            }
            if (loginResp.startsWith("Info")) {
                //clientSocket.close();
                String[] tokens = loginResp.split(" ");
                String IP = tokens[5].trim();
                String port = tokens[7].trim();
                chat(IP,Integer.valueOf(port));
            }

        }

    }

    public static void main(String[] args) {
        Client client = null;
//        System.out.println("Please required  TCP port");
//        TCPport = new Scanner(System.in).nextInt();
        System.out.println("Please required port");
        port = new Scanner(System.in).nextInt();
        try {
            client = new Client(port);
            client.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}

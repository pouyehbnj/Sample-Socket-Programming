/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 * @author Pouyeh
 */
public class Server {

    DatagramSocket serverSocket;
    ExecutorService pool;
    public static HashMap<String, String[]> users;

    public Server() throws SocketException {
        serverSocket = new DatagramSocket(20000);
        pool = Executors.newCachedThreadPool();
        users = new HashMap();
    }

    public void run() throws IOException {
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Server starts listening");
            serverSocket.receive(packet);
            System.out.println("Server Socket find a new connection");
            //TCP (IP SRC, IP DST, PORT SRC, PORT DST)
            try {
                ServerThread serverThread = new ServerThread(packet,serverSocket);
                //ServerThread serverThread = new ServerThread(packet);
                pool.execute(serverThread);
            } catch (Exception e) {
                e.printStackTrace();
            }
       }

    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

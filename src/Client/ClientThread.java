/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Client;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Pouyeh
 */
public class ClientThread implements Runnable {

    ServerSocket serverSocket;
    Socket connectionSocket;
    BufferedReader reader;
    BufferedWriter writer;

    public ClientThread(ServerSocket serverSocket) throws IOException {
        this.serverSocket = serverSocket;

//        System.out.println("Local Port : "
//                + connectionSocket.getLocalPort());
    }

    @Override
    public void run() {
        //while (true) {
        try {
            connectionSocket = serverSocket.accept();
            reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
        } catch (IOException ex) {
            Logger.getLogger(ClientThread.class.getName()).log(Level.SEVERE, null, ex);
        }

        while (true) {
            try {


                String line = reader.readLine();
                System.out.println("Server received " + line);

                if (line.startsWith("chat")) {
                    String[] tokens = line.split(" ");
                    String friend = tokens[1];
                    writer.write("Hello Dear " + friend + "\n");
                    writer.flush();
                } else if (line.startsWith("exit")) {
                    break;
                } else {
                    writer.write("Your keyword is not recognized\n");
                    writer.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
                //   break;
            }
        }
        try {
            reader.close();
            writer.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

package org.example.Server;

import java.io.*;
import java.net.Socket;

import org.example.Logger.Level;

import static org.example.Main.logger;
import static org.example.Server.Server.clients;

public class ConnectedClient extends Thread {
    private Socket socket;
    private BufferedReader inMessage;
    private BufferedWriter outMessage;

    public ConnectedClient(Socket socket) throws IOException, InterruptedException {
        this.socket = socket;
        logger.log("Подключился новый клиент с ip адреса " + socket.getInetAddress().toString(), Level.DEBUG);
        inMessage = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
        outMessage = new BufferedWriter(new OutputStreamWriter(this.socket.getOutputStream()));

        start();
    }

    @Override
    public void run() {
        try {
            while (socket.isConnected()) {
                String readed = inMessage.readLine();
                if (readed != null) {
                    if (readed.contains(": подключился к чату")) {
                        String[] parts = readed.split(":");
                        clients.put(this, parts[0]);
                    }
                    logger.log(readed, Level.INFO);
                    for (ConnectedClient cc : clients.keySet()) {
                        if (cc != this)
                            cc.sendMsg(readed);
                    }
                }
            }
        } catch (IOException e) {
            downService();
        }
    }

    public void sendMsg(String message) throws IOException {
        outMessage.write(message + "\n");
        outMessage.flush();
    }

    private void downService() {
        try {
            if (!socket.isClosed()) {
                for (ConnectedClient cc : clients.keySet()) {
                    cc.equals(this);
                    Server.clients.remove(this);
                }
                socket.close();
                inMessage.close();
                outMessage.close();
                logger.log("Отключился клиент с ip адреса " + socket.getInetAddress().toString(), Level.DEBUG);
            }
        } catch (
                IOException ignored) {
        }
    }
}

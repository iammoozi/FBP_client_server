package com.moozi;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.List;

public class ChatServer extends Thread {
    private static List<ChatServer> serverList = new LinkedList<>();
    private Socket socket;
    private BufferedWriter writer;
    private String clientId;

    public ChatServer(Socket socket) {
        serverList.add(this);
        this.socket = socket;
    }

    @Override
    public void run() {
        try (BufferedReader reader =
                new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()))) {

            this.writer = writer;
            clientId = reader.readLine();
            System.out.println(clientId + "님이 접속했습니다.");

            while (!Thread.currentThread().isInterrupted()) {
                String line = reader.readLine() + "\n";
                String[] lineArr = line.trim().split(" ");

                if (lineArr[0].equals("!list")) {
                    sendConnectedClientsList();
                    continue;
                }

                if (lineArr.length != 2) {
                    sendErrorMessage("알맞은 양식으로 입력해 주세요\n");
                    continue;
                } else {
                    sendBroadcastAndPrivateMessages(lineArr);
                }
            }
        } catch (IOException e) {
            // Handle exception
        } finally {
            serverList.remove(this);
        }
    }

    private void sendConnectedClientsList() throws IOException {
        writer.write("[접속자 목록]\n");
        for (ChatServer server : serverList) {
            writer.write(server.getClientId() + "\n");
        }
        writer.flush();
    }

    private void sendErrorMessage(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    private void sendBroadcastAndPrivateMessages(String[] lineArr) throws IOException {
        writer.write("[내가보냄] " + lineArr[1] + "\n");
        writer.flush();

        for (ChatServer server : serverList) {
            if ("@@".equals(lineArr[0])) {
                server.send("[전체메세지]" + clientId + ": " + lineArr[1] + "\n");
            }
            if (lineArr[0].equals("@" + server.getClientId())) {
                server.send("[개인메세지]" + clientId + ": " + lineArr[1] + "\n");
            }
        }
    }

    private void send(String message) throws IOException {
        writer.write(message);
        writer.flush();
    }

    public static List<ChatServer> getList() {
        return serverList;
    }

    public String getClientId() {
        return clientId;
    }

    public static void main(String[] args) {
        int port = 1234;

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            while (!Thread.currentThread().isInterrupted()) {
                Socket socket = serverSocket.accept();
                ChatServer chatServer = new ChatServer(socket);
                chatServer.start();
            }
        } catch (IOException e) {
            // Handle exception
        }

        closeServerSockets();
    }

    private static void closeServerSockets() {
        for (ChatServer server : ChatServer.getList()) {
            try {
                server.socket.close();
                server.interrupt();
                server.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (IOException e) {
                // Handle exception
            }
        }
    }
}

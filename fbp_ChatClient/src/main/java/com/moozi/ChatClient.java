package com.moozi;

import java.io.*;
import java.net.Socket;

public class ChatClient {
    public static void main(String[] args) {
        try (Socket socket = new Socket("localhost", 1234);
                BufferedWriter writer =
                        new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                BufferedReader reader =
                        new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedReader scanner = new BufferedReader(new InputStreamReader(System.in))) {

            System.out.println("Enter your ID:");
            String s = scanner.readLine();
            writer.write(s + "\n");
            writer.flush();

            Thread inputThread = new Thread(() -> {
                try {
                    String line;
                    while (true) {
                        if (scanner.ready()) {
                            line = scanner.readLine();
                            writer.write(line + "\n");
                            writer.flush();
                            if (line.equals("!exit")) {
                                break;
                            }
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            inputThread.start();

            Thread outputThread = new Thread(() -> {
                try {
                    String serverMessage;
                    while ((serverMessage = reader.readLine()) != null) {
                        System.out.println(serverMessage);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            outputThread.start();

            inputThread.join();
            outputThread.interrupt();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}

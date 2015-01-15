package com.luxoft.bankapp.service.atm;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankClient implements Runnable {
    private static final String CLIENT_TYPE = "atm";
    private static final int PORT = 2004;
    private static final String CLOSE_CONNECTION =
            "Thank you for use our services.\nPlease remove your card\nPress enter to close connection.";
    private static final String SERVER = "localhost";
    private static final Scanner SCANNER = new Scanner(System.in);

    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankClient.class.getName());

    private String message;

    public static void main(String args[]) {
        BankClient client = new BankClient();
        client.run();
    }

    @Override
    public void run() {
        try (Socket requestSocket = new Socket(SERVER, PORT);
             ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream());
             ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream())) {

            System.out.println("Connected to localhost in port 2004");
            out.flush();
            sendMessage(out, CLIENT_TYPE);
            do {
                try {
                    Object object = in.readObject();
                    if (object instanceof String) {
                        message = (String) object;
                        System.out.println("server>" + message);
                        sendMessage(out, SCANNER.nextLine());
                    } else {
                        throw new ClassNotFoundException();
                    }
                } catch (ClassNotFoundException e) {
                    EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
                }
            } while (!message.equals(CLOSE_CONNECTION));
        } catch (IOException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }

    void sendMessage(ObjectOutputStream out, final String msg) {
        try {
            out.writeObject(msg);
            out.flush();
        } catch (IOException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
    }
}
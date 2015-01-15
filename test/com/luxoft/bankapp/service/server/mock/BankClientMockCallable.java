package com.luxoft.bankapp.service.server.mock;

import com.luxoft.bankapp.model.Client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

public class BankClientMockCallable implements Callable<Long> {
    private static final Logger EXCEPTIONS_LOGGER = Logger.getLogger("LogExceptions." + BankClientMockCallable.class.getName());
    private final Client client;

    public BankClientMockCallable(Client client) {
        this.client = client;
    }

    @Override
    public Long call() {
        long startTime = System.nanoTime();
        String server = "localhost";
        int port = 2004;
        try (Socket requestSocket = new Socket(server, port);
             ObjectInputStream in = new ObjectInputStream(requestSocket.getInputStream());
             ObjectOutputStream out = new ObjectOutputStream(requestSocket.getOutputStream())) {
            out.flush();
            serverCommunication(in, out);
        } catch (IOException | ClassNotFoundException e) {
            EXCEPTIONS_LOGGER.log(Level.SEVERE, e.getMessage(), e);
        }
        long endTime = System.nanoTime();
        return endTime - startTime;
    }

    private void serverCommunication(ObjectInputStream in, ObjectOutputStream out) throws IOException, ClassNotFoundException {
        String clientType = "atm";
        sendMessage(out, clientType);
        in.readObject();
        sendMessage(out, client.getName());
        in.readObject();
        sendMessage(out, "1");
        in.readObject();
        sendMessage(out, "saving");
        in.readObject();
        sendMessage(out, "20");
        in.readObject();
        sendMessage(out, "no");
        in.readObject();
        sendMessage(out, "\n");
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